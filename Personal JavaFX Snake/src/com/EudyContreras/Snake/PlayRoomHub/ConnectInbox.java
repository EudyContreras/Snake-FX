package com.EudyContreras.Snake.PlayRoomHub;
import java.util.Random;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.PlayRoomHub.ConnectLabel.Style;
import com.EudyContreras.Snake.Utilities.FillUtility;

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
    private Rectangle clickBounds = new Rectangle(110,90);
    private Notification notification = new Notification();
    private TranslateTransition translate = new TranslateTransition();
    private ScaleTransition scale = new ScaleTransition();
    private RotateTransition rotate = new RotateTransition();
    private ParallelTransition transitions = new ParallelTransition();
    private SegmentedButtons connectMail = new SegmentedButtons();
//    private ConnectFrame mailFrame;
    private ConnectHub connect;
    private boolean showingMail = false;
    private boolean animating = false;

	public ConnectInbox(GameManager game, ConnectHub connect) {
		this.connect = connect;
		mail.setSize(720, 580);
		mail.setScale(0.13,0.07);
		mail.get().setLayoutY(65);
		mail.get().setLayoutX(50);
		mail.create();
		notification.increaseCount(50);
		notification.setLocation(80, 25);
//		mailFrame = new ConnectFrame(game);
//		mailFrame.setLabel("Mail");
//		mailFrame.addRegion(connectMail.get());
//		mailFrame.setFrameSize(635, 600);
//		mailFrame.get().setTranslateY(30);
//		mailFrame.getLabel().setFrameOpacity(100);
//		mailFrame.setFill(FillUtility.LINEAR_GRADIENT(Color.YELLOW,Color.ORANGE));
//		mailFrame.setBorderColor(FillUtility.LINEAR_GRADIENT(Color.YELLOW,Color.ORANGE,Color.ORANGE));
//		mailFrame.getLabel().setFrameFill(FillUtility.LINEAR_GRADIENT(Color.BLACK,Color.GRAY.darker(),Color.BLACK));
//		mailFrame.getLabel().setBorderColor(Color.GRAY.darker().darker());
//		mailFrame.getLabel().setStyle(Style.YELLOW_STYLE);
//		mailFrame.getLabel().getGlow().setRadius(5);
//		mailFrame.getLabel().getGlow().setBlurType(BlurType.TWO_PASS_BOX);
//		mailFrame.getLabel().setTextGlow(GlowType.NONE);
//		mailFrame.getLabel().setTextFill(FillUtility.LINEAR_GRADIENT(Color.BLACK,Color.GRAY.darker()));
		shadow.setBlurType(BlurType.THREE_PASS_BOX);
		shadow.setColor(Color.color(0, 0, 0, 0.5));
		shadow.setOffsetX(-4);
		shadow.setOffsetY(-2);
		shadow.setRadius(16);
		root.setEffect(shadow);
		clickBounds.setX(2);
		clickBounds.setOpacity(0);
		clickBounds.setOnMousePressed(e->showMessage());
		slipUpInner.relocate(0, 10);
		slipUp.setFill(FillUtility.LINEAR_GRADIENT(Color.SILVER, Color.GRAY.brighter()));
		left.setFill(FillUtility.LINEAR_GRADIENT(Color.GRAY, Color.GRAY, Color.SILVER));
		right.setFill(FillUtility.LINEAR_GRADIENT(Color.SILVER, Color.GRAY, Color.SILVER));
		slipUpInner.setFill(FillUtility.LINEAR_GRADIENT(Color.WHITE, Color.SILVER, Color.SILVER));
		root.getChildren().addAll(slipUp, slipUpInner, mail.get(), left, right, notification.get(), clickBounds);
    	transitions.getChildren().add(translate);
    	transitions.getChildren().add(rotate);
    	transitions.getChildren().add(scale);
	}

    public Pane get(){
    	return root;
    }

    public void hideMail(){
    	animating = false;
		showingMail = false;
    	mail.reset();
    }

	public void showMessage() {
		if (!animating) {
			if (showingMail) {
				animating = true;
				mail.showShadow(false);
				connect.removeFromOverlay(connectMail.get());
				connectMail.show(false);
				connect.showOverlay(false);
				connect.blurBackground(false);

				translate.setNode(mail.get());
				translate.setDuration(Duration.millis(500));
				translate.setInterpolator(Interpolator.SPLINE(0, 0, 0, 1));
				translate.setToX(0);
				translate.setToY(-85);

				rotate.setNode(mail.get());
				rotate.setDuration(Duration.millis(500));
				rotate.setInterpolator(Interpolator.SPLINE(0, 0, 0, 1));
				rotate.setToAngle(0);

				scale.setNode(mail.get());
				scale.setDuration(Duration.millis(500));
				scale.setInterpolator(Interpolator.SPLINE(0, 0, 0, 1));
				scale.setToX(0.13);
				scale.setToY(0.07);

				transitions.setOnFinished(e->{
					animating = false;
					showingMail = false;
					mail.normalize();
					mail.showShadow(true);
					mail.animate(true, null);
				});

				transitions.play();
			} else {
				animating = true;

				connectMail.show(false);
				connect.showOverlay(true);
				connect.addToOverlay(650,650,connectMail.get());

				translate.setNode(mail.get());
				translate.setDuration(Duration.millis(650));
				translate.setInterpolator(Interpolator.EASE_OUT);
				translate.setToX(-490);
				translate.setToY(355);

				rotate.setNode(mail.get());
				rotate.setDuration(Duration.millis(650));
				rotate.setInterpolator(Interpolator.EASE_OUT);
				rotate.setToAngle(-360*2);

				scale.setNode(mail.get());
				scale.setDuration(Duration.millis(650));
				scale.setInterpolator(Interpolator.EASE_OUT);
				scale.setToX(1);
				scale.setToY(1);

				mail.animate(false, ()->transitions.play());
				transitions.setOnFinished(e -> {
					showingMail = true;
					animating = false;
					mail.showShadow(true);
					mail.normalize();
					connectMail.show(true);
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
		private StackPane content;
		private TranslateTransition anim;
		private VBox layout = new VBox(30);
		private DropShadow shadow = new DropShadow();

		public Mail(double width, double height) {
			this.mail = new Rectangle(width,height);
			this.content = new StackPane();
			this.content.setBackground(FillUtility.PAINT_FILL(Color.color(0, 0, 0, 0.4)));
		}

		public void showShadow(boolean state) {
			mail.setEffect(state ? shadow : null);
		}

		public Mail(int x, int y, double width, double height) {
			this.mail = new Rectangle(x,y,width,height);
			this.content = new StackPane();
		}

		private void create(){

			Random rand = new Random();

			Line[] line = new Line[10];

			for(int i = 0; i<line.length; i++){
				double randomValue = mail.getWidth()*.5 + (mail.getWidth()*.8 - mail.getWidth()*.5) * rand.nextDouble();
				line[i]= new Line(0, 0, randomValue , 0);
				line[i].setStroke(Color.BLACK);
				line[i].setStrokeWidth(10);
				line[i].setOpacity(.6);
				layout.getChildren().add(line[i]);
			}

			layout.setPadding(new Insets(15,0,0,15));

			shadow.setColor(Color.color(0, 0, 0,.5));
			shadow.setBlurType(BlurType.GAUSSIAN);
			shadow.setOffsetX(0);
			shadow.setOffsetY(-5);

			mail.setEffect(shadow);
			mail.setStroke(FillUtility.RADIAL_GRADIENT(Color.YELLOW,Color.ORANGE));
			mail.setStrokeWidth(2);
			mail.setWidth((mail.getWidth()-mail.getStrokeWidth())-2);
			mail.setFill(FillUtility.LINEAR_GRADIENT(Color.YELLOW,Color.ORANGE));
			mail.setArcHeight(25);
			mail.setArcWidth(25);

			content.setPadding(new Insets(0,0,0,3));
			content.getChildren().addAll(mail,layout);
			animate();
		}

		public void normalize(){
			mail.setEffect(shadow);
			shadow.setColor(Color.color(0, 0, 0, 0.5));
			shadow.setBlurType(BlurType.GAUSSIAN);
			shadow.setRadius(105);
			shadow.setOffsetX(-25);
			shadow.setOffsetY(25);
			mail.setStrokeWidth(15);
		}

		public void adjust(){
			mail.setEffect(null);
			shadow.setColor(Color.color(0, 0, 0,0.5));
			shadow.setBlurType(BlurType.GAUSSIAN);
			shadow.setRadius(10);
			shadow.setOffsetX(0);
			shadow.setOffsetY(-5);
			mail.setStrokeWidth(15);
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

		public void setScale(double x, double y) {
			this.content.setScaleX(x);
			this.content.setScaleY(y);
			this.content.setManaged(false);
		}

		public void setFill(Paint fill){
			this.mail.setFill(fill);
		}

		public DropShadow getShadow(){
			return shadow;
		}

		public void reset(){

			setScale(0.13,0.07);
			this.content.setLayoutY(65);
			this.content.setLayoutX(50);
			this.content.setRotate(0);
			this.content.setTranslateY(lastY);
			this.content.setTranslateX(0);
			root.getChildren().remove(content);
			root.getChildren().add(2,content);
			anim.setDuration(Duration.seconds(1));
			anim.setFromY(lastY-5);
			anim.setToY(lastY-20);
			anim.setCycleCount(Timeline.INDEFINITE);
			anim.setAutoReverse(true);
			anim.play();
		}

		public void animate(boolean state, Runnable script){
			if(state){
				root.getChildren().remove(content);
				root.getChildren().add(2,content);
				anim.stop();
				anim.setFromY(content.getTranslateY());
				anim.setToY(lastY-5);
				anim.setFromX(0);
				anim.setToX(0);
				anim.setDuration(Duration.millis(300));
				anim.setAutoReverse(false);
				anim.setCycleCount(1);
				anim.setOnFinished(e->{
					layout.setVisible(true);
					connect.cullRegion(connect.getVRegion(), false);
					anim.stop();
					anim.setFromY(lastY-5);
					anim.setFromX(0);
					anim.setToX(0);
					anim.setToY(lastY-20);
					anim.setDelay(Duration.millis(200));
					anim.setDuration(Duration.seconds(1));
					anim.setCycleCount(Timeline.INDEFINITE);
					anim.setAutoReverse(true);
					anim.play();
				});
				anim.play();

			}else{
				connect.cullRegion(connect.getVRegion(), true);
				anim.stop();
				anim.setCycleCount(1);
				anim.setAutoReverse(false);
				anim.setDuration(Duration.millis(300));
				anim.setToY(-85);
				anim.setOnFinished(e->{
					adjust();
					layout.setVisible(false);
					showShadow(false);
					content.toFront();
					if(script!=null){
						script.run();
						connect.blurBackground(true);
					}
				});

				anim.play();
			}
		}

		private void animate(){
			lastY = content.getTranslateY();
			anim = new TranslateTransition();
			anim.setDuration(Duration.seconds(1));
			anim.setNode(content);
			anim.setFromY(lastY-5);
			anim.setToY(lastY-20);
			anim.setCycleCount(Timeline.INDEFINITE);
			anim.setAutoReverse(true);
			anim.play();
		}

		public StackPane get(){
			return content;

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
			this.circle.setFill(FillUtility.RADIAL_GRADIENT(Color.DODGERBLUE.brighter(), Color.DODGERBLUE));
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