// =================================================================
//
// File: example8.cu
// Author(s): Isaac Planter A01702962 Sandra Tello A01703658
// Speedup achieved: 55193.38462
// Description: This file contains the code that implements the
//				enumeration sort algorithm using CUDA.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <cuda_runtime.h>
#include "utils.h"

#define SIZE 10000
#define THREADS 256
#define BLOCKS	MMIN(32, ((SIZE / THREADS) + 1))

__global__ void enumerationSort(int*arr, int*aux, int size){

   int tid = threadIdx.x + (blockIdx.x * blockDim.x);

   while (tid < size) {
		int menores = 0;
		for (int j = 0; j< size; j++){
			if(arr[tid]>arr[j] || (arr[tid]==arr[j]&&tid<j) ){
				menores+=1;
			}
		}
		aux[menores] = arr[tid];
        tid += blockDim.x * gridDim.x;
	}
	for (int k = 0; k<size; k++){
		arr[k] = aux[k];
	}
}


int main(int argc, char* argv[]) {
	int i, j, *a, *aux, *d_a, *d_aux;
	double ms;

	a = (int *) malloc(sizeof(int) * SIZE);
    aux = (int *) malloc(sizeof(int) * SIZE);
    random_array(a, SIZE);
	display_array("before", a);
    
    cudaMalloc( (void**) &d_a, SIZE * sizeof(int) );
    cudaMalloc( (void**) &d_aux, SIZE * sizeof(int) );

    printf("Starting...\n");
	ms = 0;
	for (i = 1; i <= N; i++) {
        cudaMemcpy(d_a, a, SIZE * sizeof(int), cudaMemcpyHostToDevice);
        cudaMemcpy(d_aux, aux, SIZE * sizeof(int), cudaMemcpyHostToDevice);

	    start_timer();

        enumerationSort<<<1, THREADS>>>(d_a, d_aux, SIZE);
        // for (int k = 0; k<SIZE; k++){
		// 	a[k] = aux[k];
		// }

	    ms += stop_timer();
	}

    cudaMemcpy(a, d_a, SIZE * sizeof(int), cudaMemcpyDeviceToHost);
    cudaMemcpy(aux, d_aux, SIZE * sizeof(int), cudaMemcpyDeviceToHost);

    printf("avg time = %.5lf ms\n", (ms / N));

    display_array("after", a);


	cudaFree(d_a);
    cudaFree(d_aux);

	free(a);
    free(aux);

  return 0;
}
