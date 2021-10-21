// =================================================================
//
// File: example7.cpp
// Author: Sandra Tello A01703658 Isaac Planter A01702962
// speedup: 0.12
// Description: This file contains the code to brute-force all
//				prime numbers less than MAXIMUM using Intel's TBB.
//				To compile: g++ example7.cpp -ltbb
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <iostream>
#include <iomanip>
#include <cstring>
#include <cmath>
#include <tbb/parallel_for.h>
#include <tbb/blocked_range.h>
#include "utils.h"

const int MAXIMUM = 100000; //1e6

using namespace std;
using namespace tbb;

class ChecaPrimos{
private:
    int *array, size, RaizCuadrada;

public:
    ChecaPrimos(int *a, int s) : array(a), size(s) {}

    void operator() (const blocked_range<int> &r) const{
      for (int i = r.begin(); i != r.end(); i++) {
    			int RaizCuadrada = sqrt(i);
    			bool Bandera = false;
    			for(int j=2; j<=RaizCuadrada; j++){
    				if(i%j==0){
    					//No es primo
    					Bandera = true;
    					break;
    				}
    			}
    			if(Bandera==false){
    				array[i]=1;
    			}
		    }
	   }

};


 int main(int argc, char* argv[]) {
	int i, *a;
	double ms;

	a = new int[MAXIMUM + 1];
	cout << "At first, neither is a prime. We will display to TOP_VALUE:\n";
	for (i = 2; i < TOP_VALUE; i++) {
		cout << i << " ";
	}
	cout << "\n";

	cout << "Starting..." << endl;
	ms = 0;
	// create object here
	ChecaPrimos objeto(a, TOP_VALUE);

	for (int i = 0; i < N; i++) {
		start_timer();
		// call your method here.
		parallel_for(blocked_range<int>(0, MAXIMUM), objeto);


		ms += stop_timer();
	}
	cout << "Expanding the numbers that are prime to TOP_VALUE:\n";
	for (i = 2; i < TOP_VALUE; i++) {
		if (a[i] == 1) {
			printf("%i ", i);
		}
	}
	cout << "avg time = " << setprecision(15) << (ms / N) << " ms" << endl;

	delete [] a;
	return 0;
}




// place your code here
