// =================================================================
//
// File: example4.cu
// Author(s):
// Description: This file contains the code to count the number of
//				even numbers within an array using CUDA.
//
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <cuda_runtime.h>
#include "utils.h"

#define SIZE 1000000000
#define THREADS	256
#define BLOCKS	MMIN(32, ((SIZE / THREADS) + 1))

__device__ int NumberOfEvens(int num) {
    if(num % 2 == 0)
	    return 1;
    else
        return 0;
}

__global__ void NumberOFEvens(int *array, int *results) {
	__shared__ int cache[THREADS];

	int tid = threadIdx.x + (blockIdx.x * blockDim.x);
	int cacheIndex = threadIdx.x;

	int aux = 0;
	while (tid < SIZE) {
		//aux = (aux < array[tid])? aux : array[tid];
		aux = aux + NumberOfEvens(array[tid]);
		tid += blockDim.x * gridDim.x;
	}

	cache[cacheIndex] = aux;

	__syncthreads();

	int i = blockDim.x / 2;
	while (i > 0) {
		if (cacheIndex < i) {
			cache[cacheIndex] = NumberOfEvens(cache[cacheIndex], cache[cacheIndex + 1]);
		}
		__syncthreads();
		i /= 2;
	}

	if (cacheIndex == 0) {
		results[blockIdx.x] = cache[cacheIndex];
	}
}

int main(int argc, char* argv[]) {
	int i, *a, *results, pos;
  	int *d_a, *d_r;
	double ms;

	a = (int *) malloc(sizeof(int) * SIZE);
	fill_array(a, SIZE);
	display_array("a", a);

	srand(time(0));
	pos = rand() % SIZE;
	printf("Setting value 0 at %i\n", pos);
	a[pos] = 0;

  	results = (int *) malloc(sizeof(int) * BLOCKS);

	cudaMalloc( (void**) &d_a, SIZE * sizeof(int) );
	cudaMalloc( (void**) &d_r, BLOCKS * sizeof(int) );

	cudaMemcpy(d_a, a, SIZE * sizeof(int), cudaMemcpyHostToDevice);

	printf("Starting...\n");
	ms = 0;
	for (i = 1; i <= N; i++) {
		start_timer();

		NumberOfEvens<<<BLOCKS, THREADS>>> (d_a, d_r);

		ms += stop_timer();
	}

	cudaMemcpy(results, d_r, BLOCKS * sizeof(int), cudaMemcpyDeviceToHost);

	int aux = 0;
	for (i = 0; i < BLOCKS; i++) {
		aux = aux + results[i];
	}

	printf("Number of Evens = %i\n", aux);
	printf("avg time = %.5lf\n", (ms / N));

	cudaFree(d_r);
	cudaFree(d_a);

	free(a);
  	free(results);
	return 0;
}
// implement your code
