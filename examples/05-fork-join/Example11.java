// =================================================================
//
// File: Example11.java
// Author(s): Sandra Tello A01703658 Isaac Planter A01702962
// Speeddup: 0.662162162
// Description: This file implements the code that transforms a
//				grayscale image using Java's Fork-Join.
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
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class Example11 extends RecursiveAction {

	private static final int MIN = 15_000;
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

	protected void computeDirectly() {
		int index, ren, col;

		for (index = start; index < end; index++) {
			ren = index / width;
			col = index % width;
			greyPixel(ren, col);
		}
	}

	@Override
	protected void compute() {
		if ( (end - start) <= MIN ) {
			computeDirectly();
		} else {
			int mid = start + ((end - start) / 2);
			invokeAll(new Example11(src, dest, width, height, start, mid),
					  new Example11(src, dest, width, height, mid, end));
		}
	}

	public static void main(String args[]) throws Exception {
		long startTime, stopTime;
		double ms;
		int src[], dest[], w, h;
		ForkJoinPool pool;

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

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for (int i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			pool = new ForkJoinPool(Utils.MAXTHREADS);
			pool.invoke(new Example11(src, dest, w, h, 0, (w * h)));

			stopTime = System.currentTimeMillis();
			ms += (stopTime - startTime);
		}

		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
		final BufferedImage destination = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		destination.setRGB(0, 0, w, h, dest, 0, w);

		

		try {
			ImageIO.write(destination, "png", new File("grey.png"));
			System.out.println("Image was written succesfully.");
		} catch (IOException ioe) {
			System.out.println("Exception occured :" + ioe.getMessage());
		}
	}
	// place your code here
}
