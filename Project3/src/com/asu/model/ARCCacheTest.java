package com.asu.model;

/**
 * The Class ARCCacheTest.
 */
public class ARCCacheTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ARCCache cache = new ARCCache(1024, "F:/Study/MS/CEN502/Projects/project3/P4.lis");
		double val = cache.getHitRatio();
		System.out.println(val);
	}
}
