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
		bus.registerDefaultCodec(type, new Codec<E>() {

			@Override
			public String name() {
				return type.getName();
			}
		});
	}

	@PreDestroy
	public void releaseBus() {
		bus.unregisterCodec(type.getName());
	}

	/**
	 * publish e element on vert.x event bus.
	 * @param e
	 * @throws Exception
	 */
	protected void publish(E e) throws Exception {
		bus.publish(address(), e);
	}


	/**
	 * EventBus address name
	 * @return
	 */
	public abstract String address();
}