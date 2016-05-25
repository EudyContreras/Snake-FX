package com.SnakeGame.PlayerRecords;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public enum Scoreboard {

	INSTANCE;

	private final String PATH_ROOT = "." + File.separator + "Snake" + File.separator;

	private final String PATH_USER_PICS = PATH_ROOT  + File.separator + "Pictures" + File.separator;

	private final String PATH_PICS = PATH_ROOT  + "pics.ser";
	private final String PATH_RANK = PATH_ROOT  + "rank.ser";
	private final String PATH_SCORE = PATH_ROOT + "score.ser";
	private final String PATH_NAME = PATH_ROOT  + "name.ser";
	private final String PATH_DEFAULT_PIC = "/se/par/amsen/snake/rec/img/playah.png";

	private final String[] standardNames = { "Pär", "Udo", "Lena", "Linnea", "Fool Jr." };

	private ObservableList<FlowPane> listPic = FXCollections.observableArrayList();
	private ObservableList<FlowPane> listRank = FXCollections.observableArrayList();
	private ObservableList<FlowPane> listScore = FXCollections.observableArrayList();
	private ObservableList<FlowPane> listName = FXCollections.observableArrayList();

	private ArrayList<String> picsArray = new ArrayList<String>();
	private ArrayList<Integer> rankArray = new ArrayList<Integer>();
	private ArrayList<Integer> scoreArray = new ArrayList<Integer>();
	private ArrayList<String> nameArray = new ArrayList<String>();

	private IntegerProperty latestScore = new SimpleIntegerProperty(0);
	private IntegerProperty latestRank = new SimpleIntegerProperty(0);

	/**
	 * Load scoreboard from file.
	 */
	@SuppressWarnings("unchecked")
	public void load() {
		File root = new File(PATH_ROOT);
		File picsFile = new File(PATH_PICS);
		File rankFile = new File(PATH_RANK);
		File scoreFile = new File(PATH_SCORE);
		File nameFile = new File(PATH_NAME);

		if(root.exists() && picsFile.exists() && rankFile.exists() && scoreFile.exists() && nameFile.exists() ) {
			try {
				//read pics
				FileInputStream fileIn = new FileInputStream(PATH_PICS);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				picsArray = (ArrayList<String>) in.readObject();
				in.close();
				fileIn.close();

				//read rank
				fileIn = new FileInputStream(PATH_RANK);
				in = new ObjectInputStream(fileIn);
				rankArray = (ArrayList<Integer>) in.readObject();
				in.close();
				fileIn.close();

				//read score
				fileIn = new FileInputStream(PATH_SCORE);
				in = new ObjectInputStream(fileIn);
				scoreArray = (ArrayList<Integer>) in.readObject();
				in.close();
				fileIn.close();

				//read name
				fileIn = new FileInputStream(PATH_NAME);
				in = new ObjectInputStream(fileIn);
				nameArray = (ArrayList<String>) in.readObject();
				in.close();
				fileIn.close();
				System.out.println("Loaded.");
			} catch( IOException | ClassNotFoundException e) {
				System.out.println("Failed to load");
			}
		} else {
			System.out.println("Nothing to load.");
		}

		initialize();
	}

	/**
	 * Save scoreboard to file.
	 */
	public void save() {
		Runnable saveRunnable = new Runnable() {
			@Override
			public void run() {
				File root = new File(PATH_ROOT);
				File picsFile = new File(PATH_PICS);
				File rankFile = new File(PATH_RANK);
				File scoreFile = new File(PATH_SCORE);
				File nameFile = new File(PATH_NAME);

				picsFile.delete();
				rankFile.delete();
				scoreFile.delete();
				nameFile.delete();
				try {
					root.mkdirs();
					if(picsFile.createNewFile() && rankFile.createNewFile() && scoreFile.createNewFile() && nameFile.createNewFile() ) {
						if(picsArray == null || rankArray == null || scoreArray == null || nameArray == null) {
							initialize();
						}
						//Save pics
						FileOutputStream fileOut = new FileOutputStream(PATH_PICS);
						BufferedOutputStream bufferedStream = new BufferedOutputStream(fileOut);
						ObjectOutputStream out = new ObjectOutputStream(bufferedStream);
						out.writeObject(picsArray);
						out.close();
						fileOut.close();
						//Save rank
						fileOut = new FileOutputStream(PATH_RANK);
						bufferedStream = new BufferedOutputStream(fileOut);
						out = new ObjectOutputStream(bufferedStream);
						out.writeObject(rankArray);
						out.close();
						fileOut.close();
						//Save score
						fileOut = new FileOutputStream(PATH_SCORE);
						bufferedStream = new BufferedOutputStream(fileOut);
						out = new ObjectOutputStream(bufferedStream);
						out.writeObject(scoreArray);
						out.close();
						fileOut.close();
						//Save name
						fileOut = new FileOutputStream(PATH_NAME);
						bufferedStream = new BufferedOutputStream(fileOut);
						out = new ObjectOutputStream(bufferedStream);
						out.writeObject(nameArray);
						out.close();
						fileOut.close();
						System.out.println("Saved.");
					} else {
						System.out.println("Failed to save");
					}
				} catch(IOException e) {
					System.out.println("Failed to save, I/O exception");
				}
			}
		};

		Thread saveThread = new Thread(saveRunnable);

		saveThread.start();
	}

	/**
	 * Initialize scoreboard, will load if possible, else
	 * initialize scoreboard with default values
	 */
	private void initialize() {
		FlowPane flowPic;
		FlowPane flowRank;
		FlowPane flowScore;
		FlowPane flowName;

		listPic.clear();
		listRank.clear();
		listScore.clear();
		listName.clear();

		if( picsArray.size() != 5 || rankArray.size() != 5 || scoreArray.size() != 5 || nameArray.size() != 5 ) {
			initializeDefault();
			return;
		}

		for(int counter = 0 ; counter < 5 ; counter++) {
			URL imgURL = null;
			Image image = null;

			imgURL = getClass().getResource(picsArray.get(counter));

			if( imgURL == null ) {
				try {
					imgURL = new URL(picsArray.get(counter));
				} catch (MalformedURLException e) {
					System.out.println("Invalid image path!");
				}
			}

			if (imgURL != null){
				image = new Image(picsArray.get(counter));
			}

			//Add pic to scoreboard
			//Special case if the img URL is invalid(=null)
			if(imgURL != null ){
				flowPic = new FlowPane();
				flowPic.setPrefSize(80, 30);
				flowPic.setAlignment(Pos.CENTER);
				flowPic.getChildren().add(new ImageView(image));
				listPic.add(flowPic);
			}

			//Add rank to scoreboard
			flowRank = new FlowPane();
			flowRank.setPrefSize(80, 30);
			flowRank.setAlignment(Pos.CENTER);
			flowRank.getChildren().add(new Label(String.valueOf(rankArray.get(counter))));
			listRank.add(flowRank);

			//Add score to scoreboard
			flowScore = new FlowPane();
			flowScore.setPrefSize(80, 30);
			flowScore.setAlignment(Pos.CENTER);
			flowScore.getChildren().add(new Label(String.valueOf(scoreArray.get(counter))));
			listScore.add(flowScore);

			//Add name to scoreboard
			flowName = new FlowPane();
			flowName.setPrefSize(80, 30);
			flowName.setAlignment(Pos.CENTER);
			flowName.getChildren().add(new Label(nameArray.get(counter)));
			listName.add(flowName);
		}
	}

	/**
	 * Reset scoreboard to default values, saves to file.
	 */
	public void reset() {
		initializeDefault();
		save();
	}

	/**
	 * Generates the default values into the scoreboard
	 */
	private void initializeDefault() {
		FlowPane flowPic;
		FlowPane flowRank;
		FlowPane flowScore;
		FlowPane flowName;

		picsArray.clear();
		rankArray.clear();
		scoreArray.clear();
		nameArray.clear();

		listPic.clear();
		listRank.clear();
		listScore.clear();
		listName.clear();

		int score = 128;

		for(int counter = 0 ; counter < 5 ; counter++) {
			Image image = null;
			URL imgURL = null;

			imgURL = getClass().getResource(PATH_DEFAULT_PIC);

			if( imgURL == null ) {
				System.out.println("Invalid default image path!");
			} else {
				picsArray.add(PATH_DEFAULT_PIC);
				image = new Image(picsArray.get(counter));
			}

			//Add pic to scoreboard
			//Special case if the img URL is invalid(=null)
			if(imgURL != null ){
				flowPic = new FlowPane();
				flowPic.setPrefSize(80, 30);
				flowPic.setAlignment(Pos.CENTER);
				flowPic.getChildren().add(new ImageView(image));
				listPic.add(flowPic);
			}

			//Add rank to scoreboard
			flowRank = new FlowPane();
			flowRank.setPrefSize(80, 30);
			flowRank.setAlignment(Pos.CENTER);
			rankArray.add(counter+1);
			flowRank.getChildren().add(new Label(String.valueOf(rankArray.get(counter))));
			listRank.add(flowRank);

			//Add score to scoreboard
			flowScore = new FlowPane();
			flowScore.setPrefSize(80, 30);
			flowScore.setAlignment(Pos.CENTER);
			scoreArray.add((score));
			flowScore.getChildren().add(new Label(String.valueOf(scoreArray.get(counter))));
			listScore.add(flowScore);

			//Add name to scoreboard
			flowName = new FlowPane();
			flowName.setPrefSize(80, 30);
			flowName.setAlignment(Pos.CENTER);
			nameArray.add(standardNames[counter]);
			flowName.getChildren().add(new Label(nameArray.get(counter)));
			listName.add(flowName);

			score /= 2;
		}
	}

	/**
	 * Adds a new score
	 * @param pic String path to a pic or null for default picture
	 * @param name String player name or null for default value
	 * @param score int score value
	 */
	public void update(String pic, String name, int score) {

		int rank = calculateRank(score);

		if( rank != -1 ) {
			if( pic == null ) {
				pic = PATH_DEFAULT_PIC;
			}
			if( name == null ) {
				name = "---";
			}
			picsArray.add(rank, pic);
			rankArray.add(rank, rank);
			scoreArray.add(rank, score);
			nameArray.add(rank, name);

			//remove beaten highscore
			picsArray.remove(5);
			rankArray.remove(5);
			scoreArray.remove(5);
			nameArray.remove(5);

			for( int counter = 0 ; counter < rankArray.size() ; counter++) {
				rankArray.set(counter, counter + 1);
			}
		}
		initialize();

		save();

		latestRank.set(rank+1);
		latestScore.set(score);

	}

	/**
	 * Calculates the rank of the score in comparison to the other scores
	 * @param score int score value
	 * @return the rank (0-4)
	 */
	public int calculateRank(int score) {
		int rank = -1;
		for(int counter = rankArray.size() - 1 ; counter >= 0 ; counter--){
			if( score > scoreArray.get(counter)){
				rank = counter;
			}
		}
		return rank;
	}

	public ObservableList<FlowPane> getListPic() {
		return listPic;
	}

	public ObservableList<FlowPane> getListRank() {
		return listRank;
	}

	public ObservableList<FlowPane> getListScore() {
		return listScore;
	}

	public ObservableList<FlowPane> getListName() {
		return listName;
	}

	/**
	 * Update the latestScore property used by Screens to track
	 * the current/latest scored score
	 * @param score int the score
	 */
	public void updateLatestScore(int score) {
		latestScore.set(score);
	}

	/**
	 * Get the latestScore property used by Screens to track
	 * the current/latest scored score
	 * @return IntegerProperty the score as property
	 */
	public IntegerProperty getLatestScore() {
		return latestScore;
	}

	/**
	 * Update the latestRank property used by Screens to track
	 * the current/latest rank
	 * @param score int the rank
	 */
	public void updateLatestRank(int rank) {
		latestRank.set(rank+1);
	}

	/**
	 * Get the latestRankproperty used by Screens to track
	 * the current/latest rank
	 * @return IntegerProperty the rank as property
	 */
	public IntegerProperty getLatestRank() {
		return latestRank;
	}

	/**
	 * Lets the user browse to a picture,
	 * resizes the pic and returns a URL
	 * to the resized pic.
	 * @param e ActionEvent from caller
	 * @param stage Stage current stage
	 * @return URL path to the picture
	 */
	public URL browsePicture(ActionEvent e, Stage stage) {
		FileChooser fileChooser = new FileChooser();

		configureFileChooser(fileChooser);
		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			URL pic = saveAndResizeProfileImage(file);
			System.out.println(pic.toString());
			return pic;
		} else {
			return null;
		}
	}

	/**
	 * Resizes the received picture and saves it at a dir relative to the
	 * program.
	 * @param file File to be resized/saved at relative dir
	 * @return URL path to the picture or null if pic could not be saved/resized
	 */
	private URL saveAndResizeProfileImage(File file) {
		File newFile = new File(PATH_USER_PICS + file.getName());
		try {
			if( !newFile.mkdirs() && !new File(PATH_USER_PICS).isDirectory() ) {
				System.out.println("could not create dirs for profile pic");
				return null;
			}
			BufferedImage img = ImageIO.read(file);
			BufferedImage scaledImg = Scalr.resize(img, Mode.FIT_EXACT, 20, 20);
			ImageIO.write(scaledImg, "png", newFile);
			return new URL("file:" + File.separator + newFile.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Could not load image!");
		}
		return null;
	}

	private static void configureFileChooser(FileChooser fileChooser){
		fileChooser.setTitle("Choose a profile picture");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("JPG", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG", "*.png")
				);
	}

}
