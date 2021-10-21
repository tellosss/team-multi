// =================================================================
//
// File: example11.cpp
// Author(s): Sandra Tello A01703658 Isaac Planter A01702962
// Speedup: 3.464595989
// Description: This file implements the code that transforms a
//				grayscale image using Intel's TBB. Uses OpenCV,
//				to compile:
//	g++ example11.cpp `pkg-config --cflags --libs opencv4` -ltbb
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//.
// =================================================================

#include <iostream>
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/highgui/highgui_c.h>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/imgcodecs/imgcodecs.hpp>
#include <tbb/parallel_for.h>
#include <tbb/blocked_range.h>
#include "utils.h"

using namespace std;
using namespace cv;
using namespace tbb;

class Grayscale {
private:
	Mat &src, &dest;

	void grayPixel(int ren, int col) const {
		float r, g, b, aux;

		r = 0; g = 0; b = 0;

		r = (float) src.at<cv::Vec3b>(ren, col)[RED];
		g = (float) src.at<cv::Vec3b>(ren, col)[GREEN];
		b = (float) src.at<cv::Vec3b>(ren, col)[BLUE];
		aux = (r+g+b)/3;

		dest.at<cv::Vec3b>(ren, col)[RED] =  (unsigned char) (aux);
		dest.at<cv::Vec3b>(ren, col)[GREEN] = (unsigned char) (aux);
		dest.at<cv::Vec3b>(ren, col)[BLUE] = (unsigned char) (aux);
	}

public:
	Grayscale(Mat &s, Mat &d) : src(s), dest(d) {}

	void operator() (const blocked_range<int> &r) const {
		for (int i = r.begin(); i != r.end(); i++) {
			for(int j = 0; j < src.cols; j++) {
				grayPixel(i, j);
			}
		}
	}
};

int main(int argc, char* argv[]) {
	double ms;

	if (argc != 2) {
		printf("usage: %s source_file\n", argv[0]);
		return -1;
	}

	Mat src = imread(argv[1], cv::IMREAD_COLOR);
	Mat dest = Mat(src.rows, src.cols, CV_8UC3);
	if (!src.data) {
		printf("Could not load image file: %s\n", argv[1]);
		return -1;
	}

	cout << "Starting..." << endl;
	ms = 0;
	for (int  i = 0; i < N; i++) {
		start_timer();

		// place your code here
		Grayscale obj(src, dest);
		parallel_for(blocked_range<int>(0, src.rows),  obj);
		ms += stop_timer();
	}

	cout << "avg time = " << setprecision(15) << (ms / N) << " ms" << endl;

	/*
	cv::namedWindow("Original", cv::WINDOW_AUTOSIZE);
	cv::imshow("Original", src);

	cv::namedWindow("Gray scale", cv::WINDOW_AUTOSIZE);
	cv::imshow("Gray scale", dest);

	cv::waitKey(0);
	*/

	cv::imwrite("gray_scale.png", dest);

	return 0;
}
