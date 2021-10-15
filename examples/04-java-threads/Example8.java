// =================================================================
//
// File: Example8.java
// Author: Sandra Tello A01703658 Isaac Planter A01702962
// Description: This file implements the enumeration sort algorithm.
// 				The time this implementation takes will be used as the
//				basis to calculate the improvement obtained with
//				parallel technologies.
//
// Copyright (c) 2021 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

import java.util.Arrays;

public class Example8 extends Thread {
	private static final int SIZE = 100_000;
	private int array[], start, end, aux[];
	//private int aux[] = new int[SIZE];

	public Example8(int array[], int start, int end, int aux[]) {
		this.array = array;
		this.start = start;
		this. end = end;
		this.aux=aux;
	}

	public void run(){
		
		for (int i = start; i < end; i++) {
			aux[i] = 0;
		}
		for(int i=start;i<end;i++){
			for(int j=0;j<SIZE;j++){
				if(array[i]>array[j] | (array[i] == array[j] && j < i)){
					aux[i] += 1;
				}
			}
		}
		for (int k = start; k < end; k++) {
			array[aux[k]] = array[k];
		}
	}

	public int[] getSortedArray() {
		return array;
	}




	public static void main(String args[]) {
		int block;
		double ms;
		int array[] = new int[SIZE];
		int aux[]=new int[SIZE];

		long startTime, stopTime;
		Example8 threads[];

		Utils.randomArray(array);
		Utils.displayArray("before", array);
		
		block = SIZE / Utils.MAXTHREADS;
		threads = new Example8[Utils.MAXTHREADS];

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;

		for (int j = 1; j <= Utils.N; j++) {
			for (int i = 0; i < threads.length; i++) {
				if (i != threads.length - 1) {
					threads[i] = new Example8(array, (i * block), ((i + 1) * block), aux);
				} else {
					threads[i] = new Example8(array, (i * block), SIZE, aux);
				}
			}

			startTime = System.currentTimeMillis();
			for (int i = 0; i < threads.length; i++) {
				threads[i].start();
			}
			for (int i = 0; i < threads.length; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stopTime = System.currentTimeMillis();
			ms +=  (stopTime - startTime);
		}

		Utils.displayArray("after", threads[0].getSortedArray());
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}
