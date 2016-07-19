package terd;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class GraphicsScalingApp extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(final Stage stage) {
    final Group group = new Group(createStar());

    Parent zoomPane = createZoomPane(group);

    VBox layout = new VBox();
    layout.getChildren().setAll(createMenuBar(stage, group), zoomPane);

    VBox.setVgrow(zoomPane, Priority.ALWAYS);

    Scene scene = new Scene(layout);

    stage.setTitle("Zoomy");
    stage.getIcons().setAll(new Image(APP_ICON));
    stage.setScene(scene);
    stage.show();
  }

  private Parent createZoomPane(final Group group) {
    final double SCALE_DELTA = 1.1;
    final StackPane zoomPane = new StackPane();

    zoomPane.getChildren().add(group);

    final ScrollPane scroller = new ScrollPane();
    final Group scrollContent = new Group(zoomPane);
    scroller.setContent(scrollContent);

    scroller.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
      @Override
      public void changed(ObservableValue<? extends Bounds> observable,
          Bounds oldValue, Bounds newValue) {
        zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
      }
    });

    scroller.setPrefViewportWidth(256);
    scroller.setPrefViewportHeight(256);

    zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
      @Override
      public void handle(ScrollEvent event) {
        event.consume();

        if (event.getDeltaY() == 0) {
          return;
        }

        double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
            : 1 / SCALE_DELTA;

        // amount of scrolling in each direction in scrollContent coordinate
        // units
        Point2D scrollOffset = figureScrollOffset(scrollContent, scroller);

        group.setScaleX(group.getScaleX() * scaleFactor);
        group.setScaleY(group.getScaleY() * scaleFactor);

        // move viewport so that old center remains in the center after the
        // scaling
        repositionScroller(scrollContent, scroller, scaleFactor, scrollOffset);

      }
    });

    return scroller;
  }

  private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {

    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
    double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
    double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
    return new Point2D(scrollXOffset, scrollYOffset);
  }

  private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {

    double scrollXOffset = scrollOffset.getX();
    double scrollYOffset = scrollOffset.getY();
    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    if (extraWidth > 0) {
      double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
      double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
      scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    if (extraHeight > 0) {
      double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
      double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
      scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }

  }

  private SVGPath createCurve() {
    SVGPath ellipticalArc = new SVGPath();
    ellipticalArc.setContent("M10,150 A15 15 180 0 1 70 140 A15 25 180 0 0 130 130 A15 55 180 0 1 190 120");
    ellipticalArc.setStroke(Color.LIGHTGREEN);
    ellipticalArc.setStrokeWidth(4);
    ellipticalArc.setFill(null);
    return ellipticalArc;
  }

  private SVGPath createStar() {
    SVGPath star = new SVGPath();
    star.setContent("M100,10 L100,10 40,180 190,60 10,60 160,180 z");
    star.setStrokeLineJoin(StrokeLineJoin.ROUND);
    star.setStroke(Color.BLUE);
    star.setFill(Color.DARKBLUE);
    star.setStrokeWidth(4);
    return star;
  }

  private MenuBar createMenuBar(final Stage stage, final Group group) {
    Menu fileMenu = new Menu("_File");
    MenuItem exitMenuItem = new MenuItem("E_xit");
    exitMenuItem.setGraphic(new ImageView(new Image(CLOSE_ICON)));
    exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        stage.close();
      }
    });
    fileMenu.getItems().setAll(exitMenuItem);
    Menu zoomMenu = new Menu("_Zoom");
    MenuItem zoomResetMenuItem = new MenuItem("Zoom _Reset");
    zoomResetMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE));
    zoomResetMenuItem.setGraphic(new ImageView(new Image(ZOOM_RESET_ICON)));
    zoomResetMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        group.setScaleX(1);
        group.setScaleY(1);
      }
    });
    MenuItem zoomInMenuItem = new MenuItem("Zoom _In");
    zoomInMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.I));
    zoomInMenuItem.setGraphic(new ImageView(new Image(ZOOM_IN_ICON)));
    zoomInMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        group.setScaleX(group.getScaleX() * 1.5);
        group.setScaleY(group.getScaleY() * 1.5);
      }
    });
    MenuItem zoomOutMenuItem = new MenuItem("Zoom _Out");
    zoomOutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O));
    zoomOutMenuItem.setGraphic(new ImageView(new Image(ZOOM_OUT_ICON)));
    zoomOutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        group.setScaleX(group.getScaleX() * 1 / 1.5);
        group.setScaleY(group.getScaleY() * 1 / 1.5);
      }
    });
    zoomMenu.getItems().setAll(zoomResetMenuItem, zoomInMenuItem,
        zoomOutMenuItem);
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().setAll(fileMenu, zoomMenu);
    return menuBar;
  }


  public static final String APP_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/128/Zoom-icon.png";
  public static final String ZOOM_RESET_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-icon.png";
  public static final String ZOOM_OUT_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-Out-icon.png";
  public static final String ZOOM_IN_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-In-icon.png";
  public static final String CLOSE_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Button-Close-icon.png";
}