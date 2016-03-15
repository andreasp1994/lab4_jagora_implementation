package uk.ac.glasgow.jagora.impl;

import java.util.Set;

import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.StockExchange;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.Trader;

/**
 * Implements a trader with behaviours for satisfying trades, but never speaks
 * on the exchange to place buy or sell orders.
 * 
 * @author tws
 *
 */
public class DefaultTrader implements Trader {
	
	private String name;
	private Double cash;
	private Stock stock;
	private Integer quantity;

	/**
	 * Constructs a new instance of default trader with the specified cash and a
	 * single stock of the specified quantity.
	 * 
	 * @param name
	 * @param cash
	 * @param stock
	 * @param quantity
	 */
	public DefaultTrader(String name, Double cash, Stock stock, Integer quantity) {
		this.name = name;
		this.cash = cash;
		this.stock = stock;
		this.quantity = quantity;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getCash() {
		return this.cash;
	}

	@Override
	public void sellStock(Stock stock, Integer quantity, Double price)
			throws TradeException {
		if (stock != this.stock ) throw new TradeException("Stocks do not match");
		System.out.println("To remove:" + quantity);
		System.out.println("Current:" + this.quantity);
		this.quantity -= quantity;
		System.out.println("Current:" + this.quantity);
		this.cash += price*quantity;
		System.out.println("End of method");

	}

	@Override
	public void buyStock(Stock stock, Integer quantity, Double price)
			throws TradeException {
		if (stock != this.stock ) throw new TradeException("Stocks do not match");
		
		this.quantity += quantity;
		this.cash -= price*quantity;
	}

	@Override
	public Integer getInventoryHolding(Stock stock) {
		return quantity;
	}

	@Override
	public void speak(StockExchange stockExchange) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Stock> getTradingStocks() {
		// TODO Auto-generated method stub
		return null;
	}

}
