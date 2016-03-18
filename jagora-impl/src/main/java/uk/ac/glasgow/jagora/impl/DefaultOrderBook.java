package uk.ac.glasgow.jagora.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import uk.ac.glasgow.jagora.Order;
import uk.ac.glasgow.jagora.OrderBook;
import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.World;

/**
 * Provides the default implementation of an order book for sorting buy and sell orders.
 * @author tws
 */
public class DefaultOrderBook<O extends Order & Comparable<O>> implements OrderBook<O> {

	private final Queue<TickEvent<O>> backing;
	private World world;
	
	/**
	 * Constructs a new instance of the order book synchronized to the ticks of the specified world.
	 * @param world
	 */
	public DefaultOrderBook(World world) {
		this.backing = new PriorityQueue<TickEvent<O>>(new OrderBookComparator());
		this.world = world;
	}
	
	@Override
	public void recordOrder(O order) {
		TickEvent<O> tickEvent = new DefaultTickEvent<O>(order, System.currentTimeMillis());
		backing.add(tickEvent);
	}

	@Override
	public void cancelOrder(O order) {
		for (TickEvent<O> tickEvent : backing) {
			Order o = tickEvent.getEvent();
			if (o.equals(order)) {
				backing.remove(tickEvent);
				return;
			}
		}
	}

	@Override
	public O getBestOrder() {
		TickEvent<O> order = backing.peek();
		if (order != null)
			return order.getEvent();
		return null;
	}

	@Override
	public List<TickEvent<O>> getOrdersAsList() {
		return new ArrayList<TickEvent<O>>(backing);
	}
	
	private class OrderBookComparator implements Comparator<TickEvent<O>> {
		@Override
		public int compare(TickEvent<O> tickEvent1, TickEvent<O> tickEvent2) {
			if (tickEvent1.getEvent() instanceof SellOrder){
				if (tickEvent1.getEvent().getPrice() < tickEvent2.getEvent().getPrice())
					return -1;
				else if (tickEvent1.getEvent().getPrice() == tickEvent2.getEvent().getPrice())
					return 0;
				else
					return 1;
			} else {
				if (tickEvent1.getEvent().getPrice() < tickEvent2.getEvent().getPrice())
					return 1;
				else if (tickEvent1.getEvent().getPrice() == tickEvent2.getEvent().getPrice())
					return 0;
				else
					return -1;
			}
		}
	}
	
	@Override
	public String toString (){
		// TODO
		return null;
	}

}
