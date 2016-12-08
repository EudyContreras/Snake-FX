package com.EudyContreras.Snake.PathFindingAI;

public class Test {

	public static void main(String[]args){
		int[] indexes = new int[7];
		int counter = 0;
		int polarValue = 0;
		for(int i = 0; i<indexes.length; i++){
			indexes[i] = polarValue;

			if(polarValue>=0)
			counter++;

			polarValue = polarValue >= 0 ? -counter : counter;

		}
		for(int i = 0; i<indexes.length; i++){
			System.out.print(indexes[i]+", ");
		}
	}

}
