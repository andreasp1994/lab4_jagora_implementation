package uk.ac.glasgow.jagora.impl;


import java.util.ArrayList;
import java.util.List;

import uk.ac.glasgow.jagora.BuyOrder;
import uk.ac.glasgow.jagora.Market;
import uk.ac.glasgow.jagora.Order;
import uk.ac.glasgow.jagora.OrderBook;
import uk.ac.glasgow.jagora.SellOrder;
import uk.ac.glasgow.jagora.Stock;
import uk.ac.glasgow.jagora.TickEvent;
import uk.ac.glasgow.jagora.Trade;
import uk.ac.glasgow.jagora.TradeException;
import uk.ac.glasgow.jagora.World;

/**
 * Provides the behaviour of a continuous order driven market.
 * @author tws
 *
 */
public class ContinuousOrderDrivenMarket implements Market {
	
	private OrderBook<SellOrder> sellBook;
	private OrderBook<BuyOrder> buyBook;
	private Stock stock;
	private World world;
	
	/**
	 * Constructs a new continuous order driven market for the specified stock,
	 * synchronised to the tick events of the specified world.
	 * 
	 * @param stock
	 * @param world
	 */
	public ContinuousOrderDrivenMarket(Stock stock, World world) {
		this.sellBook = new DefaultOrderBook<SellOrder>(world);
		this.buyBook = new DefaultOrderBook<BuyOrder>(world);
		
		this.stock = stock;
		this.world = world;
	}

	@Override
	public Stock getStock() {
		return this.stock;
	}

	@Override
	public List<TickEvent<Trade>> doClearing() {
		List<TickEvent<Trade>> trades = new ArrayList<TickEvent<Trade>>();
		while (buyBook.getBestOrder().getPrice() >= sellBook.getBestOrder().getPrice()){
			
			
			
			Trade trade = new DefaultTrade(world, buyBook.getBestOrder(), sellBook.getBestOrder(),
							this.stock, Math.min(sellBook.getBestOrder().getRemainingQuantity(),buyBook.getBestOrder().getRemainingQuantity() ),
							Math.min(sellBook.getBestOrder().getPrice(), buyBook.getBestOrder().getPrice()));
			TickEvent<Trade> tickEvent = null;
			try {
				tickEvent = trade.execute();
			} catch (TradeException e2) {
				System.out.println("Exception..");
			}
			trades.add(tickEvent);
			try {
				
				buyBook.getBestOrder().satisfyTrade(tickEvent);
				sellBook.getBestOrder().satisfyTrade(tickEvent);
				
			} catch (TradeException e){
				
				try {
					buyBook.getBestOrder().rollBackTrade(tickEvent);
					sellBook.getBestOrder().rollBackTrade(tickEvent);
				} catch (TradeException e1) {
					System.out.println("Error applying trades... terminating..");
				}
			}
			
			if (buyBook.getBestOrder().getRemainingQuantity() == 0){
				buyBook.cancelOrder(buyBook.getBestOrder());
				sellBook.cancelOrder(sellBook.getBestOrder());
			}
		}
		return trades;		
	}

	@Override
	public void placeBuyOrder(BuyOrder buyOrder) {
		buyBook.recordOrder(buyOrder);
	}

	@Override
	public void placeSellOrder(SellOrder sellOrder) {
		sellBook.recordOrder(sellOrder);
	}

	@Override
	public void cancelBuyOrder(BuyOrder buyOrder) {
		buyBook.cancelOrder(buyOrder);
	}

	@Override
	public void cancelSellOrder(SellOrder sellOrder) {
		sellBook.cancelOrder(sellOrder);
	}

	@Override
	public Double getBestBid() {
		return buyBook.getBestOrder().getPrice();
	}

	@Override
	public Double getBestOffer() {
		//TODO
		return sellBook.getBestOrder().getPrice();
	}
	
	@Override
	public String toString(){
		//TODO
		return null;
	}
}
