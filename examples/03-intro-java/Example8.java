// =================================================================
//
// File: Example8.java
// Author(s): Sandra Tello A01703658 Isaac Planter A01702962
// Description: This file implements the enumeration sort algorithm.
// 				The time this implementation takes will be used as the
//				basis to calculate the improvement obtained with
//				parallel technologies.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

import java.util.Arrays;

public class Example8 {
	private static final int SIZE = 100_000;
	private int array[];
	private int aux[] = new int[SIZE];

	public Example8(int array[]) {
		this.array = array;
	}

	public void doTask() {
		// place your code here
		for (int i = 0; i < SIZE; i++){
			int menores = 0;
			for (int j = 0; j < SIZE; j++){
				if(array[i]>array[j] || (array[i]==array[j]&&i<j) ){
					menores+=1;
				}
			}
			aux[menores] = array[i];
		}
		for (int k = 0; k<SIZE; k++){
			array[k] = aux[k];
		}
		
	}

	public int[] getSortedArray() {
		return array;
	}

	public static void main(String args[]) {
		int array[] = new int[SIZE];
		long startTime, stopTime;
		double ms;
		Example8 obj = null;
		Example8 objeto = new Example8(array);

		Utils.randomArray(array);
		Utils.displayArray("before", array);

		System.out.printf("Starting...\n");
		ms = 0;
		for (int i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			// pace your code here.
			objeto.doTask();

			stopTime = System.currentTimeMillis();

			ms += (stopTime - startTime);
		}
		Utils.displayArray("after", objeto.getSortedArray());
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}
