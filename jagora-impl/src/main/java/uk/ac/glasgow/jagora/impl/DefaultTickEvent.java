package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.TickEvent;

public class DefaultTickEvent<T> implements TickEvent<T> {

	private T event;
	private Long tick;
	
	public DefaultTickEvent(T event, Long tick) {
		this.event = event;
		this.tick = tick;
	}

	@Override
	public int compareTo(TickEvent<T> tickEvent) {
		if (this.tick < tickEvent.getTick())
			return -1;
		else if (this.tick == tickEvent.getTick())
			return 0;
		return 1;
	}

	@Override
	public T getEvent() {
		return this.event;
	}

	@Override
	public Long getTick() {
		return this.tick;
	}
	
	public String toString(){
		//TODO
		return null;
	}
}
