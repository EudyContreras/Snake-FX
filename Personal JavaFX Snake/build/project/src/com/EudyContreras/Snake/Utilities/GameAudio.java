package com.EudyContreras.Snake.Utilities;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class is responsible for reading and decoding audio files that will be
 * used in game the methods specified within this class can be called along with
 * every event ocurring within the games framework
 * 
 * @author Eudy Contreras
 *
 */
public class GameAudio {
	/**
	 * Constructor which takes a path to an audio file which will then be
	 * process and converted into a java clip file. The audio file will later be
	 * able to be manipulated using the below non static methods
	 * 
	 * @param path
	 */
	public static MediaPlayer getAudio(String path) {
		MediaPlayer soundClip;
		soundClip = new MediaPlayer(new Media(new File(path).toURI().toString()));
		return soundClip;
	}

}
