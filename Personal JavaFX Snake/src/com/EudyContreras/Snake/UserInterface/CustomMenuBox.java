package com.EudyContreras.Snake.UserInterface;

import java.util.LinkedList;

import com.EudyContreras.Snake.Application.GameSettings;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class CustomMenuBox {
	private Rectangle background;
	private LinkedList<CustomMenuButton> buttonList;
	private StackPane pane;
	private VBox menuBox;
	private double width;
	private double height;

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
	public CustomMenuBox(double width, double height, int spacing, Paint fill, Pos alignment) {
		this.width = width;
		this.height = height;
		this.background = new Rectangle(width, height);
		this.background.setStyle(MenuButtonStyles.MENU_BOX_STYLE);
		this.background.setFill(Color.BLACK);
		this.background.setArcHeight(50);
		this.background.setArcWidth(50);
		this.menuBox = new VBox(spacing);
		this.menuBox.setAlignment(alignment);
		this.pane = new StackPane();
		this.buttonList = new LinkedList<CustomMenuButton>();
		this.pane.setTranslateX(GameSettings.WIDTH / 2 - width);
		this.pane.setTranslateY(GameSettings.HEIGHT / 2 - height);
		this.pane.getChildren().addAll(background, menuBox);
	}

	public CustomMenuBox(double width, double height, int spacing, String style, Pos alignment) {
		this.width = width;
		this.height = height;
		this.background = new Rectangle(width, height);
		this.background.setStyle(style);
		this.background.setFill(Color.BLACK);
		this.background.setArcHeight(50);
		this.background.setArcWidth(50);
		this.menuBox = new VBox(spacing);
		this.menuBox.setAlignment(alignment);
		this.pane = new StackPane();
		this.buttonList = new LinkedList<CustomMenuButton>();
		this.pane.setTranslateX(GameSettings.WIDTH / 2 - width);
		this.pane.setTranslateY(GameSettings.HEIGHT / 2 - height);
		this.pane.getChildren().addAll(background, menuBox);
	}

	public void addMenuButton(CustomMenuButton button, int index) {
		this.menuBox.getChildren().add(index, button.BUTTON());
		this.menuBox.setMaxWidth(button.getButtonWidth());
		this.buttonList.add(index, button);
	}

	public void addButtons(CustomMenuButton... buttons) {
		for (int i = 0; i < buttons.length; i++) {
			this.menuBox.getChildren().add(buttons[i].BUTTON());
			this.menuBox.setMaxWidth(buttons[i].getButtonWidth());
			this.buttonList.add(buttons[i]);
		}
	}

	public void setChosen(int index) {
		for (int i = 0; i < getButtons().size(); i++) {
			this.getButtons().get(i).setActive(false);
		}
		this.getButton(index).setActive(true);

	}

	public LinkedList<CustomMenuButton> getButtons() {
		return buttonList;
	}

	public CustomMenuButton getButton(int index) {
		return buttonList.get(index);
	}

	public int buttonCount() {
		return buttonList.size();
	}

	public final StackPane getMenu() {
		return pane;
	}

	public VBox getMenuBox() {
		return menuBox;
	}

	public void setMenuBoxSize(double width, double height) {
		this.background.setWidth(width);
		this.background.setHeight(height);
	}

	public void setMenuBoxCoordinates(double x, double y) {
		this.pane.setTranslateX(x);
		this.pane.setTranslateY(y);
	}

	public void setMenuBoxOffset(double offsetX, double offsetY) {
		this.pane.setTranslateX((GameSettings.WIDTH / 2 - width / 2) + offsetX);
		this.pane.setTranslateY((GameSettings.HEIGHT / 2 - height / 2) + offsetY);
	}

	public void setMenuBoxBackground(Paint fill) {
		this.background.setFill(fill);
	}
}
