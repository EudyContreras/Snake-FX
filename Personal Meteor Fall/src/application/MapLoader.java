
package application;

import javafx.scene.image.Image;

public class MapLoader {

    private final Game game;
    private final Image tile1 = new Image("1.gif", 64, 64, true, false, true);
    private final Image tile2 = new Image("2.gif", 64, 64, true, false, true);
    private final Image tile3 = new Image("3.gif", 64, 64, true, false, true);
    private final Image tile4 = new Image("4.gif", 64, 64, true, false, true);
    private final Image tile5 = new Image("5.gif", 64, 64, true, false, true);
    private final Image tile6 = new Image("pillar1.gif", 64, 64, false, true, true);
    private final Image tile7 = new Image("pillar2.gif", 64, 64, false, true, true);
    private final Image tile8 = new Image("pillar3.gif", 64, 64, false, true, true);
    private final Image tile9 = new Image("6.gif", 64, 64, true, false, true);
    private final Image coin = new Image("coin1.gif", 32, 32, true, false, true);
    private final Image collImg = new Image("coll.gif", 64, 64, true, false, true);
    public final int levelWidth = MapData.Level1[0].length() * 64;

    public MapLoader(Game game) {

        this.game = game;
    }

    public void loadMap() {

        for (int i = 0; i < MapData.Level1.length; i++) { //Colums
            String line = MapData.Level1[i]; //Rows

            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        createTile(j * 64, i * 64, tile1);
                        break;
                    case '2':
                        noCollTile(j * 64, i * 64, tile2);
                        break;
                    case '3':
                        createTile(j * 64, i * 64, tile3);
                        break;
                    case '4':
                        createCollide(j * 64, i * 64, collImg);
                        break;
                    case '5':
                        createTile(j * 64, i * 64, tile4);
                        break;             
                    case '6':
                        noCollTile(j * 64, i * 64, tile5);
                        break;
                    case '7':
                        noCollTile(j * 64, i * 64, tile6);
                        break;
                    case '8':
                        noCollTile(j * 64, i * 64, tile7);
                        break;
                    case '9':
                        noCollTile(j * 64, i * 64, tile8);
                        break;
                    case 'C':
                        loadCoin(j * 64, i * 64, coin);
                        break;
                    case '!':
                        createTile(j * 64, i * 64, tile9);
                        break;
                }
            }
        }
    }

	private void createTile(float x, float y, Image image) {
		TileMap texture = new TileMap(x, y,0,0, image);
		if (image.isBackgroundLoading())
			game.root.getChildren().add(texture.getView());
	} 

	private void noCollTile(float x, float y, Image image) {
		TileMap texture = new TileMap(x, y,0,0, image);
		if (image.isBackgroundLoading())
			game.root.getChildren().add(texture.getView());
	}

	private void createCollide(float x, float y, Image image) {
		TileMap collide = new TileMap(x, y,0,0, image);
		if (image.isBackgroundLoading())
			game.root.getChildren().add(collide.getView());
	}

	private void loadCoin(float x, float y, Image image) {
		TileMap coin = new TileMap(x, y,0,0, image);
		if (image.isBackgroundLoading())
			game.root.getChildren().add(coin.getView());
	}

}
