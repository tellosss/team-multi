// =================================================================
//
// File: matrix_matrix_threads.java
// Author: Isaac Planter 
// Description: This file implements the multiplication of a matrix
//				by another matrix using Java's Threads.
//
// Copyright (c) 2021 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

public class matrix_matrix_threads extends Thread {
	private static final int RENS = 3; //Era 20,000
	private static final int COLS = 3;
	private int m1[], m2[], c[], start, end;

	public matrix_matrix_threads(int m1[], int m2[], int c[], int start, int end) {
		this.m1 = m1;
		this.m2 = m2;
		this.c = c;
		this.start = start;
		this.end = end;
	}

	public void run() {
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

	public static void main(String args[]) {
		long startTime, stopTime;
		double ms;
		int block;
		matrix_matrix_threads threads[];

		int m1[] = new int[RENS * COLS];
		int m2[] = new int[RENS * COLS];
		int c[] = new int[RENS * COLS];

		for (int i = 0; i < RENS; i++) {
			for (int j = 0; j < COLS; j++) {
				m1[(i * COLS) + j] = (j + 1);
                m2[(i * COLS) + j] = (j + 1);
			}
		}

		block = RENS / Utils.MAXTHREADS;
		threads = new matrix_matrix_threads[Utils.MAXTHREADS];

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int j = 1; j <= Utils.N; j++) {
			for (int i = 0; i < threads.length; i++) {
				if (i != threads.length - 1) {
					threads[i] = new matrix_matrix_threads(m1, m2, c, (i * block), ((i + 1) * block));
				} else {
					threads[i] = new matrix_matrix_threads(m1, m2, c, (i * block), RENS);
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
		Utils.displayArray("c", c);
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}