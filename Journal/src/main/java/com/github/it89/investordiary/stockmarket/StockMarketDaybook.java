package com.github.it89.investordiary.stockmarket;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Axel on 17.09.2016.
 */
public class StockMarketDaybook {
    private final TreeMap<String, Asset> assets = new TreeMap();
    private final TreeMap<String,TradeStock> tradeStocks = new TreeMap();
    private final TreeMap<String, TradeBond> tradeBonds = new TreeMap();

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
}
