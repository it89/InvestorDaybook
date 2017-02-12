package com.github.it89.investordiary.stockmarket.analysis.stockportfolio;

import com.github.it89.investordiary.stockmarket.Asset;
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

    public long getAmountSum(Asset asset, int stageNumber, LocalDate dateFrom, LocalDate dateTo) {
        long amount = 0;
        for(StockPortfolioItem item : items) {
            if(item.getDate().compareTo(dateFrom) >= 0
                    && item.getDate().compareTo(dateTo) <= 0
                    && item.getAsset() == asset
                    && item.getStageNumber() == stageNumber) {
                amount += item.getAmount();
            }
        }
        return amount;
    }

    public LocalDate getMinDate(Asset asset, int stageNumber) {
        for(StockPortfolioItem item : items) {
            if(item.getAsset() == asset && item.getStageNumber() == stageNumber) {
                return item.getDate();
            }
        }
        return LocalDate.MAX;
    }

    public LocalDate getMaxDate(Asset asset, int stageNumber) {
        LocalDate maxDate = LocalDate.MIN;
        for(StockPortfolioItem item : items) {
            if(item.getAsset() == asset && item.getStageNumber() == stageNumber)
                maxDate = item.getDate();
            else if(maxDate != LocalDate.MIN)
                break;
        }
        return maxDate;
    }
}
