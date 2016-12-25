
package com.EudyContreras.Snake.GameObjects;

import java.util.Collections;
import java.util.LinkedList;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;
import com.EudyContreras.Snake.Application.GameSettings;
import com.EudyContreras.Snake.HudElements.ScoreKeeper;
import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.PathFindingAI.CellNode;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Every static object or esthetic object in the game such as walls, boxes etc
 * is considered a tile. This class is the main tile class and can be used for
 * creating any level object.
 *
 * @author Eudy Contreras
 *
 */
public class NoSpawnZone extends AbstractTile {
	private Rectangle2D collisionBounds;
	private Rectangle bounds;
	private GameManager game;
	private LinkedList<CellNode> cells;

	public NoSpawnZone(GameManager game, float x, float y, double width, double height, GameLevelObjectID id) {
		super(x, y, id);
		this.game = game;
		this.width = width;
		this.height = height;
		this.cells = new LinkedList<CellNode>();
		adjustBounds();
		drawBounds();

	}
	/**
	 * Method which initializes bounds for a specific object
	 */
	public void adjustBounds() {
		collisionBounds = new Rectangle2D(x, y, width, height);
	}
	/**
	 * Draws a bounding box
	 */
	public void drawBounds() {
		drawBoundingBox();
	}
	/**
	 * Draws the bounding box of this object for debugging purposes
	 */
	public void drawBoundingBox() {
		if (GameSettings.DEBUG_MODE) {
			bounds = new Rectangle(x, y, width, height);
			bounds.setStroke(Color.RED);
			bounds.setFill(Color.TRANSPARENT);
			bounds.setStrokeWidth(3);
			game.getSeventhLayer().getChildren().add(bounds);
		}
	}

	public void updateUI(){
		if(ScoreKeeper.APPLE_COUNT<GameSettings.APPLE_COUNT	){
			setAlive(false);
			clearCells();
			if(GameSettings.DEBUG_MODE)
			game.getSeventhLayer().getChildren().remove(bounds);
		}
	}

	public void setCell(CellNode... cell){
		Collections.addAll(cells, cell);
	}

	public void clearCells(){
		for(CellNode cell: cells){
			cell.setPlayerSpawnZone(false);
		}
		cells.clear();
		cells = null;
	}
	/**
	 * This methods will return specified bounds of this object
	 * based on coordinates and dimensions.
	 */
	public Rectangle2D getBounds() {
		return collisionBounds;
	}

}
