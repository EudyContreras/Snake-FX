package com.EudyContreras.Snake.AbstractModels;

import com.EudyContreras.Snake.PathFindingAI.CellNode;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

/**
 * This class is the game object super class and is the class that every game
 * object or mob extends. Actions performed within this class will reflect on
 * the children of this class.
 *
 * @author Eudy Contreras
 *
 */
public interface GameObject {

	public void initialize();

	public void addToLayer();

	public ImageView getView();

	public void addToLayer(Node node);

	public void removeFromLayer();

	public void removeFromLayer(Node node);

	public Pane getLayer();

	public void setLayer(Pane layer);

	public double getX();

	public void setX(double x);

	public double getY();

	public void setY(double y);

	public double getR();

	public void setR(double rotation);

	public void setRotationProperty(Rotate rotation);

	public void relocate(Point point);

	public double getVelX();

	public void setVelX(double velX);

	public double getVelY();

	public void setVelY(double velY);

	public double getVelR();

	public void setVelR(double velR);

	public double getXDistance(double x);

	public double getYDistance(double y);

	public double getDistance(double x, double y);

	public double getInterpolarXDistance(double x);

	public double getInterpolarYDistance(double y);

	public double getInterpolarDistance(double x, double y);

	public double getHealth();

	public double getDamage();

	public void setDamage(double damage);

	public void setHealth(double health);

	public boolean isRemovable();

	public void setRemovable(boolean removable);

	public void updateAnimation(long timePassed);

	public void addPhysics();

	public void removePhysics();

	public void updateLogic();

	public void updateUI();

	public void updatePhisics();

	public void checkCollision();

	public void onCollison(GameObject object);

	public void move();

	public boolean isAlive();

	public boolean isDead();

	public void getDamagedBy(GameObject object);

	public void kill();

	public void remove();

	public double getWidth();

	public double getHeight();

	public double getCenterX();

	public double getCenterY();

	public double getRadius();

	public Bounds getRadialBounds();

	public boolean isStatic();

	public boolean isMoving();

	public CellNode getRelativeCell();

	public void setRelativeCell(CellNode cell);

	public Rectangle2D getCollisionBounds();

	public Rectangle2D getBoundsNorth();

	public Rectangle2D getBoundsEast();

	public Rectangle2D getBoundsWest();

	public Rectangle2D getBoundsSouth();

	public void bounce(GameObject object, double x, double y);



}
