// =================================================================
//
// File: Example11.java
// Author(s): Sandra Tello A01703658 Isaac Planter A01702962
// Description: This file implements the code that transforms a
//				grayscale image. The time this implementation takes will
//				be used as the basis to calculate the improvement obtained
// 				with parallel technologies.
//Usage: java Example11 ../images/lenna.png
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


public class Example11 {
	private int src[], dest[], width, height;

	public Example11(int src[], int dest[], int width, int height) {
		this.src = src;
		this.dest = dest;
		this.width = width;
		this.height = height;
	}

	// palce your code here
	private void grayPixel(int ren, int col) {
		int i, j, pixel, dpixel;;
		float r, g, b, aux;
		
		pixel = src[(ren * width) + col];
		r = (float) ((pixel & 0x00ff0000) >> 16);
		g = (float) ((pixel & 0x0000ff00) >> 8);
		b = (float) ((pixel & 0x000000ff) >> 0);
		aux = (r+g+b)/3;

		dpixel = (0xff000000)
				| (((int) (aux)) << 16)
				| (((int) (aux)) << 8)
				| (((int) (aux)) << 0);
		dest[(ren * width) + col] = dpixel;
	}

	void doTask() {
		int index, size;
		int ren, col;

		size = width * height;
		for (index = 0; index < size; index++) {
			ren = index / width;
			col = index % width;
			grayPixel(ren, col);
		}
		// place your code here.
	}

	public static void main(String args[]) throws Exception {
		long startTime, stopTime;
		double ms;

		if (args.length != 1) {
			System.out.println("usage: java Example11 image_file");
			System.exit(-1);
		}

		final String fileName = args[0];
		File srcFile = new File(fileName);
        final BufferedImage source = ImageIO.read(srcFile);

		int w = source.getWidth();
		int h = source.getHeight();
		int src[] = source.getRGB(0, 0, w, h, null, 0, w);
		int dest[] = new int[src.length];

		System.out.printf("Starting...\n");
		ms = 0;
		Example11 e = new Example11(src, dest, w, h);
		for (int i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			e.doTask();

			stopTime = System.currentTimeMillis();

			ms += (stopTime - startTime);
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
               ImageFrame.showImage("Gray - " + fileName, destination);
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
