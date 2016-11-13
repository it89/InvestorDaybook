package com.github.it89.investordiary.stockmarket.analysis.tradejournal;

import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.CashFlowItem;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created by Axel on 12.11.2016.
 */
public class TradeJournal {
    private final TreeSet<TradeItem> tradeItems = new TreeSet(new TradeItem.CashFlowItemComp());
    private final TreeSet<CashFlowItem> cashFlowItems = new TreeSet(new CashFlowItem.CashFlowItemComp());

    public void add(Trade trade) {
        HashSet<TradeTag> tags = new HashSet();
        tags.addAll(trade.getTradeTags().values());
        tags.addAll(trade.getAsset().getTradeTags().values());

        // Buy/Sell
        TradeItem tradeItem = new TradeItem(trade);
        tradeItem.addTags(tags);
        tradeItems.add(tradeItem);
    }

    public void add(CashFlow cashFlow) {
        BigDecimal tax = null;
        Asset asset = null;
        if(cashFlow instanceof AssetIncome) {
            tax = ((AssetIncome) cashFlow).getTax();
            asset = ((AssetIncome) cashFlow).getAsset();
        }
        HashSet<TradeTag> tags = new HashSet();
        tags.addAll(cashFlow.getTradeTags().values());
        if(asset != null)
            tags.addAll(asset.getTradeTags().values());

        CashFlowItem item = new CashFlowItem(cashFlow.getDate(), LocalTime.of(0, 0), cashFlow.getCashFlowType(), cashFlow.getVolume(), tax);
        item.addTags(tags);
        cashFlowItems.add(item);
    }

    /**
     * Фильтр по тегу
     * @return копия CashFlowJournalс объектами CashFlowItem, содержащиме тег tag
     * @param tag тег, по которому фильтруем
     */
    public TradeJournal copyByTag(TradeTag tag) {
        TradeJournal newJournal = new TradeJournal();
        for(TradeItem tradeItem : tradeItems) {
            if(tradeItem.tradeTags.contains(tag))
                newJournal.tradeItems.add(tradeItem);
        }
        return newJournal;
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
}
