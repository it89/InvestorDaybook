package com.github.it89.investordiary.stockmarket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * Created by Axel on 15.10.2016.
 */
public class AssetIncome extends CashFlow {
    private Asset asset;
    private BigDecimal tax;
    private long amount;
    protected int stageNumber;

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public void setStageNumber(int stageNumber) {
        this.stageNumber = stageNumber;
    }

    @Override
    public String toString() {
        return "AssetIncome{" +
                "cashFlowType=" + cashFlowType +
                ", date=" + date +
                ", volume=" + volume +
                ", comment='" + comment + '\'' +
                ", asset=" + asset +
                ", tax=" + tax +
                ", amount" + amount +
                ", stageNumber" + stageNumber +
                '}';
    }
}
