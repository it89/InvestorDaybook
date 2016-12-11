package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created by Axel on 23.10.2016.
 */
public class CashFlowJournal {
    private final TreeSet<CashFlowItem> items = new TreeSet(new CashFlowItem.CashFlowItemComp());

    public void add(Trade trade) {
        BigDecimal volume = trade.getVolume();
        if(trade.getOperation() == TradeOperation.BUY)
            volume = volume.negate();

        /*HashSet<TradeTag> tags = new HashSet();
        tags.addAll(trade.getTradeTags().values());
        tags.addAll(trade.getAsset().getTradeTags().values());*/

        // Buy/Sell
        CashFlowItem item = new CashFlowItem(trade.getDate(), trade.getTime(), CashFlowType.TRADE, volume, null);
        //item.addTags(tags);
        items.add(item);

        // Comission
        item = new CashFlowItem(trade.getDate(), trade.getTime(), CashFlowType.COMMISSION_TRADE, trade.getCommission().negate(), null);
        //item.addTags(tags);
        items.add(item);

        // AccumulatedCouponYield
        if(trade instanceof TradeBond) {
            TradeBond tradeBond = (TradeBond) trade;
            BigDecimal accumulatedCouponYield = tradeBond.getAccumulatedCouponYield();
            if(trade.getOperation() == TradeOperation.BUY)
                accumulatedCouponYield = accumulatedCouponYield.negate();
            item = new CashFlowItem(trade.getDate(), trade.getTime(), CashFlowType.ACCUMULATED_COUPON_YIELD, accumulatedCouponYield, null);
            //item.addTags(tags);
            items.add(item);
        }
    }

    public void add(CashFlow cashFlow) {
        BigDecimal tax = null;
        Asset asset = null;
        if(cashFlow instanceof AssetIncome) {
            tax = ((AssetIncome) cashFlow).getTax();
            asset = ((AssetIncome) cashFlow).getAsset();
        }
        /*HashSet<TradeTag> tags = new HashSet();
        tags.addAll(cashFlow.getTradeTags().values());
        if(asset != null)
            tags.addAll(asset.getTradeTags().values());*/

        CashFlowItem item = new CashFlowItem(cashFlow.getDate(), LocalTime.of(0, 0), cashFlow.getCashFlowType(), cashFlow.getVolume(), tax);
        //item.addTags(tags);
        items.add(item);
    }

    /**
     * Фильтр по тегу
     * @return копия CashFlowJournalс объектами CashFlowItem, содержащиме тег tag
     * @param tag тег, по которому фильтруем
     */
    public CashFlowJournal copyByTag(TradeTag tag) {
        CashFlowJournal newJournal = new CashFlowJournal();
        for(CashFlowItem cashFlowItem : this.getItems()) {
            if(cashFlowItem.tradeTags.contains(tag))
                newJournal.items.add(cashFlowItem);
        }
        return newJournal;
    }

    public TreeSet<CashFlowItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "CashFlowJournal{" +
                "items=" + items +
                '}';
    }
}
