package org.acme.sse;

import java.util.ArrayList;

public class MemCache<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;
	private int length;

	public MemCache (int length) {
		super(length);
		this.length=length;
	}
	
	@Override
	public boolean add(E e) {
		if (length == size()) {
			remove(0);
		}
		return super.add(e);
	}
}
