
package editor;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
public class TileMap extends ImageView {

	private double x;
	private double y;
	private double z;
	private double r;
	private double width;
	private double height;
	private double scale;

	private boolean selected = false;
	private boolean enterDown = false;
	private boolean altDown = false;
	private boolean ctrlDown = false;
	private boolean spaceDown = false;

	private boolean upDown = false;
	private boolean downDown = false;
	private boolean leftDown = false;
	private boolean rightDown = false;

	private int historyMax;
	private int stateIndex;

	private Image image;

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
		this.setImage(image);
		this.setFitWidth(width);
		this.setFitHeight(height);
		this.processMousePress();
		this.processMouseRelease();
		this.processScrolling();
		this.processTouchGestures();

	}

	public TileMap(double x, double y, double width, double height, Image image) {
		this.x = x;
		this.y = y;
		this.image = image;
		this.width = width;
		this.height = height;
		this.setImage(image);
		this.setFitWidth(width);
		this.setFitHeight(height);
		this.processMousePress();
		this.processMouseRelease();
		this.processScrolling();
		this.processTouchGestures();

	}

	public TileMap(double x, double y, double width, double height, Image image, CollisionType collisionType) {
		this.x = x;
		this.y = y;
		this.image = image;
		this.width = width;
		this.height = height;
		this.collisionType = collisionType;
		this.setImage(image);
		this.setFitWidth(width);
		this.setFitHeight(height);
		this.computeBoundBox();
		this.processMousePress();
		this.processMouseRelease();
		this.processScrolling();

	}

	public void processMousePress() {
		this.setOnMouseEntered(e -> selected = true);
	}

	public void processMouseRelease() {
		this.setOnMouseExited(e -> selected = false);
	}

	public void processScrolling() {

		this.setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {

				event.consume();

				if (event.getDeltaY() == 0) {
					return;
				}

				if (event.isControlDown()) {
					double scaleFactor = (event.getDeltaY() > 0) ? Utils.SCALE_DELTA : 1 / Utils.SCALE_DELTA;

					setScale(scaleFactor);
				}

			}
		});
	}

	private void processTouchGestures() {

		this.setOnZoom(e -> {
			e.consume();
			if(!e.isAltDown())
				setScale(e.getZoomFactor());
		});
		this.setOnRotate(e -> {
			e.consume();
			if(e.isAltDown())
				this.rotate(e.getTotalAngle(), Rotation.Z_AXIS);
			else if(e.isShiftDown())
				this.rotate(e.getTotalAngle(), Rotation.X_AXIS);
		});
	}

	public void setSelected(boolean state) {
		if (state) {
			this.setStyle("-fx-effect: dropshadow(one-pass-box, dodgerblue, " + 35 + ", 0.5, 0, 0);");
			this.setOpacity(.75);
			this.selected = true;
		} else {
			this.setStyle("");
			this.setOpacity(1);
			this.selected = false;
		}

	}

	public void relocate(double x, double y) {
		this.setTranslateX(x);
		this.setTranslateY(y);
		this.setRotate(r);
	}

	public void setScale(double scale) {
		this.scale = scale;
		setFitWidth(getFitWidth() * scale);
		setFitHeight(getFitHeight() * scale);
	}

	public void rotate(double angle, Rotation type) {
		switch (type) {
		case Z_AXIS:
			setRotationAxis(Rotate.Z_AXIS);
			setRotate(angle);
			break;
		case X_AXIS:
			setRotationAxis(Rotate.X_AXIS);
			setRotate(angle);
			break;
		case Y_AXIS:
			setRotationAxis(Rotate.Y_AXIS);
			setRotate(angle);
			break;
		default:
			break;
		}
	}

	public void setState(double x, double y, double z, double r, double scale) {
		setTranslateX(x);
		setTranslateY(y);
		setTranslateZ(z);
		setRotate(r);
		setFitWidth(width * scale);
		setFitHeight(height * scale);
	}

	public void addState() {
		if (history.size() <= 10) {
			history.add(new StateHistory(x, y, z, r, scale));
		} else {
			history.remove(0);
			history.add(new StateHistory(x, y, z, r, scale));
		}
	}

	public void setPreviousState() {
		final int index = history.size() - 1;
		stateIndex = index;
		x = history.get(history.size() - 1).getX();
		y = history.get(history.size() - 1).getY();
		z = history.get(history.size() - 1).getZ();
		r = history.get(history.size() - 1).getRotation();
		scale = history.get(history.size() - 1).getScale();

	}

	public void setPrecedentState() {

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
		return this;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setR(double r) {
		this.r = r;
	}

	public double getZ() {
		return z;
	}

	public double getR() {
		return r;
	}

	public double getTileWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getTileHeight() {
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
		return this.getLayoutBounds();
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

		public StateHistory(double x, double y, double z, double rotation, double scale) {
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
