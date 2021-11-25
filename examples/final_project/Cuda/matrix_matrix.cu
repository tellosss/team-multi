// =================================================================
//
// File: matrix_matrix.cu
// Author: Isaac Planter Villalobos A01702962
// Description: This file implements the multiplication of a matrix
//				by another matrix using CUDA.
//
// Copyright (c) 2021 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <cuda_runtime.h>
#include "utils.h"

#define RENS    3
#define COLS    3
#define THREADS 256
#define BLOCKS	MMIN(32, (((RENS * COLS) / THREADS) + 1))

__global__ void matrix_matrix(int *m1, int *m2, int *c) {
    int tid = threadIdx.x + (blockIdx.x * blockDim.x);
    int i, j, acum, pos=0;


    while (tid < RENS){
        for (i = 0; i < COLS; i++) {
            acum = 0;
            for (j = 0; j < RENS; j++) {
                acum += (m1[(tid * COLS) + j] * m2[(j*RENS)+i]);
                // acum += (m1[(i * COLS) + j] * m2[(j*COLS)+i]);

            }
            // c[pos] = acum;
            c[tid*COLS)+i]= acum;
            pos++;
            
        }
        tid += blockDim.x * gridDim.x;
    }
}

int main(int argc, char* argv[]) {
	int i, j, *m1, *m2, *c;
    int *d_m1, *d_m2, *d_c;
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

    cudaMalloc((void**)&d_m1, sizeof(int) * RENS* COLS);
    cudaMalloc((void**)&d_m2, sizeof(int) * RENS* COLS);
    cudaMalloc((void**)&d_c, sizeof(int) * RENS* COLS);

    cudaMemcpy(d_m1, m1, sizeof(int) * RENS* COLS, cudaMemcpyHostToDevice);
    cudaMemcpy(d_m2, m2, sizeof(int) * RENS* COLS, cudaMemcpyHostToDevice);

	printf("Starting...\n");
	ms = 0;
	for (i = 0; i < N; i++) {
		start_timer();

		matrix_matrix<<<BLOCKS, THREADS>>>(d_m1, d_m2, d_c);

		ms += stop_timer();
	}

    cudaMemcpy(c, d_c, sizeof(int) * RENS, cudaMemcpyDeviceToHost);

	display_array("c:", c);
	printf("avg time = %.5lf ms\n", (ms / N));

    cudaFree(d_m1); cudaFree(d_m2); cudaFree(d_c);
	free(m1); free(m2); free(c);
	return 0;
}
