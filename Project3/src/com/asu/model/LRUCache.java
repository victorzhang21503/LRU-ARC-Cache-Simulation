package com.asu.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * The Class LRUCache.
 */
public class LRUCache {

	/** The capacity. */
	private int capacity;

	/** The hits. */
	private static int hits;

	/** The misses. */
	private static int misses;

	/** The map. */
	HashMap<Long, Node> map = new HashMap<Long, Node>();

	/** The file. */
	File file;

	/** The head. */
	Node head;

	/** The tail. */
	Node tail;

	/**
	 * Instantiates a new LRU cache.
	 *
	 * @param capacity
	 *            the capacity
	 * @param filePath
	 *            the file path
	 */
	public LRUCache(int capacity, String filePath) {
		this.capacity = capacity;
		file = new File(filePath);
	}

	/**
	 * Gets the.
	 *
	 * @param key
	 *            the key
	 * @return the long
	 */
	public long get(int key) {
		if (map.containsKey(key)) {
			Node node = map.get(key);
			remove(node);
			setHead(node);
			return node.pageNumber;
		}
		return -1;
	}

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

	/**
	 * Sets the.
	 *
	 * @param pageNumber
	 *            the page number
	 */
	public void set(long pageNumber) {
		if (map.containsKey(pageNumber)) {
			Node old = map.get(pageNumber);
			old.pageNumber = pageNumber;
			remove(old);
			setHead(old);
			hits++;
		} else {
			Node newNode = new Node(pageNumber);
			if ((map.size()) > capacity) {
				map.remove(tail.pageNumber);
				remove(tail);
				setHead(newNode);
			} else {
				setHead(newNode);
			}
			map.put(pageNumber, newNode);
			misses++;
		}
	}

	/**
	 * Gets the hit ratio.
	 *
	 * @return the hit ratio
	 */
	public double getHitRatio() {
		readFile();
		System.out.println(hits);
		System.out.println(misses);
		return ((double) hits / (hits + misses) * 100);
	}

	/**
	 * Read file.
	 */
	private void readFile() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				String[] arr = currentLine.split(" ");
				long pageNum = Long.parseLong(arr[0]);
				int totalPages = Integer.parseInt(arr[1]);
				for (int i = 0; i < totalPages; i++) {
					set(pageNum);
					pageNum = pageNum + 1;
				}
			}
		} catch (IOException exp) {
			System.out.println("Error while reading file." + exp);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
