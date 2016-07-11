package com.asu.model;

import java.util.HashMap;

/**
 * The Class LRU.
 */
public class LRU {

	/** The map. */
	HashMap<Long, Node> map = new HashMap<Long, Node>();

	/** The head. */
	Node head;

	/** The tail. */
	Node tail;

	/**
	 * Removes the.
	 *
	 * @param node
	 *            the node
	 */
	public void remove(Node node) {
		if (node.prev != null) {
			node.prev.next = node.next;
		} else {
			head = node.next;
		}
		if (node.next != null) {
			node.next.prev = node.prev;
		} else {
			tail = node.prev;
		}
	}

	/**
	 * Sets the head.
	 *
	 * @param node
	 *            the new head
	 */
	public void setHead(Node node) {
		node.next = head;
		node.prev = null;
		if (head != null) {
			head.prev = node;
		}
		head = node;
		if (tail == null) {
			tail = head;
		}
	}
}
