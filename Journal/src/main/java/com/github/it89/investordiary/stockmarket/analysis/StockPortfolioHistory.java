package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Отражает открытые позиции по датам
 */
@Deprecated
public class StockPortfolioHistory {
    private final StockAnalytics analytics;
    private final HashMap<Asset, TreeMap<LocalDate, Long>> mapADC = new HashMap<>();

    public StockPortfolioHistory(StockAnalytics analytics) {
        this.analytics = analytics;
    }

    public void fill(StockMarketDaybook daybook) {
        TreeSet<Trade> trades = new TreeSet<>();
        trades.addAll(daybook.getTradeBonds().values());
        trades.addAll(daybook.getTradeStocks().values());

        Asset asset = analytics.getAsset();
        if(asset != null)
            trades = Trade.filterTreeSetByAsset(trades, asset);
        Integer stageNumber = analytics.getStageNumber();
        if(stageNumber != null)
            trades = Trade.filterTreeSetByStageNumber(trades, stageNumber);

        // TODO: Добавить операцию trade в случае погашения облигации

        fill(trades);
    }

    private void fill(TreeSet<Trade> trades) {
        mapADC.clear();
        long count;
        for(Trade trade : trades) {
            if(trade.getOperation() == TradeOperation.BUY)
                count = trade.getAmount();
            else
                count = -trade.getAmount();

            Asset asset = trade.getAsset();
            if(mapADC.containsKey(asset)) {
                TreeMap<LocalDate, Long> mapDC = mapADC.get(asset);
                if(mapDC.containsKey(trade.getDate()))
                    count += mapDC.get(trade.getDate());
                else
                    count += mapDC.lastEntry().getValue();
                mapDC.put(trade.getDate(), count);
            } else {
                TreeMap<LocalDate, Long> mapDC = new TreeMap<>();
                mapDC.put(trade.getDate(), count);
                mapADC.put(asset, mapDC);
            }
        }
    }
}
