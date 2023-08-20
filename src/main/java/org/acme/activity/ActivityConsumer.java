package org.acme.activity;

import org.acme.eventbus.EventConsumer;

import jakarta.inject.Singleton;

@Singleton
public class ActivityConsumer extends EventConsumer<Activity> {

	@Override
	public String address() {
		return Activity.NAME;
	}

}
