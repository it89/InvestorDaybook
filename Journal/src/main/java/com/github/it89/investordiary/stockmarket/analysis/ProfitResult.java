package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistory;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistoryItem;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Axel on 04.12.2016.
 */
public class ProfitResult {
    private LocalDate begin;
    private @Nullable LocalDate end;
    private BigDecimal profitNotTaxed;
    private BigDecimal profitTaxed;
    private BigDecimal tax;

    public static TreeMap<Asset, TreeSet<Integer>> getAssetStageCombinations(StockMarketDaybook daybook) {
        TreeMap<Asset, TreeSet<Integer>> result = new TreeMap<Asset, TreeSet<Integer>>();
        for(Asset asset : daybook.getAssets().values()) {
            TreeSet<Trade> trades = new TreeSet<Trade>();
            if (asset.getAssetType().isBond())
                trades.addAll((Collection<? extends Trade>) daybook.getTradeBonds().values());
            else
                trades.addAll((Collection<? extends Trade>) daybook.getTradeStocks().values());
            trades = Trade.filterTreeSetByAsset(trades, asset);
            TreeSet<Integer> stages = new TreeSet();
            for(Trade trade : trades)
                stages.add(trade.getStageNumber());
            if(!stages.isEmpty())
                result.put(asset, stages);
        }
        return result;
    }

    public ProfitResult(ProfitHistory profitHistory) {
        TreeMap<LocalDate, ProfitHistoryItem> profitHistoryItems = profitHistory.getItems();

        if(profitHistoryItems.isEmpty()) {
            throw  new NullPointerException();
        }
        begin = profitHistoryItems.firstKey();
        end = profitHistoryItems.lastKey();
        ProfitHistoryItem lastProfitHistoryItem = profitHistoryItems.get(end);
        if(!lastProfitHistoryItem.getAssetCount().isEmpty())
            end = null;
        profitNotTaxed = lastProfitHistoryItem.getTotalPaperProfitNotTaxed();
        profitTaxed = lastProfitHistoryItem.getTotalPaperProfitTaxed();
        tax = lastProfitHistoryItem.getSumTax();
    }

    @Override
    public String toString() {
        return "ProfitResult{" +
                "begin=" + begin +
                ", end=" + end +
                ", profitNotTaxed=" + profitNotTaxed +
                ", profitTaxed=" + profitTaxed +
                ", tax=" + tax +
                '}';
    }
}
