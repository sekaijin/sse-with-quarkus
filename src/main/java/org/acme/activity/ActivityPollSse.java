package org.acme.activity;

import org.acme.sse.MemCache;
import org.acme.sse.SseRessource;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;

@Path("poll")
@ApplicationScoped
public class ActivityPollSse extends SseRessource<Activity>
{
   private static long lastId = 0;
   
   @Inject
   ActivityService service;
   
   public ActivityPollSse(@Context Sse sse){
      super(sse);
      setCache(new MemCache<>(5));
   }

   @Scheduled(every = "1s")
   public void sendNext() throws Exception{
      broadcast(service.next());
   }

   @Override
   public OutboundSseEvent buildEvent(Activity e){
      return eventBuilder.name("activity").id(Long.toString(lastId++)).mediaType(MediaType.APPLICATION_JSON_TYPE).data(e)
         .build();
   }
}
