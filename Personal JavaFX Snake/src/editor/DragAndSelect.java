package editor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class DragAndSelect{

  public Image image = new Image( getClass().getResource( "tiger.jpg").toExternalForm());
  private Pane scrollContent;



    SelectionModel selectionModel = new SelectionModel();
    ScaleAndPan editor;
    DragMouseGestures dragMouseGestures = new DragMouseGestures();

    static Random rnd = new Random();

    public DragAndSelect(ScaleAndPan editor, Scene scene, Pane editingWindow) {
    	this.editor = editor;
    	this.scrollContent = editingWindow;
        new RubberBandSelection( editingWindow);
        new DragAndDrop(this, scene,scrollContent);
        double width = image.getWidth();
        double height = image.getHeight();

        double padding = 20;
        for( int row=0; row < 4; row++) {
            for( int col=0; col < 4; col++) {

                Selectable selectable = new Selectable(image, width, height);
                selectable.relocate( padding * (col+1) + width * col, padding * (row + 1) + height * row);

                scrollContent.getChildren().add(selectable);

                dragMouseGestures.makeDraggable(selectable);

            }
        }
    }
    public DragMouseGestures getDragGestures(){
    	return dragMouseGestures;
    }

    public void AddSelectable(Image image, double width,double height, double x, double y){
    	Selectable selectable = new Selectable(image, width, height);
        selectable.relocate( x, y);

        scrollContent.getChildren().add(selectable);

        dragMouseGestures.makeDraggable(selectable);
    }

    private class SelectionModel {

        Set<Selectable> selection = new HashSet<>();

        public void add( Selectable tile) {

            if( !tile.getStyleClass().contains("highlight")) {
                tile.getStyleClass().add( "highlight");
            }

            selection.add( tile);
            tile.getTile().setSelected(true);
        }

        public void remove( Selectable tile) {
            tile.getStyleClass().remove( "highlight");
            selection.remove( tile);
            tile.getTile().setSelected(false);
        }

        public void clear() {

            while( !selection.isEmpty()) {
                remove( selection.iterator().next());
            }

        }

        public boolean contains( Selectable tile) {
            return selection.contains(tile);
        }

        @SuppressWarnings("unused")
		public int size() {
            return selection.size();
        }

        public void log() {
            System.out.println( "Items in model: " + Arrays.asList( selection.toArray()));
        }

    }

    private class DragMouseGestures {

        final DragContext dragContext = new DragContext();

        private boolean enabled = false;

        public void makeDraggable(final Selectable tile) {

            tile.setOnMousePressed(onMousePressedEventHandler);
            tile.setOnMouseDragged(onMouseDraggedEventHandler);
            tile.setOnMouseReleased(onMouseReleasedEventHandler);

        }
        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                // don't do anything if the user is in the process of adding to the selection model
                if( event.isControlDown() || event.isShiftDown())
                    return;

                Selectable tile = (Selectable) event.getSource();
                dragContext.x = tile.getTranslateX() - event.getSceneX();
                dragContext.y = tile.getTranslateY() - event.getSceneY();

                // clear the model if the current tile isn't in the selection => new selection
                if( !selectionModel.contains(tile)) {
                    selectionModel.clear();
                    selectionModel.add( tile);
                }

                // flag that the mouse released handler should consume the event, so it won't bubble up to the pane which has a rubberband selection mouse released handler
                enabled = true;

                // prevent rubberband selection handler
                event.consume();
            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( !enabled)
                    return;

                // all in selection
                for( Node tile: selectionModel.selection) {
                    tile.setTranslateX( dragContext.x + event.getSceneX());
                    tile.setTranslateY( dragContext.y + event.getSceneY());
                }

            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                // prevent rubberband selection handler
                if( enabled) {

                    // set tile's layout position to current position,remove translate coordinates
                    for( Selectable tile: selectionModel.selection) {
                    	tile.setOpacity(1);
                        fixPosition(tile);

                    }

                    enabled = false;

                    event.consume();
                }
            }
        };

        /**
         * Set tile's layout position to current position, remove translate coordinates.
         * @param tile
         */
        private void fixPosition( Node tile) {

            double x = tile.getTranslateX();
            double y = tile.getTranslateY();

            tile.relocate(tile.getLayoutX() + x, tile.getLayoutY() + y);

            tile.setTranslateX(0);
            tile.setTranslateY(0);

        }

        class DragContext {

            double x;
            double y;

        }

    }

    private class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect;

        Pane group;
        boolean enabled = false;

        public RubberBandSelection( Pane group) {

            this.group = group;

            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                // simple flag to prevent multiple handling of this event or we'd get an exception because rect is already on the scene
                // eg if you drag with left mouse button and while doing that click the right mouse button
                if( enabled)
                    return;

                dragContext.mouseAnchorX = event.getSceneX();
                dragContext.mouseAnchorY = event.getSceneY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add( rect);

                event.consume();

                enabled = true;
            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( !event.isShiftDown() && !event.isControlDown()) {
                    selectionModel.clear();
                }

                for( Node node: group.getChildren()) {

                    if( node instanceof Selectable) {
                    	Selectable tile = (Selectable)node;
                        if( tile.getBoundsInParent().intersects( rect.getBoundsInParent())) {

                            if( event.isShiftDown()) {

                                selectionModel.add( tile);

                            } else if( event.isControlDown()) {

                                if( selectionModel.contains( tile)) {
                                    selectionModel.remove( tile);
                                } else {
                                    selectionModel.add( tile);
                                }
                            } else {
                                selectionModel.add( tile);
                            }

                        }
                    }

                }

                selectionModel.log();

                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);

                event.consume();

                enabled = false;
            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                double offsetX = event.getSceneX() - dragContext.mouseAnchorX;
                double offsetY = event.getSceneY() - dragContext.mouseAnchorY;

                if( offsetX > 0)
                    rect.setWidth( offsetX);
                else {
                    rect.setX(event.getSceneX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if( offsetY > 0) {
                    rect.setHeight( offsetY);
                } else {
                    rect.setY(event.getSceneY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }

                event.consume();

            }
        };

        private final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;

        }
    }

}