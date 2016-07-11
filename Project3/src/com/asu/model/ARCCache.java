package com.asu.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The Class ARCCache.
 */
public class ARCCache {

	/** The capacity. */
	private int capacity;

	/** The hits. */
	private static int hits;

	/** The misses. */
	private static int misses;

	/** The delta. */
	int delta = 0;

	/** The delta2. */
	int delta2 = 0;

	/** The t1. */
	LRU t1 = new LRU();

	/** The t2. */
	LRU t2 = new LRU();

	/** The b1. */
	LRU b1 = new LRU();

	/** The b2. */
	LRU b2 = new LRU();

	/** The p. */
	int p = 0;

	/** The file. */
	File file;

	/**
	 * Instantiates a new ARC cache.
	 *
	 * @param capacity
	 *            the capacity
	 * @param filePath
	 *            the file path
	 */
	public ARCCache(int capacity, String filePath) {
		this.capacity = capacity;
		file = new File(filePath);
	}

	/**
	 * Sets the.
	 *
	 * @param pageNumber
	 *            the page number
	 */
	public void set(long pageNumber) {
		if (t1.map.containsKey(pageNumber)) {
			Node old = t1.map.get(pageNumber);
			old.pageNumber = pageNumber;
			t1.map.remove(pageNumber);
			t1.remove(old);
			t2.setHead(old);
			t2.map.put(pageNumber, old);
			hits++;
		} else if (t2.map.containsKey(pageNumber)) {
			Node old = t2.map.get(pageNumber);
			old.pageNumber = pageNumber;
			t2.remove(old);
			t2.setHead(old);
			hits++;
		} else if (b1.map.containsKey(pageNumber)) {
			Node old = b1.map.get(pageNumber);
			if (b1.map.size() >= b2.map.size()) {
				delta = 1;
			} else {
				if (b1.map.size() != 0) {
					delta = b2.map.size() / b1.map.size();
				}
			}
			p = Math.min(p + delta, capacity);
			replace(pageNumber, p);
			b1.map.remove(pageNumber);
			b1.remove(old);
			t2.setHead(old);
			t2.map.put(pageNumber, old);
			misses++;
		} else if (b2.map.containsKey(pageNumber)) {
			Node old = b2.map.get(pageNumber);
			if (b2.map.size() >= b1.map.size()) {
				delta2 = 1;
			} else {
				if (b2.map.size() != 0) {
					delta2 = b1.map.size() / b2.map.size();
				}
			}
			p = Math.max(p - delta2, 0);
			replace(pageNumber, p);
			b2.map.remove(pageNumber);
			b2.remove(old);
			t2.setHead(old);
			t2.map.put(pageNumber, old);
			misses++;
		} else {
			Node newNode = new Node(pageNumber);
			if (t1.map.size() + b1.map.size() == capacity) {
				if (t1.map.size() < capacity) {
					b1.map.remove(b1.tail.pageNumber);
					b1.remove(b1.tail);
					replace(pageNumber, p);
				} else {
					t1.map.remove(t1.tail.pageNumber);
					t1.remove(t1.tail);
				}
			} else if (t1.map.size() + b1.map.size() < capacity) {
				if (t1.map.size() + b1.map.size() + t2.map.size()
						+ b2.map.size() >= capacity) {
					if (t1.map.size() + b1.map.size() + t2.map.size()
							+ b2.map.size() == 2 * capacity) {
						b2.map.remove(b2.tail.pageNumber);
						b2.remove(b2.tail);
					}
					replace(pageNumber, p);
				}
			}
			t1.setHead(newNode);
			t1.map.put(pageNumber, newNode);
			misses++;
		}
	}

	/**
	 * Replace.
	 *
	 * @param pageNumber
	 *            the page number
	 * @param p
	 *            the p
	 */
	private void replace(long pageNumber, int p) {
		if ((!t1.map.isEmpty())
				&& ((t1.map.size() > p) || (b2.map.containsKey(pageNumber) && (t1.map
						.size() == p)))) {
			Node node = t1.tail;
			long page = t1.tail.pageNumber;
			t1.map.remove(page);
			t1.remove(t1.tail);
			b1.setHead(node);
			b1.map.put(page, node);
		} else {
			Node node = t2.tail;
			long page = t2.tail.pageNumber;
			t2.map.remove(page);
			t2.remove(t2.tail);
			b2.setHead(node);
			b2.map.put(page, node);
		}
	}

	/**
	 * Gets the hit ratio.
	 *
	 * @return the hit ratio
	 */
	public double getHitRatio() {
		readFile();
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
