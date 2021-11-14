// =================================================================
//
// File: example4.cu
// Author: Isaac Planter A01702962 Sandra Tello A01703658
// Description: This file contains the code to count the number of
//				even numbers within an array using using CUDA.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <cuda_runtime.h>
#include "utils.h"

#define SIZE 1000000000 //1e9
#define THREADS	256
#define BLOCKS	MMIN(32, ((SIZE / THREADS) + 1))

__global__ void NumberOfEvens(int *array, long *result) {
	__shared__ long cache[THREADS];

	int tid = threadIdx.x + (blockIdx.x * blockDim.x);
	int cacheIndex = threadIdx.x;

	long acum = 0;
	while (tid < SIZE) {
        if(array[tid] % 2 == 0)
			acum++;
		tid += blockDim.x * gridDim.x;
	}

	cache[cacheIndex] = acum;

	__syncthreads();

	int i = blockDim.x / 2;
	while (i > 0) {
		if (cacheIndex < i) {
			cache[cacheIndex] += cache[cacheIndex + i];
		}
		__syncthreads();
		i /= 2;
	}

	if (cacheIndex == 0) {
		result[blockIdx.x] = cache[cacheIndex];
	}
}

int main(int argc, char* argv[]) {
	int i, *array, *d_a;
	long *results, *d_r;
	double ms;

	array = (int*) malloc( SIZE * sizeof(int) );
	fill_array(array, SIZE);
	display_array("array", array);

	results = (long*) malloc( BLOCKS * sizeof(long) );

	cudaMalloc( (void**) &d_a, SIZE * sizeof(int) );
	cudaMalloc( (void**) &d_r, BLOCKS * sizeof(long) );

	cudaMemcpy(d_a, array, SIZE * sizeof(int), cudaMemcpyHostToDevice);

	printf("Starting...\n");
	ms = 0;
	for (i = 1; i <= N; i++) {
		start_timer();

		NumberOfEvens<<<BLOCKS, THREADS>>> (d_a, d_r);

		ms += stop_timer();
	}

	cudaMemcpy(results, d_r, BLOCKS * sizeof(long), cudaMemcpyDeviceToHost);

	long acum = 0;
	for (i = 0; i < BLOCKS; i++) {
		acum += results[i];
	}

	printf("result = %li\n", acum);
	printf("avg time = %.5lf\n", (ms / N));

	cudaFree(d_r);
	cudaFree(d_a);

	free(array);
	free(results);
	return 0;
}
