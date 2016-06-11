package com.EudyContreras.Snake.UserInterface;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuButton extends HBox {

	private DropShadow buttonGlow = new DropShadow(25, Color.WHITE);
    private ImagePattern buttonImageOne;
    private ImagePattern buttonImageTwo;
	private ImageView buttonView;
    private Rectangle buttonFrame;
    private TriCircle sideShapeLeft = new TriCircle();
    private TriCircle sideShapeRight = new TriCircle();
    private StackPane button;
    private Text buttonText;
    private Font buttonFont;
    private String buttonName;
    private Color buttonColor;
    private Color textColor;
    private Color glowColor;
    private double buttonWidth;
    private double buttonHeight;
    private double buttonSize;
    private double fontSize;
    private boolean pressedGlow;
    private Runnable script;
    private Pos buttonAlignment;

    public MenuButton(String name, Pos alignment, Color textColor, Color buttonColor, double fontSize, double buttonWidth, double buttonHeight, boolean pressedGlow , Color glowColor){
    	super(15);
    	this.buttonName = name;
    	this.textColor = textColor;
    	this.buttonColor = buttonColor;
    	this.fontSize = fontSize;
    	this.buttonWidth = buttonWidth;
    	this.buttonHeight = buttonHeight;
    	this.pressedGlow = pressedGlow;
    	this.glowColor = glowColor;
    	this.buttonAlignment = alignment;
    	this.initializeButtonOne( name, alignment, textColor, buttonColor, fontSize, buttonWidth, buttonHeight);
    }
    public MenuButton(Pos alignment, Image activeImage, Image innactiveImage, double width, double height, boolean pressedGlow, Color glowColor){
    	super(15);
    	this.buttonWidth = width;
    	this.buttonHeight = height;
    	this.buttonImageOne = new ImagePattern(activeImage);
    	this.buttonImageTwo = new ImagePattern(innactiveImage);
    	this.glowColor = glowColor;
    	this.pressedGlow = pressedGlow;
    	this.buttonAlignment = alignment;
    	this.initializeButtonTwo(alignment, width, height);
    }
	private void initializeButtonOne(String name, Pos alignment, Color textColor, Color notSelectedColor, double fontSize, double buttonWidth, double buttonHeight) {
		this.buttonFont = Font.font("", FontWeight.BOLD, fontSize);
		this.buttonText = new Text(name);
		this.buttonText.setFont(buttonFont);
		this.buttonText.setFill(textColor);
		this.buttonFrame = new Rectangle(buttonWidth, buttonHeight);
		this.buttonFrame.setArcHeight(20);
		this.buttonFrame.setArcWidth(20);
		this.buttonFrame.setOpacity(0.5);
		this.buttonFrame.setFill(buttonColor);
		this.buttonGlow.setColor(glowColor);
		this.buttonGlow.setSpread(0.25);
		this.button = new StackPane(buttonFrame,buttonText);
		this.button.setAlignment(alignment);
		this.setActive(false);
		this.setAlignment(alignment);
		this.processEvents();
		this.getChildren().addAll(button, sideShapeRight);
	}
	private void initializeButtonTwo(Pos alignment, double width, double height) {
		this.buttonFrame = new Rectangle(width, height);
		this.buttonFrame.setFill(buttonImageTwo);
		this.buttonFrame.setArcHeight(0);
		this.buttonFrame.setArcWidth(20);
		this.buttonGlow.setColor(glowColor);
		this.buttonGlow.setSpread(0.25);
		setActive(false);
		setAlignment(Pos.CENTER);
		processEvents();
		getChildren().addAll(buttonFrame);
	}
	private void processEvents(){
		this.setOnMousePressed(Event->{
			activate();
		});
		this.setOnMouseReleased(Event->{
			deactivate();
		});
		this.setOnMouseEntered(Event->{
			setActive(true);
		});
		this.setOnMouseExited(Event->{
			setActive(false);
		});
	}
    public void setActive(boolean state) {
		if (buttonText != null) {
			this.sideShapeLeft.setVisible(state);
			this.sideShapeRight.setVisible(state);
			this.buttonText.setFill(state ? Color.BLACK : Color.WHITE);
			this.buttonFrame.setFill(state ? Color.WHITE : Color.LIME);
		} else {
			this.buttonFrame.setFill(state ? buttonImageOne : buttonImageTwo);
		}
    }
    public void setAction(Runnable script) {
        this.script = script;
    }
    public void activate() {
        if (script != null)
            script.run();
		this.buttonFrame.setEffect(pressedGlow? buttonGlow: null );
    }
    public void deactivate(){
    	this.buttonFrame.setEffect(null);
    }

    public DropShadow getDrop() {
		return buttonGlow;
	}
	public void setDrop(DropShadow drop) {
		this.buttonGlow = drop;
	}
	public ImageView getButtonView() {
		return buttonView;
	}
	public void setButtonView(ImageView buttonView) {
		this.buttonView = buttonView;
	}
	public Rectangle getButtonFrame() {
		return buttonFrame;
	}
	public void setButtonFrame(Rectangle buttonFrame) {
		this.buttonFrame = buttonFrame;
	}
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public Color getButtonColor() {
		return buttonColor;
	}
	public void setButtonColor(Color buttonColor) {
		this.buttonColor = buttonColor;
		this.buttonFrame.setFill(buttonColor);
	}
	public Color getTextColor() {
		return textColor;
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		this.buttonText.setFill(textColor);
	}
	public ImagePattern getButtonImageOne() {
		return buttonImageOne;
	}
	public void setButtonImageOne(ImagePattern buttonImageOne) {
		this.buttonImageOne = buttonImageOne;
	}
	public ImagePattern getButtonImageTwo() {
		return buttonImageTwo;
	}
	public void setButtonImageTwo(ImagePattern buttonImageTwo) {
		this.buttonImageTwo = buttonImageTwo;
	}
	public Text getButtonText() {
		return buttonText;
	}
	public void setButtonText(Text buttonText) {
		this.buttonText = buttonText;
	}
	public Font getButtonFont() {
		return buttonFont;
	}
	public void setButtonFont(Font buttonFont) {
		this.buttonFont = buttonFont;
	}
	public double getButtonWidth() {
		return buttonWidth;
	}
	public void setButtonWidth(double buttonWidth) {
		this.buttonWidth = buttonWidth;
		this.buttonFrame.setWidth(buttonWidth);
	}
	public double getButtonHeight() {
		return buttonHeight;
	}
	public void setButtonHeight(double buttonHeight) {
		this.buttonHeight = buttonHeight;
		this.buttonFrame.setHeight(buttonHeight);
	}
	public double getButtonSize() {
		return buttonSize;
	}
	public void setButtonSize(double buttonSize) {
		this.buttonSize = buttonSize;
	}
	public double getFontSize() {
		return fontSize;
	}
	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}
	public boolean isPressedGlow() {
		return pressedGlow;
	}
	public void setPressedGlow(boolean pressedGlow) {
		this.pressedGlow = pressedGlow;
	}
	public Pos getButtonAlignment() {
		return buttonAlignment;
	}
	public void setButtonAlignment(Pos buttonAlignment) {
		this.buttonAlignment = buttonAlignment;
		this.setAlignment(buttonAlignment);
	}

	private static class TriCircle extends Parent {
        public TriCircle() {
            Shape shape1 = Shape.subtract(new Circle(5), new Circle(2));
            shape1.setFill(Color.WHITE);

            Shape shape2 = Shape.subtract(new Circle(5), new Circle(2));
            shape2.setFill(Color.WHITE);
            shape2.setTranslateX(5);

            Shape shape3 = Shape.subtract(new Circle(5), new Circle(2));
            shape3.setFill(Color.WHITE);
            shape3.setTranslateX(2.5);
            shape3.setTranslateY(-5);

            Shape shape4 = Shape.subtract(new Circle(5), new Circle(2));
            shape4.setFill(Color.WHITE);
            shape4.setTranslateX(2.5);
            shape4.setTranslateY(5);

            getChildren().addAll(shape1, shape2, shape3, shape4);

            setEffect(new GaussianBlur(2));
        }
    }
}