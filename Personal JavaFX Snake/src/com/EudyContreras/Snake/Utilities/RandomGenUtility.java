package com.EudyContreras.Snake.Utilities;

import java.util.Random;

public abstract class RandomGenUtility {

	public static Random rand = new Random();

	private static final char[] symbols;

	private static char[] buffer;

	static{
		StringBuilder tmp = new StringBuilder();
		for (char ch = '0'; ch <= '9'; ++ch)
			tmp.append(ch);
		for (char ch = 'a'; ch <= 'z'; ++ch)
			tmp.append(ch);
		symbols = tmp.toString().toCharArray();

	}

	//Gets a random number, from 0 to maxValue. exclusive.
	public static double getERNG(double maxValue) {
		return rand.nextDouble()*(maxValue);
	}

	//Gets a random number, from 0 to maxValue. inclusive.
	public static double getRNG(double maxValue) {
		return rand.nextDouble()*(maxValue + 1);
	}

	//Gets a random double number in range. inclusive.
	public static double getRandom(double rangeMin, double rangeMax) {
		return rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
	}

	//Gets a random float number in range. inclusive.
	public static float getRandom(float minValue, float maxValue) {
		return rand.nextFloat()*(maxValue + 1 - minValue) + minValue;
	}

	//Gets a random int number in range. inclusive.
	public static int getRandom(int minValue, int maxValue) {
		return rand.nextInt(maxValue + 1 - minValue) + minValue;
	}

	public static boolean getRandBool(){
		return rand.nextBoolean();
	}

	public static String getRandomString(int length) {
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
		buffer = new char[length];
		return nextString();
	}

	private static String nextString() {
		for (int idx = 0; idx < buffer.length; ++idx)
			buffer[idx] = symbols[rand.nextInt(symbols.length)];
		return new String(buffer);
	}
}
