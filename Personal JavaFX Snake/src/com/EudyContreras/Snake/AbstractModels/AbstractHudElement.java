
package com.EudyContreras.Snake.AbstractModels;

import com.EudyContreras.Snake.FrameWork.GameSettings;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

/**

 *
 * @author Eudy Contreras
 *
 */
public abstract class AbstractHudElement {

	protected double baseY;
	protected double baseX;
	protected double xOne;
	protected double yOne;
	protected double rOne;
	protected double xTwo;
	protected double yTwo;
	protected double rTwo;
	protected double xThree;
	protected double yThree;
	protected double rThree;
	protected double xFour;
	protected double yFour;
	protected double rFour;
	protected double velXOne;
	protected double velYOne;
	protected double velROne;
	protected double velXTwo;
	protected double velYTwo;
	protected double velRTwo;
	protected double velXThree;
	protected double velYThree;
	protected double velRThree;
	protected double velXFour;
	protected double velYFour;
	protected double velRFour;
	protected double widthOne;
	protected double heightOne;
	protected double radiusOne;
	protected double widthTwo;
	protected double heightTwo;
	protected double radiusTwo;
	protected double widthThree;
	protected double heightThree;
	protected double radiusThree;
	protected double widthFour;
	protected double heightFour;
	protected double radiusFour;
	protected Image imageOne;
	protected Image imageTwo;
	protected Image imageThree;
	protected Image imageFour;
	protected ImageView viewOne;
	protected ImageView viewTwo;
	protected ImageView viewThree;
	protected ImageView viewFour;
	protected Rectangle rectOne;
	protected Rectangle rectTwo;
	protected Rectangle rectThree;
	protected Rectangle rectFour;

	public void move() {
		xOne = (xOne + (velXOne * GameSettings.FRAME_SCALE)) ;
		yOne = (yOne + (velYOne * GameSettings.FRAME_SCALE)) ;
		rOne = (rOne + (velROne * GameSettings.FRAME_SCALE)) ;
		xTwo = (xTwo + (velXTwo * GameSettings.FRAME_SCALE)) ;
		yTwo = (yTwo + (velYTwo * GameSettings.FRAME_SCALE)) ;
		rTwo = (rTwo + (velRTwo * GameSettings.FRAME_SCALE)) ;
		xThree = (xThree + (velXThree * GameSettings.FRAME_SCALE)) ;
		yThree = (yThree + (velYThree * GameSettings.FRAME_SCALE)) ;
		rThree = (rThree + (velRThree * GameSettings.FRAME_SCALE)) ;
		xFour = (xFour + (velXFour * GameSettings.FRAME_SCALE)) ;
		yFour = (yFour + (velYFour * GameSettings.FRAME_SCALE)) ;
		rFour = (rFour + (velRFour * GameSettings.FRAME_SCALE)) ;
	}

	public void scale() {
		widthOne = (widthOne * GameSettings.SIZE_SCALE) ;
		heightOne = (heightOne * GameSettings.SIZE_SCALE) ;
		radiusOne = (radiusOne * GameSettings.SIZE_SCALE) ;
		widthTwo = (widthTwo * GameSettings.SIZE_SCALE) ;
		heightTwo = (heightTwo * GameSettings.SIZE_SCALE) ;
		radiusTwo = (radiusTwo * GameSettings.SIZE_SCALE) ;
		widthThree = (widthThree * GameSettings.SIZE_SCALE) ;
		heightThree = (heightThree * GameSettings.SIZE_SCALE) ;
		radiusThree = (radiusThree * GameSettings.SIZE_SCALE) ;
		widthFour = (widthFour * GameSettings.SIZE_SCALE) ;
		heightFour = (heightFour * GameSettings.SIZE_SCALE) ;
		radiusFour = (radiusFour * GameSettings.SIZE_SCALE) ;
	}

	public void updateUI() {
		if(viewOne!=null){
			viewOne.setTranslateX(xOne);
			viewOne.setTranslateY(yOne);
			viewOne.setRotate(rOne);
		}
	}

	public double getxOne() {
		return xOne;
	}

	public void setxOne(double xOne) {
		this.xOne = xOne;
	}

	public double getyOne() {
		return yOne;
	}

	public void setyOne(double yOne) {
		this.yOne = yOne;
	}

	public double getrOne() {
		return rOne;
	}

	public void setrOne(double rOne) {
		this.rOne = rOne;
	}

	public double getxTwo() {
		return xTwo;
	}

	public void setxTwo(double xTwo) {
		this.xTwo = xTwo;
	}

	public double getyTwo() {
		return yTwo;
	}

	public void setyTwo(double yTwo) {
		this.yTwo = yTwo;
	}

	public double getrTwo() {
		return rTwo;
	}

	public void setrTwo(double rTwo) {
		this.rTwo = rTwo;
	}

	public double getxThree() {
		return xThree;
	}

	public void setxThree(double xThree) {
		this.xThree = xThree;
	}

	public double getyThree() {
		return yThree;
	}

	public void setyThree(double yThree) {
		this.yThree = yThree;
	}

	public double getrThree() {
		return rThree;
	}

	public void setrThree(double rThree) {
		this.rThree = rThree;
	}

	public double getxFour() {
		return xFour;
	}

	public void setxFour(double xFour) {
		this.xFour = xFour;
	}

	public double getyFour() {
		return yFour;
	}

	public void setyFour(double yFour) {
		this.yFour = yFour;
	}

	public double getrFour() {
		return rFour;
	}

	public void setrFour(double rFour) {
		this.rFour = rFour;
	}

