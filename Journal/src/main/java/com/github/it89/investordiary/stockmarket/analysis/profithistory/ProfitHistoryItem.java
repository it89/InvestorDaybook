package com.github.it89.investordiary.stockmarket.analysis.profithistory;

import com.github.it89.investordiary.TaxCalculator;
import com.github.it89.investordiary.stockmarket.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
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

    ProfitHistoryItem(BigDecimal sumProfitNotTaxed, BigDecimal sumProfitTaxed, BigDecimal tax) {
        this.sumProfitNotTaxed = sumProfitNotTaxed;
        this.sumProfitTaxed = sumProfitTaxed;
        this.sumTax = tax;
    }

    ProfitHistoryItem(Trade trade) {
        this(trade.getTotalProfit(), new BigDecimal(0), new BigDecimal(0));
        addAssetCount(trade);
    }

    ProfitHistoryItem(CashFlow cashFlow) {
        BigDecimal tax = null;
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
        }

    }

    ProfitHistoryItem add(Trade trade) {
        ProfitHistoryItem item = new ProfitHistoryItem(this.sumProfitNotTaxed.add(trade.getTotalProfit()), this.sumProfitTaxed, this.sumTax);
        item.assetCount.putAll(this.assetCount);
        item.addAssetCount(trade);
        return item;
    }

    ProfitHistoryItem add(CashFlow cashFlow) {
        BigDecimal cashFlowTax = null;
        ProfitHistoryItem item;

        if(cashFlow instanceof AssetIncome)
            cashFlowTax = ((AssetIncome) cashFlow).getTax();
        if(cashFlowTax == null)
            item = new ProfitHistoryItem(this.sumProfitNotTaxed.add(cashFlow.getVolume()), this.sumProfitTaxed, this.sumTax);
        else
            item = new ProfitHistoryItem(this.sumProfitNotTaxed, this.sumProfitTaxed.add(cashFlow.getVolume()), this.sumTax.add(cashFlowTax));
        item.assetCount.putAll(this.assetCount);
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

    ProfitHistoryItem copy() {
        ProfitHistoryItem newItem = new ProfitHistoryItem(sumProfitNotTaxed, sumProfitTaxed, sumTax);
        newItem.assetCount.putAll(assetCount);
        return newItem;
    }
}
