// =================================================================
//
// Multiprocesadores: C++
// Fecha: 4-Septiembre-2021
// File: example8.cpp
// Author(s): Isaac Planter Villalobos A01702962 Sandra Tello Salinas A01703658
// Description: This file contains the code that implements the
//				enumeration sort algorithm. The time this implementation
//				takes ill be used as the basis to calculate the
//				improvement obtained with parallel technologies.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <iostream>
#include <iomanip>
#include <cstring>
#include "utils.h"

const int SIZE = 100000; //1e5

using namespace std;

// implement your class here
class enumerationSort {
private:
	int *array, size;

public:
	enumerationSort(int *a, int s) : array(a), size(s) {}

		void ordenar (){
			int aux[size];
			for (int i = 0; i < size; i++){
				int menores = 0;
				for (int j = 0; j< size; j++){
					if(array[i]>array[j] || (array[i]==array[j]&&i<j) ){
						menores+=1;
					}
				}
				aux[menores] = array[i];
			}
			for (int k = 0; k<size; k++){
				array[k] = aux[k];
			}
		}

};

int main(int argc, char* argv[]) {
	int *a;
	double ms;

	a = new int[SIZE];
	random_array(a, SIZE);
	display_array("before", a);

	cout << "Starting..." << endl;
	ms = 0;
	// create object here
	enumerationSort objeto(a, SIZE);
	for (int i = 0; i < N; i++) {
		start_timer();

		// call your method here.
		objeto.ordenar();
		ms += stop_timer();
	}

	display_array("after", a);
	cout << "avg time = " << setprecision(15) << (ms / N) << " ms" << endl;

	delete [] a;
	return 0;
}
