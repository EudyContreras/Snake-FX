package com.EudyContreras.Snake.Utilities;

import java.util.Random;

public abstract class RandomGenUtility {
	public static Random rand = new Random();

	//Gets a random number, from 0 to maxValue. exclusive.
	public static int getERNG(int maxValue) {
		return rand.nextInt(maxValue);
	}

	//Gets a random number, from 0 to maxValue. inclusive.
	public static int getRNG(int maxValue) {
		return rand.nextInt(maxValue + 1);
	}

	//Gets a random number in range. inclusive.
	public static int getRNG(int minValue, int maxValue) {
		return rand.nextInt(maxValue + 1 - minValue) + minValue;
	}
}
