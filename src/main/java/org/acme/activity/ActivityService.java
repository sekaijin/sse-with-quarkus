package org.acme.activity;

import org.acme.eventbus.EventPublisher;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ActivityService extends EventPublisher<Activity> {

	static long lasId;
	
	public static final String NAME = "activity";

	public ActivityService() {
		super(Activity.class);
	}

	public Activity next() {
		return new Activity(lasId++);
	}

	@Override
	public String address() {
		return NAME;
	}
	
    @Scheduled(every = "1s")
    public void sendNext() throws Exception {
    	publish(next());
    }

}
