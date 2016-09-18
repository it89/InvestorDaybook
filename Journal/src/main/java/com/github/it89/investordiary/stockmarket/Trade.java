package com.github.it89.investordiary.stockmarket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Currency;

abstract class Trade {
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
                '}';
    }

}