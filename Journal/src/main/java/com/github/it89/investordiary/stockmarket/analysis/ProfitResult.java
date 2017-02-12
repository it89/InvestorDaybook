package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.cashflow.CashFlowItem;
import com.github.it89.investordiary.stockmarket.analysis.cashflow.CashFlowJournal;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistory;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistoryItem;
import com.github.it89.investordiary.stockmarket.analysis.stockportfolio.StockPortfolioJournal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Axel on 04.12.2016.
 */
public class ProfitResult implements Comparable<ProfitResult> {
    private LocalDate dateBegin;
    private @Nullable LocalDate dateEnd;
    private BigDecimal profitNotTaxed = BigDecimal.ZERO;
    private BigDecimal profitTaxed = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private @Nullable Asset asset;
    private @Nullable Integer stageNumber;

    private ProfitResult() {}

    @Deprecated
    public ProfitResult(ProfitHistory profitHistory) {
        TreeMap<LocalDate, ProfitHistoryItem> profitHistoryItems = profitHistory.getItems();

        if(profitHistoryItems.isEmpty()) {
            throw  new NullPointerException();
        }
        dateBegin = profitHistoryItems.firstKey();
        dateEnd = profitHistoryItems.lastKey();
        ProfitHistoryItem lastProfitHistoryItem = profitHistoryItems.get(dateEnd);
        if(!lastProfitHistoryItem.getAssetCount().isEmpty())
            dateEnd = null;
        profitNotTaxed = lastProfitHistoryItem.getTotalPaperProfitNotTaxed();
        profitTaxed = lastProfitHistoryItem.getTotalPaperProfitTaxed();
        tax = lastProfitHistoryItem.getSumTax();
    }

    @Deprecated
    public ProfitResult(ProfitHistory profitHistory, Asset asset, int stageNumber) {
        this(profitHistory);
        this.asset = asset;
        this.stageNumber = stageNumber;
    }

    public static TreeSet<ProfitResult> generateByCombinatons(StockMarketDaybook daybook) {
        TreeSet<ProfitResult> results = new TreeSet<>();
        TreeMap<Asset, TreeSet<Integer>> combinations = daybook.getAssetStageCombinations();
        TreeSet<Trade> tradesAll = new TreeSet<>();
        tradesAll.addAll(daybook.getTradeBonds().values());
        tradesAll.addAll(daybook.getTradeStocks().values());
        TreeSet<CashFlow> cashFlowsAll = new TreeSet<>();
        cashFlowsAll.addAll(daybook.getCashFlows());

        for(Map.Entry<Asset, TreeSet<Integer>> entry : combinations.entrySet()) {
            Asset asset = entry.getKey();
            TreeSet<Trade> tradesAsset = Trade.filterTreeSetByAsset(tradesAll, asset);
            TreeSet<CashFlow> cashFlowsAsset = CashFlow.filterTreeSetByAsset(cashFlowsAll, asset);
            for(Integer stageNumber : entry.getValue()) {
                ProfitResult newItem = new ProfitResult();
                newItem.asset = asset;
                newItem.stageNumber = stageNumber;

                CashFlowJournal cashFlowJournal = new CashFlowJournal();
                TreeSet<Trade> trades = Trade.filterTreeSetByStageNumber(tradesAsset, stageNumber);
                TreeSet<CashFlow> cashFlows = CashFlow.filterTreeSetByStageNumber(cashFlowsAsset, stageNumber);
                cashFlowJournal.addTrades(trades);
                cashFlowJournal.addCashFlows(cashFlows);

                StockPortfolioJournal stockPortfolioJournal = new StockPortfolioJournal();
                stockPortfolioJournal.addTrades(trades);
                newItem.dateBegin = stockPortfolioJournal.getMinDate(asset, stageNumber);
                long assetCount = stockPortfolioJournal.getAmountSum(asset, stageNumber, newItem.dateBegin, LocalDate.MAX);
                if(assetCount == 0)
                    newItem.dateEnd = stockPortfolioJournal.getMaxDate(asset, stageNumber);

                newItem.calcCashFlow(cashFlowJournal);
                results.add(newItem);
            }
        }
        return results;
    }

    private void calcCashFlow(CashFlowJournal cashFlowJournal) {
        for(CashFlowItem cashFlowItem : cashFlowJournal.getItems()) {
            if(cashFlowItem.getTax() == null)
                profitNotTaxed = profitNotTaxed.add(cashFlowItem.getMoney());
            else {
                profitTaxed = profitTaxed.add(cashFlowItem.getMoney());
                tax = tax.add(cashFlowItem.getTax());
            }
        }
    }

    public LocalDate getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(LocalDate dateBegin) {
        this.dateBegin = dateBegin;
    }

    @Nullable
    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(@Nullable LocalDate dateEnd) {
        this.dateEnd = dateEnd;
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
                "dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", profitNotTaxed=" + profitNotTaxed +
                ", profitTaxed=" + profitTaxed +
                ", tax=" + tax +
                ", asset=" + asset +
                ", stageNumber=" + stageNumber +
                '}';
    }

    public int compareTo(@NotNull ProfitResult o) {
        if(dateEnd == null && o.dateEnd != null)
            return 1;
        else if(dateEnd != null && o.dateEnd == null)
            return -1;
        int result = dateBegin.compareTo(o.dateBegin);
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
