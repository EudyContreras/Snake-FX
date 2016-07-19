package editor;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;

public class MenuBox extends MenuBar {
	ScaleAndPan tileEditor;

	public MenuBox(ScaleAndPan tileEditor, final Stage stage, final Group group) {
			this.tileEditor = tileEditor;
	      //Edit menu
	      Menu editMenu = new Menu("_Edit");
	      editMenu.getItems().add(new MenuItem("Cut"));
	      editMenu.getItems().add(new MenuItem("Copy"));
	      MenuItem paste = new MenuItem("Paste");
	      paste.setOnAction(e -> System.out.println("Pasting some crap"));
	      paste.setDisable(true);
	      editMenu.getItems().add(paste);

	      //Help menu
	      Menu helpMenu = new Menu("Help");
	      CheckMenuItem showLines = new CheckMenuItem("Show Line Numbers");
	      showLines.setOnAction(e -> {
	          if(showLines.isSelected())
	              System.out.println("Program will now display line numbers");
	          else
	              System.out.println("Hiding line number");
	      });
	      CheckMenuItem autoSave = new CheckMenuItem("Enable Autosave");
	      autoSave.setSelected(true);
	      helpMenu.getItems().addAll(showLines, autoSave);

	      //Difficulty RadioMenuItems
	      Menu difficultyMenu = new Menu("Difficulty");
	      ToggleGroup difficultyToggle = new ToggleGroup();

	      RadioMenuItem easy = new RadioMenuItem("Easy");
	      RadioMenuItem medium = new RadioMenuItem("Medium");
	      RadioMenuItem hard = new RadioMenuItem("Hard");

	      easy.setToggleGroup(difficultyToggle);
	      medium.setToggleGroup(difficultyToggle);
	      hard.setToggleGroup(difficultyToggle);

	      difficultyMenu.getItems().addAll(easy, medium, hard);


	      //////////////////////////
	    Menu fileMenu = new Menu("_File");
	    MenuItem exitMenuItem = new MenuItem("E_xit");
	    MenuItem newFile = new MenuItem("New...");
	    newFile.setOnAction(e -> System.out.println("Create a new file..."));
	    exitMenuItem.setGraphic(new ImageView(new Image(CLOSE_ICON)));
	    exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
	      @Override
	      public void handle(ActionEvent event) {
	        stage.close();
	      }
	    });
	    fileMenu.getItems().add(newFile);
	    fileMenu.getItems().add(new MenuItem("Open..."));
	    fileMenu.getItems().add(new MenuItem("Save..."));
	    fileMenu.getItems().add(new SeparatorMenuItem());
	    fileMenu.getItems().add(new MenuItem("Settings..."));
	    fileMenu.getItems().add(new SeparatorMenuItem());
	    fileMenu.getItems().add(exitMenuItem);

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
	    zoomMenu.getItems().setAll(zoomResetMenuItem, zoomInMenuItem,zoomOutMenuItem);

	    this.getMenus().setAll(fileMenu, zoomMenu,editMenu, helpMenu, difficultyMenu);



	  }
	  public static final String APP_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/128/Zoom-icon.png";
	  public static final String ZOOM_RESET_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-icon.png";
	  public static final String ZOOM_OUT_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-Out-icon.png";
	  public static final String ZOOM_IN_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Zoom-In-icon.png";
	  public static final String CLOSE_ICON = "http://icons.iconarchive.com/icons/deleket/soft-scraps/24/Button-Close-icon.png";
}
