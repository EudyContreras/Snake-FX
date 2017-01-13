package com.EudyContreras.Snake.CustomNodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.EudyContreras.Snake.CustomNodes.FXListAnimation.AnimationType;
import com.EudyContreras.Snake.CustomNodes.FXListListener.FXListState;
import com.EudyContreras.Snake.Utilities.Animator;
import com.EudyContreras.Snake.Utilities.AnimatorListener;
import com.EudyContreras.Snake.Utilities.FillUtility;
import com.EudyContreras.Snake.Utilities.Interpolators;
import com.EudyContreras.Snake.Utilities.Period;
import com.EudyContreras.Snake.Utilities.ResizeAnimator;
import com.EudyContreras.Snake.Utilities.ResizeTransition;
import com.EudyContreras.Snake.Utilities.TimePeriod;
import com.EudyContreras.Snake.Utilities.ResizeTransition.Resize;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class FXListView<T> {

	private final static int MAX_INITIAL_COUNT = 70;
	private final static int FRUSTUM_CULLING_OFFSET = 50;

	private boolean allowAnimations = true;
	private boolean allowSelfRestore = true;
	private boolean divider = false;

	private int animDuration = 150;
	private int animationIndex = 0;

	private FXCallback<FXListView<T>, FXListCell<T>> callBack;

	private ObservableList<T> observableData;

	private FXListListener<T> listListener;

	private List<FXListCell<T>> nodeCollection;

	private Map<T, FXListCell<T>> pairCollection;

	private LinkedList<FXListCell<?>> visibleCells;

	private FXTransition fxListTranstion;

	private Timeline[] scrollTimelines;

	private Transition removeTransition;
	private Transition newCellTransition;

	private SnapshotParameters parameters;

	private FXListAnimation<T> animation;

	private WritableImage writableImage;

	private StackPane parentContainer;

	private ImageView placeHolder;

	private ScrollPane container;

	private ScrollBar scrollBar;

	private VBox listLayout;

	private AddOrder addOrder;

	private AnimationType animType = AnimationType.NONE;

	public FXListView(AddOrder order, ObservableList<T> data) {
		this.addOrder = order;
		this.observableData = data;
		this.divider = true;
		this.listLayout = new VBox();
		this.container = new ScrollPane();
		this.pairCollection = new LinkedHashMap<>();
		this.animation = new FXListAnimation<>();
		this.fxListTranstion = new FXTransition();
		this.visibleCells = new LinkedList<>();
		this.nodeCollection = new LinkedList<>();
		this.parentContainer = new StackPane();
		this.container.setContent(listLayout);
		this.container.setFitToWidth(true);
		this.listLayout.setFillWidth(true);
		this.parentContainer.getChildren().add(container);
		this.placeHolder = new ImageView();
		this.parameters = new SnapshotParameters();
		this.parameters.setFill(Color.TRANSPARENT);
		this.createScrollAnimations();
		this.observeChanges();
	}

	public FXListView(AddOrder order) {
		this(order, FXCollections.observableArrayList());
	}

	@SuppressWarnings("unchecked")
	private void observeChanges() {

		listLayout.setOnScrollFinished(e -> {
			// cacheState();
		});

		container.heightProperty().addListener((obs, oldValue, newValue) -> {

			double vPadding = container.getPadding().getTop() + container.getPadding().getBottom();

			listLayout.setMinHeight(newValue.doubleValue() - vPadding);

		});

		container.vvalueProperty().addListener((obs, oldValue, newValue) -> {
			double value = newValue.doubleValue();

			if (scrollBar == null) {
				scrollBar = getScrollBar(container, Orientation.VERTICAL);

				scrollBar.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {

				});
				scrollBar.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {

					cullCells(container, oldValue.doubleValue(), newValue.doubleValue());

					// cacheState();
				});
			}

			if (value >= scrollBar.getMax()) {

				double targetValue = value * observableData.size();

				addCell((int) (listLayout.getChildren().size() /20));

				scrollBar.setValue(targetValue / observableData.size());
			}

			cullCells(container, oldValue.doubleValue(), newValue.doubleValue());
		});

		container.sceneProperty().addListener((obs, oldScene, newScene) -> {
			performReset(oldScene, newScene);
		});

		observableData.addListener(new ListChangeListener<T>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends T> change) {
				while (change.next()) {
					if (change.wasAdded()) {
						if (change.getAddedSize() == 1) {
							T item = change.getAddedSubList().get(0);
							addItem(item);
						} else {
							addAllItems(change.getAddedSubList());
						}
					} else if (change.wasRemoved()) {
						if (observableData.isEmpty()) {
							animateClearing(()->clearItems());
						} else {
							for (T item : change.getRemoved()) {
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
			resetCells(false);
		} else {
			resetCells(true);
		}
		// if (newScene == null) {
		// switch (addOrder) {
		// case BOTTOM:
		// for (Node cell : listLayout.getChildren()) {
		// nodeCollection.add((FXListCell<T>) cell);
		// }
		// break;
		// case TOP:
		// for (Node cell : listLayout.getChildren()) {
		// nodeCollection.add(0,(FXListCell<T>) cell);
		// }
		// break;
		// }
		//
		// listLayout.getChildren().clear();
		//
		// } else {
		//
		// showPlaceHolder(true);
		//
		// AnimationTimer.runLater(TimePeriod.millis(1600), ()->{
		//
		// listLayout.getChildren().clear();
		//
		// switch (addOrder) {
		// case BOTTOM:
		// for (FXListCell<T> cell : nodeCollection) {
		// listLayout.getChildren().add(cell);
		// }
		// break;
		// case TOP:
		// for (FXListCell<T> cell : nodeCollection) {
		// listLayout.getChildren().add(0, cell);
		// }
		// break;
		// }
		//
		// nodeCollection.clear();
		//
		// showPlaceHolder(false);
		//
		// cacheState();
		// });
		// }

		if (allowSelfRestore) {
			restoreDefaultTransitions();
			allowSelfRestore = false;
		}
	}

	private void listenToRemovals(FXListCell<T> cell) {
		if (listListener != null)
			listListener.onUpdate(FXListState.CELL_REMOVE, cell);
	}

	private void listenToAdditions(FXListCell<T> cell) {
		if (listListener != null)
			listListener.onUpdate(FXListState.CELL_ADDED, cell);
	}

	@SuppressWarnings("unchecked")
	private void resetCells(boolean state) {
		if (state) {
			cullCells(container);
		} else {
			ObservableList<Node> nodes = listLayout.getChildren();

			switch (addOrder) {
			case BOTTOM:
				for (int i = nodes.size() - 1; i >= 0; i--) {
					nodeCollection.add(0, (FXListCell<T>) nodes.get(i));
				}
				break;
			case TOP:
				for (int i = nodes.size() - 1; i >= 0; i--) {
					nodeCollection.add((FXListCell<T>) nodes.get(i));
				}
				break;
			}

			listLayout.getChildren().clear();

			addCell(MAX_INITIAL_COUNT);

			container.setVvalue(0);
		}
	}

	private void cullCells(ScrollPane container) {
		cullCells(container, 0, 0);
	}

	private void cullCells(ScrollPane container, double oldValue, double newValue) {
		Bounds paneBounds = container.localToScene(container.getBoundsInParent());

		if (nodeCollection.isEmpty()) {

			int index = 0;

			int listSize = listLayout.getChildren().size();

			int size = listLayout.getChildren().size();

			if (oldValue > 0 || newValue > 0) {
				if (newValue > oldValue) {
					index = (int) ((newValue - 0.1) * listSize);
					if (index < 0) {
						index = 0;
					}
					size = (int) ((newValue + 0.1) * listSize);
					if (size > listSize) {
						size = listSize;
					}
				} else {
					index = (int) ((newValue - 0.1) * listSize);
					if (index < 0) {
						index = 0;
					}
					size = (int) ((newValue + 0.1) * listSize);
					if (size > listSize) {
						size = listSize;
					}
				}
			}

			for (int i = index; i < size; i++) {
				FXListCell<?> cell = (FXListCell<?>) listLayout.getChildren().get(i);
				Bounds nodeBounds = cell.localToScene(cell.getBoundsInLocal());
				if (withinBounds(FRUSTUM_CULLING_OFFSET, paneBounds, nodeBounds)) {
					if (!cell.isRendered()) {
						cell.render(true);
						cell.resetProperties();
						animateOnScroll(cell);
					}
				} else {
					if (cell.isRendered()) {
						cell.render(false);
						cell.resetProperties();
					}
				}
			}
		} else {
			for (Node node : listLayout.getChildrenUnmodifiable()) {
				Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
				FXListCell<?> cell = (FXListCell<?>) node;
				if (withinBounds(FRUSTUM_CULLING_OFFSET, paneBounds, nodeBounds)) {
					if (!cell.isRendered()) {
						cell.render(true);
						cell.resetProperties();
						animateOnScroll(cell);
					}
				} else {
					if (cell.isRendered()) {
						cell.render(false);
						cell.resetProperties();
					}
				}
			}
		}
	}

	private void addToVisiblePile(FXListCell<?> cell){
		if(visibleCells.size()<20){
			visibleCells.add(cell);
		}else{
			visibleCells.poll();
			visibleCells.add(cell);
		}
	}

	private void showPlaceHolder(boolean state) {
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
		writableImage = new WritableImage((int) container.getWidth(), (int) container.getHeight());
		container.snapshot(parameters, writableImage);
		placeHolder.setImage(writableImage);
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

	private boolean withinBounds(double offset, Bounds parent, Bounds child) {
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

	@SuppressWarnings("unchecked")
	protected KeyFrame[] getKeyFrames(Node node, int duration, AnimationType... types) {
		KeyFrame[] frames = null;

		animation.setCell((FXListCell<T>) node);

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
			default:
				break;
			}
		}
		return frames;
	}

	@SuppressWarnings("unused")
	private void repeat(int count, FXListEvent event) {
		if (count >= 0) {
			for (int i = 0; i < count; i++) {
				if (event != null) {
					event.fire();
				}
			}
		}
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

	private void addCell(int max) {
		if (nodeCollection.size() > max) {
			for (int i = 0; i < max; i++) {
				Node node = null;
				switch (addOrder) {
				case BOTTOM:
					node = nodeCollection.get(0);
					node.setCache(true);
					node.setCacheHint(CacheHint.SPEED);
					listLayout.getChildren().add(node);
					nodeCollection.remove(node);
					break;
				case TOP:
					node = nodeCollection.get(nodeCollection.size() - 1);
					node.setCache(true);
					node.setCacheHint(CacheHint.SPEED);
					listLayout.getChildren().add(node);
					nodeCollection.remove(node);
					break;
				}
			}
		} else {
			for (int i = 0; i < nodeCollection.size(); i++) {
				Node node = null;
				switch (addOrder) {
				case BOTTOM:
					node = nodeCollection.get(0);
					node.setCache(true);
					node.setCacheHint(CacheHint.SPEED);
					listLayout.getChildren().add(node);
					nodeCollection.remove(node);
					break;
				case TOP:
					node = nodeCollection.get(nodeCollection.size() - 1);
					node.setCache(true);
					node.setCacheHint(CacheHint.SPEED);
					listLayout.getChildren().add(node);
					nodeCollection.remove(node);
					break;
				}
			}

		}
		setDividers(true);
	}

	private void populteList(ObservableList<T> list) {
		addAllItems(list);
	}

	private void addCell(int index, FXListCell<T> cell) {
		if (allowAnimations) {
			animateAddition(cell, () -> {
				listLayout.getChildren().add(index, cell);
				listenToAdditions(cell);
			}, () -> {
				addDividers(cell);
			});
		} else {
			listLayout.getChildren().add(index, cell);
			listenToAdditions(cell);
			addDividers(cell);
		}
	}

	private void removeCell(T item) {
		FXListCell<T> cell = pairCollection.get(item);
		listenToRemovals(cell);

		if (allowAnimations) {
			animateRemoval(cell, () -> {
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
			addCell(MAX_INITIAL_COUNT - size);
		}
	}

	private void animateRemoval(FXListCell<T> node, FXListEvent event) {
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
		ResizeTransition resize = new ResizeTransition(Resize.HEIGHT);
		resize.setInterpolator(Interpolator.EASE_OUT);
		resize.setDuration(Duration.millis(250));
		resize.fromHeight(node.getBoundsInParent().getHeight());
		resize.toHeight(0);
		resize.setRegion(node);
		resize.setOnFinished((e) -> {
			if (event != null) {
				event.fire();
			}
		});
		resize.play();
	}

	private void animateAddition(FXListCell<T> node, FXListEvent startEvent, FXListEvent endEvent){
		ResizeAnimator resize = new ResizeAnimator(ResizeAnimator.RESIZE_HEIGHT);
		resize.setInterpolator(Interpolators.getEasingInstance(0,0));
		resize.setDuration(TimePeriod.millis(250));
		resize.addListener(new AnimatorListener(){
			@Override
			public void OnStart(Animator animation) {
				if(node.getAddedTransition()!=null || newCellTransition!=null){
					node.setOpacity(0);
				}
				node.setPrefHeight(0);
				if(startEvent!=null){
					startEvent.fire();
				}

				if(endEvent!=null){
					endEvent.fire();
				}
			}
			@Override
			public void OnRepeat(Animator animation) {}

			@Override
			public void OnEnd(Animator animation) {
				node.setOpacity(1);

				addCellAnimation(node);

//				cullCells(container);

			}
		});

		resize.setStartHeight(0);
		resize.setEndHeight(node.getBoundsInParent().getHeight());
		resize.setRegion(node);
		resize.play();
	}
	private void addCellAnimation(FXListCell<T> node) {
		if (node.getAddedTransition() != null) {
			Transition transition = node.getAddedTransition();
			transition.play();

		} else if (newCellTransition != null) {
			Transition transition = fxListTranstion.getTransition(newCellTransition, node);
			transition.play();
		}
	}

	private void addItem(T item) {
		FXListCell<T> cell = callBack.call(this);
		cell.createCell(item);
		pairCollection.put(item, cell);
		switch (addOrder) {
		case BOTTOM:
			addCell(listLayout.getChildren().size() - 1, cell);
			break;
		case TOP:
			addCell(0, cell);
			break;
		}
	}

	private void addAllItems(List<? extends T> addedSubList) {
		int index = 0;
		for (T item : addedSubList) {
			FXListCell<T> cell = callBack.call(this);
			cell.createCell(item);
			cell.setCache(true);
			cell.setCacheHint(CacheHint.SPEED);
			if (index < addedSubList.size() - MAX_INITIAL_COUNT) {
				cell.render(false);
			}
			addDividers(cell);
			listenToAdditions(cell);
			nodeCollection.add(cell);
			pairCollection.put(item, cell);
			index++;
		}

		// for(int i = 0; i<nodeCollection.size()/2; i++){
		// Node cell = null;
		// switch (addOrder) {
		// case BOTTOM:
		// cell = nodeCollection.get(0);
		// listLayout.getChildren().add(cell);
		// nodeCollection.remove(0);
		// break;
		// case TOP:
		// cell = nodeCollection.get(nodeCollection.size()-1);
		// listLayout.getChildren().add(cell);
		// nodeCollection.remove(nodeCollection.size()-1);
		// break;
		// }
		// }

		if (nodeCollection.size() > MAX_INITIAL_COUNT) {
			for (int i = nodeCollection.size() - MAX_INITIAL_COUNT; i < nodeCollection.size(); i++) {
				FXListCell<T> cell = null;
				switch (addOrder) {
				case BOTTOM:
					cell = nodeCollection.get(0);
					listLayout.getChildren().add(cell);
					nodeCollection.remove(0);
					break;
				case TOP:
					cell = nodeCollection.get(nodeCollection.size() - 1);
					listLayout.getChildren().add(cell);
					nodeCollection.remove(nodeCollection.size() - 1);
					break;
				}
			}
		} else {
			for (int i = 0; i < nodeCollection.size(); i++) {
				FXListCell<T> cell = null;
				switch (addOrder) {
				case BOTTOM:
					cell = nodeCollection.get(0);
					listLayout.getChildren().add(cell);
					nodeCollection.remove(0);
					break;
				case TOP:
					cell = nodeCollection.get(nodeCollection.size() - 1);
					listLayout.getChildren().add(cell);
					nodeCollection.remove(nodeCollection.size() - 1);
					break;
				}
			}
		}
	}

	public final AddOrder getAddOrder() {
		return addOrder;
	}

	private void removeItem(T item) {
		removeCell(item);
		pairCollection.remove(item);
	}

	private void clearItems() {
		listLayout.getChildren().clear();
		nodeCollection.clear();
		pairCollection.clear();
	}


	private void animateClearing(FXListEvent event){
		visibleCells.clear();

		Bounds paneBounds = container.localToScene(container.getBoundsInParent());
		for (Node node : listLayout.getChildrenUnmodifiable()) {
			Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
			FXListCell<?> cell = (FXListCell<?>) node;
			if (withinBounds(FRUSTUM_CULLING_OFFSET, paneBounds, nodeBounds)) {
				addToVisiblePile(cell);
			}
		}

		System.out.println(visibleCells.size());

		int delay = 0;

		for(int i = 0; i<visibleCells.size(); i++){
			Node cell = visibleCells.get(i);
			KeyFrame[] frames = getKeyFrames(cell, animDuration, AnimationType.ZOOM_OUT);
			if (frames != null) {
				Timeline anim = new Timeline(frames);
				anim.getKeyFrames().addAll(frames);
				anim.setDelay(Duration.millis(delay));
				anim.play();
				delay+=50;
				if(i==visibleCells.size()-1){
					anim.setOnFinished(e->{
						if(event!=null){
							event.fire();
						}
					});
				}
			}
		}
	}

	public void add(T item) {
		observableData.add(item);
	}

	public void addAll(@SuppressWarnings("unchecked") T... items) {
		observableData.addAll(items);
	}

	public void remove(T item) {
		observableData.remove(item);
	}

	public void removeAll(@SuppressWarnings("unchecked") T... items) {
		observableData.removeAll(items);
	}

	public void clear() {
		listLayout.getChildren().clear();
		pairCollection.clear();
		nodeCollection.clear();
		observableData.clear();
	}

	public void addListListener(FXListListener<T> listener) {
		this.listListener = listener;
	}

	public void removeListListener() {
		this.listListener = null;
	}

	public ObservableList<T> getItems() {
		return observableData;
	}

	public List<FXListCell<T>> getCells() {
		return new ArrayList<FXListCell<T>>(pairCollection.values());
	}

	public void setCellFactory(FXCallback<FXListView<T>, FXListCell<T>> callBack) {
		this.callBack = callBack;
		if (observableData != null) {
			if (!observableData.isEmpty()) {
				this.populteList(observableData);
			}
		}
	}

	public FXCallback<FXListView<T>, FXListCell<T>> getCellFactory() {
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

	private void addDividers(Node node) {
		if (divider) {
			String style = "-fx-border-color: rgb(60,60,60);" + "-fx-border-width: 0 0 2 0;";
			node.setStyle(style);
		} else {
			node.setStyle(null);
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
