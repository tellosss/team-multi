// =================================================================
//Hola 
// File: Example3.cpp
// Authors:
// Description: This file contains the code to count the number of
//				even numbers within an array using Intel's TBB.
//              To compile: g++ example4.cpp -ltbb
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <iostream>
#include <iomanip>
#include <cmath>
#include <tbb/parallel_reduce.h>
#include <tbb/blocked_range.h>
#include "utils.h"

#define SIZE 100000000 //1e8

using namespace std;
using namespace tbb;

class NumberofEvens {
private:
	int *array, EvensCount;

public:
	NumberofEvens(int *a) : array(a), EvensCount(0) {}


	NumberofEvens(NumberofEvens &x, split) : array(x.array), EvensCount(0) {}

	int getEvensCount() const {
		return EvensCount;
	}

	void operator() (const blocked_range<int> &r) {
		for (int i = r.begin(); i != r.end(); i++) {
            if(array[i] % 2 == 0)
				EvensCount++;
		}
	}

    void join(const NumberofEvens &x) {
		EvensCount += x.EvensCount;
	}


};

int main(int argc, char* argv[]) {
	int *a, pos, result;
	double ms;

	a = new int[SIZE];
	fill_array(a, SIZE);
	display_array("a", a);

	

	cout << "Starting..." << endl;
	ms = 0;
	for (int i = 0; i < N; i++) {
		start_timer();

		NumberofEvens obj(a);
		parallel_reduce(blocked_range<int>(0, SIZE), obj);
		result = obj.getEvensCount();

		ms += stop_timer();
	}
	cout << "result = " << result << endl;
	cout << "avg time = " << setprecision(15) << (ms / N) << " ms" << endl;

	delete [] a;
	return 0;
}

// place your code here
