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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CustomMenuButton {

    private ImagePattern buttonImageOne;
    private ImagePattern buttonImageTwo;
	private DropShadow buttonGlow;
	private ImageView buttonView;
    private Rectangle buttonFrame;
    private StackPane button;
    private HBox buttonContainer;
    private Text buttonText;
    private Font buttonFont;
    private Paint buttonFill;
    private Paint textColor;
    private Color glowColor;
    private String buttonName;
    private double frameOpacity;
    private double buttonWidth;
    private double buttonHeight;
    private double buttonSize;
    private double fontSize;
    private boolean selected;
    private boolean pressedGlow;
    private Runnable actionScript;
    private Runnable hoverScript;
    private Pos buttonAlignment;
    private ButtonStyle style;


    public CustomMenuButton(Pos alignment, Image activeImage, Image innactiveImage, double width, double height, boolean pressedGlow, Color glowColor){
    	this.buttonContainer = new HBox(15);
    	this.buttonWidth = width;
    	this.buttonHeight = height;
    	this.buttonImageOne = new ImagePattern(activeImage);
    	this.buttonImageTwo = new ImagePattern(innactiveImage);
    	this.glowColor = glowColor;
    	this.pressedGlow = pressedGlow;
    	this.buttonAlignment = alignment;
    	this.initializeButtonTwo();
    }

    public CustomMenuButton(String name, Pos alignment, ButtonStyle style, Paint textColor,double fontSize, double buttonWidth, double buttonHeight, boolean pressedGlow ){
    	this.buttonContainer = new HBox(15);
    	this.buttonName = name;
    	this.textColor = textColor;
    	this.style = style;
    	this.fontSize = fontSize;
    	this.buttonWidth = buttonWidth;
    	this.buttonHeight = buttonHeight;
    	this.pressedGlow = pressedGlow;
    	this.buttonAlignment = alignment;
    	this.frameOpacity = 1;
    	this.initializeButtonOne();
    }

	private void initializeButtonOne() {
		this.buttonFont = Font.font("Bauhaus 93", FontWeight.EXTRA_BOLD, fontSize);
		this.buttonText = new Text(buttonName);
		this.buttonText.setFont(buttonFont);
		this.buttonText.setStyle(MenuButtonStyles.BUTTON_TEXT);
		this.buttonFrame = new Rectangle(buttonWidth, buttonHeight);
		this.buttonFrame.setArcHeight(20);
		this.buttonFrame.setArcWidth(20);
		this.buttonFrame.setOpacity(frameOpacity);
		this.button = new StackPane(buttonFrame,buttonText);
		this.button.setAlignment(buttonAlignment);
		this.buttonFrame.setStyle(MenuButtonStyles.STANDARD_BUTTON_STYLE);
		this.buttonContainer.setAlignment(buttonAlignment);
		this.buttonContainer.getChildren().addAll(button);
		this.processEvents();
		this.setActive(false);
	}

	private void initializeButtonTwo() {
		this.buttonGlow = new DropShadow(15, Color.WHITE);
		this.buttonFrame = new Rectangle(buttonWidth, buttonHeight);
		this.buttonFrame.setFill(buttonImageTwo);
		this.buttonFrame.setArcHeight(0);
		this.buttonFrame.setArcWidth(20);
		this.buttonGlow.setColor(glowColor);
		this.buttonGlow.setSpread(0.25);
		this.buttonContainer.setAlignment(buttonAlignment);
		this.buttonContainer.getChildren().addAll(buttonFrame);
		this.processEvents();
		this.setActive(false);
	}

	@SuppressWarnings("unused")
	private void processStyle(){
		switch (style){
		case BLACK:
			break;
		case BLUE:
			break;
		case GRAY:
			break;
		case GREEN:
			break;
		case ORANGE:
			break;
		case RED:
			break;
		default:
			break;
		}
	}

	private void processEvents(){
		this.buttonContainer.setOnMousePressed(Event->{
			activate();
		});
		this.buttonContainer.setOnMouseReleased(Event->{
			deactivate();
		});
		this.buttonContainer.setOnMouseEntered(Event->{
			setActive(true);
			setHoverAction();
		});
		this.buttonContainer.setOnMouseExited(Event->{
			setActive(false);
		});
	}

	public enum ButtonStyle {
		GREEN, BLUE, BLACK, RED,
		ORANGE, GRAY
	}

	public HBox BUTTON(){
		return buttonContainer;
	}

    public void setActive(boolean state) {
		if (buttonText != null) {
			this.setSelected(state);
			this.buttonFrame.setStyle(state ? MenuButtonStyles.HOVERED_BUTTON_STYLE : MenuButtonStyles.STANDARD_BUTTON_STYLE);
			//this.buttonText.setFill(state? Color.WHITE : Color.WHITE);
		} else {
			this.buttonFrame.setFill(state ? buttonImageOne : buttonImageTwo);
		}
    }

    public void setAction(Runnable script) {
        this.actionScript = script;
    }

    public void activate() {
    	this.buttonFrame.setStyle(MenuButtonStyles.PRESSED_BUTTON_STYLE);
    }

    public void deactivate(){
        if (actionScript != null)
            actionScript.run();
        this.buttonFrame.setStyle(MenuButtonStyles.HOVERED_BUTTON_STYLE);
    }

    public void setOnHover(Runnable script){
    	this.hoverScript = script;
    }

    public void setHoverAction(){
    	if(hoverScript != null){
    		hoverScript.run();
    	}
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

	public Paint getButtonColor() {
		return buttonFill;
	}

	public void setButtonColor(Color buttonColor) {
		this.buttonFill = buttonColor;
		this.buttonFrame.setFill(buttonColor);
	}

	public Paint getTextColor() {
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
		this.buttonContainer.setAlignment(buttonAlignment);
	}

	public void setFrameOpacity(double opacity) {
		this.frameOpacity = opacity;
	}

	public static class Indicators extends Parent {
        public Indicators() {
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

	public void setSelected(boolean state){
		this.selected = state;
	}

	public boolean isSelected() {
		return selected;
	}
}