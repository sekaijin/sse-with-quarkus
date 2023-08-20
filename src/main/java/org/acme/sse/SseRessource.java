package org.acme.sse;

import java.util.List;

import org.acme.eventbus.EventConsumer;
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

public abstract class SseRessource<E> extends EventConsumer<E> {

	protected static final Logger LOG = LoggerFactory.getLogger(SseRessource.class);
	protected Builder eventBuilder;
	protected SseBroadcaster sseBroadcaster;
	private static long lastId = 0;
	private List<OutboundSseEvent> events;
	
	public SseRessource(Sse sse, int cacheSize) {
		super();
		LOG.info("#init {}", this);

		eventBuilder = sse.newEventBuilder();
		sseBroadcaster = sse.newBroadcaster();
		sseBroadcaster.onClose(this::onClose);
		sseBroadcaster.onError(this::onError);
		events = new MemCache<>(cacheSize);

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
		sendPreviousEvents(sseEventSink, 0);

	}

	protected void consume(E e2) {
		try {
			OutboundSseEvent event = eventBuilder.name(address()).id(Long.toString(lastId++))
					.mediaType(MediaType.APPLICATION_JSON_TYPE).data(e2).build();
			events.add(event);

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

	private void sendPreviousEvents(SseEventSink sseEventSink, int lastEventId) {
		for (int i = lastEventId; i < events.size(); i++) {
			sseEventSink.send(events.get(i));
		}
	}


}