	public double getVelXOne() {
		return velXOne;
	}

	public void setVelXOne(double velXOne) {
		this.velXOne = velXOne;
	}

	public double getVelYOne() {
		return velYOne;
	}

	public void setVelYOne(double velYOne) {
		this.velYOne = velYOne;
	}

	public double getVelROne() {
		return velROne;
	}

	public void setVelROne(double velROne) {
		this.velROne = velROne;
	}

	public double getVelXTwo() {
		return velXTwo;
	}

	public void setVelXTwo(double velXTwo) {
		this.velXTwo = velXTwo;
	}

	public double getVelYTwo() {
		return velYTwo;
	}

	public void setVelYTwo(double velYTwo) {
		this.velYTwo = velYTwo;
	}

	public double getVelRTwo() {
		return velRTwo;
	}

	public void setVelRTwo(double velRTwo) {
		this.velRTwo = velRTwo;
	}

	public double getVelXThree() {
		return velXThree;
	}

	public void setVelXThree(double velXThree) {
		this.velXThree = velXThree;
	}

	public double getVelYThree() {
		return velYThree;
	}

	public void setVelYThree(double velYThree) {
		this.velYThree = velYThree;
	}

	public double getVelRThree() {
		return velRThree;
	}

	public void setVelRThree(double velRThree) {
		this.velRThree = velRThree;
	}

	public double getVelXFour() {
		return velXFour;
	}

	public void setVelXFour(double velXFour) {
		this.velXFour = velXFour;
	}

	public double getVelYFour() {
		return velYFour;
	}

	public void setVelYFour(double velYFour) {
		this.velYFour = velYFour;
	}

	public double getVelRFour() {
		return velRFour;
	}

	public void setVelRFour(double velRFour) {
		this.velRFour = velRFour;
	}

	public double getWidthOne() {
		return widthOne;
	}

	public void setWidthOne(double widthOne) {
		this.widthOne = widthOne;
	}

	public double getHeightOne() {
		return heightOne;
	}

	public void setHeightOne(double heightOne) {
		this.heightOne = heightOne;
	}

	public double getRadiusOne() {
		return radiusOne;
	}

	public void setRadiusOne(double radiusOne) {
		this.radiusOne = radiusOne;
	}

	public double getWidthTwo() {
		return widthTwo;
	}

	public void setWidthTwo(double widthTwo) {
		this.widthTwo = widthTwo;
	}

	public double getHeightTwo() {
		return heightTwo;
	}

	public void setHeightTwo(double heightTwo) {
		this.heightTwo = heightTwo;
	}

	public double getRadiusTwo() {
		return radiusTwo;
	}

	public void setRadiusTwo(double radiusTwo) {
		this.radiusTwo = radiusTwo;
	}

	public double getWidthThree() {
		return widthThree;
	}

	public void setWidthThree(double widthThree) {
		this.widthThree = widthThree;
	}

	public double getHeightThree() {
		return heightThree;
	}

	public void setHeightThree(double heightThree) {
		this.heightThree = heightThree;
	}

	public double getRadiusThree() {
		return radiusThree;
	}

	public void setRadiusThree(double radiusThree) {
		this.radiusThree = radiusThree;
	}

	public double getWidthFour() {
		return widthFour;
	}

	public void setWidthFour(double widthFour) {
		this.widthFour = widthFour;
	}

	public double getHeightFour() {
		return heightFour;
	}

	public void setHeightFour(double heightFour) {
		this.heightFour = heightFour;
	}

	public double getRadiusFour() {
		return radiusFour;
	}

	public void setRadiusFour(double radiusFour) {
		this.radiusFour = radiusFour;
	}

	public Image getImageOne() {
		return imageOne;
	}

	public void setImageOne(Image imageOne) {
		this.imageOne = imageOne;
	}

	public Image getImageTwo() {
		return imageTwo;
	}

	public void setImageTwo(Image imageTwo) {
		this.imageTwo = imageTwo;
	}

	public Image getImageThree() {
		return imageThree;
	}

	public void setImageThree(Image imageThree) {
		this.imageThree = imageThree;
	}

	public Image getImageFour() {
		return imageFour;
	}

	public void setImageFour(Image imageFour) {
		this.imageFour = imageFour;
	}

	public ImageView getViewOne() {
		return viewOne;
	}

	public void setViewOne(ImageView viewOne) {
		this.viewOne = viewOne;
	}

	public ImageView getViewTwo() {
		return viewTwo;
	}

	public void setViewTwo(ImageView viewTwo) {
		this.viewTwo = viewTwo;
	}

	public ImageView getViewThree() {
		return viewThree;
	}

	public void setViewThree(ImageView viewThree) {
		this.viewThree = viewThree;
	}

	public ImageView getViewFour() {
		return viewFour;
	}

	public void setViewFour(ImageView viewFour) {
		this.viewFour = viewFour;
	}

	public Rectangle getRectOne() {
		return rectOne;
	}

	public void setRectOne(Rectangle rectOne) {
		this.rectOne = rectOne;
	}

	public Rectangle getRectTwo() {
		return rectTwo;
	}

	public void setRectTwo(Rectangle rectTwo) {
		this.rectTwo = rectTwo;
	}

	public Rectangle getRectThree() {
		return rectThree;
	}

	public void setRectThree(Rectangle rectThree) {
		this.rectThree = rectThree;
	}

	public Rectangle getRectFour() {
		return rectFour;
	}

	public void setRectFour(Rectangle rectFour) {
		this.rectFour = rectFour;
	}


}
