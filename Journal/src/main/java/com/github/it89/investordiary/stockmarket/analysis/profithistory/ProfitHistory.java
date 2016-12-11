package com.github.it89.investordiary.stockmarket.analysis.profithistory;

import com.github.it89.investordiary.stockmarket.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Axel on 20.11.2016.
 */
public class ProfitHistory {
    private final TreeMap<LocalDate, ProfitHistoryItem> items = new TreeMap();
    private AssetPriceHistory assetPriceHistory;
    private BondNominalHistory bondNominalHistory;

    public ProfitHistory(AssetPriceHistory assetPriceHistory, BondNominalHistory bondNominalHistory) {
        this.assetPriceHistory = assetPriceHistory;
        this.bondNominalHistory = bondNominalHistory;
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
        expandByAssetPriceHistory();
        fillPaperProfit();
    }

    private void expandByAssetPriceHistory() {
        TreeMap<LocalDate, ProfitHistoryItem> newItems = new TreeMap();

        ProfitHistoryItem itemPrev = null;
        LocalDate datePrev = null;
        if(items.isEmpty())
            throw new NullPointerException("TreeMap ProfitHistoryItem is null");
        // Даты в промежутках
        for(Map.Entry<LocalDate, ProfitHistoryItem> entryDateItem : items.entrySet()) {
            if(itemPrev == null) {
                itemPrev = entryDateItem.getValue();
                datePrev = entryDateItem.getKey();
            } else {
                ProfitHistoryItem item = entryDateItem.getValue();
                LocalDate date = entryDateItem.getKey();

                TreeSet<LocalDate> localDates = assetPriceHistory.getDates(itemPrev.assetCount.keySet(), datePrev, date);
                for(LocalDate localDate : localDates) {
                    if(localDate.isAfter(datePrev) && localDate.isBefore(date)) {
                        ProfitHistoryItem newItem = itemPrev.copy();
                        newItems.put(localDate, newItem);
                    }
                }
                itemPrev = item;
                datePrev = date;
            }
        }

        // Оставшиеся даты
        TreeSet<LocalDate> localDates = assetPriceHistory.getDates(itemPrev.assetCount.keySet(), datePrev.plusDays(1), LocalDate.now());
        for(LocalDate date : localDates) {
            ProfitHistoryItem newItem = itemPrev.copy();
            newItems.put(date, newItem);
        }

        items.putAll(newItems);
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

    private void fillPaperProfit() {
        for(Map.Entry<LocalDate, ProfitHistoryItem> entryDateItem : items.entrySet()) {
            BigDecimal paperProfit = new BigDecimal(0);
            for(Map.Entry<Asset, Long> entryAssetCount : entryDateItem.getValue().assetCount.entrySet()) {
                BigDecimal price = assetPriceHistory.getPrice(entryAssetCount.getKey(), entryDateItem.getKey());
                if(entryAssetCount.getKey().getAssetType().isBond()) {
                    BigDecimal nominal = bondNominalHistory.getNominal(entryAssetCount.getKey(), entryDateItem.getKey());
                    price = price.divide(BigDecimal.valueOf(100)).multiply(nominal);
                }
                paperProfit = paperProfit.add(new BigDecimal(entryAssetCount.getValue()).multiply(price));
            }
            entryDateItem.getValue().setPaperProfit(paperProfit);
        }
    }

    public TreeMap<LocalDate, ProfitHistoryItem> getItems() {
        return items;
    }

    public void fill(StockMarketDaybook daybook, Asset asset, int stageNumber) {
        TreeSet<Trade> tradeSet = new TreeSet();
        tradeSet.addAll(daybook.getTradeStocks().values());
        tradeSet.addAll(daybook.getTradeBonds().values());
        tradeSet = Trade.filterTreeSetByAsset(tradeSet, asset);
        tradeSet = Trade.filterTreeSetByStageNumber(tradeSet, stageNumber);

        TreeSet<CashFlow> cashFlowSet = new TreeSet();
        cashFlowSet.addAll(daybook.getCashFlows());
        cashFlowSet = CashFlow.filterTreeSetByAsset(cashFlowSet, asset);
        cashFlowSet = CashFlow.filterTreeSetByStageNumber(cashFlowSet, stageNumber);

        if(tradeSet.isEmpty() && cashFlowSet.isEmpty())
            throw new NullPointerException("Test:" + asset.getCaption() + " [" + stageNumber + "]");
        fill(tradeSet, cashFlowSet);
    }
}
