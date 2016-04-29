package com.SnakeGame.Core;

import javax.sound.sampled.Clip;

/**
 * This is the class where we load all audioFiles. we first precreate the
 * properly decoded and formated audio file and then we use it in the game. This
 * way audio files are only loaded once and the game is able to reused the file
 * an unlimited amount of times.
 * 
 * @author Eudy Contreras
 *
 */
public class GameAudioBank {

	public static Clip name;

	public GameAudioBank() {
		// name = GameAudioManager.processAudioFile("");
	}
}