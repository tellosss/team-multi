// =================================================================
//
// File: Example8.java
// Author(s): Sandra Tello A01703658 Isaac Planter A01702962
// Speedup: 1.10
// Description: This file implements the enumeration sort algorithm
// 				using Java's Fork-Join.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class Example8 extends RecursiveAction {
	private static final int SIZE = 100_000;
	private static final int MIN = 1_000;
	private int array[], start, end, aux[];


	public Example8(int array[], int start, int end, int aux[]) {
		this.array = array;
		this.start = start;
		this. end = end;
		this.aux=aux;
	}

	public void computeDirectly() {
		// place yout code here
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


	@Override
	protected void compute() {
		if ( (end - start) <= MIN ) {
			computeDirectly();
		} else {
			int mid = start + ((end - start) / 2);
			invokeAll(new Example8(array, start, mid, aux),
					  new Example8(array, mid, end, aux));
		}
	}

	public static void main(String args[]) {
		long startTime, stopTime;
		int array[] = new int[SIZE];
		int aux[]=new int[SIZE];
		double ms;
		ForkJoinPool pool;

		Utils.randomArray(array);
		Utils.displayArray("before", array);

		
		// int pos = Math.abs(Utils.r.nextInt()) % SIZE;
		// System.out.printf("Setting value 0 at %d\n", pos);
		// array[pos] = 0;

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int i = 0; i <= Utils.N; i++) {
			startTime = System.currentTimeMillis();

			pool = new ForkJoinPool(Utils.MAXTHREADS);
			pool.invoke(new Example8(array, 0, array.length, aux));

			stopTime = System.currentTimeMillis();
			ms += (stopTime - startTime);

		}

		Utils.displayArray("after", array);
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}

  // place your code here

}
