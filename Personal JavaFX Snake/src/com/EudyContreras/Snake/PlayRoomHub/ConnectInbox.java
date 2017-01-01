package com.EudyContreras.Snake.PlayRoomHub;
import java.util.Random;

import com.EudyContreras.Snake.Utilities.ShapeUtility;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ConnectInbox{

    private Pane root = new Pane();
    private Mail mail = new Mail(97,40);
    private SlipUp slipUp = new SlipUp();
    private SlipUp slipUpInner = new SlipUp();
    private SlipLeft left = new SlipLeft();
    private SlipRight right = new SlipRight();
    private DropShadow shadow = new DropShadow();
    private Rectangle clickBounds = new Rectangle(80,80);
    private Notification notification = new Notification();
    private TranslateTransition translate = new TranslateTransition();
    private ScaleTransition scale = new ScaleTransition();
    private RotateTransition rotate = new RotateTransition();
    private ParallelTransition transitions = new ParallelTransition();
    private ConnectHub connect;
    private boolean showingMail = false;
    private boolean animating = false;

	public ConnectInbox(ConnectHub connect) {
		this.connect = connect;
		mail.setSize(97, 40);
		mail.get().setLayoutY(40);
		notification.increaseCount(50);
		notification.setLocation(80, 25);
		shadow.setBlurType(BlurType.THREE_PASS_BOX);
		shadow.setColor(Color.color(0, 0, 0, 0.5));
		shadow.setOffsetX(-4);
		shadow.setOffsetY(-2);
		shadow.setRadius(16);
		root.setEffect(shadow);
		clickBounds.setX(15);
		clickBounds.setOpacity(0);
		clickBounds.setOnMousePressed(e->showMessage());
		slipUpInner.relocate(0, 10);
		slipUp.setFill(ShapeUtility.LINEAR_GRADIENT(Color.SILVER, Color.GRAY.brighter()));
		left.setFill(ShapeUtility.LINEAR_GRADIENT(Color.GRAY, Color.GRAY, Color.SILVER));
		right.setFill(ShapeUtility.LINEAR_GRADIENT(Color.SILVER, Color.GRAY, Color.SILVER));
		slipUpInner.setFill(ShapeUtility.LINEAR_GRADIENT(Color.WHITE, Color.SILVER, Color.SILVER));
		root.getChildren().addAll(slipUp, slipUpInner, mail.get(), left, right, notification.get(), clickBounds);
    	transitions.getChildren().add(translate);
    	transitions.getChildren().add(rotate);
    	transitions.getChildren().add(scale);
	}

    public Pane get(){
    	return root;
    }

	public void showMessage() {
		if (!animating) {
			if (showingMail) {
				animating = true;

				translate.setNode(mail.get());
				translate.setDuration(Duration.millis(500));
				translate.setInterpolator(Interpolator.EASE_OUT);
				translate.setToX(0);
				translate.setToY(-115);

				rotate.setNode(mail.get());
				rotate.setDuration(Duration.millis(500));
				rotate.setInterpolator(Interpolator.EASE_OUT);
				rotate.setToAngle(0);

				scale.setNode(mail.get());
				scale.setDuration(Duration.millis(500));
				scale.setInterpolator(Interpolator.EASE_OUT);
				scale.setToX(1);
				scale.setToY(1);

				transitions.setOnFinished(e->{
					animating = false;
					showingMail = false;
					mail.animate(true, null);
				});

				transitions.play();
			} else {
				animating = true;

				translate.setNode(mail.get());
				translate.setDuration(Duration.millis(650));
				translate.setInterpolator(Interpolator.EASE_OUT);
				translate.setToX(-550);
				translate.setToY(355);

				rotate.setNode(mail.get());
				rotate.setDuration(Duration.millis(650));
				rotate.setInterpolator(Interpolator.EASE_OUT);
				rotate.setToAngle(-360*2);

				scale.setNode(mail.get());
				scale.setDuration(Duration.millis(650));
				scale.setInterpolator(Interpolator.EASE_OUT);
				scale.setToX(7);
				scale.setToY(15);

				mail.animate(false, ()->transitions.play());

				transitions.setOnFinished(e -> {
					showingMail = true;
					animating = false;
				});

			}
		}
	}

    public void increaseCounter(){
    	notification.increaseCount(1);
    }

    public void decreaseCounter(){
    	notification.decreaseCount(1);
    }

	private class SlipRight extends Path {
		public SlipRight() {

			DropShadow shadow = new DropShadow();
			LineTo[] line = new LineTo[3];
			MoveTo moveTo = new MoveTo();

			shadow.setBlurType(BlurType.THREE_PASS_BOX);
			shadow.setColor(Color.color(0, 0, 0,0.5));
			shadow.setOffsetX(-4);
			shadow.setOffsetY(-2);

			moveTo.setX(150);
			moveTo.setY(100);

			line[0] = new LineTo();
			line[0].setX(150);
			line[0].setY(50);

			line[1] = new LineTo();
			line[1].setX(50);
			line[1].setY(100);

			line[2] = new LineTo();
			line[2].setX(150);
			line[2].setY(100);

			setStroke(Color.SILVER);
			setEffect(shadow);
			getElements().add(moveTo);
			getElements().addAll(line);
			relocate(0, 32);
		}
    }

	private class SlipLeft extends Path {
		public SlipLeft() {

			LineTo[] line = new LineTo[3];
			MoveTo moveTo = new MoveTo();

			moveTo.setX(150);
			moveTo.setY(100);

			line[0] = new LineTo();
			line[0].setX(50);
			line[0].setY(50);

			line[1] = new LineTo();
			line[1].setX(50);
			line[1].setY(100);

			line[2] = new LineTo();
			line[2].setX(150);
			line[2].setY(100);

			setStroke(Color.SILVER);
			getElements().add(moveTo);
			getElements().addAll(line);
			relocate(0, 32);
		}
    }

	private class SlipUp extends Path {
		public SlipUp() {

			LineTo[] line = new LineTo[3];
			MoveTo moveTo = new MoveTo();
			DropShadow shadow = new DropShadow();


			shadow.setColor(Color.color(0, 0, 0,0.5));
			shadow.setBlurType(BlurType.GAUSSIAN);
			shadow.setOffsetX(0);
			shadow.setOffsetY(-4);

			moveTo.setX(150);
			moveTo.setY(150);

			line[0] = new LineTo();
			line[0].setX(100);
			line[0].setY(115);

			line[1] = new LineTo();
			line[1].setX(50);
			line[1].setY(150);

			line[2] = new LineTo();
			line[2].setX(150);
			line[2].setY(150);

			setStroke(Color.SILVER);
			setEffect(shadow);
			getElements().add(moveTo);
			getElements().addAll(line);
			relocate(0, 0);
		}
    }

	private class Mail{

		private double lastY;
		private Rectangle mail;
		private StackPane container;
		private TranslateTransition anim;

		public Mail() {
			super();
			this.create();
		}

		public Mail(double width, double height) {
			this.mail = new Rectangle(width,height);
			this.container = new StackPane();
			this.create();
		}

		public Mail(int x, int y, double width, double height) {
			this.mail = new Rectangle(x,y,width,height);
			this.container = new StackPane();
			this.create();
		}

		private void create(){
			DropShadow shadow = new DropShadow();
			Random rand = new Random();
			VBox layout = new VBox(2);
			Line[] line = new Line[6];

			for(int i = 0; i<line.length; i++){
				double randomValue = mail.getWidth()*.5 + (mail.getWidth()*.8 - mail.getWidth()*.5) * rand.nextDouble();
				line[i]= new Line(0, 0, randomValue , 0);
				line[i].setStroke(Color.BLACK);
				line[i].setStrokeWidth(2);
				line[i].setOpacity(.4);
				layout.getChildren().add(line[i]);
			}

			layout.setPadding(new Insets(15,0,0,15));

			shadow.setColor(Color.color(0, 0, 0,.5));
			shadow.setBlurType(BlurType.GAUSSIAN);
			shadow.setOffsetX(0);
			shadow.setOffsetY(-5);

			mail.setEffect(shadow);
			mail.setStroke(ShapeUtility.RADIAL_GRADIENT(Color.YELLOW,Color.ORANGE));
			mail.setStrokeWidth(2);
			mail.setWidth((mail.getWidth()-mail.getStrokeWidth())-2);
			mail.setFill(ShapeUtility.LINEAR_GRADIENT(Color.YELLOW,Color.ORANGE));
			mail.setArcHeight(5);
			mail.setArcWidth(5);

			container.setPadding(new Insets(0,0,0,3));
			container.getChildren().addAll(mail,layout);
			animate();
		}

		public void setWidth(int width){
			this.mail.setWidth(width);
		}

		public void setHeight(int height){
			this.mail.setHeight(height);
		}

		public void setSize(int width, int height){
			this.mail.setWidth(width);
			this.mail.setHeight(height);
		}

		public void setFill(Paint fill){
			this.mail.setFill(fill);
		}

		public void animate(boolean state, Runnable script){
			if(state){
				root.getChildren().remove(container);
				root.getChildren().add(2,container);
				anim.stop();
				anim.setFromY(container.getTranslateY());
				anim.setToY(lastY);
				anim.setFromX(0);
				anim.setToX(0);
				anim.setDelay(Duration.millis(50));
				anim.setDuration(Duration.millis(300));
				anim.setAutoReverse(false);
				anim.setCycleCount(1);
				anim.setOnFinished(e->{
					connect.cullRegion(connect.getVRegion(), false);
					anim.stop();
					anim.setFromY(lastY);
					anim.setFromX(0);
					anim.setToX(0);
					anim.setToY(lastY-25);
					anim.setDelay(Duration.millis(200));
					anim.setDuration(Duration.seconds(1));
					anim.setCycleCount(Timeline.INDEFINITE);
					anim.setAutoReverse(true);
					anim.play();
				});
				anim.play();

			}else{
				anim.stop();
				anim.setCycleCount(1);
				anim.setAutoReverse(false);
				anim.setDuration(Duration.millis(300));
				anim.setToY(-125);
				anim.setToX(-50);
				anim.setOnFinished(e->{
					connect.cullRegion(connect.getVRegion(), true);
					container.toFront();
					if(script!=null){
						script.run();
					}
				});
				anim.play();
			}
		}

		private void animate(){
			lastY = container.getTranslateY();
			anim = new TranslateTransition(Duration.seconds(1), container);
			anim.setFromY(lastY);
			anim.setToY(lastY-25);
			anim.setCycleCount(Timeline.INDEFINITE);
			anim.setAutoReverse(true);
			anim.play();
		}

		public StackPane get(){
			return container;

		}
	}

	private class Notification{

		private StackPane container;
		private Circle circle;
		private Text text;
		private int count;

		public Notification(){
			this.text = new Text("0");
			this.circle = new Circle(15);
			this.container = new StackPane();
			this.circle.setFill(ShapeUtility.RADIAL_GRADIENT(Color.DODGERBLUE.brighter(), Color.DODGERBLUE));
			this.circle.setStroke(Color.WHITE);
			this.circle.setStrokeWidth(2);
			this.text.setFill(Color.WHITE);
			this.text.setFont(Font.font("BAUHAUS 91",FontWeight.EXTRA_BOLD,12));
			this.container.getChildren().addAll(circle,text);
			this.addAnimAndEffects();
		}

		private void addAnimAndEffects(){
			DropShadow shadow = new DropShadow();
			shadow.setColor(Color.color(0, 0, 0,0.5));
			shadow.setBlurType(BlurType.GAUSSIAN);
			shadow.setOffsetX(-4);
			shadow.setOffsetY(-4);
			container.setEffect(shadow);
			ScaleTransition anim = new ScaleTransition();
			anim.setNode(container);
			anim.setAutoReverse(true);
			anim.setDuration(Duration.seconds(1));
			anim.setCycleCount(Timeline.INDEFINITE);
			anim.setFromX(1);
			anim.setFromY(1);
			anim.setToX(1.2);
			anim.setToY(1.2);
			anim.play();
		}

		public StackPane get(){
			return container;
		}

		public void setLocation(int x, int y){
			this.container.setTranslateX(x);
			this.container.setTranslateY(y);
		}

		public void setBubbleSize(int size){
			this.circle.setRadius(size);
		}

		public void setTextSize(int size){
			this.text.setFont(Font.font("BAUHAUS 91",FontWeight.EXTRA_BOLD, size));
		}

		public void increaseCount(int count){
			this.count = this.count+count;
			if(count>99){
				text.setText("99+");
			}else{
				text.setText(count+"");
			}
		}

		public void decreaseCount(int count){
			this.count = this.count-count;
			if(count>99){
				text.setText("99+");
			}else{
				text.setText(count+"");
			}
		}
	}
}