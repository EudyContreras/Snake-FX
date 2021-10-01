package com.EudyContreras.Snake.Controllers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;
import com.EudyContreras.Snake.Application.GameManager;

/**
 * This manager class is responsible for managing, updating moving, displaying,
 * adding and removing different level objects each level object should have its
 * own separate list according to the amount of objects that will be rendered in
 * the game.
 *
 * @author Eudy Contreras
 *
 */
public class GameTileController {

	private List<AbstractTile> tiles;
	private List<AbstractTile> blocks;
	private List<AbstractTile> traps;
	private List<AbstractTile> edible;
	private AbstractTile tempTile;
	private AbstractTile tempTrap;
	private AbstractTile tempBlock;
	private AbstractTile tempEdible;
	private GameManager game;

	public GameTileController(GameManager game) {
		this.game = game;
		this.tiles = new LinkedList<AbstractTile>();
		this.blocks = new LinkedList<AbstractTile>();
		this.traps = new LinkedList<AbstractTile>();
		this.edible = new LinkedList<AbstractTile>();
	}

	public List<AbstractTile> getTile() {
		return tiles;
	}

	public List<AbstractTile> getBlock() {
		return blocks;
	}

	public List<AbstractTile> getTrap() {
		return traps;
	}

	public List<AbstractTile> getEdible() {
		return edible;
	}

	public void addTile(AbstractTile tile) {
		this.tiles.add(tile);
	}

	public void addBlock(AbstractTile tile) {
		this.blocks.add(tile);
	}

	public void addTrap(AbstractTile tile) {
		this.traps.add(tile);
	}

	public void addEdible(AbstractTile tile) {
		this.edible.add(tile);
	}

	public void addObjectAsArray(AbstractTile... tl) {
		if (tl.length > 1) {
			tiles.addAll(Arrays.asList(tl));
		} else {
			tiles.add(tl[0]);
		}
	}

	/**
	 * This method updates the objects using and iterator which only iterates
	 * through every object once. faster in some cases but it makes it so the
	 * list can only be modified through this method or else an exception will
	 * be thrown
	 */
	public void updateAll() {
		Iterator<AbstractTile> tileList = tiles.iterator();
		Iterator<AbstractTile> blockList = blocks.iterator();
		Iterator<AbstractTile> trapList = traps.iterator();
		Iterator<AbstractTile> edibleList = edible.iterator();

		while (tileList.hasNext()) {
			AbstractTile tempTile = tileList.next();
			tempTile.updateUI();
			tempTile.move();
			tempTile.checkCollision();
			if (!tempTile.isAlive()) {
				game.getDirtLayer().getChildren().remove(tempTile.getView());
				game.getGameRoot().getChildren().remove(tempTile.getView());
				game.getLevelLayer().getChildren().remove(tempTile.getView());
				game.getSecondLayer().getChildren().remove(tempTile.getView());
				game.getDirtLayer().getChildren().remove(tempTile.getView());
				tileList.remove();
				continue;
			}
		}
		while(blockList.hasNext()) {
			AbstractTile tempTile = blockList.next();
			tempTile.updateUI();
			tempTile.move();
			tempTile.checkCollision();
			if (!tempTile.isAlive()) {
				game.getGameRoot().getChildren().remove(tempTile.getView());
				game.getLevelLayer().getChildren().remove(tempTile.getView());
				game.getSecondLayer().getChildren().remove(tempTile.getView());
				blockList.remove();
				continue;
			}
		}
		while(trapList.hasNext()) {
			AbstractTile tempTile = trapList.next();
			tempTile.updateUI();
			tempTile.move();
			tempTile.checkCollision();
			if (!tempTile.isAlive()) {
				game.getGameRoot().getChildren().remove(tempTile.getView());
				game.getLevelLayer().getChildren().remove(tempTile.getView());
				game.getSecondLayer().getChildren().remove(tempTile.getView());
				trapList.remove();
				continue;
			}
		}
		while(edibleList.hasNext()) {
			AbstractTile tempTile = edibleList.next();
			tempTile.updateUI();
			tempTile.move();
			tempTile.checkCollision();
			if (!tempTile.isAlive()) {
				game.getGameRoot().getChildren().remove(tempTile.getView());
				game.getLevelLayer().getChildren().remove(tempTile.getView());
				game.getSecondLayer().getChildren().remove(tempTile.getView());
				game.getDirtLayer().getChildren().remove(tempTile.getView());
				edibleList.remove();
				continue;
			}
		}
	}

	/**
	 * Individually updates the tiles
	 */
	public void updateTiles() {
		for (int i = 0; i < tiles.size(); i++) {
			tempTile = tiles.get(i);
			tempTile.updateUI();
			tempTile.move();
			tempTile.checkCollision();
		}
	}

	/**
	 * Individually updates the blocks
	 */
	public void updateBlocks() {
		for (int i = 0; i < blocks.size(); i++) {
			tempBlock = blocks.get(i);
			tempBlock.updateUI();
			tempBlock.move();
			tempBlock.checkCollision();
		}
	}

	/**
	 * Individually updates the traps
	 */
	public void updateTraps() {
		for (int i = 0; i < traps.size(); i++) {
			tempTrap = traps.get(i);
			tempTrap.updateUI();
			tempTrap.move();
			tempTrap.checkCollision();
		}
	}
	/**
	 * Individually updates the traps
	 */
	public void updateEdibles() {
		for (int i = 0; i < edible.size(); i++) {
			tempEdible = edible.get(i);
			tempEdible.updateUI();
			tempEdible.move();
			tempEdible.checkCollision();
		}
	}

	/**
	 * This method loops through all the items and checks which item is ready to
	 * be removed and removes it.
	 */
	public void checkIfRemovable() {
		for (int i = 0; i < tiles.size(); i++) {
			tempTile = tiles.get(i);
			if (!tempTile.isAlive()) {
				game.getGameRoot().getChildren().remove(tempTile.getView());
				game.getSecondLayer().getChildren().remove(tempTile.getView());
				game.getDirtLayer().getChildren().remove(tempTile.getView());
				tiles.remove(i);
			}
		}
		for (int i = 0; i < traps.size(); i++) {
			tempTrap = traps.get(i);
			if (!tempTrap.isAlive()) {
				game.getGameRoot().getChildren().remove(tempTrap.getView());
				game.getSecondLayer().getChildren().remove(tempTrap.getView());
				traps.remove(i);
			}
		}
		for (int i = 0; i < blocks.size(); i++) {
			tempBlock = blocks.get(i);
			if (!tempBlock.isAlive()) {
				game.getGameRoot().getChildren().remove(tempBlock.getView());
				game.getSecondLayer().getChildren().remove(tempBlock.getView());
				blocks.remove(i);
			}
		}
		for (int i = 0; i < edible.size(); i++) {
			tempEdible = edible.get(i);
			if (!tempEdible.isAlive()) {
				game.getGameRoot().getChildren().remove(tempEdible.getView());
				game.getSecondLayer().getChildren().remove(tempEdible.getView());
				edible.remove(i);
			}
		}
	}

	public void clearTiles() {
		tiles.clear();
	}

	public void clearBlocks() {
		blocks.clear();
	}

	public void clearTraps() {
		traps.clear();
	}

	public void clearEdibles() {
		edible.clear();
	}

	public void clearAll() {
		tiles.clear();
		blocks.clear();
		traps.clear();
		edible.clear();
	}

}
