// =================================================================
//
// File: Example7.java
// Author(s):
// Description: This file contains the code to brute-force all
//				prime numbers less than MAXIMUM. The time this
//				implementation takes will be used as the basis to
//				calculate the improvement obtained with parallel
//				technologies.
//
// Copyright (c) 2020 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================
import java.lang.Math;

public class Example7 {
	private static final int SIZE = 1_000_000;
	private boolean array[], Bandera;
	private double RaizCuadrada;


	public Example7(boolean array[]) {
		this.array = array;
	}

	// place yout code here

	public void calculate() {
		RaizCuadrada = 0;
		for(int i=2; i<array.length; i++){
			RaizCuadrada = Math.sqrt(i);
			Bandera = false;
			for(int j=2; j<=RaizCuadrada; j++){
				if(i%j==0){
					//No es primo
					Bandera = true;
					break;
				}
			}
			if(Bandera==false){
				array[i]=true;
			}
		}
		// place yout code here
	}

	

	public static void main(String args[]) {
		boolean array[] = new boolean[SIZE + 1];
		long startTime, stopTime;
		double acum = 0;

		System.out.println("At first, neither is a prime. We will display to TOP_VALUE:");
		for (int i = 2; i < Utils.TOP_VALUE; i++) {
			array[i] = false;
			System.out.print("" + i + ", ");
		}
		System.out.println("");

		// Create the object here.
		Example7 objeto = new Example7(array);

		acum = 0;
		System.out.printf("Starting...\n");
		for (int i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			// Call yout method here.
			objeto.calculate();

			stopTime = System.currentTimeMillis();

			acum += (stopTime - startTime);
		}
		System.out.println("Expanding the numbers that are prime to TOP_VALUE:");
		for (int i = 2; i < Utils.TOP_VALUE; i++) {
			if (array[i]) {
				System.out.print("" + i + ", ");
			}
		}
		System.out.println("");
		System.out.printf("avg time = %.5f ms\n", (acum / Utils.N));
	}
}
