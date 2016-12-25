
package com.EudyContreras.Snake.AbstractModels;

import com.EudyContreras.Snake.Identifiers.GameLevelObjectID;
import com.EudyContreras.Snake.PathFindingAI.CellNode;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class is the game tile super class and is the class that every tile
 * object or level object extends. Events within this class will reflect on the
 * children of this class.
 *
 * @author Eudy Contreras
 *
 */
public interface GameTile {

	public ImageView getView();

	public void setView(ImageView view);

	public double getX();

	public double getY();

	public double getR();

	public void setR(double r);

	public double getVelX();

	public void setVelX(double velX);

	public double getVelY();

	public void setVelY(double velY);

	public double getVelR();

	public void setVelR(double velR);

	public void relocate(double x, double y);

	public void relocate(Point2D point);

	public void move();

	public Image getTexture();

	public void setTexture(Image image);

	public void setCell(CellNode... cell);

	public void setDimension(Dimension2D dimension);

	public Dimension2D getDimension();

	public double getWidth();

	public void setWidth(double width);

	public double getHeight();

	public void setHeight(double height);

	public void setX(double x);

	public void setY(double y);

	public void setId(GameLevelObjectID id);

	public GameLevelObjectID getId();

	public boolean isVisible();

	public boolean isCullable();

	public void addPhysics();

	public void removePhysics();

	public void updateLogic();

	public void updateUI();

	public void updatePhisics();

	public Rectangle2D getCollisionBounds();

	public Rectangle2D getBoundsNorth();

	public Rectangle2D getBoundsEast();

	public Rectangle2D getBoundsWest();

	public Rectangle2D getBoundsSouth();

	public Bounds getNodeBounds();


}
