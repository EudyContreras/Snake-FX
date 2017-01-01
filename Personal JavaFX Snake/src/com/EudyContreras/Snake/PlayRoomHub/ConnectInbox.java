package com.EudyContreras.Snake.PlayRoomHub;
import java.util.Random;

import com.EudyContreras.Snake.Utilities.ShapeUtility;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class ConnectInbox extends Application {

    public static void main(String[] args) {

       Application.launch(args);
    }

    public void start(Stage primaryStage) {

      primaryStage.setTitle("Path");

      Pane root = new Pane();
      SlipUp slipUp = new SlipUp();
      SlipUp slipUpInner = new SlipUp();
      SlipRight slipRight = new SlipRight();
      SlipLeft slipLeft = new SlipLeft();
      Mail mail = new Mail(200,80);
      Notification notification = new Notification();
      Scene scene = new Scene(new Group(root), 700, 250, Color.WHITE);

      mail.get().setLayoutY(70);
      notification.setLocation(160, 50);
      slipUpInner.relocate(0, 10);
      slipUp.setFill(ShapeUtility.LINEAR_GRADIENT(Color.DODGERBLUE,Color.DODGERBLUE.brighter()));
      slipRight.setFill(ShapeUtility.LINEAR_GRADIENT(Color.CYAN,Color.DODGERBLUE));
      slipLeft.setFill(ShapeUtility.LINEAR_GRADIENT(Color.CYAN,Color.DODGERBLUE));
      slipUpInner.setFill(ShapeUtility.LINEAR_GRADIENT(Color.WHITE,Color.SILVER));
      root.setTranslateX(0);

      root.getChildren().addAll(slipUp,slipUpInner,mail.get(),slipRight,slipLeft, notification.get());
      primaryStage.setScene(scene);
      primaryStage.show();
    }


	private class SlipLeft extends Path {
		public SlipLeft() {

			DropShadow shadow = new DropShadow();
			LineTo[] line = new LineTo[3];
			MoveTo moveTo = new MoveTo();

			shadow.setBlurType(BlurType.THREE_PASS_BOX);
			shadow.setColor(Color.color(0, 0, 0,0.5));
			shadow.setOffsetX(-4);
			shadow.setOffsetY(-2);

			moveTo.setX(250);
			moveTo.setY(150);

			line[0] = new LineTo();
			line[0].setX(250);
			line[0].setY(50);

			line[1] = new LineTo();
			line[1].setX(50);
			line[1].setY(150);

			line[2] = new LineTo();
			line[2].setX(250);
			line[2].setY(150);

			setEffect(shadow);
			getElements().add(moveTo);
			getElements().addAll(line);
			relocate(0, 70);
		}
    }

	private class SlipRight extends Path {
		public SlipRight() {

			LineTo[] line = new LineTo[3];
			MoveTo moveTo = new MoveTo();

			moveTo.setX(250);
			moveTo.setY(150);

			line[0] = new LineTo();
			line[0].setX(50);
			line[0].setY(50);

			line[1] = new LineTo();
			line[1].setX(50);
			line[1].setY(150);

			line[2] = new LineTo();
			line[2].setX(250);
			line[2].setY(150);

			getElements().add(moveTo);
			getElements().addAll(line);
			relocate(0, 70);
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

			moveTo.setX(250);
			moveTo.setY(150);

			line[0] = new LineTo();
			line[0].setX(150);
			line[0].setY(80);

			line[1] = new LineTo();
			line[1].setX(50);
			line[1].setY(150);

			line[2] = new LineTo();
			line[2].setX(250);
			line[2].setY(150);

			setEffect(shadow);
			getElements().add(moveTo);
			getElements().addAll(line);
			relocate(0, 0);
		}
    }

	private class Mail{

		private Rectangle mail;
		private StackPane container;

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
			Random rand = new Random();
			VBox layout = new VBox(5);
			Line[] line = new Line[8];

			for(int i = 0; i<line.length; i++){
				double randomValue = mail.getWidth()*.5 + (mail.getWidth()*.8 - mail.getWidth()*.5) * rand.nextDouble();
				line[i]= new Line(0, 0, randomValue , 0);
				line[i].setStroke(Color.WHITE);
				line[i].setStrokeWidth(4);
				line[i].setOpacity(.4);
				layout.getChildren().add(line[i]);
			}

			layout.setPadding(new Insets(15,0,0,15));
			DropShadow shadow = new DropShadow();
			shadow.setColor(Color.color(0, 0, 0,0.5));
			shadow.setBlurType(BlurType.GAUSSIAN);
			shadow.setOffsetX(-5);
			shadow.setOffsetY(-6);
			mail.setEffect(shadow);
			mail.setStroke(ShapeUtility.RADIAL_GRADIENT(Color.YELLOW,Color.ORANGE));
			mail.setStrokeWidth(5);
			mail.setWidth((mail.getWidth()-mail.getStrokeWidth())-5);
			mail.setFill(ShapeUtility.LINEAR_GRADIENT(Color.YELLOW,Color.ORANGE));
			mail.setArcHeight(20);
			mail.setArcWidth(20);
			container.setPadding(new Insets(0,0,0,7));
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

		private void animate(){
			TranslateTransition anim = new TranslateTransition(Duration.seconds(1), container);
			anim.setFromY(container.getTranslateY()+10);
			anim.setToY(container.getTranslateY()-35);
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
			this.circle = new Circle(30);
			this.container = new StackPane();
			this.circle.setFill(Color.RED);
			this.circle.setStroke(Color.WHITE);
			this.circle.setStrokeWidth(5);
			this.text.setFill(Color.WHITE);
			this.text.setFont(Font.font(null,FontWeight.EXTRA_BOLD,25));
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
			anim.setFromX(0.9);
			anim.setFromY(0.9);
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

		public void setSize(int size){
			this.circle.setRadius(size);
		}

		public void setTextSize(int size){
			this.text.setFont(Font.font(null,FontWeight.EXTRA_BOLD, size));
		}

		public void increaseCount(){
			count++;
			text.setText(count+"");
		}

		public void increaseCount(int count){
			this.count = this.count+count;
			text.setText(count+"");
		}

		public void decreaseCount(){
			count--;
			text.setText(count+"");
		}

		public void decreaseCount(int count){
			this.count = this.count-count;
			text.setText(count+"");
		}
	}
}