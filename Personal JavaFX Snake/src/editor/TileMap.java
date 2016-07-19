
package editor;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;

/**
 * This class is the game tile super class and is the class that every tile
 * object or level object extends. Events within this class will reflect on the
 * children of this class.
 *
 * @author Eudy Contreras
 *
 */
public class TileMap {

    private double x;
    private double y;
    private double z;
    private double r;
    private double width;
    private double height;
    private double scale;

    private boolean selected;

    private int historyMax;
    private int stateIndex;

    private Image image;

    private ImageView view = new ImageView();

    ArrayList<StateHistory> history = new ArrayList<>();

    private Rectangle2D bounds;
    private Rectangle2D topBounds;
    private Rectangle2D leftBounds;
    private Rectangle2D rightBounds;
    private Rectangle2D bottomBounds;

    private CollisionType collisionType;


    public TileMap(double x, double y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.view.setImage(image);
        this.view.setFitWidth(width);
        this.view.setFitHeight(height);
        this.setMouseEntered();
        this.setMouseExited();
        this.setOnScroll();
    }

    public TileMap(double x, double y, double width, double height, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.width = width;
        this.height = height;
        this.view.setImage(image);
        this.view.setFitWidth(width);
        this.view.setFitHeight(height);
        this.setMouseEntered();
        this.setMouseExited();
        this.setOnScroll();
    }

    public TileMap(double x, double y, double width, double height, Image image, CollisionType collisionType) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.width = width;
        this.height = height;
        this.collisionType = collisionType;
        this.view.setImage(image);
        this.view.setFitWidth(width);
        this.view.setFitHeight(height);
        this.computeBoundBox();
        this.setMouseEntered();
        this.setMouseExited();
        this.setOnScroll();
    }
    public void setMouseEntered(){
    	this.view.setOnMouseEntered(e -> selected = true);
    }
    public void setMouseExited(){
    	this.view.setOnMouseExited(e -> selected = false);
    }
    public void setOnScroll(){
    	
    	this.view.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				event.consume();

				if (event.getDeltaY() == 0) {
					return;
				}

				double scaleFactor = (event.getDeltaY() > 0) ? Utils.SCALE_DELTA : 1 /  Utils.SCALE_DELTA;

				setScale(scaleFactor);

			}
    	});
    }
    public void relocate(double x, double y) {
        view.setTranslateX(x);
        view.setTranslateY(y);
        view.setRotate(r);
    }

    public void setScale(double scale) {
    	this.scale = scale;
        view.setFitWidth(view.getFitWidth() * scale);
        view.setFitHeight(view.getFitHeight() * scale);
    }

    public void rotate(double angle, Rotation type) {
        switch (type) {
        case Z_AXIS:
            view.setRotationAxis(Rotate.Z_AXIS);
            view.setRotate(angle);
            break;
        case X_AXIS:
            view.setRotationAxis(Rotate.X_AXIS);
            view.setRotate(angle);
            break;
        case Y_AXIS:
            view.setRotationAxis(Rotate.Y_AXIS);
            view.setRotate(angle);
            break;
        default:
            break;
        }
    }
    public void setState(double x, double y, double z, double r, double scale){
    	view.setTranslateX(x);
    	view.setTranslateY(y);
    	view.setTranslateZ(z);
    	view.setRotate(r);
    	view.setFitWidth(width*scale);
    	view.setFitHeight(height*scale);
    }
    public void addState(){
    	if(history.size()<=10){
    		history.add(new StateHistory(x,y,z,r,scale));
    	}
    	else{
    		history.remove(0);
    		history.add(new StateHistory(x,y,z,r,scale));
    	}
    }
    public void setPreviousState(){
    	final int index = history.size()-1;
    	stateIndex = index;
    	x = history.get(history.size()-1).getX();
    	y = history.get(history.size()-1).getY();
    	z = history.get(history.size()-1).getZ();
    	r = history.get(history.size()-1).getRotation();
    	scale = history.get(history.size()-1).getScale();

    }
    public void setPrecedentState(){

    }
    public void computeBoundBox() {
        switch (collisionType) {
        case COMPLEX:

            break;
        case SIMPLE:

            break;
        default:
            break;

        }
    }

    public ImageView getView() {
        return view;
    }

    public void setView(ImageView view) {
        this.view = view;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getR() {
        return r;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void createBounds(double offsetX, double offsetY, double offsetWidth, double offsetHeight) {
        bounds = new Rectangle2D(x + offsetX, y + offsetY, width + offsetWidth, height + offsetHeight);
    }

    public void createTopBounds(double offsetX, double offsetY, double offsetWidth, double offsetHeight) {
        topBounds = new Rectangle2D(x + offsetX, y + offsetY, width + offsetWidth, height + offsetHeight);
    }

    public void createLeftBounds(double offsetX, double offsetY, double offsetWidth, double offsetHeight) {
        leftBounds = new Rectangle2D(x + offsetX, y + offsetY, width + offsetWidth, height + offsetHeight);
    }

    public void createRightBounds(double offsetX, double offsetY, double offsetWidth, double offsetHeight) {
        rightBounds = new Rectangle2D(x + offsetX, y + offsetY, width + offsetWidth, height + offsetHeight);
    }

    public void createBottomBounds(double offsetX, double offsetY, double offsetWidth, double offsetHeight) {
        bottomBounds = new Rectangle2D(x + offsetX, y + offsetY, width + offsetWidth, height + offsetHeight);
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public Rectangle2D getBoundsTop() {
        return topBounds;
    }

    public Rectangle2D getBoundsRight() {
        return rightBounds;
    }

    public Rectangle2D getBoundsLeft() {
        return leftBounds;
    }

    public Rectangle2D getBoundsBottom() {
        return bottomBounds;
    }

    public Bounds getCollisionBounds() {
        return this.view.getLayoutBounds();
    }

    public Rectangle2D getComplexBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    public enum Rotation {
        Y_AXIS, X_AXIS, Z_AXIS
    }

    public enum CollisionType {
        SIMPLE, COMPLEX
    }
    private class StateHistory {
        private double x;
        private double y;
        private double z;
        private double rotation;
        private double scale;

        public StateHistory(double x, double y, double z, double rotation,  double scale){
            this.x = x;
            this.y = y;
            this.z = z;
            this.rotation = rotation;
            this.scale = scale;
        }
        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }
        public double getZ() {
            return z;
        }
        public double getRotation() {
            return rotation;
        }
        public double getScale() {
            return scale;
        }
    }
}
