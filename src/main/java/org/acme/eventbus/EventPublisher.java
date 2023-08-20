package org.acme.eventbus;

import io.vertx.core.eventbus.EventBus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public abstract class EventPublisher<E> {

	private Class<E> type;

	@Inject
	EventBus bus;

	public EventPublisher(Class<E> clazz) {
		this.type = clazz;
	}

	@PostConstruct
	public void initBus() {
		try {
			bus.registerDefaultCodec(type, new Codec<E>() {

				@Override
				public String name() {
					return type.getName();
				}
			});
		} catch (IllegalStateException e) {
			return; // ingore Already a default codec registered
		}
	}

	@PreDestroy
	public void releaseBus() {
		bus.unregisterDefaultCodec(type);
	}

	/**
	 * publish e element on vert.x event bus.
	 * 
	 * @param e
	 * @throws Exception
	 */
	public void publish(E e) throws Exception {
		bus.publish(address(), e);
	}

	/**
	 * EventBus address name
	 * 
	 * @return
	 */
	public abstract String address();
}