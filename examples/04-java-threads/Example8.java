// =================================================================
//
// File: Example8.java
// Author: Pedro Perez
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

public class Example8 extends Thread {
	private static final int SIZE = 100_000;
	private int array[], temp[], start, end, depth;
	private int aux[] = new int[SIZE];

	public Example8(int array[], int temp[], int start, int end, int depth) {
		this.array = array;
		this.temp = temp;
		this.start = start;
		this. end = end;
		this.depth = depth;
		//System.out.println("start = " + start + " end = " + end + " depth = " + depth);
	}

	private void doSort() {
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

	private void mergeAndCopy() {
		int i, j, k;
		int mid = start + ((end - start) / 2);

		i = start;
		j = mid;
		k = start;
		while (i < mid && j < end) {
			if (array[i] < array[j]) {
				temp[k] = array[i];
				i++;
			} else {
				temp[k] = array[j];
				j++;
			}
			k++;
		}
		for (; j < end; j++) {
			temp[k++] = array[j];
		}
		for (; i < mid; i++) {
			temp[k++] = array[i];
		}

		for (i = start; i < end; i++) {
			array[i] = temp[i];
		}
	}

	public void run() {
		if (depth == 0) {
			doSort();
		} else {
			int mid = start + ((end - start) / 2);
			Example8 left = new Example8(array, temp, start, mid, depth - 1);
			Example8 right = new Example8(array, temp, mid, end, depth - 1);

			left.start(); right.start();
			try {
				left.join(); right.join();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			mergeAndCopy();
		}
	}

	public int[] getSortedArray() {
		return array;
	}

	public static void main(String args[]) {
		int array[] = new int[SIZE];
		int temp[] = new int[SIZE];

		long startTime, stopTime;
		Example8 obj = null;
		int depth = (int)(Math.log(Utils.MAXTHREADS) / Math.log(2));
		double ms;

		Utils.randomArray(array);
		Utils.displayArray("before", array);

		System.out.printf("Starting...\n");
		ms = 0;
		for (int i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			obj = new Example8(Arrays.copyOf(array, array.length), temp, 0, array.length, depth);
			obj.start();
			try {
				obj.join();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}

			stopTime = System.currentTimeMillis();

			ms += (stopTime - startTime);
		}
		Utils.displayArray("after", obj.getSortedArray());
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}
