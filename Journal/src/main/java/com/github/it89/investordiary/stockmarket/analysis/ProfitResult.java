package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistory;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistoryItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Axel on 04.12.2016.
 */
public class ProfitResult implements Comparable<ProfitResult> {
    private LocalDate begin;
    private @Nullable LocalDate end;
    private BigDecimal profitNotTaxed;
    private BigDecimal profitTaxed;
    private BigDecimal tax;
    private @Nullable Asset asset;
    private @Nullable Integer stageNumber;

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

    public ProfitResult(ProfitHistory profitHistory, Asset asset, int stageNumber) {
        this(profitHistory);
        this.asset = asset;
        this.stageNumber = stageNumber;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    @Nullable
    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(@Nullable LocalDate end) {
        this.end = end;
    }

    public BigDecimal getProfitNotTaxed() {
        return profitNotTaxed;
    }

    public void setProfitNotTaxed(BigDecimal profitNotTaxed) {
        this.profitNotTaxed = profitNotTaxed;
    }

    public BigDecimal getProfitTaxed() {
        return profitTaxed;
    }

    public void setProfitTaxed(BigDecimal profitTaxed) {
        this.profitTaxed = profitTaxed;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    @Nullable
    public Asset getAsset() {
        return asset;
    }

    public void setAsset(@Nullable Asset asset) {
        this.asset = asset;
    }

    @Nullable
    public Integer getStageNumber() {
        return stageNumber;
    }

    public void setStageNumber(@Nullable Integer stageNumber) {
        this.stageNumber = stageNumber;
    }

    @Override
    public String toString() {
        return "ProfitResult{" +
                "begin=" + begin +
                ", end=" + end +
                ", profitNotTaxed=" + profitNotTaxed +
                ", profitTaxed=" + profitTaxed +
                ", tax=" + tax +
                ", asset=" + asset +
                ", stageNumber=" + stageNumber +
                '}';
    }

    public int compareTo(@NotNull ProfitResult o) {
        if(end == null && o.end != null)
            return 1;
        else if(end != null && o.end == null)
            return -1;
        int result = begin.compareTo(o.begin);
        if(result != 0)
            return result;
        if(asset != null)
            result = asset.compareTo(o.asset);
        if(result != 0)
            return result;
        if(stageNumber != null)
        result = stageNumber.compareTo(o.stageNumber);

        return result;
    }
}
