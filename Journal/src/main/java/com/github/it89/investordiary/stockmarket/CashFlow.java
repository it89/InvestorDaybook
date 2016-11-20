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
    protected final TreeMap<String, TradeTag> tradeTags = new TreeMap();

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

    public TreeMap<String, TradeTag> getTradeTags() {
        return tradeTags;
    }

    public void addTradeTag(TradeTag tradeTag) {
        tradeTags.put(tradeTag.getTag(), tradeTag);
    }

    @Override
    public String toString() {
        return "CashFlow{" +
                "cashFlowType=" + cashFlowType +
                ", date=" + date +
                ", volume=" + volume +
                ", comment='" + comment + '\'' +
                ", tradeTags=" + tradeTags +
                '}';
    }

    public static TreeSet<CashFlow> filterTreeSetByTag(TreeSet<CashFlow> set, TradeTag tag) {
        TreeSet<CashFlow> newSet = new TreeSet();
        for(CashFlow cashFlow : set) {
            if(cashFlow.tradeTags.values().contains(tag))
                newSet.add(cashFlow);
            else if(cashFlow instanceof AssetIncome) {
                if(((AssetIncome)cashFlow).getAsset().getTradeTags().values().contains(tag))
                    newSet.add(cashFlow);
            }
        }
        return newSet;
    }

    public int compareTo(@NotNull CashFlow o) {
        int result = this.date.compareTo(o.date);
        if(result != 0)
            return result;
        return this.volume.compareTo(o.volume);
    }
}
