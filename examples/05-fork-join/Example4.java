// =================================================================
//
// File: Example4.java
// Authors: Sandra Tello A01703658 Isaac Planter A01702962
// Speedup: 0.74
// Description: This file contains the code to count the number of
//				even numbers within an array using Java's Fork-Join.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class Example4 extends RecursiveTask<Integer> {
	private static final int SIZE = 100_000_00;
	private static final int MIN = 100_000;
	//HOLA
	private int array[], start, end, result;

	public Example4(int array[], int start, int end) {
		this.array = array;
		this.start = start;
		this.end = end;
		this.result = 0;
	}

	public Integer computeDirectly() {
		result = 0;
		for(int i=0; i<array.length; i++){
			if(array[i]%2==0){
				result++;
			}
		}
		return result;
	}

	@Override
	protected Integer compute() {
		if ( (end - start) <= MIN ) {
			return computeDirectly();
		} else {
			int mid = start + ( (end - start) / 2 );
			Example4 lowerMid = new Example4(array, start, mid);
			lowerMid.fork();
			Example4 upperMid = new Example4(array, mid, end);
			return ((int) Math.min(upperMid.compute(), lowerMid.join()));
		}
	}

	public static void main(String args[]) {
		long startTime, stopTime;
		int array[], result = 0;
		double ms;
		ForkJoinPool pool;

		array = new int[SIZE];
		Utils.fillArray(array);
		Utils.displayArray("array", array);

		int pos = Math.abs(Utils.r.nextInt()) % SIZE;
		System.out.printf("Setting value 0 at %d\n", pos);
		array[pos] = 0;

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			pool = new ForkJoinPool(Utils.MAXTHREADS);
			result = pool.invoke(new Example4(array, 0, array.length));

			stopTime = System.currentTimeMillis();
			ms += (stopTime - startTime);
		}
		System.out.printf("result = %d\n", result);
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}


