package org.acme.activity;

import org.acme.sse.MemCache;
import org.acme.sse.SseRessource;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;

@Path(Activity.NAME)
@ApplicationScoped
public class ActivityEventBusSse extends SseRessource<Activity> {
   private static long lastId = 0;

   @Inject
   ActivityConsumer eventConsumer;

	public ActivityEventBusSse(@Context Sse sse) {
		super(sse);
	}

   @PostConstruct
   public void initConsumer() {
      setCache(new MemCache<>(5));
      eventConsumer.onEvent(this::broadcast);
   }

   @Override
   public OutboundSseEvent buildEvent(Activity e) {
      return getEventBuilder().name("activity").id(Long.toString(lastId++))
      .mediaType(MediaType.APPLICATION_JSON_TYPE).data(e).build();
   }
}
