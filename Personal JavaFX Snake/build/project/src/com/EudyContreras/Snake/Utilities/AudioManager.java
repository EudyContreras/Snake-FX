package com.EudyContreras.Snake.Utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.EudyContreras.Snake.FrameWork.GameManager;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public enum AudioManager {
	INSTANCE;

	private final String PATH_ROOT = "." + File.separator + "Snake" + File.separator;
	private final String PATH_PROPERTIES_FILE = PATH_ROOT + "audio.properties";

	/**
	 * Constants for sfx ID
	 */
	public static final int SFX_ID_START = 0;
	public static final int SFX_ID_END = 1;
	public static final int SFX_ID_DIE1 = 2;
	public static final int SFX_ID_DIE2 = 3;
	public static final int SFX_ID_DIE3 = 4;
	public static final int SFX_ID_APPLE_SPAWN = 5;
	public static final int SFX_ID_APPLE_DIE = 6;
	public static final int SFX_ID_FAST = 7;
	public static final int SFX_ID_SLOW = 8;

	private String[] SFX_PATHS = { GameManager.class.getResource("/com/Snake/sfx/sfx_default.wav").toExternalForm(),
			GameManager.class.getResource("/com/Snake/sfx/sfx_default.wav").toExternalForm(),
			GameManager.class.getResource("/com/Snake/sfx/sfx_die1.wav").toExternalForm(),
			GameManager.class.getResource("/com/Snake/sfx/sfx_die2.wav").toExternalForm(),
			GameManager.class.getResource("/com/Snake/sfx/sfx_die3.wav").toExternalForm(),
			GameManager.class.getResource("/com/Snake/sfx/sfx_apple_spawn.wav").toExternalForm(),
			GameManager.class.getResource("/com/Snake/sfx/sfx_apple_die.wav").toExternalForm(),
			GameManager.class.getResource("/com/Snake/sfx/sfx_fast.wav").toExternalForm(),
			GameManager.class.getResource("/com/Snake/sfx/sfx_slow.wav").toExternalForm() };

	private final String PROPERTIES_COMMENT = "AUDIO SETTINGS";

	private final String GAME_MUSIC_URL = GameManager.class.getResource("/com/Snake/sfx/sfx_default.wav")
			.toExternalForm();

	private final String MUSIC_VOL = "MUSIC_VOL";
	private final String AUDIO_VOL = "AUDIO_VOL";

	private DoubleProperty musicVolume = new SimpleDoubleProperty(1);
	private DoubleProperty audioVolume = new SimpleDoubleProperty(1);

	Media gameMedia = new Media(GAME_MUSIC_URL);

	private MediaPlayer audioPlayer = new MediaPlayer(gameMedia);

	private AudioClip[] sfxAudio = new AudioClip[SFX_PATHS.length];

	File propFile = new File(PATH_PROPERTIES_FILE);
	File root = new File(PATH_ROOT);

	AudioManager() {

		audioPlayer.volumeProperty().set(musicVolume.get());
		audioPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		Bindings.bindBidirectional(audioPlayer.volumeProperty(), musicVolume);

		for (int i = 0; i < SFX_PATHS.length; i++) {
			sfxAudio[i] = new AudioClip(SFX_PATHS[i]);
			sfxAudio[i].volumeProperty().set(audioVolume.get());
			Bindings.bindBidirectional(sfxAudio[i].volumeProperty(), audioVolume);
		}

	}

	public void initialize() {
		audioPlayer.setAutoPlay(true);
		load();
		if (!propFile.exists()) {
			save();
		}
	}

	/**
	 * Save audio settings to file.
	 */
	public void save() {
		Runnable saveRunnable = new Runnable() {
			@Override
			public void run() {
				Properties prop = new Properties();

				try {
					if (propFile.exists()) {
						propFile.delete();
					} else if (!root.exists()) {
						root.mkdirs();
					}
					propFile.createNewFile();

					prop.setProperty(MUSIC_VOL, String.valueOf(musicVolume.get()));
					prop.setProperty(AUDIO_VOL, String.valueOf(audioVolume.get()));

					prop.store(new BufferedOutputStream(new FileOutputStream(PATH_PROPERTIES_FILE)),
							PROPERTIES_COMMENT);
					System.out.println("Saved audio settings");
				} catch (IOException ex) {
					System.out.println("Could not save audio settings");
				}
			}
		};

		Thread saveThread = new Thread(saveRunnable);

		saveThread.start();
	}

	/**
	 * @return true if file could be loaded, else false
	 */
	public boolean load() {
		Properties prop = new Properties();

		try {
			// load a properties file from class path, inside static method
			prop.load(new BufferedInputStream(new FileInputStream(PATH_PROPERTIES_FILE)));

			musicVolume.set(Double.parseDouble(prop.getProperty(MUSIC_VOL)));
			audioVolume.set(Double.parseDouble(prop.getProperty(AUDIO_VOL)));
			return true;
		} catch (IOException | NumberFormatException ex) {
			System.out.println("Could not load audio settings");
			return false;
		}

	}

	/**
	 * Plays a sound effect
	 * 
	 * @param sfxID
	 *            ID of sound effect to play, use SFX_ID.. constants
	 */
	public void playSFX(int sfxID) {
		sfxAudio[sfxID].play();
	}

	public DoubleProperty getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(double musicVolume) {
		this.musicVolume.set(musicVolume);
	}

	public DoubleProperty getAudioVolume() {
		return audioVolume;
	}

	public void setAudioVolume(double audioVolume) {
		this.audioVolume.set(audioVolume);
	}

	public DoubleProperty getGameSongRateProperty() {
		return audioPlayer.rateProperty();
	}

	public void setGameSongRateProperty(DoubleProperty rate) {
		audioPlayer.rateProperty().set(rate.get());
	}

	public void resetGameSongRate() {
		Timeline fade = fader(new Duration(3000), audioPlayer.rateProperty(), audioPlayer.getRate(), 1);
		fade.play();
	}

	/**
	 * Method "fading" a DoubleProperty value, i.e. incrementing or decrementing
	 * the "fadeVal" during a Duration (in millis)
	 * 
	 * @param duration
	 *            Duration fade duration
	 * @param fadeVal
	 *            DoubleProperty value to be faded/changed
	 * @param fadeFromVal
	 *            double fade from
	 * @param fadeToVal
	 *            double fade to
	 * @return Timeline which will fade the "fadeVal"
	 */
	private Timeline fader(Duration duration, DoubleProperty fadeVal, double fadeFromVal, double fadeToVal) {
		KeyFrame fadeFrom = new KeyFrame(Duration.ZERO, new KeyValue(fadeVal, fadeFromVal));
		KeyFrame fadeTo = new KeyFrame(duration, new KeyValue(fadeVal, fadeToVal));
		return new Timeline(fadeFrom, fadeTo);
	}
}
