package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameManager;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MenuButton {

	private Button menuButton;
    private String buttonName;
    private Font buttonFont;
    private Runnable actionScript;
    private Runnable hoverScript;
    private double fontSize;
    private Pos buttonAlignment;
    private ButtonStyle buttonStyle;

    public MenuButton(String name, Pos alignment, ButtonStyle style, double fontSize, boolean pressedGlow , Color glow){
    	this.menuButton = new Button(name);
    	this.buttonName = name;
    	this.buttonStyle = style;
    	this.buttonAlignment = alignment;
    	this.fontSize = GameManager.ScaleX_Y(fontSize);
    	this.initializeButtonOne();
    }
	private void initializeButtonOne() {
		this.buttonFont = Font.font("Bauhaus 93", FontWeight.BOLD, fontSize);
		this.menuButton.setAlignment(buttonAlignment);
		this.menuButton.setFont(buttonFont);
		this.menuButton.setMinWidth(500);
		this.menuButton.setStyle(MenuButtonStyles.STANDARD_BUTTON_STYLE);

		this.processEvents();
	}
	@SuppressWarnings("unused")
	private void processStyle(){
		this.menuButton.getStylesheets().add(MenuButton.class.getResource("menuButtons.css").toExternalForm());
		switch (buttonStyle){
		case green:
			this.menuButton.setId("styleOne");
			break;
		case black:
			this.menuButton.setId("styleOne");
			break;
		case blue:
			this.menuButton.setId("styleOne");
			break;
		case gray:
			this.menuButton.setId("styleOne");
			break;
		case orange:
			this.menuButton.setId("styleOne");
			break;
		case red:
			this.menuButton.setId("styleOne");
			break;
		default:
			break;
		}
	}
	private void processEvents(){
		this.menuButton.setOnMousePressed(e->{
			activate();
		});
		this.menuButton.setOnMouseReleased(e->{
			deactivate();
		});
		this.menuButton.setOnMouseEntered(e->{
			setHoverAction();
		});
	}
	public enum ButtonStyle {
		green, blue, black, red,
		orange, gray
	}
	public Button getButton(){
		return menuButton;
	}
	public void setActive(boolean state){
		this.menuButton.setStyle(state ? MenuButtonStyles.HOVERED_BUTTON_STYLE : MenuButtonStyles.STANDARD_BUTTON_STYLE);
	}
    public void setAction(Runnable script) {
        this.actionScript = script;
    }
    public void activate() {
    	this.menuButton.setStyle(MenuButtonStyles.PRESSED_BUTTON_STYLE);
    }
    public void deactivate(){
        if (actionScript != null)
            actionScript.run();
    	this.menuButton.setStyle(MenuButtonStyles.STANDARD_BUTTON_STYLE);
    }
    public void setOnHover(Runnable script){
    	this.hoverScript = script;
    }
    public void setHoverAction(){
    	if(hoverScript != null){
    		hoverScript.run();
    	}
    }
	public String getButtonName() {
		return buttonName;
	}
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	public double getFontSize() {
		return fontSize;
	}
	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}
	public Pos getButtonAlignment() {
		return buttonAlignment;
	}
	public void setButtonAlignment(Pos buttonAlignment) {
		this.buttonAlignment = buttonAlignment;
		this.menuButton.setAlignment(buttonAlignment);
	}

}