package com.github.it89.investordiary.stockmarket;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Axel on 17.09.2016.
 */
public class StockMarketDaybook {
    private final TreeMap<String, Asset> assets = new TreeMap();
    private final TreeMap<String,TradeStock> tradeStocks = new TreeMap();
    private final TreeMap<String, TradeBond> tradeBonds = new TreeMap();
    private final HashSet<CashFlow> cashFlows = new HashSet();

    public TreeMap<Asset, TreeSet<Integer>> getAssetStageCombinations() {
        TreeMap<Asset, TreeSet<Integer>> result = new TreeMap<Asset, TreeSet<Integer>>();
        for(Asset asset : assets.values()) {
            TreeSet<Trade> trades = new TreeSet<Trade>();
            if (asset.getAssetType().isBond())
                trades.addAll(tradeBonds.values());
            else
                trades.addAll(tradeStocks.values());
            trades = Trade.filterTreeSetByAsset(trades, asset);
            TreeSet<Integer> stages = new TreeSet();
            for(Trade trade : trades)
                stages.add(trade.getStageNumber());
            if(!stages.isEmpty())
                result.put(asset, stages);
        }
        return result;
    }

    public TreeMap<String, Asset> getAssets() {
        return assets;
    }

    public TreeMap<String, TradeStock> getTradeStocks() {
        return tradeStocks;
    }

    public TreeMap<String, TradeBond> getTradeBonds() {
        return tradeBonds;
    }

    public Asset getAsset(String ticker) {
        return assets.get(ticker);
    }

    public void addAsset(Asset asset) {
        assets.put(asset.getTicker(), asset);
    }

    public void addTradeStock(TradeStock tradeStock) {
        tradeStocks.put(tradeStock.getTradeNumber(), tradeStock);
    }

    public void addTradeBond(TradeBond tradeBond) {
        tradeBonds.put(tradeBond.getTradeNumber(), tradeBond);
    }

    public HashSet<CashFlow> getCashFlows() {
        return cashFlows;
    }

    public void addCashFlow(CashFlow cashFlow) {
        cashFlows.add(cashFlow);
    }

    @Override
    public String toString() {
        return "StockMarketDaybook{" +
                "assets=" + assets +
                ", tradeStocks=" + tradeStocks +
                ", tradeBonds=" + tradeBonds +
                ", cashFlows=" + cashFlows +
                '}';
    }

    public LocalTime getTimeCloseDay() {
        return LocalTime.of(19, 0);
    }
}
