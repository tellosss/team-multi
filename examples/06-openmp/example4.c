// =================================================================
//
// File: example4.c
// Author(s):
// Description: This file contains the code to count the number of
//				even numbers within an array using OpenMP.
//
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include "utils.h"

#define SIZE 100000000

int NumberOfEvens(int *arr, int size){
    int EvensCount = 0;

    #pragma omp parallel for shared(arr, size) reduction(+:EvensCount)
    for(int i = 0; i < size; i++){
            if(arr[i] % 2 == 0)
                EvensCount++;
        }
    return EvensCount;
    /////////////////////////////////////////////////////

    // #pragma omp parallel
	// {
    // int localEvensCount = EvensCount;
    //     #pragma omp for nowait
    //     for(int i = 0; i < size; i++){
    //         if(arr[i] % 2 == 0)
    //             localEvensCount++;
    //     }
    //     #pragma omp critical
	// 	{
	// 		EvensCount = EvensCount + localEvensCount;
	// 	}  
    // }
    // return EvensCount;
}

int main(int argc, char* argv[]) {
	int i, *a, result;
	double ms;

	a = (int *) malloc(sizeof(int) * SIZE);
	fill_array(a, SIZE);
	display_array("a", a);

	printf("Starting...\n");
    printf("Hola\n");
	ms = 0;
	for (i = 0; i < N; i++) {
		start_timer();

		// call the implemented function
		result = NumberOfEvens(a, SIZE);

		ms += stop_timer();
	}
	printf("result = %i\n", result);
	printf("avg time = %.5lf ms\n", (ms / N));
	// must display: result = 500000000

	free(a);
	return 0;
}

// implement your code
