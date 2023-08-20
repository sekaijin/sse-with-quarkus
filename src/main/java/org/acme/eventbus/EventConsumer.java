package org.acme.eventbus;

import java.util.function.Consumer;

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
	private Consumer<E> handler;

	public EventConsumer() {
		super();
	}

	@PostConstruct
	public void initConsumer() {
		consumer = bus.localConsumer(address(), this::consume);
	}

	@PreDestroy
	public void releaseConsumer() {
		consumer.unregister().andThen(a -> {
		});
	}

	public void consume(Message<E> msg) {
		try {
			// consume(msg.body());
			handler.accept(msg.body());
		} catch (Exception e) {
			Log.error("Invalid data {}", msg.body(), e);
		}
	}

	/**
	 * EventBus address name
	 * 
	 * @return
	 */
	public abstract String address();

	public void onEvent(Consumer<E> handler) {
		this.handler = handler;
	}

}