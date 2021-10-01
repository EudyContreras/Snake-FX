package com.EudyContreras.Snake.CustomNodes;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;

public class FXTransition {

	private TranslateTransition translate;
	private RotateTransition rotate;
	private ScaleTransition scale;
	private FadeTransition fade;
	private SequentialTransition sequential;
	private ParallelTransition parallel;


	public TranslateTransition TranslateTransition(Transition transition){
		translate = (TranslateTransition) transition;
		return translate;
	}

	public ScaleTransition ScaleTransition(Transition transition){
		scale = (ScaleTransition) transition;
		return scale;
	}

	public FadeTransition FadeTransition(Transition transition){
		fade = (FadeTransition) transition;
		return fade;
	}

	public RotateTransition RotateTransition(Transition transition){
		rotate = (RotateTransition) transition;
		return rotate;
	}

	public ParallelTransition ParallelTransition(Transition transition){
		parallel = (ParallelTransition) transition;
		return parallel;
	}

	public SequentialTransition SequentialTransition(Transition transition){
		sequential = (SequentialTransition) transition;
		return sequential;
	}

	public Transition getTransition(Transition transition, Node node) {
		TransitionType type = getType(transition);

		createType(transition, type);

		switch (type) {
		case FADE:
			return fade;
		case PARALLEL:
			return parallel;
		case ROTATE:
			return rotate;
		case SCALE:
			ScaleTransition scale = new ScaleTransition();
			scale.setFromX(this.scale.getFromX());;
			scale.setFromY(this.scale.getFromY());
			scale.setToX(this.scale.getToX());
			scale.setToY(this.scale.getToY());
			scale.setDuration(this.scale.getDuration());
			scale.setOnFinished(this.scale.getOnFinished());
			scale.setInterpolator(this.scale.getInterpolator());
			scale.setRate(this.scale.getRate());
			scale.setNode(node);
			return scale;
		case SEQUENTIAL:
			return sequential;
		case TRANSLATE:
			TranslateTransition translate = new TranslateTransition();
			translate.setByX(this.translate.getByX());
			translate.setByY(this.translate.getByY());
			translate.setToX(this.translate.getToX());
			translate.setToY(this.translate.getToY());
			translate.setFromX(this.translate.getFromX());
			translate.setFromY(this.translate.getFromY());
			translate.setFromZ(this.translate.getFromZ());
			translate.setAutoReverse(this.translate.isAutoReverse());
			translate.setDelay(this.translate.getDelay());
			translate.setDuration(this.translate.getDuration());
			translate.setOnFinished(this.translate.getOnFinished());
			translate.setInterpolator(this.translate.getInterpolator());
			translate.setNode(node);
			return translate;
		}
		return null;
	}

	private void createType(Transition transition, TransitionType type) {
		switch(type){
		case FADE:
			FadeTransition(transition);
			break;
		case PARALLEL:
			ParallelTransition(transition);
			break;
		case ROTATE:
			RotateTransition(transition);
			break;
		case SCALE:
			ScaleTransition(transition);
			break;
		case SEQUENTIAL:
			SequentialTransition(transition);
			break;
		case TRANSLATE:
			TranslateTransition(transition);
			break;
		}
	}

	public static TransitionType getType(Transition transition){
		if(transition instanceof TranslateTransition){
			return TransitionType.TRANSLATE;
		}else if(transition instanceof ScaleTransition){
			return TransitionType.SCALE;
		}else if(transition instanceof RotateTransition){
			return TransitionType.ROTATE;
		}else if(transition instanceof FadeTransition){
			return TransitionType.FADE;
		}else if(transition instanceof ParallelTransition){
			return TransitionType.PARALLEL;
		}else if(transition instanceof SequentialTransition){
			return TransitionType.SEQUENTIAL;
		}
		return null;
	}

	public static void addTransition(Transition transition, Node node){
		if(transition instanceof TranslateTransition){
			((TranslateTransition)transition).setNode(node);
		}else if(transition instanceof ScaleTransition){
			((ScaleTransition)transition).setNode(node);
		}else if(transition instanceof RotateTransition){
			((RotateTransition)transition).setNode(node);
		}else if(transition instanceof FadeTransition){
			((FadeTransition)transition).setNode(node);
		}else if(transition instanceof ParallelTransition){
			((ParallelTransition)transition).setNode(node);
		}else if(transition instanceof SequentialTransition){
			((SequentialTransition)transition).setNode(node);
		}
	}

	public static void removeTransition(Transition transition, Node node){
		if(transition instanceof TranslateTransition){
			((TranslateTransition)transition).setNode(null);
		}else if(transition instanceof ScaleTransition){
			((ScaleTransition)transition).setNode(null);
		}else if(transition instanceof RotateTransition){
			((RotateTransition)transition).setNode(null);
		}else if(transition instanceof FadeTransition){
			((FadeTransition)transition).setNode(null);
		}else if(transition instanceof ParallelTransition){
			((ParallelTransition)transition).setNode(null);
		}else if(transition instanceof SequentialTransition){
			((SequentialTransition)transition).setNode(null);
		}
	}


	public enum TransitionType{
		TRANSLATE,ROTATE,FADE,SCALE,SEQUENTIAL,PARALLEL
	}

}
