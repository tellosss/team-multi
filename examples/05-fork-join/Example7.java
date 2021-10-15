// =================================================================
//
// File: Example7.java
// Author(s): Sandra Tello A01703658 Isaac Planter A01702962
// Speedup: 0.1
// Description: This file contains the code to brute-force all
//				prime numbers less than MAXIMUM using Java's
//				Fork-Join.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class Example7 extends RecursiveAction {
	private static final int MAXIMUM = 1_000_00;
	private static final int MIN = 1_000;

	private boolean array[], Bandera;
	private int start, end;
	private double RaizCuadrada;

	public Example7(boolean array[], int start, int end) {
		this.array = array;
		this.start = start;
		this.end = end;
	}

	public void computeDirectly() {
		// place yout code here
		RaizCuadrada = 0;
		for(int i=2; i<array.length; i++){
			RaizCuadrada = Math.sqrt(i);
			Bandera = false;
			for(int j=2; j<=RaizCuadrada; j++){
				if(i%j==0){
					//No es primo
					Bandera = true;
					break;
				}
			}
			if(Bandera==false){
				array[i]=true;
			}
		}

	}


	@Override
	protected void compute() {
		if ( (end - start) <= MIN ) {
			computeDirectly();
		} else {
			int mid = start + ((end - start) / 2);
			invokeAll(new Example7(array, start, mid),
					  new Example7(array, mid, end));
		}
	}

	public static void main(String args[]) {
		long startTime, stopTime;
		boolean array[] = new boolean[MAXIMUM + 1];
		double ms;
		ForkJoinPool pool;

		System.out.println("At first, neither is a prime. We will display to TOP_VALUE:");
		for (int i = 2; i < Utils.TOP_VALUE; i++) {
			array[i] = false;
			System.out.print("" + i + ", ");
		}
		System.out.println("");


		// int pos = Math.abs(Utils.r.nextInt()) % SIZE;
		// System.out.printf("Setting value 0 at %d\n", pos);
		// array[pos] = 0;

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int i = 0; i <= Utils.N; i++) {
			startTime = System.currentTimeMillis();

			pool = new ForkJoinPool(Utils.MAXTHREADS);
			pool.invoke(new Example7(array, 0, array.length));

			stopTime = System.currentTimeMillis();
			ms += (stopTime - startTime);

			if (i == Utils.N) {
				System.out.println("Expanding the numbers that are prime to TOP_VALUE:");
				for (int j = 2; j < Utils.TOP_VALUE; j++) {
					if (array[j]) {
						System.out.print("" + j + ", ");
					}
				}
			}
		}
		System.out.println("");
		System.out.printf("avg time = %.5f ms\n", (ms / Utils.N));
	}

  // place your code here

}
