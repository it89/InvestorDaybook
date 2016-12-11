package com.github.it89.investordiary.stockmarket;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Axel on 15.10.2016.
 */
public class CashFlow implements Comparable<CashFlow>{
    protected CashFlowType cashFlowType;
    protected LocalDate date;
    protected BigDecimal volume;
    protected String comment;

    public CashFlowType getCashFlowType() {
        return cashFlowType;
    }

    public void setCashFlowType(CashFlowType cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CashFlow{" +
                "cashFlowType=" + cashFlowType +
                ", date=" + date +
                ", volume=" + volume +
                ", comment='" + comment + '\'' +
                '}';
    }

    public static TreeSet<CashFlow> filterTreeSetByAsset(TreeSet<CashFlow> set, Asset asset) {
        TreeSet<CashFlow> newSet = new TreeSet();
        for(CashFlow cashFlow : set)
            if(cashFlow.getCashFlowType().isAssetIncome())
                if(cashFlow instanceof AssetIncome)
                    if(((AssetIncome)cashFlow).getAsset().equals(asset))
                        newSet.add(cashFlow);

        return newSet;
    }

    public static TreeSet<CashFlow> filterTreeSetByStageNumber(TreeSet<CashFlow> set, int stageNumber) {
        TreeSet<CashFlow> newSet = new TreeSet();
        for(CashFlow cashFlow : set)
            if(cashFlow.getCashFlowType().isAssetIncome())
                if(cashFlow instanceof AssetIncome)
                    if(((AssetIncome)cashFlow).getStageNumber() == stageNumber)
                        newSet.add(cashFlow);
        return newSet;
    }

    public int compareTo(@NotNull CashFlow o) {
        int result = this.date.compareTo(o.date);
        if(result != 0)
            return result;
        return this.volume.compareTo(o.volume);
    }
}
