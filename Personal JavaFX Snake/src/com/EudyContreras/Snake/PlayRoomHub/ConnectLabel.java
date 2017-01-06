package com.EudyContreras.Snake.PlayRoomHub;

import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.UserInterface.MenuButtonStyles;
import com.EudyContreras.Snake.Utilities.FillUtility;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConnectLabel {

	private Text text;
	private GameManager game;
	private Rectangle background;
	private StackPane container;
	private InnerShadow shadow;
	private DropShadow glow;
	private double glowValue;
	private double glowPulse;

	public ConnectLabel(GameManager game, String text, int textSize, int width, int height, Style style){
		this.game = game;
		this.text = new Text(text);
		this.glow = new DropShadow();
		this.background = new Rectangle();
		this.shadow = new InnerShadow();
		this.container = new StackPane();
		this.container.setPrefSize(width, height);
		this.container.setMinSize(width, height);
		this.container.setMaxSize(width, height);
		this.container.getChildren().add(background);
		this.container.getChildren().add(this.text);
		this.setupText(style, textSize);
	}

	private void setupText(Style style, int size) {
		this.glowValue = 10;
		this.glowPulse = 0.2;
		this.background.setArcHeight(30);
		this.background.setArcWidth(30);
		this.background.setStroke(Color.SILVER);
		this.background.setStrokeWidth(10);
		this.background.widthProperty().bind(container.widthProperty());
		this.background.heightProperty().bind(container.heightProperty());
		this.background.setStyle(MenuButtonStyles.FX_CONNECT_BOX_STYLE);
		this.text.setFont(Font.font("AR DESTINE", FontWeight.EXTRA_BOLD, size));
		this.shadow.setOffsetX(0);
		this.shadow.setOffsetY(0);
		this.shadow.setRadius(2);
		this.shadow.setColor(Color.color(0, 0, 0,1));
		this.glow.setOffsetX(0);
		this.glow.setOffsetY(0);
		this.glow.setRadius(4);
		this.glow.setSpread(0.2);
		this.glow.setBlurType(BlurType.TWO_PASS_BOX);
		this.setStyle(style);
	}

	public void setFrameSize(double width, double height){
		this.container.setPrefSize(width, height);
		this.container.setMinSize(width, height);
		this.container.setMaxSize(width, height);
	}

	public void setFrameStyle(String style){
		this.background.setStyle(style);
	}

	public void setFrameFill(Paint fill){
		this.background.setStyle(null);
		this.background.setFill(fill);
	}

	public void setFrameGradient(Color...colors){
		this.background.setStyle(null);
		this.background.setFill(FillUtility.LINEAR_GRADIENT(colors));
	}

	public void setFrameOpacity(double opacity) {
		this.background.setOpacity(opacity/100);
	}

	public void setBorderColor(Paint fill){
		this.background.setStroke(fill);
	}

	public void setTextSize(int size){
		this.text.setFont(Font.font("AR DESTINE", FontWeight.EXTRA_BOLD, size));
	}

	public void setTextInnerShadow(boolean state){
		if(state){
			text.setEffect(shadow);
		}else{
			text.setEffect(null);
		}
	}
	public void setText(String text){
		this.text.setText(text);
	}

	public void setTextGlow(GlowType type){
		switch(type){
		case NONE:
			text.setEffect(null);
			break;
		case PULSING:
			glow.setSpread(0.3);
			text.setEffect(glow);
			game.performGUIUpdate(()->{
				glow.setRadius(glowValue);
				glowValue+=glowPulse;
				if(glowValue>=18){
					glowPulse = -0.2;
				}
				if(glowValue<=5){
					glowPulse = 0.2;
				}
			});
			break;
		case STATIC:
			glow.setSpread(0.4);
			glow.setRadius(10);
			text.setEffect(glow);
			break;
		}
	}

	public DropShadow getGlow(){
		return glow;
	}
	
	public void setStyle(Style style){
		this.text.setStyle(getStyle(style));
		this.text.setStroke(getColor(style).darker());
		this.glow.setColor(getColor(style));
		this.text.setEffect(glow);
	}

	public void setTextGlow(double radius, double spread){
		this.glow.setRadius(radius);
		this.glow.setSpread(spread);
		this.text.setEffect(glow);
	}

	public void setTextFill(Paint fill) {
		text.setStyle(null);
		text.setFill(fill);
	}

	public void setTextFill(String style) {
		text.setStyle(style);
	}

	private String getStyle(Style style){
		String blueStyle = "-fx-fill:  linear-gradient(cyan, dodgerblue);";
		String yellowStyle = "-fx-fill:  linear-gradient(yellow, orange );";
		String silverStyle = "-fx-fill:  linear-gradient(silver, gray);";
		String darkStyle = "-fx-fill:  linear-gradient(black, "
				+ "gray, black);";

		switch(style){
		case BLUE_STYLE:
			return blueStyle;
		case DARK_STYLE:
			return darkStyle;
		case SILVER_STYLE:
			return silverStyle;
		case YELLOW_STYLE:
			return yellowStyle;
		}
		return null;
	}

	private Color getColor(Style style){
		switch(style){
		case BLUE_STYLE:
			return Color.rgb(0, 140, 250);
		case DARK_STYLE:
			return Color.rgb(0, 0, 0);
		case SILVER_STYLE:
			return Color.rgb(180, 180, 180);
		case YELLOW_STYLE:
			return Color.rgb(250, 175, 0);
		}
		return null;
	}

	public StackPane get(){
		return container;
	}

	public enum Style{
		BLUE_STYLE,YELLOW_STYLE,SILVER_STYLE,DARK_STYLE;
	}

	public enum GlowType{
		STATIC, NONE, PULSING
	}
}

