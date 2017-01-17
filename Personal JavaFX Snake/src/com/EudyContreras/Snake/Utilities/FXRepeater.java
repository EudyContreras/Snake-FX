package com.EudyContreras.Snake.Utilities;

import java.util.stream.IntStream;

public class FXRepeater{

	public static final Repeater iterations(int times){
		return new Repetitions(times);
	}

	public static void repeat(int times, RepeatWrapper repeater){
		IntStream.range(0, times).forEach(i -> repeater.repeat(i));
	}

	public static void repeat(int start, int end, RepeatWrapper repeater){
		IntStream.range(start, end).forEach(i -> repeater.repeat(i));
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