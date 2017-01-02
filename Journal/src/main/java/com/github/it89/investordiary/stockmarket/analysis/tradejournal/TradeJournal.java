package com.github.it89.investordiary.stockmarket.analysis.tradejournal;

import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.cashflow.CashFlowItem;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.TreeSet;

/**
 * Created by Axel on 12.11.2016.
 */
public class TradeJournal {
    private final TreeSet<TradeItem> tradeItems = new TreeSet(new TradeItem.CashFlowItemComp());
    private final TreeSet<CashFlowItem> cashFlowItems = new TreeSet(new CashFlowItem.CashFlowItemComp());

    public void add(Trade trade) {
        // Buy/Sell
        TradeItem tradeItem = new TradeItem(trade);
        tradeItems.add(tradeItem);
    }

    public void add(CashFlow cashFlow) {
        BigDecimal tax = null;
        Asset asset = null;
        Integer stageNumber = null;
        if(cashFlow instanceof AssetIncome) {
            tax = ((AssetIncome) cashFlow).getTax();
            asset = ((AssetIncome) cashFlow).getAsset();
            stageNumber = ((AssetIncome) cashFlow).getStageNumber();
        }

        CashFlowItem item = new CashFlowItem(cashFlow.getDate(), LocalTime.of(0, 0), cashFlow.getCashFlowType(), cashFlow.getVolume(), tax, asset, stageNumber);
        cashFlowItems.add(item);
    }

    @Override
    public String toString() {
        return "TradeJournal{" +
                "tradeItems=" + tradeItems +
                '}';
    }

    public TreeSet<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public TreeSet<CashFlowItem> getCashFlowItems() {
        return cashFlowItems;
    }

    public TradeJournal copyByAsset(Asset asset, Integer stageNumber) {
        TradeJournal newJournal = new TradeJournal();
        for(TradeItem tradeItem : tradeItems) {
            if(tradeItem.getAsset() == asset)
                if(stageNumber == null || stageNumber == tradeItem.getStageNumber())
                    newJournal.tradeItems.add(tradeItem);
        }
        for(CashFlowItem cashFlowItem : cashFlowItems) {
            if(cashFlowItem.getAsset() == asset)
                if(stageNumber == null || stageNumber == cashFlowItem.getStageNumber())
                    newJournal.cashFlowItems.add(cashFlowItem);
        }
        return newJournal;
    }
}
