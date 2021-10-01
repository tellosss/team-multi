// =================================================================
//
// File: Example11.java
// Author(s):
// Description: This file implements the code that transforms a
//				grayscale image. The time this implementation takes will
//				be used as the basis to calculate the improvement obtained
// 				with parallel technologies.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Example11 extends Thread{
	// place your code here
	private static final int BLUR_WINDOW = 15;
	private int src[], dest[], width, height, start, end;

	public Example11(int src[], int dest[], int width, int height, int start, int end) {
		this.src = src;
		this.dest = dest;
		this.width = width;
		this.height = height;
		this.start = start;
		this.end = end;
	}

	private void greyPixel(int ren, int col) {
		int  i, j;
		int pixel, dpixel;
		float r, g, b, aux;

		
		r = 0; g = 0; b = 0;
		
		
		pixel = src[(ren * width) + col];

		r += (float) ((pixel & 0x00ff0000) >> 16);
		g += (float) ((pixel & 0x0000ff00) >> 8);
		b += (float) ((pixel & 0x000000ff) >> 0);
		aux = (r+g+b)/3;
		

		dpixel = (0xff000000)
				| (((int) (aux)) << 16)
				| (((int) (aux)) << 8)
				| (((int) (aux)) << 0);
		dest[(ren * width) + col] = dpixel;
	}

	public void run() {
		int index, ren, col;

		for (index = start; index < end; index++) {
			ren = index / width;
			col = index % width;
			greyPixel(ren, col);
		}
	}

	public static void main(String args[]) throws Exception {
		long startTime, stopTime;
		double ms;
		Example11 threads[];
		int src[], dest[], w, h, block;

		if (args.length != 1) {
			System.out.println("usage: java Example11 image_file");
			System.exit(-1);
		}

		final String fileName = args[0];
		File srcFile = new File(fileName);
        final BufferedImage source = ImageIO.read(srcFile);

		w = source.getWidth();
		h = source.getHeight();
		src = source.getRGB(0, 0, w, h, null, 0, w);
		dest = new int[src.length];

		block = (w * h) / Utils.MAXTHREADS;
		threads = new Example11[Utils.MAXTHREADS];

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int j = 1; j <= Utils.N; j++) {
			for (int i = 0; i < threads.length; i++) {
				if (i != threads.length - 1) {
					threads[i] = new Example11(src, dest, w, h,
										(i * block), ((i + 1) * block));
				} else {
					threads[i] = new Example11(src, dest, w, h,
										(i * block), (w * h));
				}
			}

			startTime = System.currentTimeMillis();
			for (int i = 0; i < threads.length; i++) {
				threads[i].start();
			}
			for (int i = 0; i < threads.length; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stopTime = System.currentTimeMillis();
			ms +=  (stopTime - startTime);
		}
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
		final BufferedImage destination = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		destination.setRGB(0, 0, w, h, dest, 0, w);

		/*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               ImageFrame.showImage("Original - " + fileName, source);
            }
        });

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               ImageFrame.showImage("Blur - " + fileName, destination);
            }
        });*/
		try {
			ImageIO.write(destination, "png", new File("grayscale.png"));
			System.out.println("Image was written succesfully.");
		} catch (IOException ioe) {
			System.out.println("Exception occured :" + ioe.getMessage());
		}
	}
}
