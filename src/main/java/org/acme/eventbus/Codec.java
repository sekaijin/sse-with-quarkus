package org.acme.eventbus;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * Warning: only works with a localConsumer
 * 
 * @param <E>
 */
public abstract class Codec<E> implements MessageCodec<E, E> {

	@Override
	public void encodeToWire(Buffer buffer, E s) {
		return;
	}

	@Override
	public E decodeFromWire(int pos, Buffer buffer) {
		return null;
	}

	@Override
	public E transform(E s) {
		return s;
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}
}