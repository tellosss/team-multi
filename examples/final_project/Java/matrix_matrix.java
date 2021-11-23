
// =================================================================
//
// File: matrix_matrix.java
// Author: Isaac Planter
// Description: This file implements the multiplication of a matrix
//				by a vector. The time this implementation takes will
//				be used as the basis to calculate the improvement
//				obtained with parallel technologies.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

public class matrix_matrix {
	private static final int RENS = 300;
	private static final int COLS = 300;
	private int m1[], m2[], c[];

	public matrix_matrix(int m1[], int m2[], int c[]) {
		this.m1 = m1;
		this.m2 = m2;
		this.c = c;
	}

	public void calculate() {
		int acum, pos;
        pos=0;

        for(int h=0; h<RENS; h++){
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

		int m1[] = new int[RENS * COLS];
		int m2[] = new int[RENS * COLS];
		int c[] = new int[RENS * COLS];

		for (int i = 0; i < RENS; i++) {
			for (int j = 0; j < COLS; j++) {
				m1[(i * COLS) + j] = (j + 1);
                m2[(i * COLS) + j] = (j + 1);
			}
		}

		System.out.printf("Starting...\n");
		ms = 0;
		matrix_matrix e = new matrix_matrix(m1, m2, c);
		for (int i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			e.calculate();

			stopTime = System.currentTimeMillis();

			ms += (stopTime - startTime);
		}
		Utils.displayArray("c", c);
		System.out.printf("avg time = %.5f ms\n", (ms / Utils.N));
	}
}