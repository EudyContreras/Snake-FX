package com.EudyContreras.Snake.UserInterface;

import com.EudyContreras.Snake.AbstractModels.AbstractMenuElement;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.FrameWork.ResizeHelper;
import com.EudyContreras.Snake.Identifiers.GameModeID;
import com.EudyContreras.Snake.UserInterface.CustomMenuButton.ButtonStyle;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MenuModes extends AbstractMenuElement {

	private final static Pane GAME_MODE_MENU = new Pane();
	private final static CustomMenuBox MODES_MENU_BOX = new CustomMenuBox(1200, 700, 30, Color.BLACK,Pos.CENTER);

	public MenuModes(GameManager game, MenuManager menu) {
		this.game = game;
		this.menu = menu;
		this.setUp();
	}

	public void setUp() {

		MODES_MENU_BOX.setMenuBoxOffset(0,140);

		MODES_MENU_BOX.addButtons(
				new CustomMenuButton("CLASSIC MODE", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true),
				new CustomMenuButton("TIME MODE", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true),
				new CustomMenuButton("MULTIPLAYER", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true),
				new CustomMenuButton("GO BACK", Pos.CENTER, ButtonStyle.BLACK, Color.WHITE, fontSize, buttonSizeW,
						buttonSizeH, true));
	}

	public Pane modes_menu_screen() {
		currentChoice = 0;

		MODES_MENU_BOX.setChosen(currentChoice);

		game.getScene().setOnKeyPressed(e -> {

			switch (e.getCode()) {
			case UP:
				currentChoice -= 1;
				if (currentChoice < 0) {
					currentChoice = MODES_MENU_BOX.buttonCount() - 1;
				}
				MODES_MENU_BOX.setChosen(currentChoice);
				break;
			case DOWN:
				currentChoice += 1;
				if (currentChoice > MODES_MENU_BOX.buttonCount() - 1) {
					currentChoice = 0;
				}
				MODES_MENU_BOX.setChosen(currentChoice);
				break;
			case W:
				currentChoice -= 1;
				if (currentChoice < 0) {
					currentChoice = MODES_MENU_BOX.buttonCount() - 1;
				}
				MODES_MENU_BOX.setChosen(currentChoice);
				break;
			case S:
				currentChoice += 1;
				if (currentChoice > MODES_MENU_BOX.buttonCount() - 1) {
					currentChoice = 0;
				}
				MODES_MENU_BOX.setChosen(currentChoice);
				break;
			case ESCAPE:
				if(menu.getConnectHub().isShowing()){
					menu.getConnectHub().swipeUp(()->menu.onFocus());
				}else{
					MODES_MENU_BOX.getButton(3).deactivate();
				}
				break;
			case H:
				menu.showFXConnect();
				break;
			case ENTER:
				if(!e.isControlDown()){
					MODES_MENU_BOX.getButton(currentChoice).activate();
				}
				if(!game.getMainWindow().isFullScreen()&& ResizeHelper.isSupportedRatio()){
					game.setNewRatio(true);
					game.getMainWindow().setFullScreen(true);
					game.getGameBorder().showBorders(false);
				}
				else{
					game.setNewRatio(false);
					game.getMainWindow().setFullScreen(false);
					game.getGameBorder().showBorders(true);
				}
				break;
			case SPACE:
				MODES_MENU_BOX.getButton(currentChoice).activate();
			default:
				break;
			}
		});
		game.getScene().setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case ENTER:
				if(!e.isControlDown()){
					MODES_MENU_BOX.getButton(currentChoice).deactivate();
				}
				break;
			case SPACE:
				MODES_MENU_BOX.getButton(currentChoice).deactivate();
				break;
			case ESCAPE:

				break;
			default:
				break;
			}
		});
		MODES_MENU_BOX.getButton(0).setAction(() -> menu.startSelected(GameModeID.ClassicMode));
		MODES_MENU_BOX.getButton(2).setAction(() -> menu.startSelected(GameModeID.LocalMultiplayer));
		MODES_MENU_BOX.getButton(3).setAction(() -> menu.goBack());

		for (int i = 0; i < MODES_MENU_BOX.getButtons().size(); i++) {
			final int index = i;

			MODES_MENU_BOX.getButtons().get(i).setOnHover(() -> {
				resetSelections(MODES_MENU_BOX, index);
			});

		}
		GAME_MODE_MENU.getChildren().remove(MODES_MENU_BOX.getMenu());
		GAME_MODE_MENU.getChildren().add(MODES_MENU_BOX.getMenu());

		return GAME_MODE_MENU;
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
