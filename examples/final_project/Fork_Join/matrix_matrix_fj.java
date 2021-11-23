// =================================================================
//
// File: matrix_matrix_threads.java
// Author: Isaac Planter Villalobos A01702962
// Description: This file implements the multiplication of a matrix
//				by another matrix using Java's Fork Join.
//
// Copyright (c) 2021 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class matrix_matrix_fj extends RecursiveAction {
	private static final int RENS = 30;
	private static final int COLS = 30;
	private static final int MIN = 5_000;
	private int m1[], m2[], c[], start, end;

	public matrix_matrix_fj(int m1[], int m2[], int c[], int start, int end) {
		this.m1 = m1;
		this.m2 = m2;
		this.c = c;
		this.start = start;
		this.end = end;
	}

	public void computeDirectly() {
		int acum, pos;
        pos=0;

        for(int h=start; h<end; h++){
            for (int i = 0; i < COLS; i++) {
                acum = 0;
                for (int j = 0; j < COLS; j++) {
                    acum += (m1[(i * COLS) + j] * m2[(j*COLS)+i]);
                }
                c[pos] = acum;
                pos++;
            }
        }
	}

	@Override
	protected void compute() {
		if ( (end - start) <= MIN ) {
			computeDirectly();
		} else {
			int mid = start + ((end - start) / 2);
			invokeAll(new matrix_matrix_fj(m1, m2, c, start, mid),
					  new matrix_matrix_fj(m1, m2, c, mid, end));
		}
	}

	public static void main(String args[]) {
		long startTime, stopTime;
		double ms;
		ForkJoinPool pool;

		int m1[] = new int[RENS * COLS];
		int m2[] = new int[RENS * COLS];
		int c[] = new int[RENS * COLS];

		for (int i = 0; i < RENS; i++) {
			for (int j = 0; j < COLS; j++) {
				m1[(i * COLS) + j] = (j + 1);
                m2[(i * COLS) + j] = (j + 1);
			}
		}

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			pool = new ForkJoinPool(Utils.MAXTHREADS);
			pool.invoke(new matrix_matrix_fj(m1, m2, c, 0, RENS));

			stopTime = System.currentTimeMillis();
			ms += (stopTime - startTime);
		}
		Utils.displayArray("c", c);
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}