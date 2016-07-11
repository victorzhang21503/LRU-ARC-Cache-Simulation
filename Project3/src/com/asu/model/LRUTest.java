package com.asu.model;

/**
 * The Class LRUTest.
 */
public class LRUTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		LRUCache cache = new LRUCache(1024, "F:/Study/MS/CEN502/Projects/project3/P4.lis");
		double val = cache.getHitRatio();
		System.out.println(val);
	}

}
