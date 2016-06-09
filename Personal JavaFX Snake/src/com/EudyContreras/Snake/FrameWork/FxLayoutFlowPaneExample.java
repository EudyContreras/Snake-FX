package com.EudyContreras.Snake.FrameWork;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class FxLayoutFlowPaneExample extends Application
{

	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	@Override
	public void start(Stage stage) 
	{	
		// Set the hgap property to 10 pixels
		double hgap = 10;
		// Set the vgap property to 10 pixels
		double vgap = 10;
		
		// Create the horizontal FlowPane with a 10px spacing
		FlowPane root = new FlowPane(Orientation.VERTICAL,hgap, vgap);
		FlowPane root1 = new FlowPane(Orientation.VERTICAL,hgap, vgap);
		
		// Add the children (ten buttons) to the flow pane
		for(int i = 1; i <= 3; i++) 
		{
			root.getChildren().add(new Button("Button " + i));
			root1.getChildren().add(new Button("Button " + i));

		}
		
//		// Set the padding of the FlowPane		
//		root.setStyle("-fx-padding: 10;");
//		// Set the border-style of the FlowPane
//		root.setStyle("-fx-border-style: solid inside;");
//		// Set the border-width of the FlowPane
//		root.setStyle("-fx-border-width: 2;");
//		// Set the border-insets of the FlowPane
//		root.setStyle("-fx-border-insets: 5;");
//		// Set the border-radius of the FlowPane
//		root.setStyle("-fx-border-radius: 5;");
//		// Set the border-color of the FlowPane
//		root.setStyle("-fx-border-color: blue;");
//		// Set the size of the FlowPane
		root.setPrefSize(200, 300);
	//	root1.setPrefSize(200, 300);

		
		TilePane  paneOfPanes = new TilePane (Orientation.HORIZONTAL,hgap, vgap);
	
		paneOfPanes.setPrefColumns(4);

		paneOfPanes.getChildren().add(root);
		paneOfPanes.getChildren().add(root1);
//		paneOfPanes.getChildren().add(root2);
//		paneOfPanes.getChildren().add(root3);
		Pane group = new Pane();
		group.getChildren().add(paneOfPanes);
		// Create the Scene
		Scene scene = new Scene(group);
		stage.setWidth(300);
		// Add the scene to the Stage
		stage.setScene(scene);
		// Set the title of the Stage
		stage.setTitle("A FlowPane Example");
		// Display the Stage
		stage.show();
	}
}
