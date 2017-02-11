package com.github.it89.investordiary.stockmarket.analysis.stockportfolio;

import com.github.it89.investordiary.stockmarket.Trade;
import com.github.it89.investordiary.stockmarket.TradeOperation;

import java.time.LocalDate;
import java.util.TreeSet;

/**
 * Created by Axel on 29.01.2017.
 */
public class StockPortfolioJournal {
    private final TreeSet<StockPortfolioItem> items = new TreeSet<>();

    public void add(Trade trade) {
        long amount = trade.getAmount();
        if(trade.getOperation() == TradeOperation.SELL)
            amount = -amount;
        StockPortfolioItem item = new StockPortfolioItem(trade.getAsset(), trade.getStageNumber(), trade.getDate(), amount);
        items.add(item);
    }

    public void addTrades(TreeSet<Trade> trades) {
        for(Trade trade : trades)
            add(trade);
    }

    public long getAmountSum(LocalDate dateFrom, LocalDate dateTo) {
        long amount = 0;
        for(StockPortfolioItem item : items) {
            if(item.getDate().compareTo(dateFrom) >= 0 && item.getDate().compareTo(dateTo) <= 0) {
                amount += item.getAmount();
            }
        }
        return amount;
    }
}
