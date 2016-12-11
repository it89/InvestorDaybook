package com.github.it89.investordiary.stockmarket;

import com.github.it89.investordiary.stockmarket.analysis.tradejournal.TradeItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Currency;
import java.util.TreeMap;
import java.util.TreeSet;

public abstract class Trade implements Comparable{
    protected Asset asset;
    protected String applicationNumber;
    protected final String tradeNumber;
    protected LocalDate date;
    protected LocalTime time;
    protected TradeOperation operation;
    protected long amount;
    protected Currency currency;
    protected BigDecimal volume;
    protected BigDecimal commission;
    protected int stageNumber;

    protected Trade(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public TradeOperation getOperation() {
        return operation;
    }

    public void setOperation(TradeOperation operation) {
        this.operation = operation;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal comission) {
        this.commission = comission;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public void setStageNumber(int stageNumber) {
        this.stageNumber = stageNumber;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "asset=" + asset +
                ", applicationNumber='" + applicationNumber + '\'' +
                ", tradeNumber='" + tradeNumber + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", operation=" + operation +
                ", amount=" + amount +
                ", currency=" + currency +
                ", volume=" + volume +
                ", commission=" + commission +
                ", stageNumber=" + stageNumber +
                '}';
    }

    public static TreeSet<Trade> filterTreeSetByAsset(TreeSet<Trade> set, Asset asset) {
        TreeSet<Trade> newSet = new TreeSet();
        for(Trade trade : set)
            if(trade.asset.equals(asset))
                newSet.add(trade);
        return newSet;
    }

    public static TreeSet<Trade> filterTreeSetByStageNumber(TreeSet<Trade> set, int stageNumber) {
        TreeSet<Trade> newSet = new TreeSet();
        for(Trade trade : set)
            if(trade.stageNumber == stageNumber)
                newSet.add(trade);
        return newSet;
    }

    public int compareTo(Object o) {
        return this.tradeNumber.compareTo(((Trade)o).tradeNumber);
    }

    public BigDecimal getTotalProfit() {
        BigDecimal totalProfit = volume;
        if(operation == TradeOperation.BUY)
            totalProfit = totalProfit.negate();

        totalProfit = totalProfit.add(commission.negate());
        return totalProfit;
    }

}