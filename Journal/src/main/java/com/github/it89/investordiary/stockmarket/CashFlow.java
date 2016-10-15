package com.github.it89.investordiary.stockmarket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

/**
 * Created by Axel on 15.10.2016.
 */
public class CashFlow {
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
}
