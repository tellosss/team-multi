// =================================================================
//
// File: c_sequential.c
// Author: Isaac Planter
// Description: This file implements the multiplication of a matrix
//				by another matrix. The time this implementation takes will
//				be used as the basis to calculate the improvement
//				obtained with parallel technologies.
//
// Copyright (c) 2021 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include "utils.h"

#define RENS 300
#define COLS 300

void matrix_matrix(int *m1, int *m2, int *c) {
	int h, i, j,pos, acum;
    pos = 0;
    
    //Algoritmo nuevo
    // for(h=0; h<RENS; h++){
    //     for (i = 0; i < COLS; i++) {
    //         acum = 0;
    //         for (j = 0; j < RENS; j++) {
    //             acum += (m1[(h * COLS) + j] * m2[(j*RENS)+i]);
    //         }
    //         c[(h*COLS)+i] = acum;
    //     }
    // }

    //Algoritmo viejo
    for(h=0; h<RENS; h++){
        for (i = 0; i < COLS; i++) {
            acum = 0;
            for (j = 0; j < COLS; j++) {
                acum += (m1[(i * COLS) + j] * m2[(j*COLS)+i]);
            }
            c[pos] = acum;
            pos++;
        }
    }
}

int main(int argc, char* argv[]) {
	int i, j, *m, *b, *c, *m1, *m2;
	double ms;

	m1 = (int*) malloc(sizeof(int) * RENS* COLS);
	m2 = (int*) malloc(sizeof(int) * RENS* COLS);
	c = (int*) malloc(sizeof(int) * RENS* COLS);

	for (i = 0; i < RENS; i++) {
		for (j = 0; j < COLS; j++) {
			m1[(i * COLS) + j] = (j + 1);
            m2[(i * COLS) + j] = (j + 1);
		}
	}

    // printf("M1: ");
    // for(i=0;i<RENS*COLS;i++){
    //     printf("%i ", m1[i]);
    // }
    // printf(" \nM2: ");
    // for(i=0;i<RENS*COLS;i++){
    //     printf("%i ", m2[i]);
    // }
    // printf("\n");

    display_array("m1:", m1);
    display_array("m2:", m2);

	printf("Starting...\n");
	ms = 0;
	for (i = 0; i < N; i++) {
		start_timer();

		matrix_matrix(m1, m2, c);

		ms += stop_timer();
	}
	display_array("c:", c);

    printf("c: ");
    // for(i=0;i<RENS*COLS;i++){
    //     printf("%i ", c[i]);
    // }
	printf("avg time = %.5lf ms\n", (ms / N));

	free(m1); free(m2); free(c);
	return 0;
}