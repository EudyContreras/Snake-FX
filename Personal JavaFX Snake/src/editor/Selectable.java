package editor;

import javafx.scene.image.Image;
import javafx.scene.layout.Region;

public class Selectable extends Region {

    private TileMap tile;

    public Selectable(Image image, double width, double height) {
    	tile = new TileMap(0,0, width, height, image);
        this.getChildren().add( tile.getView());
        this.setPrefSize(width, height);
    }

}
