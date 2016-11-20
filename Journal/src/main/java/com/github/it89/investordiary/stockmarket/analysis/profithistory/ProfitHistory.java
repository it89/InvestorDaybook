package com.github.it89.investordiary.stockmarket.analysis.profithistory;

import com.github.it89.investordiary.stockmarket.Asset;
import com.github.it89.investordiary.stockmarket.AssetPriceHistory;
import com.github.it89.investordiary.stockmarket.CashFlow;
import com.github.it89.investordiary.stockmarket.Trade;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Axel on 20.11.2016.
 */
public class ProfitHistory {
    private final TreeMap<LocalDate, ProfitHistoryItem> items = new TreeMap();
    private AssetPriceHistory assetPriceHistory;

    public ProfitHistory(AssetPriceHistory assetPriceHistory) {
        this.assetPriceHistory = assetPriceHistory;
    }

    public void fill(TreeSet<Trade> trades, TreeSet<CashFlow> cashFlows) {
        Iterator<Trade> tradeIterator = trades.iterator();
        Iterator<CashFlow> cashFlowIterator = cashFlows.iterator();
        Trade trade = null;
        CashFlow cashFlow = null;
        ProfitHistoryItem currentItem = null;

        if(tradeIterator.hasNext())
            trade = tradeIterator.next();
        if(cashFlowIterator.hasNext())
            cashFlow = cashFlowIterator.next();

        while(trade != null || cashFlow != null) {
            if(trade != null && cashFlow != null) {
                if(cashFlow.getDate().compareTo(trade.getDate()) <= 0) {
                    //System.out.println(cashFlow);
                    currentItem = add(currentItem, cashFlow);
                    cashFlow = null;
                    if(cashFlowIterator.hasNext())
                        cashFlow = cashFlowIterator.next();
                } else {
                    //System.out.println(trade);
                    currentItem = add(currentItem, trade);
                    trade = null;
                    if(tradeIterator.hasNext())
                        trade = tradeIterator.next();
                }
            } else if(cashFlow == null) {
                do {
                    //System.out.println(trade);
                    currentItem = add(currentItem, trade);
                    trade = null;
                    if(tradeIterator.hasNext())
                        trade = tradeIterator.next();
                } while(trade != null);
            } else if(trade == null) {
                do {
                    //System.out.println(cashFlow);
                    currentItem = add(currentItem, cashFlow);
                    cashFlow = null;
                    if (cashFlowIterator.hasNext())
                        cashFlow = cashFlowIterator.next();
                } while (trade != null);
            }
        }
    }

    private ProfitHistoryItem add(ProfitHistoryItem item, Trade trade) {
        ProfitHistoryItem newItem;
        if(items.containsKey(trade.getDate())) {
            newItem = items.get(trade.getDate());
            newItem = newItem.add(trade);
            items.put(trade.getDate(), newItem);
        } else {
            if(item != null)
                newItem = item.add(trade);
            else
                newItem = new ProfitHistoryItem(trade);
            items.put(trade.getDate(), newItem);
        }
        return newItem;
    }

    private ProfitHistoryItem add(ProfitHistoryItem item, CashFlow cashFlow) {
        ProfitHistoryItem newItem;
        if(items.containsKey(cashFlow.getDate())) {
            newItem = items.get(cashFlow.getDate());
            newItem = newItem.add(cashFlow);
            items.put(cashFlow.getDate(), newItem);
        } else {
            if(item != null)
                newItem = item.add(cashFlow);
            else
                newItem = new ProfitHistoryItem(cashFlow);
            items.put(cashFlow.getDate(), newItem);
        }
        return newItem;
    }

    public TreeMap<LocalDate, ProfitHistoryItem> getItems() {
        return items;
    }
}
