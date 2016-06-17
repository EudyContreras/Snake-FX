package com.EudyContreras.Snake.UserInterface;

import java.util.LinkedList;

import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class MenuBox{
	private Rectangle background;
	private LinkedList<MenuButton> buttonList;
    private VBox menuBox;
    private StackPane pane;
/**
 *
 * @param manager
 * @param x
 * @param y
 * @param width
 * @param height
 * @param spacing
 * @param fill
 * @param alignment
 */
	public MenuBox(GameManager manager, double x, double y, double width, double height, int spacing, Paint fill, Pos alignment ) {
		this.background = new Rectangle(GameManager.ScaleX(width), GameManager.ScaleY(height), fill);
		this.background.setArcHeight(50);
		this.background.setArcWidth(50);
		this.menuBox = new VBox(spacing);
		this.menuBox.setAlignment(alignment);
		this.pane = new StackPane();
		this.buttonList = new LinkedList<MenuButton>();
		this.pane.setTranslateX(GameManager.ScaleX(x));
		this.pane.setTranslateY(GameManager.ScaleY(y));
		this.pane.getChildren().addAll(background,menuBox);
	}
	public void addMenuButton(MenuButton button, int index) {
		this.menuBox.getChildren().add(index, button.BUTTON());
		this.buttonList.add(index, button);
	}
	public void addButtons(MenuButton... buttons){
		for(int i = 0; i<buttons.length; i++){
			this.menuBox.getChildren().add(buttons[i].BUTTON());
			this.buttonList.add(buttons[i]);
		}
	}
	public LinkedList<MenuButton> getButtons(){
		return buttonList;
	}
	public MenuButton getButton(int index){
		return buttonList.get(index);
	}
	public final StackPane getMenu(){
		return pane;
	}
	public void setMenuBoxSize(double width, double height) {
		this.background.setWidth(GameManager.ScaleX(width));
		this.background.setHeight(GameManager.ScaleY(height));
	}
	public void setMenuBoxCoordinates(double x, double y) {
		this.pane.setTranslateX(GameSettings.WIDTH/2-GameManager.ScaleX(x));
		this.pane.setTranslateY(GameSettings.HEIGHT/2-GameManager.ScaleY(y));
	}
	public void setMenuBoxBackground(Paint fill) {
		this.background.setFill(fill);
	}
}
