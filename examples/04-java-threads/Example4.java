// =================================================================
//
// File: Example4.java
// Authors: Tello e Isaac A01703658 A01702962
// Description: This file contains the code to count the number of
//				even numbers within an array using Threads.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

public class Example4 extends Thread {
	private static final int SIZE = 100_000_000;
	private int array[], start, end;
	private int result;

	public Example4(int array[], int start, int end) {
		this.array = array;
		this.start = start;
		this.end = end;
		this.result = 0;
	}

	public int getResult() {
		return result;
	}

	public void run() {
		result = 0;
		for(int i=0; i<array.length; i++){
			if(array[i]%2==0){
				result++;
			}
		}
	}

	public static void main(String args[]) {
		long startTime, stopTime;
		int array[], block, evens = 0;
		Example4 threads[];
		double ms;

		array = new int[SIZE];
		Utils.fillArray(array);
		Utils.displayArray("array", array);

		int pos = Math.abs(Utils.r.nextInt()) % SIZE;
		System.out.printf("Setting value 0 at %d\n", pos);
		array[pos] = 0;

		block = SIZE / Utils.MAXTHREADS;
		threads = new Example4[Utils.MAXTHREADS];

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int j = 1; j <= Utils.N; j++) {
			for (int i = 0; i < threads.length; i++) {
				if (i != threads.length - 1) {
					threads[i] = new Example4(array, (i * block), ((i + 1) * block));
				} else {
					threads[i] = new Example4(array, (i * block), SIZE);
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
				evens = Integer.MAX_VALUE;
				for (int i = 0; i < threads.length; i++) {
					evens = (int) Math.min(evens, threads[i].getResult());
				}
			}
		}
		System.out.printf("result = %d\n", evens);
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}

