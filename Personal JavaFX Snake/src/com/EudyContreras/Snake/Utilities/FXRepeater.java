package com.EudyContreras.Snake.Utilities;

import java.util.stream.IntStream;

public class FXRepeater{

	public static final Repeater iterations(int times){
		return new Repetitions(times);
	}

	public static void repeat(int times, RepeatWrapper repeater){
		for(int i = 0; i<times; repeater.repeat(i), i++);
	}

	public static void repeat(int start, int end, RepeatWrapper repeater){
		for(int i = start; i<end; repeater.repeat(i), i++);
	}

	public interface RepeatWrapper{
		public void repeat(int index);
	}

	private static class Repetitions implements Repeater{
		private int times;

		public Repetitions(int times){
			this.times = times;
		}

		@Override
		public int getRepeats() {
			return times;
		}
	}
}