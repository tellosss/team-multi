// =================================================================
//
// File: example7.cpp
// Author: Pedro Perez
// Description: This file contains the code that implements the
//				enumeration sort algorithm using Intel's TBB.
//				To compile: g++ example8.cpp -ltbb
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//..
// =================================================================

#include <iostream>
#include <iomanip>
#include <cstring>
#include <cmath>
#include <tbb/parallel_for.h>
#include <tbb/blocked_range.h>
#include "utils.h"

const int SIZE = 100000;

using namespace std;
using namespace tbb;

// place your code here
class enumerationSort {
private:
	int *array, size, *aux;

public:
	enumerationSort(int *a, int s, int *ax) : array(a), size(s), aux(ax) {}

    void operator() (const blocked_range<int> &r) const{
			for (int i = r.begin(); i != r.end(); i++) {
				int menores = 0;
				for (int j = 0; j< size;  j++){
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
	int *a, i, *ax;
	double ms;

	a = new int[SIZE];
  ax = new int[SIZE];
  random_array(a, SIZE);
  display_array("before", a);

	cout << "Starting..." << endl;

	ms = 0;
	// create object here
  enumerationSort objeto(a, SIZE, ax);

	for (int i = 0; i < N; i++) {
		start_timer();
		// call your method here.
		parallel_for(blocked_range<int>(0, SIZE), objeto);

    for (int k = 0; k<SIZE; k++){
      a[k] = ax[k];
    }
		ms += stop_timer();
	}

	display_array("after", a);


	cout << "avg time = " << setprecision(15) << (ms / N) << " ms" << endl;

	delete [] a;
	return 0;
}
