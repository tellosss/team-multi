// =================================================================
//
// File: example7.c
// Author(s): Sandra Tello A01703658 Isaac Planter A01702962
// Speedup: 4.07
// Description: This file contains the code that implements the
//				enumeration sort algorithm using OpenMP.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "utils.h"

#define SIZE 10000

void enumerationSort (int *arr, int size){

    int aux[size];
    #pragma omp parallel for shared(arr, size)
        for (int i = 0; i < size; i++){
            int menores = 0;
            for (int j = 0; j< size; j++){
                if(arr[i]>arr[j] || (arr[i]==arr[j]&&i<j) ){
                    menores+=1;
                }
            }
            aux[menores] = arr[i];
        }
        for (int k = 0; k<size; k++){
            arr[k] = aux[k];
        }

}

int main(int argc, char* argv[]) {
	int i, *a;
	double ms;

	a = (int*) malloc(sizeof(int) * SIZE);
	random_array(a, SIZE);
	display_array("before", a);

	printf("Starting...\n");
	ms = 0;
	for (i = 0; i < N; i++) {
		start_timer();

		// call the implemented function
		enumerationSort(a, SIZE);
		ms += stop_timer();
	}
	display_array("after", a);
	printf("avg time = %.5lf ms\n", (ms / N));

	free(a);
	return 0;
}
// implement your code
