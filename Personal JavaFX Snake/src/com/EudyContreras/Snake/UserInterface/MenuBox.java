package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.FrameWork.GameManager;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class MenuBox{
	private Rectangle background;
    private VBox menuBox;

	public MenuBox(GameManager manager, double x, double y, double width, double height, int spacing, Paint fill, Pos alignment ) {
		this.background = new Rectangle(width, height, fill);
		this.menuBox = new VBox(spacing);
		this.menuBox.setAlignment(alignment);
		this.menuBox.setTranslateX(x);
		this.menuBox.setTranslateY(y);
		this.menuBox.getChildren().add(0,background);
	}
	public void addMenuButton(MenuButton button) {
		this.menuBox.getChildren().add(button);
	}
	public void addButtons(MenuButton[] buttons){
		for(int i = 0; i<buttons.length; i++){
			this.menuBox.getChildren().add(buttons[i]);
		}
	}
	public final VBox getMenu(){
		return menuBox;
	}
	public void setMenuBoxSize(double width, double height) {
		this.background.setWidth(width);
		this.background.setHeight(height);
	}
	public void setMenuBoxCoordinates(double x, double y) {
		this.menuBox.setTranslateX(x);
		this.menuBox.setTranslateY(y);
	}
	public void setMenuBoxBackground(Paint fill) {
		this.background.setFill(fill);
	}
}
