package com.EudyContreras.Snake.CustomNodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.EudyContreras.Snake.CustomControll.CustomProgressIndicator;
import com.EudyContreras.Snake.CustomNodes.FXListAnimation.AnimationType;
import com.EudyContreras.Snake.CustomNodes.FXListListener.FXListState;
import com.EudyContreras.Snake.Utilities.FXRepeater;
import com.EudyContreras.Snake.Utilities.FXTimer;
import com.EudyContreras.Snake.Utilities.FillUtility;
import com.EudyContreras.Snake.Utilities.Period;
import com.EudyContreras.Snake.Utilities.ResizeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class FXListView<ITEM> {

    private final static int MAX_INITIAL_COUNT = 70;
    private final static int FRUSTUM_CULLING_OFFSET = 50;

    private boolean allowAnimations = true;
    private boolean allowSelfRestore = true;
    private boolean allowLazyLoading = true;
    private boolean firstLoad = true;
    private boolean listVisible = false;
    private boolean divider = false;

    private int animDuration = 350;
    private int animationIndex = 0;

    private FXCallback<FXListView<ITEM>, FXListCell<ITEM>> callBack;

    private ObservableList<Node> tempCollection;

    private ObservableList<Node> nodeCollection;

    private ObservableList<ITEM> observableData;

    private FXListListener<ITEM> listListener;

    private Map<ITEM, FXListCell<ITEM>> pairCollection;

    private Timeline[] scrollTimelines;

    private CustomProgressIndicator indicator;

    private LinkedList<FXListCell<?>> visibleCells;

    private FXTransition fxListTranstion;

    private Transition removeTransition;
    private Transition newCellTransition;

    private SnapshotParameters parameters;

    private FXListAnimation<ITEM> animation;

    private WritableImage writableImage;

    private StackPane parentContainer;

    private ImageView placeHolder;

    private ScrollPane container;

    private ScrollBar scrollBar;

    private VBox listLayout;

    private AddOrder addOrder;

    private AnimationType animType = AnimationType.NONE;

    public FXListView(AddOrder order, ObservableList<ITEM> data) {
        this.addOrder = order;
        this.observableData = data;
        this.divider = true;
        this.listLayout = new VBox();
        this.container = new ScrollPane();
        this.pairCollection = new LinkedHashMap<>();
        this.animation = new FXListAnimation<>();
        this.fxListTranstion = new FXTransition();
        this.visibleCells = new LinkedList<>();
        this.nodeCollection = FXCollections.observableArrayList();
        this.tempCollection = FXCollections.observableArrayList();
        this.parentContainer = new StackPane();
        this.container.setContent(listLayout);
        this.container.setFitToWidth(true);
        this.listLayout.setFillWidth(true);
        this.parentContainer.getChildren().add(container);
        this.placeHolder = new ImageView();
        this.parameters = new SnapshotParameters();
        this.parameters.setFill(Color.TRANSPARENT);
        this.indicator = new CustomProgressIndicator();
        this.indicator.setRadius(50);;
        this.indicator.setColor(Color.ORANGE);
        this.indicator.setVisible(false);
        this.restoreDefaultTransitions();
        this.createScrollAnimations();
        this.observeChanges();
    }

    public FXListView(AddOrder order) {
        this(order, FXCollections.observableArrayList());
    }

    private void observeChanges() {

        container.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                double deltaY = scrollEvent.getDeltaY() / 16;
                double width = container.getContent().getBoundsInLocal().getWidth();
                double hvalue = container.getHvalue();
                container.setHvalue(hvalue + -deltaY / width);
            }
        });

        container.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            cacheState();
        });

        parentContainer.setOnMouseExited(e -> {
            cacheState();
        });

        container.heightProperty().addListener((obs, oldValue, newValue) -> {

            double vPadding = container.getPadding().getTop() + container.getPadding().getBottom();

            listLayout.setMinHeight(newValue.doubleValue() - vPadding);

        });

        container.vvalueProperty().addListener((obs, oldValue, newValue) -> {

            if (scrollBar == null) {

                scrollBar = getScrollBar(container, Orientation.VERTICAL);

                scrollBar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {

                });
                scrollBar.addEventFilter(MouseEvent.DRAG_DETECTED, event -> {

                });
                scrollBar.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

                });
                scrollBar.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {

                });

            }

            double value = newValue.doubleValue();

            popCell();

            if (value == scrollBar.getMin()) {

                double targetValue = value * observableData.size();

                int toAdd = (int) (listLayout.getChildren().size() * .020);

                if(toAdd <= 0){
                    toAdd = (int) (listLayout.getChildren().size() * .20);
                }
                addStoredCell(toAdd);

                if(!observableData.isEmpty())
                scrollBar.setValue(targetValue / observableData.size());

			} else if (value == scrollBar.getMax()) {


				double targetValue = value * observableData.size();

				int toAdd = (int) (listLayout.getChildren().size() * .020);

				if (toAdd <= 0) {
					toAdd = (int) (listLayout.getChildren().size() * .20);
				}
				lazyLoadCells(toAdd);

				scrollBar.setValue(targetValue / observableData.size());

			}

			cullCells(container, oldValue.doubleValue(), newValue.doubleValue());
        });

        parentContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {

            performReset(oldScene, newScene);
        });

        observableData.addListener(new ListChangeListener<ITEM>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ITEM> change) {
                while (change.next()) {

                    if (change.wasAdded()) {

                        if (change.getAddedSize() == 1) {

                            ITEM item = change.getAddedSubList().get(0);

                            addItem(item);

                        } else {

                            addAllItems(change.getAddedSubList());
                        }
                    } else if (change.wasRemoved()) {

                        if (observableData.isEmpty()) {

                            animateClearing(() -> clearItems());

                        } else {

                            for (ITEM item : change.getRemoved()) {
                                removeItem(item);
                            }
                        }
                    }
                }
            }
        });
    }

    private void performReset(Scene oldScene, Scene newScene) {
        if (newScene == null) {

            cacheState();

            nodeCollection.clear();

            nodeCollection = FXCollections.observableArrayList(listLayout.getChildren());

            LinkedList<FXListCell<?>> cells = getVisibleCells();

            if(!cells.isEmpty())
            visibleCells = cells;

            listLayout.getChildren().clear();

            listVisible = false;

        } else {

            if (firstLoad) {

            	if(allowLazyLoading){

            		firstLoad = false;

                    listVisible = true;

                    if (allowSelfRestore) {
                        restoreDefaultTransitions();
                        allowSelfRestore = false;
                    }
            		return;
            	}



				indicator.setIndeterminate(true);

                indicator.setOnProgressComplete(e-> {

                    indicator.progressProperty().unbind();

                    parentContainer.getChildren().remove(indicator.get());
                });

                if (!parentContainer.getChildren().contains(indicator))

                    parentContainer.getChildren().add(indicator.get());

                FXTimer.runAsLong(Duration.millis(1), (ticks, capsule) -> {

                    if (nodeCollection.isEmpty()) {
                        capsule.stop();
                    }

                    capsule.updateProgress(ticks, nodeCollection.size());

                    indicator.progressProperty().bind(capsule.getProgressProperty());

                    listLayout.getChildren().add(nodeCollection.remove(0));

                }, nodeCollection.size(), (bool) -> !nodeCollection.isEmpty(), fire -> {

                	firstLoad = false;

                    listVisible = true;

                    indicator.setIndeterminate(false);

                    if (allowSelfRestore) {
                        restoreDefaultTransitions();
                        allowSelfRestore = false;
                    }

                });

            } else {
                showCachedContent(true);

                FXTimer.runLater(Duration.seconds(1), (ticks, capsule) -> {

                    listLayout.getChildren().setAll(nodeCollection);

                    nodeCollection.clear();

                    showCachedContent(false);

                    listVisible = true;
                });
            }

        }

    }

    private void listenToRemovals(FXListCell<ITEM> cell) {
        if (listListener != null)
            listListener.onUpdate(FXListState.CELL_REMOVE, cell);
    }

    private void listenToAdditions(FXListCell<ITEM> cell) {
        if (listListener != null)
            listListener.onUpdate(FXListState.CELL_ADDED, cell);
    }

    private void cullCells(ScrollPane container) {
        if (scrollBar == null) {
            cullCells(container, 0, 0);
        } else {
            cullCells(container, scrollBar.getValue() - 0.2, scrollBar.getValue());
        }

    }

    private void cullCells(ScrollPane container, double oldValue, double newValue) {
        Bounds paneBounds = container.localToScene(container.getBoundsInParent());

        if(nodeCollection.isEmpty()){
        int index = 0;

        int listSize = listLayout.getChildren().size();

        int size = listLayout.getChildren().size();

        if (oldValue > 0 || newValue > 0) {
            if (newValue > oldValue) {

                index = (int) ((newValue - 0.05) * listSize);

                if (index < 0) {
                    index = 0;
                }

                size = (int) ((newValue + 0.05) * listSize);

                if (size > listSize) {
                    size = listSize;
                }
            } else {
                index = (int) ((newValue - 0.05) * listSize);

                if (index < 0) {
                    index = 0;
                }

                size = (int) ((newValue + 0.05) * listSize);

                if (size > listSize) {
                    size = listSize;
                }
            }
        }

        FXRepeater.repeat(index, size, i -> {

            FXListCell<?> cell = (FXListCell<?>) listLayout.getChildren().get(i);

            Bounds nodeBounds = cell.localToScene(cell.getBoundsInLocal());

            if (renderBounds(FRUSTUM_CULLING_OFFSET, paneBounds, nodeBounds)) {
                if (!cell.isRendered()) {

                    cell.render(true);

                    animateOnScroll(cell);
                }
            } else {
                if (cell.isRendered()) {

                    cell.render(false);
                }
            }
        });
        }else{

            for (Node node : listLayout.getChildrenUnmodifiable()) {

                Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());

                FXListCell<?> cell = (FXListCell<?>) node;

                if (renderBounds(FRUSTUM_CULLING_OFFSET, paneBounds, nodeBounds)) {
                    if (!cell.isRendered()) {
                        cell.render(true);

                        animateOnScroll(cell);
                    }
                } else {
                    if (cell.isRendered()) {

                        cell.render(false);

                    }
                }
            }
        }
    }


    private boolean cellVisible(FXListCell<ITEM> cell) {
        Bounds paneBounds = container.localToScene(container.getBoundsInParent());

        Bounds nodeBounds = cell.localToScene(cell.getBoundsInLocal());

        if (nodeBounds.intersects(paneBounds)) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isCellVisible(FXListCell<ITEM> cell) {
        for (Node c : visibleCells) {
            if (((FXListCell<ITEM>) c).equals(cell)) {
                return true;
            }
        }
        return false;
    }

    private LinkedList<FXListCell<?>> getVisibleCells() {
        return getVisibleCells(listLayout.getChildren());
    }

    private LinkedList<FXListCell<?>> getVisibleCells(List<Node> list) {

        LinkedList<FXListCell<?>> cells = new LinkedList<>();

        Bounds paneBounds = container.localToScene(container.getBoundsInParent());

        for (Node node : list) {

            Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());

            FXListCell<?> cell = (FXListCell<?>) node;

            if (nodeBounds.intersects(paneBounds)) {

                cells.add(cell);
            }
        }
        return cells;
    }


    private void showCachedContent(boolean state) {
        if (state) {
            if (container.getHeight() > 10) {
                this.parentContainer.getChildren().clear();

                this.parentContainer.getChildren().add(placeHolder);
            }
        } else {
            this.parentContainer.getChildren().clear();

            this.parentContainer.getChildren().add(container);
        }
    }

    private void cacheState() {
        if (writableImage == null) {

            writableImage = new WritableImage((int) container.getWidth(), (int) container.getHeight());

        } else {
            if (writableImage.getWidth() != container.getWidth() || writableImage.getHeight() != container.getHeight()) {

                writableImage = new WritableImage((int) container.getWidth(), (int) container.getHeight());
            }
        }

        container.snapshot(parameters, writableImage);

        placeHolder.setImage(writableImage);
    }

    private boolean renderBounds(double offset, Bounds parent, Bounds child) {
        return new Rectangle2D(
                parent.getMinX(),
                parent.getMinY() - offset,
                parent.getWidth(),
                parent.getHeight() + offset * 2).
                intersects(
                        child.getMinX(),
                        child.getMinY(),
                        child.getWidth(),
                        child.getHeight());
    }


    private void createScrollAnimations() {
        this.scrollTimelines = new Timeline[8];
        for (int i = 0; i < scrollTimelines.length; i++) {
            scrollTimelines[i] = new Timeline();
        }
    }

    private void animateOnScroll(FXListCell<?> node) {
        KeyFrame[] frames = getKeyFrames(node, animDuration, animType);

        if (frames != null) {
            if (animationIndex >= this.scrollTimelines.length) {
                animationIndex = 0;
            }
            this.scrollTimelines[animationIndex].getKeyFrames().clear();
            this.scrollTimelines[animationIndex].getKeyFrames().addAll(frames);
            this.scrollTimelines[animationIndex].stop();
            this.scrollTimelines[animationIndex].play();

            animationIndex++;
        }
    }

    @SuppressWarnings("unchecked")
    protected KeyFrame[] getKeyFrames(Node node, int duration, AnimationType... types) {
        KeyFrame[] frames = null;

        animation.setCell((FXListCell<ITEM>) node);

        for (AnimationType type : types) {
            switch (type) {
            case FADE_IN:
                frames = animation.fadeIn(node, duration, frames);
                break;
            case FADE_OUT:
                frames = animation.fadeOut(node, duration, frames);
                break;
            case FLAP_RIGHT:
                frames = animation.getFlapRight(node, duration, frames);
                break;
            case POP_AND_SPAND:
                frames = animation.getFlatternOut(node, duration, frames);
                break;
            case FLY_FROM_DOWN:
                frames = animation.getFlyFromDown(node, duration, frames);
                break;
            case FLY_FROM_UP:
                frames = animation.getFlyFromUp(node, duration, frames);
                break;
            case ROTATE_RIGHT:
                frames = animation.getRotateYRight(node, duration, frames);
                break;
            case SPEED_LEFT:
                frames = animation.getSpeedLeft(node, duration, frames);
                break;
            case SPEED_RIGHT:
                frames = animation.getSpeedRight(node, duration, frames);
                break;
            case TRANSITION_DOWN:
                frames = animation.getTransitionDown(node, duration, frames);
                break;
            case TRANSITION_LEFT:
                frames = animation.getTransitionLeft(node, duration, frames);
                break;
            case TRANSITION_RIGHT:
                frames = animation.getTransitionRight(node, duration, frames);
                break;
            case TRANSITION_TOP:
                frames = animation.getTransitionTop(node, duration, frames);
                break;
            case ZOOM_IN:
                frames = animation.getZoomIn(node, duration, 0.7, frames);
                break;
            case ZOOM_OUT:
                frames = animation.getZoomOut(node, duration, 1.0, frames);
                break;
            case POP_OUT:
                frames = animation.getPopOut(node, duration, frames);
                break;
            case NONE:
                return null;
            }
        }
        return frames;
    }

    private ScrollBar getScrollBar(ScrollPane table, Orientation orientation) {
        ScrollBar result = null;

        for (Node node : table.lookupAll(".scroll-bar")) {

            if (node instanceof ScrollBar) {

                ScrollBar bar = (ScrollBar) node;

                if (bar.getOrientation().equals(orientation)) {

                    result = bar;
                }
            }
        }
        return result;
    }

    public void log(Object obj) {
        System.out.println(obj);
    }

    private void addItem(ITEM item) {
        FXListCell<ITEM> cell = callBack.call(this);

        cell.createCell(item);

        cell.setCache(true);

        cell.setCacheHint(CacheHint.SPEED);

        pairCollection.put(item, cell);

        addCell(cell);

    }

    private void addAllItems(List<? extends ITEM> sublist) {

        for (ITEM item : sublist) {
            FXListCell<ITEM> cell = callBack.call(this);

            cell.createCell(item);

            cell.setCache(true);

            cell.setCacheHint(CacheHint.SPEED);

            addDividers(cell);

            pairCollection.put(item, cell);

            tempCollection.add(cell);

        }
    }



    private void populateList(List<? extends ITEM> sublist) {
        int index = 0;

        for (ITEM item : sublist) {
            FXListCell<ITEM> cell = callBack.call(this);

            cell.createCell(item);

            cell.setCache(true);

            cell.setCacheHint(CacheHint.SPEED);

            switch (addOrder) {
            case BOTTOM:
                if (index > MAX_INITIAL_COUNT) {
                    cell.render(false);
                }
                break;
            case TOP:
                if (index < sublist.size() - MAX_INITIAL_COUNT) {
                    cell.render(false);
                }
                break;
            }

            addDividers(cell);

            pairCollection.put(item, cell);

            index++;
        }

        nodeCollection.setAll(pairCollection.values());

        if (addOrder == AddOrder.TOP) {

            Collections.reverse(nodeCollection);

            visibleCells.add((FXListCell<?>) nodeCollection.get(0));

            if(!allowLazyLoading)
            	return;

            for(int i = 0; i<MAX_INITIAL_COUNT; i++){

            	if(i>=nodeCollection.size()){
            		break;
            	}
            	listLayout.getChildren().add(nodeCollection.remove(i));
            }
        }else{

        	visibleCells.add((FXListCell<?>) nodeCollection.get(0));

        	if(!allowLazyLoading)
        		return;

            for(int i = 0; i<MAX_INITIAL_COUNT; i++){

            	if(i>=nodeCollection.size()){
            		break;
            	}
            	listLayout.getChildren().add(nodeCollection.remove(i));
            }
        }
    }

    private void removeItem(ITEM item) {
        removeCell(item);

        pairCollection.remove(item);
    }

    private void populteList(ObservableList<ITEM> list) {
        populateList(list);
    }

    private void popCell(){
        switch (addOrder) {
        case BOTTOM:
            if(!tempCollection.isEmpty())
            listLayout.getChildren().add(tempCollection.remove(0));
            break;
        case TOP:
            if(!tempCollection.isEmpty())
            listLayout.getChildren().add(0,tempCollection.remove(0));
            break;
        }
    }

    private void addStoredCell(int limit) {

        int size = tempCollection.size() > limit ? limit : tempCollection.size();

        switch (addOrder) {
        case BOTTOM:
            for (int i = 0; i < size; i++) {

                listLayout.getChildren().add(tempCollection.remove(0));
            }
            break;
        case TOP:
            for (int i = 0; i < size; i++) {

                listLayout.getChildren().add(0, tempCollection.remove(0));
            }
            break;
        }
    }

    private void lazyLoadCells(int limit) {

        int size = nodeCollection.size() > limit ? limit : nodeCollection.size();

    	for (int i = 0; i < size; i++) {
			listLayout.getChildren().add(nodeCollection.remove(0));
		}
	}

    @SuppressWarnings("unchecked")
    private void addCell(FXListCell<ITEM> cell) {
        if (allowAnimations && listVisible) {

            animateCellAddition(cell, () -> {

                switch (addOrder) {
                case BOTTOM:
                    listLayout.getChildren().add(cell);
                    break;
                case TOP:
                    if (!listLayout.getChildren().isEmpty()) {
                        if (cellVisible(((FXListCell<ITEM>) listLayout.getChildren().get(0)))) {
                            listLayout.getChildren().add(0, cell);
                            visibleCells.add(cell);
                        } else {
                            tempCollection.add(cell);
                        }
                    } else {
                        listLayout.getChildren().add(0, cell);
                    }
                    break;
                }

                listenToAdditions(cell);

                addDividers(cell);

            });
        } else {
            switch (addOrder) {
            case BOTTOM:
                if (listVisible) {
                    listLayout.getChildren().add(cell);
                } else {
                    nodeCollection.add(cell);
                }
                break;
            case TOP:
                if (listVisible) {
                    if (!listLayout.getChildren().isEmpty()) {
                        if (cellVisible(((FXListCell<ITEM>) listLayout.getChildren().get(0)))) {
                            listLayout.getChildren().add(0, cell);
                            visibleCells.add(cell);
                        } else {
                            tempCollection.add(cell);
                        }
                    } else {
                        listLayout.getChildren().add(0, cell);
                    }
                } else {
                    if (!nodeCollection.isEmpty()) {
                        if (isCellVisible((FXListCell<ITEM>) nodeCollection.get(0))) {
                            nodeCollection.add(0, cell);
                            visibleCells.add(cell);
                        } else {
                            tempCollection.add(cell);
                        }
                    } else {
                        nodeCollection.add(0, cell);
                    }
                }
                break;
            }
            listenToAdditions(cell);

            addDividers(cell);
        }
    }

    private void removeCell(ITEM item) {
        FXListCell<ITEM> cell = pairCollection.get(item);

        listenToRemovals(cell);

        if (allowAnimations) {
            animateCellRemoval(cell, () -> {

                listLayout.getChildren().remove(cell);

                nodeCollection.remove(cell);

                checkState();

                cullCells(container);
            });
        } else {
            listLayout.getChildren().remove(cell);

            nodeCollection.remove(cell);

            checkState();

            cullCells(container);
        }
    }

    private void checkState() {
        int size = listLayout.getChildren().size();

        if (size < MAX_INITIAL_COUNT) {

            addStoredCell(MAX_INITIAL_COUNT - size);
        }
    }

    private void animateCellRemoval(FXListCell<ITEM> node, FXListEvent event) {
        if (node.getDeleteTransition() != null) {

            EventHandler<ActionEvent> actionEvent = node.getDeleteTransition().getOnFinished();

            node.getDeleteTransition().setOnFinished(null);

            node.getDeleteTransition().setOnFinished(e -> {

                if (actionEvent != null) {
                    actionEvent.handle(e);
                }

                removeCellAnimation(node, event);

            });

            node.getDeleteTransition().play();

        } else if (removeTransition != null) {

        	log("not null");

            Transition transition = fxListTranstion.getTransition(removeTransition, node);

            EventHandler<ActionEvent> actionEvent = transition.getOnFinished();

            transition.setOnFinished(null);

            transition.setOnFinished(e -> {

                if (actionEvent != null) {
                    actionEvent.handle(e);
                }

                removeCellAnimation(node, event);
            });

            transition.play();

        } else {
            removeCellAnimation(node, event);
        }

    }

    private void removeCellAnimation(Region node, FXListEvent event) {
        ResizeTransition resize = new ResizeTransition(ResizeTransition.RESIZE_HEIGHT);

        resize.setInterpolator(Interpolator.EASE_BOTH);

        resize.setDuration(Duration.millis(250));

        resize.fromHeight(node.getBoundsInParent().getHeight());

        resize.toHeight(0);

        resize.setRegion(node);

        resize.setOnFinished((e) -> {
            if (event != null) {
                event.fireEvent();

                cacheState();
            }
        });

        resize.play();
    }

    private void animateCellAddition(FXListCell<ITEM> node, FXListEvent startEvent) {
        if (node.getAddedTransition() != null | newCellTransition != null) {
            node.setOpacity(0);
        }
        node.setPrefHeight(0);
        if (startEvent != null) {
            startEvent.fireEvent();
        }
        ResizeTransition resize = new ResizeTransition(ResizeTransition.RESIZE_HEIGHT);
        resize.setInterpolator(Interpolator.EASE_OUT);
        resize.setDuration(Duration.millis(250));
        resize.fromHeight(0);
        resize.toHeight(node.getBoundsInParent().getHeight());
        resize.setRegion(node);
        resize.setOnFinished(e->{

            node.setScaleX(0);
            node.setOpacity(1);
            addCellAnimation(node);

//			if (endEvent != null) {
//				endEvent.fireEvent();
//			}
        });
        resize.play();
    }

    private void addCellAnimation(FXListCell<ITEM> node) {
        if (node.getAddedTransition() != null) {

            Transition transition = node.getAddedTransition();

            EventHandler<ActionEvent> actionEvent = transition.getOnFinished();

            transition.setOnFinished(null);

            transition.setOnFinished(e -> {

                if (actionEvent != null) {
                    actionEvent.handle(e);
                }
            });

            transition.play();

        } else if (newCellTransition != null) {

            Transition transition = fxListTranstion.getTransition(newCellTransition, node);

            EventHandler<ActionEvent> actionEvent = transition.getOnFinished();

            transition.setOnFinished(null);

            transition.setOnFinished(e -> {

                if (actionEvent != null) {
                    actionEvent.handle(e);
                }
            });

            transition.play();
        }
    }

    public final AddOrder getAddOrder() {
        return addOrder;
    }

    private void clearItems() {
        listLayout.getChildren().clear();

        nodeCollection.clear();

        pairCollection.clear();

        tempCollection.clear();

        visibleCells.clear();
    }

    private void animateClearing(FXListEvent event) {
        visibleCells = getVisibleCells();

        int delay = 0;

        for (int i = 0; i < visibleCells.size(); i++) {
            Node cell = visibleCells.get(i);

            KeyFrame[] frames = getKeyFrames(cell, animDuration, AnimationType.ZOOM_OUT);

            if (frames != null) {
                Timeline anim = new Timeline(frames);

                anim.getKeyFrames().addAll(frames);

                anim.setDelay(Duration.millis(delay));

                anim.play();

                delay += 50;

                if (i == visibleCells.size() - 1) {

                    anim.setOnFinished(e -> {

                        if (event != null) {

                            event.fireEvent();
                        }
                    });
                }
            }
        }
    }

    public void add(ITEM item) {
        observableData.add(item);
    }

    public void addAll(@SuppressWarnings("unchecked") ITEM... items) {
        observableData.addAll(items);
    }

    public void remove(ITEM item) {
        observableData.remove(item);
    }

    public void removeAll(@SuppressWarnings("unchecked") ITEM... items) {
        observableData.removeAll(items);
    }

    public void clear() {
        listLayout.getChildren().clear();

        pairCollection.clear();

        nodeCollection.clear();

        tempCollection.clear();

        observableData.clear();

        visibleCells.clear();
    }

    public void addListListener(FXListListener<ITEM> listener) {
        this.listListener = listener;
    }

    public void removeListListener() {
        this.listListener = null;
    }

    public ObservableList<ITEM> getItems() {
        return observableData;
    }

    public FXListCell<ITEM> getCell(ITEM item) {
        return pairCollection.get(item);
    }

    public List<FXListCell<ITEM>> getCells() {
        return new ArrayList<FXListCell<ITEM>>(pairCollection.values());
    }

    public void setCellFactory(FXCallback<FXListView<ITEM>, FXListCell<ITEM>> callBack) {
        this.callBack = callBack;

        if (observableData != null) {

            if (!observableData.isEmpty()) {

                this.populteList(observableData);
            }
        }
    }

    public FXCallback<FXListView<ITEM>, FXListCell<ITEM>> getCellFactory() {
        return callBack;
    }

    public boolean isAllowAnimations() {
        return allowAnimations;
    }

    public void setAllowAnimations(boolean allowAnimaions) {
        this.allowAnimations = allowAnimaions;
    }

    public void setCellAddedTransition(Transition transition) {
        this.newCellTransition = transition;
    }

    public void setCellDeletedTransition(Transition transition) {
        this.removeTransition = transition;
    }

    public void setScrollAnimation(AnimationType animation) {
        this.animType = animation;
    }

    public void restoreDefaultTransitions() {
        TranslateTransition translate = new TranslateTransition();

        translate.setDuration(Duration.millis(500));

        translate.setFromX(0);

        translate.setToX(getWidth());

        ScaleTransition scale = new ScaleTransition();

        scale.setInterpolator(Interpolator.SPLINE(0, 0, 0, 1));

        scale.setDuration(Duration.millis(500));

        scale.setFromX(0);

        scale.setFromY(0);

        scale.setToX(1);

        scale.setToY(1);

        setCellDeletedTransition(translate);

        setCellAddedTransition(scale);
    }

    public void setScrollAnimationDuration(Period duration) {
        this.animDuration = (int) duration.getDuration();
    }

    public void setSpacing(double spacing) {
        listLayout.setSpacing(spacing);
    }

    public void setPadding(Insets insets) {
        parentContainer.setPadding(insets);
    }

    public void setFill(Paint fill) {
        container.setBackground(FillUtility.PAINT_FILL(fill));

        listLayout.setBackground(FillUtility.PAINT_FILL(fill));

        parentContainer.setBackground(FillUtility.PAINT_FILL(fill));
    }

    private void addDividers(Region node) {
        if (divider) {

            node.setBorder(new Border(new BorderStroke(null, null, Color.rgb(60, 60, 60), null, null, null,
                    BorderStrokeStyle.SOLID, null, null, new BorderWidths(0, 0, 2, 0), null)));

        } else {

            node.setBorder(null);
        }
    }

    public void setDividers(boolean state) {
        this.divider = state;
    }

    public void setSize(double width, double height) {
        setWidth(width);

        setHeight(height);
    }

    public void setWidth(double width) {
        container.setMaxWidth(width);

        container.setPrefWidth(width);

        container.setMinWidth(width);
    }

    public void setHeight(double height) {
        container.setMaxHeight(height);

        container.setPrefHeight(height);

        container.setMinHeight(height);
    }

    public double getWidth() {
        return container.getPrefWidth();
    }

    public double getHeight() {
        return container.getPrefHeight();
    }

    public int getCellCount() {
        return listLayout.getChildren().size();
    }

    public Region get() {
        return parentContainer;
    }

    public enum AddOrder {
        TOP, BOTTOM
    }

}
