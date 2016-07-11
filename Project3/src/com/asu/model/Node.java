package com.asu.model;

/**
 * The Class Node.
 */
public class Node {

	/** The page number. */
	long pageNumber;

	/** The prev. */
	Node prev;

	/** The next. */
	Node next;

	/**
	 * Instantiates a new node.
	 *
	 * @param pageNumber
	 *            the page number
	 */
	public Node(long pageNumber) {
		this.pageNumber = pageNumber;
	}
}
