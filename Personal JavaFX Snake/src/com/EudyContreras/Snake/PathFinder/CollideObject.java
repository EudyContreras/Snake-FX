package com.EudyContreras.Snake.PathFinder;

import com.EudyContreras.Snake.AbstractModels.AbstractTile;

import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
/**
 * So once the snake gets close to the object the object activates
 * and becomes a risk. If the object is a risk it sends information to the
 * AI controller about where not to turn and or where to turn in order to avoid
 * collision with the object
 *
 * @author Eudy Contreras
 *
 */
public class CollideObject {
	private double x;
	private double y;
	private double width;
	private double height;
	private double threshold;
	private double clearRadius;
	private Double distance;
	private Double distanceX;
	private Double distanceY;
	private Dimension2D dimension;
	private ObjectEvasionAI evader;
	private RangeFactor range;
	private Location location;

	public CollideObject(ObjectEvasionAI evader, AbstractTile warning) {
		super();
		this.evader = evader;
		this.x = warning.getX();
		this.y = warning.getY();
		this.width = warning.getWidth();
		this.height = warning.getHeight();
		this.clearRadius = 30;
		this.threshold = 120;
		this.dimension = new Dimension2D(width,height);
	}
	public void updateProperties(){
		updateDistances();
		calculateRelativeLocation(evader,evader.getX(),evader.getY());
	}
	private void calculateRelativeLocation(ObjectEvasionAI evader, double evaderX, double evaderY){
		if(evader.getCollisionBounds().getMaxX()>=getCollideRadius().getMinX()
		&& evader.getCollisionBounds().getMinX()<=getCollideRadius().getMaxX()){
			range = RangeFactor.WITHIN_RANGE;
			if(y<evaderY){
				location = Location.NORTH_OF_PLAYER;
			}
			else{
				location = Location.SOUTH_OF_PLAYER;
			}
		}
		if(evader.getCollisionBounds().getMaxY()>=getCollideRadius().getMinY()
		&& evader.getCollisionBounds().getMinY()<=getCollideRadius().getMaxY()){
			range = RangeFactor.WITHIN_RANGE;
			if(x<evaderX){
				location = Location.WEST_OF_PLAYER;
			}
			else{
				location = Location.EAST_OF_PlAYER;
			}
		}
		else{
			range = RangeFactor.OUT_OF_RANGE;
		}
	}
	public void updateDistances(){
		distanceX = Math.abs(evader.getX() - x);
		distanceY = Math.abs(evader.getY() - y);
		distance = Math.abs(evader.getX() - x)+Math.abs(evader.getY() - y);
	}
	public RiskFactor getRiskFactor(){
		if(distance<=threshold){
			return RiskFactor.HIGH;
		}
		else{
			return RiskFactor.LOW;
		}
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public Dimension2D Dimension(){
		return dimension;
	}
	public double getRangeX() {
		return distanceX;
	}
	public double getRangeY() {
		return distanceY;
	}
	public double getDistance() {
		return distance;
	}
	public RangeFactor getRange(){
		return range;
	}
	public Location getLocation(){
		return location;
	}
	public enum Location{
		NORTH_OF_PLAYER,SOUTH_OF_PLAYER,EAST_OF_PlAYER,WEST_OF_PLAYER
	}
	public enum RiskFactor{
		HIGH,LOW
	}
	public enum RangeFactor{
		WITHIN_RANGE,OUT_OF_RANGE
	}
	public Rectangle2D getCollideRadius(){
		return new Rectangle2D(x-clearRadius, y-clearRadius, width+clearRadius*2, height+clearRadius*2);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;

		CollideObject test = (CollideObject) obj;
		return dimension == test.dimension && (distance == test.distance || (distance != null && distance.equals(test.distance)));
	}
	@Override
	public int hashCode() {
		int hash = 7;
		Double data = distance;
		hash = (int) (31 * hash + distance);
		hash = 31 * hash + (null == data ? 0 : data.hashCode());
		return hash;
	}

}
