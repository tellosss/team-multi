// =================================================================
//
// File: Example7.java
// Author(s): Sandra e Isaac A01703658 A01702962
// Description: This file contains the code to brute-force all
//				prime numbers less than MAXIMUM. The time this
//				implementation takes will be used as the basis to
//				calculate the improvement obtained with parallel
//				technologies.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

public class Example7 extends Thread {
	private static final int SIZE = 1_000_00;
	private boolean array[], Bandera;
	private int start, end;
	private double RaizCuadrada;


	public Example7(boolean array[], int start, int end) {
		this.array = array;
		this.start = start;
		this.end = end;
	}

	public void run() {
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

	public static void main(String args[]) {
		boolean array[] = new boolean[SIZE + 1];
		int block;
		Example7 threads[];
		long startTime, stopTime;
		double ms;

		System.out.println("At first, neither is a prime. We will display to TOP_VALUE:");
		for (int i = 2; i < Utils.TOP_VALUE; i++) {
			array[i] = false;
			System.out.print("" + i + ", ");
		}
		System.out.println("");

		block = SIZE / Utils.MAXTHREADS;
		threads = new Example7[Utils.MAXTHREADS];

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int j = 1; j <= Utils.N; j++) {
			for (int i = 0; i < threads.length; i++) {
				if (i != threads.length - 1) {
					threads[i] = new Example7(array, (i * block), ((i + 1) * block));
				} else {
					threads[i] = new Example7(array, (i * block), SIZE);
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

			if (j == Utils.N) {
				System.out.println("Expanding the numbers that are prime to TOP_VALUE:");
				for (int i = 2; i < Utils.TOP_VALUE; i++) {
					if (array[i]) {
						System.out.print("" + i + ", ");
					}
				}
			}
		}

		/////////////////////////////////////////////////////////////////////////
		System.out.println("");
		System.out.printf("avg time = %.5f ms\n", (ms / Utils.N));
	}
	///////////////////////////////////////////////////////////////////////////////////////

}
