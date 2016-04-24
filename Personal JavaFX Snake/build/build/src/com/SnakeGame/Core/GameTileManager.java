package com.SnakeGame.Core;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This manager class is responsible for managing, updating
 * moving, displaying, adding and removing different level objects 
 * each level object should have its own separate list according to 
 * the amount of objects that will be rendered in the game.
 * @author Eudy Contreras
 *
 */
public class GameTileManager {
	
	
	public List<Tile> tile;
	public List<Tile> block;
	public List<Tile> trap;
	public Tile tempTile;
	public Tile tempTrap;
	public Tile tempBlock;
	public SnakeGame game;
	
	public GameTileManager(SnakeGame game){
		this.game = game;
		this.tile = new LinkedList<Tile> ();
		this.block = new LinkedList<Tile>();
		this.trap = new LinkedList<Tile>();
	}
	public void addTile(Tile tile){
		this.tile.add(tile);
	}
	public void addBlock(Tile tile){
		this.block.add(tile);
	}
	public void addTrap(Tile tile){
		this.trap.add(tile);
	}
    public void addObjectAsArray(Tile... tl) {
        if (tl.length > 1) {
        	tile.addAll(Arrays.asList(tl));
        } else {
        	tile.add(tl[0]);
        }
    } 
	/**
	 * This method updates the objects using and iterator
	 * which only iterates through every object once. faster
	 * in some cases but it makes it so the list can only 
	 * be modified through this method or else an exception will be thrown
	 */
	public void updateAll(){
		for(Iterator<Tile> tileList = tile.iterator(); tileList.hasNext();){
			Tile tempTile = tileList.next();
			tempTile.updateUI();;
			tempTile.move();
			if(tempTile.isBehindCharacter() || !tempTile.isAlive()){
				game.root.getChildren().remove(tempTile.view);
				game.levelLayer.getChildren().remove(tempTile.view);
				game.bottomLayer.getChildren().remove(tempTile.view);
				tileList.remove();
				continue;
			}
			
		}
	}
	/**
	 * Individually updates the tiles
	 */
	public void updateTiles(){		
		for(int i = 0; i<tile.size(); i++){
			tempTile = tile.get(i);
			tempTile.updateUI();
			tempTile.move();
		}
	}
	/**
	 * Individually updates the blocks
	 */
	public void updateBlock(){		
		for(int i = 0; i<block.size(); i++){
			tempBlock = block.get(i);
			tempBlock.updateUI();
			tempBlock.move();
		}
	}
	/**
	 * Individually updates the traps
	 */
	public void updateTrap(){		
		for(int i = 0; i<trap.size(); i++){
			tempTrap = trap.get(i);
			tempTrap.updateUI();
			tempTrap.move();
		}
	}
	/**
	 * This method loops through all the items and checks which item
	 * is ready to be removed and removes it.
	 */
	public void checkIfRemovable(){
		for(int i = 0; i<tile.size(); i++){
			tempTile = tile.get(i);
			if(tempTile.isBehindCharacter() || !tempTile.isAlive()){
				game.root.getChildren().remove(tempTile.view);
				game.bottomLayer.getChildren().remove(tempTile.view);
				tile.remove(i);
			}
		}
		for(int i = 0; i<trap.size(); i++){
			tempTrap = trap.get(i);
			if(tempTrap.isBehindCharacter() || !tempTrap.isAlive()){
				game.root.getChildren().remove(tempTrap.view);
				game.bottomLayer.getChildren().remove(tempTrap.view);
				trap.remove(i);
			}
		}
		for(int i = 0; i<block.size(); i++){
			tempBlock = block.get(i);
			if(tempBlock.isBehindCharacter() || !tempBlock.isAlive()){
				game.root.getChildren().remove(tempBlock.view);
				game.bottomLayer.getChildren().remove(tempBlock.view);
				block.remove(i);
			}
		}
	}
	public void clearAll(){
		tile.clear();
		block.clear();
		trap.clear();
	}

}

