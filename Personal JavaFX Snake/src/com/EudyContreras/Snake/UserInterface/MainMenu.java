package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.AbstractModels.AbstractMenuElement;
import com.EudyContreras.Snake.FrameWork.GameManager;
import com.EudyContreras.Snake.UserInterface.CustomMenuButton.ButtonStyle;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainMenu extends AbstractMenuElement {

	private final static Pane MAIN_MENU = new Pane();
	private final static CustomMenuBox MAIN_MENU_BOX = new CustomMenuBox(800, 700, 30, Color.BLACK, Pos.CENTER);

	public MainMenu(GameManager game, MenuManager menu) {
		this.game = game;
		this.menu = menu;
		this.setUp();
	}

	public void setUp() {

		MAIN_MENU_BOX.setMenuBoxOffset(-600,150);

		MAIN_MENU_BOX.addButtons(
				new CustomMenuButton("START GAME", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true),
				new CustomMenuButton("OPTIONS", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true),
				new CustomMenuButton("GAME MODES", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true),
				new CustomMenuButton("HIGH SCORES", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true),
				new CustomMenuButton("MULTIPLAYER", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true),
				new CustomMenuButton("EXIT", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true));
	}

	public final Pane main_menu_screen() {
		currentChoice = 0;

		MAIN_MENU_BOX.setChosen(currentChoice);

		game.getScene().setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP:
				currentChoice -= 1;
				if (currentChoice < 0) {
					currentChoice = MAIN_MENU_BOX.buttonCount() - 1;
				}
				MAIN_MENU_BOX.setChosen(currentChoice);
				break;
			case DOWN:
				currentChoice += 1;
				if (currentChoice > MAIN_MENU_BOX.buttonCount() - 1) {
					currentChoice = 0;
				}
				MAIN_MENU_BOX.setChosen(currentChoice);
				break;
			case W:
				currentChoice -= 1;
				if (currentChoice < 0) {
					currentChoice = MAIN_MENU_BOX.buttonCount() - 1;
				}
				MAIN_MENU_BOX.setChosen(currentChoice);
				break;
			case S:
				currentChoice += 1;
				if (currentChoice > MAIN_MENU_BOX.buttonCount() - 1) {
					currentChoice = 0;
				}
				MAIN_MENU_BOX.setChosen(currentChoice);
				break;
			case ENTER:
				MAIN_MENU_BOX.getButton(currentChoice).activate();
				break;
			case SPACE:
				MAIN_MENU_BOX.getButton(currentChoice).activate();
			default:
				break;
			}
		});
		game.getScene().setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case ENTER:
				MAIN_MENU_BOX.getButton(currentChoice).deactivate();
				break;
			case SPACE:
				MAIN_MENU_BOX.getButton(currentChoice).deactivate();
				break;
			default:
				break;
			}
		});

		MAIN_MENU_BOX.getButton(0).setAction(() -> menu.startSelected(game.getModeID()));
		MAIN_MENU_BOX.getButton(2).setAction(() -> menu.gameModesMenu());
		MAIN_MENU_BOX.getButton(5).setAction(() -> menu.closeGame());

		for (int i = 0; i < MAIN_MENU_BOX.buttonCount(); i++) {
			final int index = i;

			MAIN_MENU_BOX.getButtons().get(i).setOnHover(() -> {
				resetSelections(MAIN_MENU_BOX, index);
			});

		}

		MAIN_MENU.getChildren().remove(MAIN_MENU_BOX.getMenu());
		MAIN_MENU.getChildren().add(MAIN_MENU_BOX.getMenu());

		return MAIN_MENU;
	}

	private final void resetSelections(CustomMenuBox menu, int index) {
		currentChoice = index;
		for (int i = 0; i < menu.buttonCount(); i++) {
			if (i != currentChoice) {
				menu.getButton(i).setActive(false);
			} else {
				menu.getButton(i).setActive(true);
			}
		}
	}
}
