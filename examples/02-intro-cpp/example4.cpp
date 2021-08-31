// =================================================================
//
// File: example2.cpp
// Author(s):
// Description: This file contains the code to count the number of
//				even numbers within an array. The time this implementation
//				takes will be used as the basis to calculate the
//				improvement obtained with parallel technologies.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <iostream>
#include <iomanip>
#include <climits>
#include <algorithm>
#include "utils.h"

const int SIZE = 100000000; //1e8

using namespace std;

// implement your class here
class NumberofEvens {
private:
	int *array, size, EvensCount;

public:
	NumberofEvens(int *a, int s) : array(a), size(s) {}

	int getEvensCount() const {
		return EvensCount;
	}

	void calculate() {
		EvensCount = 0;
		for(int i = 0; i < size; i++){
			if(array[i] % 2 == 0)
				EvensCount++;
		}
	}
};

int main(int argc, char* argv[]) {
	int *a;
	double ms;

	a = new int[SIZE];
	fill_array(a, SIZE);
	display_array("a", a);

	cout << "Starting..." << endl;
	ms = 0;
	// createn object here
	NumberofEvens objeto(a,SIZE);

	for (int i = 0; i < N; i++) {
		start_timer();

		objeto.calculate();
		// call your method here.

		ms += stop_timer();
	}
	// display the result here
	cout << "result = " << objeto.getEvensCount() << endl;
	cout << "avg time = " << setprecision(15) << (ms / N) << " ms" << endl;

	delete [] a;
	return 0;
}
