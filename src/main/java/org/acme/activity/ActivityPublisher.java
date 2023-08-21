package org.acme.activity;

import org.acme.eventbus.EventPublisher;

import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ActivityPublisher extends EventPublisher<Activity> {

   @Inject
   ActivityService service;

	public ActivityPublisher() {
		super(Activity.class);
	}

	@Override
	public String address() {
		return Activity.NAME;
	}

   @Scheduled(every = "1s")
   public void sendNext() throws Exception{
      publish(service.next());
   }

}
