package com.EudyContreras.Snake.Utilities;

import java.util.Random;

public abstract class RandomGenUtility {
	public static Random rand = new Random();

	//Gets a random number, from 0 to maxValue. exclusive.
	public static double getERNG(double maxValue) {

		return rand.nextDouble()*(maxValue);
	}

	//Gets a random number, from 0 to maxValue. inclusive.
	public static double getRNG(double maxValue) {

		return rand.nextDouble()*(maxValue + 1);
	}

	//Gets a random number in range. inclusive.
	public static double getRNG(double minValue, double maxValue) {

		return rand.nextDouble()*(maxValue + 1 - minValue) + minValue;
	}
}
