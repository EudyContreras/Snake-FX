package com.EudyContreras.Snake.Tutorial;
/*
 * JavaFX Game Save and Load utility
 *
 * The author of this software "Eudy Contreras" grants you ("Licensee")
 * a non-exclusive, royalty free, license to use,modify and redistribute this
 * software in source and binary code form.
 *
 * Please be aware that this software is simply part of a personal test
 * and may in fact be unstable. The software in its current state is not
 * considered a finished product and has plenty of room for improvement and
 * changes due to the range of different approaches which can be used to
 * achieved the desired result of the software.
 *
 */

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
/**
 *
 * @author Eudy Contreras
 *
 * This program saves a game state or settings of a game. The save and stores the data to a predefined destination.
 * The program allows the user to save and retrieve the saved data which can than be processed and implemented to the
 * current game!
 *
 */
public enum SaveAndLoadUtility {

	INSTANCE;


	private final String DIRECTORY_NAME = "Game Save Data"+ File.separator;

	/**
	 * Directory where the game files are located!
	 */
	private final String PATH_ROOT = "." + File.separator + DIRECTORY_NAME ;

	/**
	 * Desktop !
	 */
	private final String PATH_ROOT_ALT = System.getProperty("user.home") + "/Desktop" + File.separator + DIRECTORY_NAME;

	private final String SAVE_FILE = "gameData.saveSlot";
	private final String IMAGE_TYPE = "png";

	private final String SAVE_FILE_PATH = PATH_ROOT + SAVE_FILE;

	private SaveSlot savedData;



	public void saveToFile() {
		BufferedImage image = SwingFXUtils.fromFXImage(GameSettings.profilePic, null);
		SaveSlot saveData = new SaveSlot(GameSettings.profileName,ImageToByte(image),GameSettings.score,GameSettings.level,GameSettings.gameVolume,GameSettings.resolutionX,GameSettings.resolutionY);
		saveGame(saveData);

	}

	/**
	 * Method which when called will attempt to save a SaveSlot
	 * to a specified directory.
	 * @param saveData
	 */
	public void saveGame(SaveSlot saveData) {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {

				File root = new File(PATH_ROOT);
				File file = new File(SAVE_FILE_PATH);

				file.delete();
				logState("Saving file...");
				try {
					root.mkdirs();

					FileOutputStream fileOut = new FileOutputStream(SAVE_FILE_PATH);
					BufferedOutputStream bufferedStream = new BufferedOutputStream(fileOut);
					ObjectOutputStream outputStream = new ObjectOutputStream(bufferedStream);

					outputStream.writeObject(saveData);
					outputStream.close();
					fileOut.close();

					logState("File saved.");

				} catch (IOException e) {
					logState("Failed to save, I/O exception");
					e.printStackTrace();
				}
				return null;
			}
		};

		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}
	/**
	 * Method which when called attempts to retrieve the saved data
	 * from a specified directory
	 */

	public void loadGame() {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {

				File root = new File(PATH_ROOT);
				File file = new File(SAVE_FILE_PATH);

				if (root.exists() && file.exists()) {
					try {
						logState("Loading file");

						FileInputStream fileIn = new FileInputStream(SAVE_FILE_PATH);
						ObjectInputStream inputStream = new ObjectInputStream(fileIn);

						savedData = (SaveSlot) inputStream.readObject();
						processSavedData(savedData);
						inputStream.close();
						fileIn.close();

						logState("File loaded");

					} catch (IOException | ClassNotFoundException e) {
						logState("Failed to load! " + e.getLocalizedMessage());
					}
				} else {
					logState("Nothing to load.");
				}
				return null;
			}
		};

		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();

	}
	private void processSavedData(SaveSlot savedData){
		GameSettings.profilePic = byteToImage(savedData.getProfilePic());
		GameSettings.profileName = savedData.getProfileName();
		GameSettings.score = savedData.getScore();
		GameSettings.level = savedData.getLevel();
		GameSettings.gameVolume = savedData.getGameVolume();
		GameSettings.resolutionX = savedData.getResolutionX();
		GameSettings.resolutionY = savedData.getResolutionY();
	}
	private String logState(String log){
		System.out.println("Game Saver: " + log);
		return log;
	}
	/**
	 * Method which converts a buffered image to byte array
	 * @param image: image to be converted
	 * @return: byte array of the image
	 * @throws IOException
	 */
	public final byte[] ImageToByte(BufferedImage image) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		byte[] imageInByte = null;
		try {
			if (image != null) {
				ImageIO.write(image, IMAGE_TYPE, byteOutput);
				imageInByte = byteOutput.toByteArray();
				byteOutput.flush();
				byteOutput.close();

			}
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		try {
			return imageInByte;
		} finally {
			byteOutput.reset();
		}
	}
	/**
	 * Method which converts a byte array to a ImageView
	 * @param data: byte array to be converted.
	 * @return: imageView of the byte array
	 */
	public final Image byteToImage(byte[] data) {
		BufferedImage newImage = null;
		Image image = null;
		try {
			InputStream inputStream = new ByteArrayInputStream(data);
			newImage = ImageIO.read(inputStream);
			inputStream.close();
			image = SwingFXUtils.toFXImage(newImage, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}

}
