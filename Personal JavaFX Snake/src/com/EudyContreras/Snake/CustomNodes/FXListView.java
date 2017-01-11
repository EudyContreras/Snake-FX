package com.EudyContreras.Snake.CustomNodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.EudyContreras.Snake.CustomNodes.FXListListener.FXListState;
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
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class FXListView<T> {

	private final static int MAX_INITIAL_COUNT = 20;
	private final static int MAX_TO_ADD_ON_SCROLL = 20;
	private final static int FRUSTUM_CULLING_OFFSET = 50;

	private boolean allowAnimations = true;
	private boolean allowSelfRestore = true;
	private boolean autoRefresh = true;
	private boolean divider = false;

	private int animDuration = 400;

	private FXCallback<FXListView<T>, FXListCell<T>> callBack;

	private ObservableList<T> observableData;

	private List<Node> nodeCollection;

	private FXListListener<T> listListener;

	private Map<T, FXListCell<T>> pairCollection;

	private FXTransition fxListTranstion;

	private Transition removeTransition;
	private Transition newCellTransition;

	private FXListAnimation<T> animation;

	private ScrollPane container;

	private ScrollBar scrollBar;

	private VBox listLayout;

	private AnimationType animType = AnimationType.NONE;

	private AddOrder order;


	public FXListView(ObservableList<T> data, AddOrder order) {
		this.observableData = data;
		this.order = order;
		this.divider = true;
		this.listLayout = new VBox();
		this.container = new ScrollPane();
		this.nodeCollection = new LinkedList<>();
		this.pairCollection = new LinkedHashMap<>();
		this.animation = new FXListAnimation<>();
		this.fxListTranstion = new FXTransition();
		this.container.setContent(listLayout);
		this.observeChanges();
		this.monitorScrolls();
	}

	public FXListView(ObservableList<T> data) {
		this(data,AddOrder.BOTTOM);
	}

	public FXListView(AddOrder order) {
		this(FXCollections.observableArrayList(),order);
	}

	public FXListView() {
		this(FXCollections.observableArrayList());;
	}

	private void observeChanges() {

		container.vvalueProperty().addListener((obs, oldValue, newValue) -> {
			double value = newValue.doubleValue();

			if (scrollBar == null) {
				scrollBar = getScrollBar(container, Orientation.VERTICAL);
			}

			if (value == scrollBar.getMax()) {

				double targetValue = value * observableData.size();

				addCell(MAX_TO_ADD_ON_SCROLL);

				scrollBar.setValue(targetValue / observableData.size());
			}

			cullCells(container,oldValue.doubleValue(),newValue.doubleValue());
		});

		container.sceneProperty().addListener((obs, oldScene, newScene) -> {
			if (newScene == null) {
				resetCells(false);
			}else{
				resetCells(true);
				if (allowSelfRestore) {
					restoreDefaultTransitions();
					allowSelfRestore = false;
				}
			}
		});

		observableData.addListener(new ListChangeListener<T>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends T> change) {
				while (change.next()) {
					if (change.wasAdded()) {
						for (T item : change.getAddedSubList()) {
							addItem(item, change.getAddedSize());
							cullCells(container);
						}
					} else if (change.wasRemoved()) {
						if(observableData.isEmpty()){
							clearItems();
						} else {
							for (T item : change.getRemoved()) {
								removeItem(item);
								cullCells(container);
							}
						}
					}
				}
			}
		});
	}

	private void monitorScrolls() {
		listLayout.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				double deltaY = event.getDeltaY()/16;
				double width = container.getContent().getBoundsInLocal().getWidth();
				double vvalue = container.getVvalue();
				container.setVvalue(vvalue + -deltaY/width);
			}
		});
	}

	private void listenToRemovals(FXListCell<T> cell){
		if(listListener!=null)
		listListener.onUpdate(FXListState.CELL_REMOVE, cell);
	}

	private void listenToAdditions(FXListCell<T> cell){
		if(listListener!=null)
			listListener.onUpdate(FXListState.CELL_ADDED,cell);
	}

	private void resetCells(boolean state) {
		if(state){
			listLayout.getChildren().stream().parallel().forEach(node-> node.setVisible(true));
		}else{
			ObservableList<Node> nodes = listLayout.getChildren();

			switch(order){
			case BOTTOM:
				for(int i = nodes.size()-1; i>=0; i--){
					nodeCollection.add(0,nodes.get(i));
				}
				break;
			case TOP:
				for(int i = nodes.size()-1; i>=0; i--){
					nodeCollection.add(nodes.get(i));
				}
				break;
			}

			listLayout.getChildren().clear();

			addCell(MAX_TO_ADD_ON_SCROLL);

			container.setVvalue(0);
		}
	}

	private void cullCells(ScrollPane container) {
		cullCells(container,0,0);
	}

	private void cullCells(ScrollPane container, double oldValue, double newValue) {
		Bounds paneBounds = container.localToScene(container.getBoundsInParent());
		if (container.getContent() instanceof Parent) {
			for (Node node : ((Parent) container.getContent()).getChildrenUnmodifiable()) {
				Bounds nodeBounds = node.localToScene(node.getBoundsInLocal());
				if (withinBounds(FRUSTUM_CULLING_OFFSET*2, paneBounds, nodeBounds)) {
					if(!node.isVisible()){
						node.setVisible(true);
						animateOnSight(node);
					}
				} else {
					if(node.isVisible()){
						node.setVisible(false);
					}
				}
			}
		}
	}

	private void animateOnSight(Node node) {
		KeyFrame[] frames = getKeyFrames(node, animDuration, animType);
		if (frames != null) {
			final Timeline timeline = new Timeline();
			timeline.getKeyFrames().addAll(frames);
			timeline.play();
		}
	}

	private boolean withinBounds(double offset, Bounds parent, Bounds child){
		return new Rectangle2D(
				parent.getMinX()-offset,
				parent.getMinY()-offset,
				parent.getWidth()+offset*2,
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
				frames = animation.getZoomIn(node, duration, 0, frames);
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

	public void log(Object obj){
		System.out.println(obj);
	}

	private void addCell(int max) {
		if (nodeCollection.size() > max) {
			for (int i = 0; i < max; i++) {
				Node node = null;
				switch(order){
				case BOTTOM:
					node = nodeCollection.get(0);
					node.setCache(true);
					node.setCacheHint(CacheHint.SPEED);
					listLayout.getChildren().add(node);
					nodeCollection.remove(node);
					break;
				case TOP:
					node = nodeCollection.get(nodeCollection.size()-1);
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
				switch(order){
				case BOTTOM:
					node = nodeCollection.get(0);
					node.setCache(true);
					node.setCacheHint(CacheHint.SPEED);
					listLayout.getChildren().add(node);
					nodeCollection.remove(node);
					break;
				case TOP:
					node = nodeCollection.get(nodeCollection.size()-1);
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

	private void populate(ObservableList<T> list) {
		list.stream().forEach(item -> addItem(item,list.size()));
	}

	private void addCell(FXListCell<T> cell) {
//		if (listLayout.getChildren().size() < MAX_INITIAL_COUNT) {
			if(allowAnimations){
			animateAddition(cell,()->{
				switch(order){
				case BOTTOM:
					listLayout.getChildren().add(cell);
					break;
				case TOP:
					listLayout.getChildren().add(0,cell);
					break;
				}
				listenToAdditions(cell);
			},()->{addDividers(cell);});
			}else{
				switch(order){
				case BOTTOM:
					listLayout.getChildren().add(cell);
					break;
				case TOP:
					listLayout.getChildren().add(0,cell);
					break;
				}
				listenToAdditions(cell);
				addDividers(cell);
			}
//		} else {
//			nodeCollection.add(cell);
//			addDividers(cell);
//		}
	}

	private void removeCell(T item) {
		FXListCell<T> cell = pairCollection.get(item);
		listenToRemovals(cell);

		if(allowAnimations){
		animateRemoval(cell,()->{
			listLayout.getChildren().remove(cell);
			nodeCollection.remove(cell);
			checkState();
		});
		}else{
			listLayout.getChildren().remove(cell);
			nodeCollection.remove(cell);
			checkState();
		}
	}

	private void checkState() {
		int size = listLayout.getChildren().size();
		if(size<MAX_INITIAL_COUNT){
			addCell(MAX_INITIAL_COUNT-size);
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
			if(event!=null){
				event.fire();
			}
		});
		resize.play();
	}

	private void animateAddition(FXListCell<T> node, FXListEvent startEvent, FXListEvent endEvent){
//		startEvent.fire();
//		endEvent.fire();
		ResizeAnimator resize = new ResizeAnimator(ResizeAnimator.RESIZE_HEIGHT);
		resize.setInterpolator(Interpolators.getEasingInstance(0,0));
		resize.setDuration(TimePeriod.millis(250));
		resize.addListener(new AnimatorListener(){
			@Override
			public void OnStart(Animator animation) {
				if(node.getAddedTransition()!=null | newCellTransition!=null){
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
		cell.setCache(true);
		cell.setCacheHint(CacheHint.SPEED);
		pairCollection.put(item, cell);
		if(count==1){
			addCell(cell);
		}else{
			switch(order){
			case BOTTOM:
				listLayout.getChildren().add(cell);
				break;
			case TOP:
				listLayout.getChildren().add(0,cell);
				break;
			}
			listenToAdditions(cell);
			addDividers(cell);
		}
	}

	public final AddOrder getAddOrder(){
		return order;
	}

	private void removeItem(T item) {
		removeCell(item);
		pairCollection.remove(item);
	}

	private void clearItems(){
		listLayout.getChildren().clear();
		nodeCollection.clear();
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
		nodeCollection.clear();
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
				this.populate(observableData);
			}
		}
	}

	public FXCallback<FXListView<T>, FXListCell<T>> getCellFactory() {
		return callBack;
	}

	public void setAutoRefresh(boolean state){
		this.autoRefresh = state;
	}

	public boolean getAutoRefresh(){
		return autoRefresh;
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
		listLayout.setPrefWidth(width-30);
		listLayout.setMinWidth(width-30);
		container.setMinWidth(width);
	}

	public void setHeight(double height) {
		container.setMaxHeight(height);
		container.setPrefHeight(height);
		container.setMinHeight(height);
		listLayout.setMinHeight(height);
	}

	public double getWidth() {
		return container.getPrefWidth();
	}

	public double getHeight() {
		return container.getPrefHeight();
	}

	public Region get() {
		return container;
	}

	public enum AddOrder{
		TOP,BOTTOM
	}

	public enum Edge{
		TOP,BOTTOM

	}
}
