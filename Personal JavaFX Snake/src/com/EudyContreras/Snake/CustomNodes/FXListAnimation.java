package com.EudyContreras.Snake.CustomNodes;

import com.EudyContreras.Snake.Utilities.ArrayUtils;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Shear;
import javafx.util.Duration;

public class FXListAnimation<VALUE> {

	public FXListCell<VALUE> cell;

	final Timeline timeLine = new Timeline();

	final Timeline reversedTimeLine = new Timeline();

	public Shear shear;

	public Rotate rotate;

	public FXListCell<VALUE> getCell() {
		try {
			return (FXListCell<VALUE>) cell;
		} catch (ClassCastException cce) {
			System.out.println(cce);
			return null;
		}
	}

	public void setCell(FXListCell<VALUE> cell) {
		this.cell = cell;
	}

	public KeyFrame[] getFlatternOut(Node node, int animDuration, KeyFrame... frames) {
		return new KeyFrame[] {
				new KeyFrame(Duration.millis(0), new KeyValue(node.scaleXProperty(), 0)),
				new KeyFrame(Duration.millis(0), new KeyValue(node.scaleYProperty(), 0.9)),
				new KeyFrame(Duration.millis(animDuration * 0.4), new KeyValue(node.scaleXProperty(), 0.001)),
				new KeyFrame(Duration.millis(animDuration * 0.6),new KeyValue(node.scaleXProperty(), 1.2, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(animDuration), new KeyValue(node.scaleYProperty(), 1)),
				new KeyFrame(Duration.millis(animDuration), new KeyValue(node.scaleXProperty(), 1, Interpolator.EASE_BOTH)) };

	}

	public KeyFrame[] getPopOut(Node node, int animDuration, KeyFrame... frames) {
		if (rotate == null) {
			rotate = new Rotate();
			rotate.setAxis(Rotate.Y_AXIS);
			rotate.pivotYProperty().bind(getCell().heightProperty().divide(2));
			node.getTransforms().add(rotate);
		}
		return new KeyFrame[] {
				new KeyFrame(Duration.millis(0), new KeyValue(node.scaleXProperty(), 0.4, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(0), new KeyValue(node.scaleYProperty(), 0.4, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(animDuration * 0.8),new KeyValue(node.scaleXProperty(), 1.3, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(animDuration * 0.8),new KeyValue(node.scaleYProperty(), 1.3, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(animDuration),new KeyValue(node.scaleXProperty(), 1, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(animDuration),new KeyValue(node.scaleYProperty(), 1, Interpolator.EASE_BOTH)), };
	}

	public KeyFrame[] getFlapRight(Node node, int animDuration, KeyFrame... frames) {
		rotate = new Rotate();
		rotate.setAxis(Rotate.Y_AXIS);
		rotate.pivotYProperty().bind(getCell().heightProperty().divide(2));

		if (!node.getTransforms().contains(rotate)) {
			node.getTransforms().add(rotate);
		}

		return getZoomIn(node, animDuration, 0.9d,
				ArrayUtils.CONCAT(frames,new KeyFrame[] {
								new KeyFrame(Duration.millis(0),new KeyValue(rotate.angleProperty(), -90, Interpolator.EASE_BOTH)),
								new KeyFrame(Duration.millis(animDuration),new KeyValue(rotate.angleProperty(), 0, Interpolator.EASE_IN)), }));
	}

	public KeyFrame[] getFlyFromUp(Node node, int animDuration, KeyFrame... frames) {
		rotate = new Rotate();
		rotate.setAxis(Rotate.X_AXIS);
		rotate.pivotXProperty().bind(getCell().widthProperty().divide(2));
		rotate.pivotYProperty().bind(getCell().heightProperty().divide(2));
		if (!node.getTransforms().contains(rotate)) {
			node.getTransforms().add(rotate);
		}

		return getZoomIn(node, animDuration, 0.7d, ArrayUtils.CONCAT(frames, new KeyFrame[] {
				new KeyFrame(Duration.millis(0), new KeyValue(rotate.angleProperty(), 90, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(0),new KeyValue(node.translateYProperty(), -getCell().prefHeightProperty().get(),Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(0), new KeyValue(rotate.pivotZProperty(), 90, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(animDuration),new KeyValue(rotate.angleProperty(), 0, Interpolator.EASE_IN)),
				new KeyFrame(Duration.millis(animDuration),new KeyValue(node.translateYProperty(), 0, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(animDuration),new KeyValue(rotate.pivotZProperty(), 0, Interpolator.EASE_IN)), }));
	}

	public KeyFrame[] getFlyFromDown(Node node, int animDuration, KeyFrame... frames) {
		rotate = new Rotate();
		rotate.setAxis(Rotate.X_AXIS);
		rotate.pivotXProperty().bind(getCell().widthProperty().divide(2));
		rotate.pivotYProperty().bind(getCell().heightProperty().divide(2));
		if (!node.getTransforms().contains(rotate)) {
			node.getTransforms().add(rotate);
		}
		return getZoomIn(node, animDuration, 0.7d, ArrayUtils.CONCAT(frames, new KeyFrame[] {
				new KeyFrame(Duration.millis(0), new KeyValue(rotate.angleProperty(), -90, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(0), new KeyValue(rotate.pivotZProperty(), -90, Interpolator.EASE_BOTH)),
				new KeyFrame(Duration.millis(animDuration),new KeyValue(rotate.angleProperty(), 0, Interpolator.EASE_IN)),
				new KeyFrame(Duration.millis(animDuration),new KeyValue(rotate.pivotZProperty(), 0, Interpolator.EASE_IN)), }));
	}

	public KeyFrame[] getRotateYRight(Node node, int animDuration, KeyFrame... frames) {
		rotate = new Rotate();
		rotate.setAxis(Rotate.Y_AXIS);
		rotate.pivotXProperty().bind(getCell().widthProperty().divide(2));
		rotate.pivotYProperty().bind(getCell().heightProperty().divide(2));
		if (!node.getTransforms().contains(rotate)) {
			node.getTransforms().add(rotate);
		}

		return getZoomIn(node, animDuration, 0.7d,
				ArrayUtils.CONCAT(frames,
						new KeyFrame[] {
								new KeyFrame(Duration.millis(0), new KeyValue(rotate.angleProperty(), -180)),
								new KeyFrame(Duration.millis(animDuration), new KeyValue(rotate.angleProperty(), 0)), }));
	}

	public KeyFrame[] getSpeedLeft(Node node, int animDuration, KeyFrame... frames) {

		shear = new Shear();
		shear.setPivotX(100);
		shear.setPivotY(35);
		shear.setY(0);

		if (!node.getTransforms().contains(shear)) {
			node.getTransforms().add(shear);
		}
		return ArrayUtils.CONCAT(frames,
				getTransitionLeft(node, animDuration,
						new KeyFrame(Duration.millis(0), new KeyValue(shear.xProperty(), -0.5, Interpolator.EASE_IN)),
						new KeyFrame(Duration.millis(animDuration),new KeyValue(shear.xProperty(), 0, Interpolator.EASE_OUT))));
	}

	public KeyFrame[] getSpeedRight(Node node, int animDuration, KeyFrame... frames) {

		if (shear == null) {
			shear = new Shear();
			shear.setPivotX(100);
			shear.setPivotY(35);
			shear.setY(0);
			node.getTransforms().add(shear);
		}

		return ArrayUtils.CONCAT(frames,
				getTransitionRight(node, animDuration,
						new KeyFrame(Duration.millis(0), new KeyValue(shear.xProperty(), 0.5)),
						new KeyFrame(Duration.millis(animDuration), new KeyValue(shear.xProperty(), 0))));
	}

	public KeyFrame[] getTransitionTop(Node node, int animDuration, KeyFrame... frames) {
		return ArrayUtils.CONCAT(frames,
				new KeyFrame[] {
						new KeyFrame(Duration.millis(0),new KeyValue(node.translateYProperty(), Math.max(getCell().getHeight(), 50))),
						new KeyFrame(Duration.millis(animDuration), new KeyValue(node.translateYProperty(), 0)), });
	}

	public KeyFrame[] getTransitionDown(Node node, int animDuration, KeyFrame... frames) {
		return new KeyFrame[] {
				new KeyFrame(Duration.millis(0),new KeyValue(node.translateYProperty(), Math.min(-getCell().getHeight(), -50))),
				new KeyFrame(Duration.millis(animDuration), new KeyValue(node.translateYProperty(), 0)), };
	}

	public KeyFrame[] getTransitionLeft(Node node, int animDuration, KeyFrame... frames) {
		return ArrayUtils.CONCAT(frames,
				new KeyFrame[] {
						new KeyFrame(Duration.millis(0),new KeyValue(node.translateXProperty(), Math.max(getCell().getWidth(), 200))),
						new KeyFrame(Duration.millis(animDuration), new KeyValue(node.translateXProperty(), 0)), });
	}

	public KeyFrame[] getTransitionRight(Node node, int animDuration, KeyFrame... frames) {
		return ArrayUtils.CONCAT(frames,
				new KeyFrame[] {
						new KeyFrame(Duration.millis(0),new KeyValue(node.translateXProperty(), Math.min(-getCell().getWidth(), -200))),
						new KeyFrame(Duration.millis(animDuration), new KeyValue(node.translateXProperty(), 0)), });
	}

	public KeyFrame[] getZoomIn(Node node, int animDuration, double from, KeyFrame... frames) {
		return ArrayUtils.CONCAT(frames,
				new KeyFrame[] {
						new KeyFrame(Duration.millis(0), new KeyValue(node.scaleXProperty(), from)),
						new KeyFrame(Duration.millis(0), new KeyValue(node.scaleYProperty(), from)),
						new KeyFrame(Duration.millis(animDuration), new KeyValue(node.scaleXProperty(), 1)),
						new KeyFrame(Duration.millis(animDuration), new KeyValue(node.scaleYProperty(), 1))});
	}

	public KeyFrame[] fadeIn(Node node, int animDuration, KeyFrame... frames) {
		return ArrayUtils.CONCAT(frames,
				new KeyFrame[] {
						new KeyFrame(Duration.millis(0), new KeyValue(node.opacityProperty(), 0)),
						new KeyFrame(Duration.millis(animDuration), new KeyValue(node.opacityProperty(), 1)) });
	}

	public ObservableList<KeyFrame> getKeyFrames() {
		return timeLine.getKeyFrames();
	}

	public Timeline getTimeline() {
		return timeLine;
	}

	public Timeline getReversedTimeline() {
		Duration start = null;
		Duration end = null;
		for (KeyFrame f : timeLine.getKeyFrames()) {
			Duration dur = f.getTime();
			if (start == null && end == null) {
				start = end = dur;
			}
			if (dur.greaterThan(end)) {
				end = dur;
			}
			if (dur.lessThan(start)) {
				start = dur;
			}
		}

		for (KeyFrame f : timeLine.getKeyFrames()) {
			Duration dur = f.getTime();
			reversedTimeLine.getKeyFrames()
					.add(new KeyFrame(Duration.millis((1 - (dur.toMillis() / end.toMillis())) * end.toMillis()),
							f.getValues().toArray(new KeyValue[f.getValues().size()])));
		}

		return reversedTimeLine;
	}
}
