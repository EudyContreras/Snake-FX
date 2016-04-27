package application;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;



public class GameTileManager {
	
	
	public List<Tile> tile;
	public List<Tile> barrel;
	public List<Tile> box;
	public Tile tempTile;
	public Tile tempBox;
	public Tile tempBarrel;
	public Game game;
	
	public GameTileManager(Game game){
		this.game = game;
		this.tile = new ArrayList<Tile> ();
		this.barrel = new ArrayList<Tile>();
		this.box = new ArrayList<Tile>();
	}
	public void addTile(Tile tile){
		this.tile.add(tile);
	}
	public void addBarrel(Tile tile){
		this.barrel.add(tile);
	}
	public void addBox(Tile tile){
		this.box.add(tile);
	}
    public void addObjectAsArray(Tile... tl) {
        if (tl.length > 1) {
        	tile.addAll(Arrays.asList(tl));
        } else {
        	tile.add(tl[0]);
        }
    } 
	public void updateAll(){
		for(Iterator<Tile> tileList = tile.iterator(); tileList.hasNext();){
			Tile tempTile = tileList.next();
			tempTile.updateUI();;
			tempTile.move();
			if(tempTile.isBehindCharacter() || !tempTile.isAlive()){
				game.levelLayer.getChildren().remove(tempTile.view);
				game.bottomLayer.getChildren().remove(tempTile.view);
				tileList.remove();
				continue;
			}
			
		}
	}
	public void updateTiles(){		
		for(int i = 0; i<tile.size(); i++){
			tempTile = tile.get(i);
			tempTile.updateUI();
			tempTile.move();
		}
	}
	public void updateBarrels(){		
		for(int i = 0; i<barrel.size(); i++){
			tempBarrel = barrel.get(i);
			tempBarrel.updateUI();
			tempBarrel.move();
		}
	}
	public void updateBoxes(){		
		for(int i = 0; i<box.size(); i++){
			tempBox = box.get(i);
			tempBox.updateUI();
			tempBox.move();
		}
	}
	public void checkIfRemovable(){
		for(int i = 0; i<tile.size(); i++){
			tempTile = tile.get(i);
			if(tempTile.isBehindCharacter() || !tempTile.isAlive()){
				game.levelLayer.getChildren().remove(tempTile.view);
				game.bottomLayer.getChildren().remove(tempTile.view);
				tile.remove(i);
			}
		}
		for(int i = 0; i<box.size(); i++){
			tempBox = box.get(i);
			if(tempBox.isBehindCharacter() || !tempBox.isAlive()){
				game.levelLayer.getChildren().remove(tempBox.view);
				game.bottomLayer.getChildren().remove(tempBox.view);
				box.remove(i);
			}
		}
		for(int i = 0; i<barrel.size(); i++){
			tempBarrel = barrel.get(i);
			if(tempBarrel.isBehindCharacter() || !tempBarrel.isAlive()){
				game.levelLayer.getChildren().remove(tempBarrel.view);
				game.bottomLayer.getChildren().remove(tempBarrel.view);
				barrel.remove(i);
			}
		}
		
	}
	public void clearAll(){
		tile.clear();
		box.clear();
		barrel.clear();
	}

}

