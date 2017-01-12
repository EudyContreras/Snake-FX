package com.EudyContreras.Snake.CustomNodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.EudyContreras.Snake.CustomNodes.FXListAnimation.AnimationType;
import com.EudyContreras.Snake.CustomNodes.FXListListener.FXListState;
import com.EudyContreras.Snake.Utilities.AnimationTimer;
import com.EudyContreras.Snake.Utilities.Animator;
import com.EudyContreras.Snake.Utilities.AnimatorListener;
import com.EudyContreras.Snake.Utilities.FillUtility;
import com.EudyContreras.Snake.Utilities.Interpolators;
import com.EudyContreras.Snake.Utilities.Period;
import com.EudyContreras.Snake.Utilities.ResizeAnimator;
import com.EudyContreras.Snake.Utilities.TimePeriod;

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
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class FXListView<T> {

	private final static int MAX_INITIAL_COUNT = 80;
	private final static int FRUSTUM_CULLING_OFFSET = 100;

	private boolean allowAnimations = true;
	private boolean allowSelfRestore = true;
	private boolean divider = false;

	private int animDuration = 250;

	private FXCallback<FXListView<T>, FXListCell<T>> callBack;

	private ObservableList<T> observableData;

	private FXListListener<T> listListener;

	private List<FXListCell<T>> nodeCollection;

	private Map<T, FXListCell<T>> pairCollection;

	private FXTransition fxListTranstion;

	private Transition removeTransition;
	private Transition newCellTransition;

	private FXListAnimation<T> animation;

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
		this.nodeCollection = new LinkedList<>();
		this.container.setContent(listLayout);
		this.container.setFitToWidth(true);
		this.listLayout.setFillWidth(true);
		this.listLayout.setMaxHeight(2000);
		this.observeChanges();
	}

	public FXListView(AddOrder order) {
		this(order,FXCollections.observableArrayList());;
	}

	private void observeChanges() {

		container.heightProperty().addListener((obs, oldValue, newValue) -> {

			double vPadding = container.getPadding().getTop()+container.getPadding().getBottom();

			listLayout.setMinHeight(newValue.doubleValue()-vPadding);

			double hPadding = container.getPadding().getRight()+container.getPadding().getLeft()+24;

			if (scrollBar != null) {
				if (scrollBar.isVisible()) {
					hPadding = hPadding + scrollBar.getWidth();
				} else {
					hPadding = container.getPadding().getRight() + container.getPadding().getLeft();
				}
			}

			listLayout.setMinWidth(container.getWidth()-hPadding);
		});

		container.vvalueProperty().addListener((obs, oldValue, newValue) -> {

			if (scrollBar == null) {
				scrollBar = getScrollBar(container, Orientation.VERTICAL);

				scrollBar.visibleProperty().addListener((o,oV,nV) -> {

					double padding = container.getPadding().getRight()+container.getPadding().getLeft();

					if (scrollBar.isVisible()) {
						padding = padding + scrollBar.getWidth();
					} else {
						padding = container.getPadding().getRight() + container.getPadding().getLeft();
					}

					listLayout.setMinWidth(container.getWidth()-padding);
				});

				scrollBar.addEventFilter(MouseEvent.MOUSE_DRAGGED,event -> {

				});
				scrollBar.addEventFilter(MouseEvent.MOUSE_RELEASED,event ->{
					cullCells(container,oldValue.doubleValue(),newValue.doubleValue());
			    });
			}

			cullCells(container,oldValue.doubleValue(),newValue.doubleValue());
		});

		container.sceneProperty().addListener((obs, oldScene, newScene) -> {

			if (newScene == null) {
				// not showing...
			} else {
//				int size = nodeCollection.size();
//
//				switch (addOrder) {
//				case BOTTOM:
//
//					for (int i = 0; i < MAX_INITIAL_COUNT; i++) {
//						if (i < size) {
//							FXListCell<T> cell = nodeCollection.get(nodeCollection.size()-1);
//							listLayout.getChildren().add(cell);
//							nodeCollection.remove(nodeCollection.size()-1);
//						}
//					}
//					break;
//				case TOP:
//					for (int i = 0; i < MAX_INITIAL_COUNT; i++) {
//						if (i < size) {
//							FXListCell<T> cell = nodeCollection.get(nodeCollection.size()-1);
//							listLayout.getChildren().add(0, cell);
//							nodeCollection.remove(nodeCollection.size()-1);
//						}
//					}
//					break;
//				}

//				cullCells(container);

				AnimationTimer.runLater(TimePeriod.millis(1200), ()->{

					listLayout.setMaxHeight(Integer.MAX_VALUE);
					switch (addOrder) {
					case BOTTOM:
						for (FXListCell<T> cell : nodeCollection) {
							listLayout.getChildren().add(cell);
						}
						break;
					case TOP:
						for (FXListCell<T> cell : nodeCollection) {
							listLayout.getChildren().add(0, cell);
						}
						break;
					}
				});
			}

			if (allowSelfRestore) {
				restoreDefaultTransitions();
				allowSelfRestore = false;
			}
		});

		observableData.addListener(new ListChangeListener<T>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends T> change) {
				while (change.next()) {
					if (change.wasAdded()) {
						if(change.getAddedSize()==1){
							T item = change.getAddedSubList().get(0);
							addItem(item, change.getAddedSize());
						}else{
							addAllItems(change.getAddedSubList());
						}
					} else if (change.wasRemoved()) {
						if(observableData.isEmpty()){
							clearItems();
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

	private void listenToRemovals(FXListCell<T> cell){
		if(listListener!=null)
		listListener.onUpdate(FXListState.CELL_REMOVE, cell);
	}

	private void listenToAdditions(FXListCell<T> cell){
		if(listListener!=null)
			listListener.onUpdate(FXListState.CELL_ADDED,cell);
	}

	private void cullCells(ScrollPane container) {
		cullCells(container,0,0);
	}

	private void cullCells(ScrollPane container, double oldValue, double newValue) {
		Bounds paneBounds = container.localToScene(container.getBoundsInParent());

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
				if(!cell.isRendered()){
					cell.render(true);
					animateOnSight(cell);
				}
			} else {
				if(cell.isRendered()){
					cell.render(false);
				}
			}
		}
	}

	private void animateOnSight(FXListCell<?> node) {
		KeyFrame[] frames = getKeyFrames(node, animDuration, animType);
		if (frames != null) {
			final Timeline timeline = new Timeline();
			timeline.getKeyFrames().addAll(frames);
			timeline.play();
		}
	}

	private boolean withinBounds(double offset, Bounds parent, Bounds child){
		return new Rectangle2D(
				parent.getMinX(),
				parent.getMinY()-offset,
				parent.getWidth(),
				parent.getHeight()+offset*2).
				intersects(
						child.getMinX(),
						child.getMinY(),
						child.getWidth(),
						child.getHeight());
	}

	@SuppressWarnings("unchecked")
	protected KeyFrame[] getKeyFrames(Node node, int duration, AnimationType... types) {
		KeyFrame[] frames = null;

		animation.setCell((FXListCell<T>) node);

		for (AnimationType type : types) {
			switch (type) {
			case FADE_OUT:
				frames = animation.fadeIn(node, duration, frames);
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
				frames = animation.getZoomIn(node, duration, 0.6, frames);
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
	private void repeat(int count, FXListEvent event){
		if(count>=0){
			for(int i = 0; i<count; i++){
				if(event!=null){
					event.fire();
				}
			}
		}
	}
//
//	private ScrollBar getScrollBar(ScrollPane table, Orientation orientation) {
//		ScrollBar result = null;
//		for (Node node : table.lookupAll(".scroll-bar")) {
//			if (node instanceof ScrollBar) {
//				ScrollBar bar = (ScrollBar) node;
//				if (bar.getOrientation().equals(orientation)) {
//					result = bar;
//				}
//			}
//		}
//		return result;
//	}

	@SuppressWarnings("unused")
	private void log(Object obj){
		System.out.println(obj);
	}


	private void populateList(ObservableList<T> list) {
		addAllItems(list);
	}

	private void addCell(int index, FXListCell<T> cell) {
		if (allowAnimations) {
			animateAddition(cell, () -> {
				listLayout.getChildren().add(index,cell);
				listenToAdditions(cell);
			}, () -> {
				addDividers(cell);
			});
		} else {
			listLayout.getChildren().add(index,cell);
			listenToAdditions(cell);
			addDividers(cell);
		}

	}

	private void removeCell(T item) {
		FXListCell<T> cell = pairCollection.get(item);
		listenToRemovals(cell);

		if(allowAnimations){
		animateRemoval(cell,()->{
			listLayout.getChildren().remove(cell);
		});
		}else{
			listLayout.getChildren().remove(cell);
		}
	}

	private void animateRemoval(FXListCell<T> node, FXListEvent event) {
		if (node.getDeleteTransition() != null) {

			EventHandler<ActionEvent> actionEvent = node.getDeleteTransition().getOnFinished();

			node.getDeleteTransition().setOnFinished(null);

			node.getDeleteTransition().setOnFinished(e -> {
				if(actionEvent!=null){
					actionEvent.handle(e);
				}
				removeCellAnimation(node, event);

			});
			node.getDeleteTransition().play();

		} else if (removeTransition != null) {

			Transition transition = fxListTranstion.getTransition(removeTransition,node);

			EventHandler<ActionEvent> actionEvent = transition.getOnFinished();

			transition.setOnFinished(null);

			transition.setOnFinished(e -> {

				if(actionEvent!=null){
					actionEvent.handle(e);
				}

				removeCellAnimation(node, event);
			});

			transition.play();
		}else{
			removeCellAnimation(node, event);
		}

	}

	private void removeCellAnimation(Region node, FXListEvent event) {
		ResizeAnimator resize = new ResizeAnimator(ResizeAnimator.RESIZE_HEIGHT);
		resize.setInterpolator(Interpolators.getEasingInstance(0,0));
		resize.setDuration(TimePeriod.millis(250));
		resize.setStartHeight(node.getBoundsInParent().getHeight());
		resize.setEndHeight(0);
		resize.setRegion(node);
		resize.setOnFinished(()->{
			cullCells(container);
			if(event!=null){
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
			}
			@Override
			public void OnRepeat(Animator animation) {}

			@Override
			public void OnEnd(Animator animation) {
				node.setOpacity(1);

				addCellAnimation(node);

				cullCells(container);

				if(endEvent!=null){
					endEvent.fire();
				}
			}
		});

		resize.setStartHeight(0);
		resize.setEndHeight(node.getBoundsInParent().getHeight());
		resize.setRegion(node);
		resize.play();
	}

	private void addCellAnimation(FXListCell<T> node) {
		if(node.getAddedTransition()!=null){
			node.getAddedTransition().play();

		}else if(newCellTransition!=null){
			Transition transition = fxListTranstion.getTransition(newCellTransition,node);
			transition.play();
		}
	}

	private void addItem(T item, int count) {
		FXListCell<T> cell = callBack.call(this);
		cell.createCell(item);
		pairCollection.put(item, cell);
		switch(addOrder){
		case BOTTOM:
			addCell(listLayout.getChildren().size()-1, cell);
			break;
		case TOP:
			addCell(0, cell);
			break;
		}
	}

	private void addAllItems(List<? extends T> nodes) {
		int index = 0;
		for(T item: nodes){
			FXListCell<T> cell = callBack.call(FXListView.this);
			cell.createCell(item);
			if(index<nodes.size()-MAX_INITIAL_COUNT){
				cell.render(false);
			}
			addDividers(cell);
			listenToAdditions(cell);
			nodeCollection.add(cell);
			pairCollection.put(item, cell);
			index++;
		}
	}

	private void removeItem(T item) {
		removeCell(item);
		pairCollection.remove(item);
	}

	private void clearItems(){
		listLayout.getChildren().clear();
		pairCollection.clear();
	}

	public void add(T item){
		observableData.add(item);
	}

	public void addAll(@SuppressWarnings("unchecked") T... items){
		observableData.addAll(items);
	}

	public void remove(T item){
		observableData.remove(item);
	}

	public void removeAll(@SuppressWarnings("unchecked") T... items){
		observableData.removeAll(items);
	}

	public void clear(){
		listLayout.getChildren().clear();
		pairCollection.clear();
		observableData.clear();
	}

	public void addListListener(FXListListener<T> listener){
		this.listListener = listener;
	}

	public void removeListListener(){
		this.listListener = null;
	}

	public ObservableList<T> getItems() {
		return observableData;
	}

	public List<FXListCell<T>> getCells(){
		return new ArrayList<FXListCell<T>>(pairCollection.values());
	}

	public void setCellFactory(FXCallback<FXListView<T>, FXListCell<T>> callBack) {
		this.callBack = callBack;
		if (observableData != null) {
			if (!observableData.isEmpty()) {
				this.populateList(observableData);
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

	public void setCellAddedTransition(Transition transition){
		this.newCellTransition = transition;
	}

	public void setCellDeletedTransition(Transition transition){
		this.removeTransition = transition;
	}

	public void setScrollAnimation(AnimationType animation){
		this.animType = animation;
	}

	public void restoreDefaultTransitions(){
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

	public void setScrollAnimationDuration(Period duration){
		this.animDuration = (int)duration.getDuration();
	}

	public void setSpacing(double spacing){
		listLayout.setSpacing(spacing);
	}

	public void setPadding(Insets insets){
		container.setPadding(insets);
	}

	public void setFill(Paint fill) {
		container.setBackground(FillUtility.PAINT_FILL(fill));
		listLayout.setBackground(FillUtility.PAINT_FILL(fill));
	}

	private void addDividers(Node node){
		if(divider){
			String style = "-fx-border-color: rgb(60,60,60);"+
					"-fx-border-width: 0 0 2 0;";
			node.setStyle(style);
		}else{
			node.setStyle(null);
		}
	}

	public void setDividers(boolean state){
		this.divider = state;
	}

	public void setSize(double width, double height){
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
		return container;
	}

	public enum AddOrder{
		TOP,BOTTOM
	}

}
