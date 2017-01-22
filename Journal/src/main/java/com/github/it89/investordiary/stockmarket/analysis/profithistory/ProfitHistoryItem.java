package com.github.it89.investordiary.stockmarket.analysis.profithistory;

import com.github.it89.investordiary.TaxCalculator;
import com.github.it89.investordiary.stockmarket.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

/**
 * Created by Axel on 20.11.2016.
 */
public class ProfitHistoryItem {
    /** Прибыль, с которой еще не удержан налог*/
    private final BigDecimal sumProfitNotTaxed;
    /** Прибыль с которой уже удержан налог (включая прибыль, которая не облагается налогом*/
    private final BigDecimal sumProfitTaxed;
    private final BigDecimal sumTax;
    private BigDecimal paperProfit;
    final TreeMap<Asset, Long> assetCount = new TreeMap();
    private final TreeMap<LocalDate, BigDecimal> cashFlows = new TreeMap<>();

    ProfitHistoryItem(BigDecimal sumProfitNotTaxed, BigDecimal sumProfitTaxed, BigDecimal tax) {
        this.sumProfitNotTaxed = sumProfitNotTaxed;
        this.sumProfitTaxed = sumProfitTaxed;
        this.sumTax = tax;
    }

    ProfitHistoryItem(Trade trade) {
        this(trade.getTotalProfit(), new BigDecimal(0), new BigDecimal(0));
        addAssetCount(trade);
        addCashFlow(trade.getDate(), trade.getTotalProfit());
    }

    ProfitHistoryItem(CashFlow cashFlow) {
        BigDecimal tax = null;
        BigDecimal cashFlowSum = cashFlow.getVolume();

        if(cashFlow instanceof AssetIncome)
            tax = ((AssetIncome)cashFlow).getTax();
        if(tax == null) {
            this.sumProfitNotTaxed = cashFlow.getVolume();
            this.sumProfitTaxed = new BigDecimal(0);
            this.sumTax = new BigDecimal(0);
        } else {
            this.sumProfitNotTaxed = new BigDecimal(0);
            this.sumProfitTaxed = cashFlow.getVolume();
            this.sumTax = tax;
            cashFlowSum = cashFlowSum.add(tax.negate());
        }
        addCashFlow(cashFlow.getDate(), cashFlowSum);
    }

    ProfitHistoryItem add(Trade trade) {
        ProfitHistoryItem item = new ProfitHistoryItem(this.sumProfitNotTaxed.add(trade.getTotalProfit()), this.sumProfitTaxed, this.sumTax);
        item.assetCount.putAll(this.assetCount);
        item.cashFlows.putAll(this.cashFlows);
        item.addAssetCount(trade);
        item.addCashFlow(trade.getDate(), trade.getTotalProfit());
        return item;
    }

    ProfitHistoryItem add(CashFlow cashFlow) {
        BigDecimal cashFlowTax = null;
        ProfitHistoryItem item;
        BigDecimal cashFlowSum = cashFlow.getVolume();

        if (cashFlow instanceof AssetIncome)
            cashFlowTax = ((AssetIncome) cashFlow).getTax();
        if (cashFlowTax == null)
            item = new ProfitHistoryItem(this.sumProfitNotTaxed.add(cashFlow.getVolume()), this.sumProfitTaxed, this.sumTax);
        else {
            item = new ProfitHistoryItem(this.sumProfitNotTaxed, this.sumProfitTaxed.add(cashFlow.getVolume()), this.sumTax.add(cashFlowTax));
            cashFlowSum = cashFlowSum.add(cashFlowTax.negate());
        }

        item.assetCount.putAll(this.assetCount);
        item.cashFlows.putAll(this.cashFlows);
        item.addCashFlow(cashFlow.getDate(), cashFlowSum);
        return item;
    }

    private void addAssetCount(Trade trade) {
        Asset asset = trade.getAsset();
        long countAdd = trade.getAmount();
        if(trade.getOperation() == TradeOperation.SELL)
            countAdd = -countAdd;
        if(assetCount.containsKey(asset)) {
            long count = assetCount.get(asset) + countAdd;
            if(count == 0)
                assetCount.remove(asset);
            else
                assetCount.put(asset, count);
        } else
            assetCount.put(asset, countAdd);
    }


    public BigDecimal getSumProfitNotTaxed() {
        return sumProfitNotTaxed;
    }

    public BigDecimal getSumProfitTaxed() {
        return sumProfitTaxed;
    }

    public BigDecimal getSumTax() {
        return sumTax;
    }

    public BigDecimal getPaperProfit() {
        return paperProfit;
    }

    public void setPaperProfit(BigDecimal paperProfit) {
        this.paperProfit = paperProfit;
    }

    public BigDecimal getTotalProfitNotTaxed() {
        return sumProfitNotTaxed.add(sumProfitTaxed);
    }

    public BigDecimal getTotalProfitTaxed() {
        BigDecimal totalProfitTaxed = sumProfitTaxed.add(sumTax.negate());
        totalProfitTaxed = totalProfitTaxed.add(sumProfitNotTaxed.add(TaxCalculator.getTax(sumProfitNotTaxed).negate()));
        return totalProfitTaxed;
    }

    public BigDecimal getTotalPaperProfitNotTaxed() {
        return sumProfitNotTaxed.add(sumProfitTaxed).add(paperProfit);
    }

    public BigDecimal getTotalPaperProfitTaxed() {
        BigDecimal totalProfitTaxed = sumProfitTaxed.add(sumTax.negate());
        BigDecimal totalProfitNotTaxed = sumProfitNotTaxed.add(paperProfit);
        totalProfitTaxed = totalProfitTaxed.add(totalProfitNotTaxed.add(TaxCalculator.getTax(totalProfitNotTaxed).negate()));
        return totalProfitTaxed;
    }

    public TreeMap<Asset, Long> getAssetCount() {
        return assetCount;
    }

    public TreeMap<LocalDate, BigDecimal> getCashFlows() {
        return cashFlows;
    }

    ProfitHistoryItem copy() {
        ProfitHistoryItem newItem = new ProfitHistoryItem(sumProfitNotTaxed, sumProfitTaxed, sumTax);
        newItem.assetCount.putAll(assetCount);
        newItem.cashFlows.putAll(cashFlows);
        return newItem;
    }

    private void addCashFlow(LocalDate date, BigDecimal cashFlowSum) {
        if(cashFlows.containsKey(date)) {
            BigDecimal cash = cashFlows.get(date).add(cashFlowSum);
            cashFlows.put(date, cash);
        } else {
            cashFlows.put(date, cashFlowSum);
        }

    }
}
