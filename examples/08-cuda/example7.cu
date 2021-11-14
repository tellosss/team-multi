// =================================================================
//
// File: example7.cu
// Author(s):
// Description: This file contains the code to brute-force all
//				prime numbers less than MAXIMUM using CUDA.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <cuda_runtime.h>
#include "utils.h"

#define SIZE 1000000 //1e6
#define THREADS 256
#define BLOCKS	MMIN(32, ((SIZE / THREADS) + 1))

__global__ void ChecaPrimos(int*arr, int size){
   int i, aux, RaizCuadrada;
   int tid = threadIdx.x + (blockIdx.x * blockDim.x);
   RaizCuadrada = 0;

   while (tid < size) {
       RaizCuadrada = sqrtf(tid);
		bool Bandera = false;
		for(int j=2; j<=RaizCuadrada; j++){
			if(tid % j==0){
				//No es primo
				Bandera = true;
				break;
			}
		}
		if(Bandera==false){
			arr[tid]=1;
		}
		tid += blockDim.x * gridDim.x;
	}

}

// __global__ void even(int* arr, int size) {
//   int i, aux, RaizCuadrada;
//   RaizCuadrada = 0;

//   i = (threadIdx.x * 2);
//   if (i <= size - 2) {
//     if (arr[i] > arr[i + 1]) {
//       aux = arr[i];
//       arr[i] = arr[i + 1];
//       arr[i + 1] = aux;
//     }
//   }
// }

// __global__ void odd(int* arr, int size) {
//   int i, aux;

//   i = (threadIdx.x * 2) + 1;
//   if (i <= size - 2) {
//     if (arr[i] > arr[i + 1]) {
//       aux = arr[i];
//       arr[i] = arr[i + 1];
//       arr[i + 1] = aux;
//     }
//   }
// }

int main(int argc, char* argv[]) {
	int i, j, *a, *d_a;
	double ms;

	a = (int *) malloc(sizeof(int) * SIZE);
    printf("At first, neither is a prime. We will display to TOP_VALUE:\n");
	for (i = 2; i < TOP_VALUE; i++) {
		if (a[i] == 0) {
			printf("%i ", i);
		}
	}
    printf("\n");


    cudaMalloc( (void**) &d_a, SIZE * sizeof(int) );

    printf("Starting...\n");
	ms = 0;
	for (i = 1; i <= N; i++) {
        cudaMemcpy(d_a, a, SIZE * sizeof(int), cudaMemcpyHostToDevice);

	    start_timer();

        ChecaPrimos<<<1, THREADS>>>(d_a, SIZE);

	    ms += stop_timer();
	}

    cudaMemcpy(a, d_a, SIZE * sizeof(int), cudaMemcpyDeviceToHost);
    printf("avg time = %.5lf ms\n", (ms / N));

    display_array("array", a);

    for (i = 2; i < TOP_VALUE; i++) {
		if (a[i] == 1) {
			printf("%i ", i);
		}
	}

	cudaFree(d_a);

	free(a);

  return 0;
}
