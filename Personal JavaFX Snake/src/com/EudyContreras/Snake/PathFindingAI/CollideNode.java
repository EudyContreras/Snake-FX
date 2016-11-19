package com.EudyContreras.Snake.PathFindingAI;

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
public class CollideNode {
	private double x;
	private double y;
	private double width;
	private double height;
	private double threshold;
	private double clearRadius;
	private AbstractTile object;
	private Dimension2D dimension;
	private RiskFactor riskFactor;

	public CollideNode(AIPathFinder evader, AbstractTile warning, RiskFactor risk) {
		super();
		this.x = warning.getX();
		this.y = warning.getY();
		this.width = warning.getWidth();
		this.height = warning.getHeight();
		this.object = warning;

		if(warning.getBounds()!=null){
			this.x = warning.getBounds().getMinX();
			this.y = warning.getBounds().getMinY();
			this.width = warning.getBounds().getWidth();
			this.height = warning.getBounds().getHeight();
		}

		this.riskFactor = risk;
		this.clearRadius = 0;
		this.threshold = 100;
		this.dimension = new Dimension2D(width,height);
	}

	public RiskFactor getRiskFactor(double x, double y){
		if(getDistance(x,y)<=threshold/2){
			return RiskFactor.VERY_HIGH;
		}
		else if(getDistance(x,y)<=threshold && getDistance(x,y)>threshold/2){
			return RiskFactor.HIGH;
		}
		else if(getDistance(x,y)>threshold*1.2 && getDistance(x,y)<=threshold*2){
			return RiskFactor.MEDIUM;
		}
		else{
			return RiskFactor.LOW;
		}
	}

	public AbstractTile getObject() {
		return object;
	}

	public RiskFactor getRiskFactor() {
		return riskFactor;
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

	public Dimension2D Dimension() {
		return dimension;
	}

	public double getRangeX(double x) {
		return Math.abs(x - this.x);
	}

	public double getRangeY(double y) {
		return Math.abs(y - this.y);
	}

	public double getDistance(double x, double y) {
		return Math.abs(x - this.x) + Math.abs(y - this.y);
	}

	public enum RiskFactor {
		VERY_HIGH, HIGH, MEDIUM, LOW, NO_SPAWN_ZONE
	}

	public Rectangle2D getCollideRadius() {
		return new Rectangle2D(x - clearRadius, y - clearRadius, width + clearRadius * 2, height + clearRadius * 2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		CollideNode test = (CollideNode) obj;
		return (dimension.getWidth() == test.dimension.getWidth() &&  dimension.getHeight() == test.dimension.getHeight() &&
			    x == test.x && y == test.y);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		Double data = dimension.getWidth()+dimension.getHeight()+x+y;
		hash = (int) (31 * hash + data);
		hash = 31 * hash + (null == data ? 0 : data.hashCode());
		return hash;
	}

}
