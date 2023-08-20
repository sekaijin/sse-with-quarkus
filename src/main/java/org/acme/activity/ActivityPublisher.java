package org.acme.activity;

import org.acme.eventbus.EventPublisher;

import jakarta.inject.Singleton;

@Singleton
public class ActivityPublisher extends EventPublisher<Activity> {

	public ActivityPublisher() {
		super(Activity.class);
	}

	@Override
	public String address() {
		return Activity.NAME;
	}

}
