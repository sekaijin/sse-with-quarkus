package org.acme.sse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.OutboundSseEvent.Builder;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

public abstract class SseRessource<E> {

	protected static final Logger LOG = LoggerFactory.getLogger(SseRessource.class);
	protected Builder eventBuilder;
	protected SseBroadcaster sseBroadcaster;
	private Optional<List<OutboundSseEvent>> cache;

	public SseRessource(Sse sse) {
		super();
		LOG.info("#init {}", this);

		eventBuilder = sse.newEventBuilder();
		sseBroadcaster = sse.newBroadcaster();
		sseBroadcaster.onClose(this::onClose);
		sseBroadcaster.onError(this::onError);
		cache = Optional.ofNullable(null);
	}

	protected void setCache(List<OutboundSseEvent> c){
	   cache = Optional.ofNullable(c);
   }

   public SseRessource() {
		super();
	}

	@GET
	@Path("subscribe")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void onSubscribe(@Context SseEventSink sseEventSink) {
		LOG.info("#onSubscribe {} {}", sseEventSink.hashCode(), this);
		sseBroadcaster.register(sseEventSink);
		sendPreviousEvents(sseEventSink);

	}
	
   public OutboundSseEvent buildEvent(E e) {
      return getEventBuilder().name(getClass().getSimpleName()).id(UUID.randomUUID().toString())
         .mediaType(MediaType.TEXT_PLAIN_TYPE).data(e).build();
   }

	protected Builder getEventBuilder(){
      return eventBuilder;
   }

   protected void broadcast(E e2) {
		try {
			OutboundSseEvent event = buildEvent(e2);
			   
			cache.ifPresent(c -> c.add(event));

			sseBroadcaster.broadcast(event);
		} catch (Exception e) {
			LOG.warn("{}", e.getMessage());
		}
	}

	protected void onClose(SseEventSink sseEventSink) {
		LOG.info("#onClose {}", sseEventSink.hashCode());
	}

	protected void onError(SseEventSink sseEventSink, Throwable t) {
		if (!(t instanceof IllegalStateException)) {// ignore sink closed
			LOG.info("#onError {}", sseEventSink.hashCode(), t);
		}
	}

	private void sendPreviousEvents(SseEventSink sseEventSink) {
		cache.ifPresent( c -> c.forEach(event -> {
			sseEventSink.send(event);
		}));
	}

}