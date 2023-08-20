package org.acme.activity;

import java.time.Instant;

public class Activity {

	public static final String NAME = "activity";

	public String now;
	public String name;
	public long number;

	public Activity() {
	}

	public Activity(long i) {
		now = Instant.now().toString();
		name = (i % 2 == 0) ? "Up" : "Down";
		number = i = (i + 1);
	}
}
