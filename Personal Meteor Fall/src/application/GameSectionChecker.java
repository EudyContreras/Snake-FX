
package application;
import javafx.scene.image.Image;

public class GameSectionChecker extends Tile {
	Game game;
	boolean loadFirstSect = false;
	boolean loadSecondSect = false;
	boolean loadThirdSect = false;
	boolean loadFourthSect = false;
	boolean loadFithSect = false;
	boolean loadFirst = false;
	boolean loadSecond = false;
	boolean loadThird = false;
	boolean loadFourth = false;
	boolean loadFith = false;
    public GameSectionChecker(Game game,float x, float y, float velX, float velY,Image image ) {
        super(x,y,image);
        this.game = game;
        this.velX = velX;
        this.velY = velY;
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
    }

}
