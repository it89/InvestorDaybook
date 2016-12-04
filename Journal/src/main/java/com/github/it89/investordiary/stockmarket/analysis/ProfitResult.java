package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Axel on 04.12.2016.
 */
public class ProfitResult {
    private LocalDate begin;
    private LocalDate end;
    private BigDecimal profitNotTaxed;
    private BigDecimal profitTaxed;
    private BigDecimal tax;

    public static TreeMap<TradeTag, TreeSet<TradeTag>> getAssetTradeTagCombinations(StockMarketDaybook daybook) {
        TreeMap<TradeTag, TreeSet<TradeTag>> result = new TreeMap();
        for(Asset asset : daybook.getAssets().values()) {
            TradeTag assetTag = daybook.getTradeTag(asset.getTicker());
            if(assetTag != null) {
                TreeSet<Trade> trades = new TreeSet();
                if (asset.getAssetType().isBond()) {
                    trades.addAll((Collection<? extends Trade>) daybook.getTradeBonds().values());
                } else {
                    trades.addAll((Collection<? extends Trade>) daybook.getTradeStocks().values());
                }
                trades = Trade.filterTreeSetByTag(trades, assetTag);
                // Пока предположим, что тег только 1, в будущем перейдем от тегов к параметрам
                TreeSet<TradeTag> logicalTrades = new TreeSet();
                for (Trade trade : trades) {
                    TradeTag logicalTrade = trade.getTradeTags().firstEntry().getValue();
                    logicalTrades.add(logicalTrade);
                }
                result.put(assetTag, logicalTrades);
            }
        }
        return result;
    }
}
