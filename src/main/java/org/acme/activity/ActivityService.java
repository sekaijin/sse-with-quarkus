package org.acme.activity;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ActivityService {

	static long lasId;

	@Inject
	ActivityPublisher publisher;

	public Activity next() {
		return new Activity(lasId++);
	}

	@Scheduled(every = "1s")
	public void sendNext() throws Exception {
		publisher.publish(next());
	}

}
