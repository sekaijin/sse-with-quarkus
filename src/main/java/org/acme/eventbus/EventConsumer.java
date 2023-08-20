package org.acme.eventbus;

import io.quarkus.logging.Log;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public abstract class EventConsumer<E> {

	@Inject
	EventBus bus;
	private MessageConsumer<E> consumer;

	public EventConsumer() {
		super();
	}

	@PostConstruct
	public void initConsumer() {
		consumer = bus.localConsumer(address(), this::consume);
	}

	@PreDestroy
	public void releaseConsumer() {
		consumer.unregister().andThen(a -> {});
	}

	public void consume(Message<E> msg) {
		try {
			consume(msg.body());
		} catch (Exception e) {
			Log.error("Invalid data {}", msg.body(), e);
		}
	}

	/**
	 * consume an E element from vert.x event bus
	 * @param body
	 */
	protected abstract void consume(E body);

	/**
	 * EventBus address name
	 * @return
	 */
	protected abstract String address();

}