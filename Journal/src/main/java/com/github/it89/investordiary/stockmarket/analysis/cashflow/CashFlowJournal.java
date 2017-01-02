package com.github.it89.investordiary.stockmarket.analysis.cashflow;

import com.github.it89.investordiary.stockmarket.*;

import java.math.BigDecimal;
import java.time.LocalTime;
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

        CashFlowItem item;

        if(trade instanceof TradeBond) {
            TradeBond tradeBond = (TradeBond) trade;
            BigDecimal accumulatedCouponYield = tradeBond.getAccumulatedCouponYield();
            if(trade.getOperation() == TradeOperation.BUY)
                accumulatedCouponYield = accumulatedCouponYield.negate();
            item = new CashFlowItem(trade.getDate(), trade.getTime(), CashFlowType.ACCUMULATED_COUPON_YIELD, accumulatedCouponYield, null, trade.getAsset(), trade.getStageNumber());
            items.add(item);
            volume = volume.add(accumulatedCouponYield.negate());
        }

        // Buy/Sell
        item = new CashFlowItem(trade.getDate(), trade.getTime(), CashFlowType.TRADE, volume, null, trade.getAsset(), trade.getStageNumber());
        items.add(item);

        // Comission
        item = new CashFlowItem(trade.getDate(), trade.getTime(), CashFlowType.COMMISSION_TRADE, trade.getCommission().negate(), null, trade.getAsset(), trade.getStageNumber());
        items.add(item);
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
        items.add(item);
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

    public void addTrades(TreeSet<Trade> trades) {
        for(Trade trade : trades)
            add(trade);
    }

    public void addCashFlows(TreeSet<CashFlow> cashFlows) {
        for(CashFlow cashFlow : cashFlows)
            add(cashFlow);
    }

    public void fill(StockMarketDaybook daybook) {
        for(TradeStock stock : daybook.getTradeStocks().values())
            add(stock);
        for(TradeBond bond : daybook.getTradeBonds().values())
            add(bond);
        for(CashFlow cashFlow : daybook.getCashFlows())
            add(cashFlow);
    }
}
