package com.EudyContreras.Snake.CustomNodes;

import java.util.stream.IntStream;

public class FXRepeater {

	public static void repeat(int times, RepeatWrapper repeater){
		IntStream.range(0, times).forEach(i -> repeater.repeat(i));
	}

	public static void repeat(int start, int end, RepeatWrapper repeater){
		IntStream.range(start, end).forEach(i -> repeater.repeat(i));
	}

	public interface RepeatWrapper{
		public void repeat(int index);
	}
}