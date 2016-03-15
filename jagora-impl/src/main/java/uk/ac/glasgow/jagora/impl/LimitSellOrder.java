package uk.ac.glasgow.jagora.impl;

import uk.ac.glasgow.jagora.Order;
import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

public class LimitSellOrder implements SellOrder {

	private Trader trader;
	private Stock stock;
	private Integer quantity;
	private Double price;
	
	public LimitSellOrder(Trader trader, Stock stock, Integer quantity, Double price) {
		this.trader = trader;
		this.stock = stock;
		this.quantity = quantity;
		this.price = price;
	}

	@Override
	public Double getPrice() {
		return this.price;
	}
	
	public String toString(){
		return "SellOrder of " + quantity + " " + stock + " at " + price + " by " + trader;
	}

	@Override
	public Trader getTrader() {
		return trader;
	}

	@Override
	public Stock getStock() {
		return stock;
	}

	@Override
	public Integer getRemainingQuantity() {
		return quantity;
	}

	@Override
	public void satisfyTrade(TickEvent<Trade> tradeEvent) throws TradeException {
		Trade trade = tradeEvent.getEvent();
		this.trader.sellStock(trade.getStock(), trade.getQuantity(), trade.getPrice());
		this.quantity -= trade.getQuantity();
	}

	@Override
	public void rollBackTrade(TickEvent<Trade> tradeEvent)
			throws TradeException {
		Trade trade = tradeEvent.getEvent();
		this.trader.buyStock(trade.getStock(), trade.getQuantity(), trade.getPrice());
		this.quantity +=trade.getQuantity();
	}

	@Override
	public int compareTo(SellOrder order) {
		if (this.price < order.getPrice())
			return -1;
		else if (this.price == order.getPrice())
			return 0;
		else
			return 1;
	}
	
	@Override
	public boolean equals(Object order){
		if (order instanceof LimitSellOrder){
			if ((((Order) order).getPrice() == null) && (this.price == null)) return true;
			return this.price.equals(((Order) order).getPrice());
		}	
		return false;
		
	}

}
