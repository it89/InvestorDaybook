package com.github.it89.investordiary.stockmarket.analysis.cashflow;

import com.github.it89.investordiary.stockmarket.Asset;
import com.github.it89.investordiary.stockmarket.CashFlowType;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

/**
 * Created by Axel on 23.10.2016.
 */
public class CashFlowItem {
    private final LocalDate date;
    private final LocalTime time;
    private final CashFlowType cashFlowType;
    private final BigDecimal money;
    private final BigDecimal tax;
    private final @Nullable Asset asset;
    private final @Nullable Integer stageNumber;

    public CashFlowItem(LocalDate date, LocalTime time, CashFlowType cashFlowType, BigDecimal money, BigDecimal tax, Asset asset, Integer stageNumber) {
        this.date = date;
        this.time = time;
        this.cashFlowType = cashFlowType;
        this.money = money;
        this.tax = tax;
        this.asset = asset;
        this.stageNumber = stageNumber;
    }

    @Override
    public String toString() {
        return "CashFlowItem{" +
                "date=" + date +
                ", time=" + time +
                ", cashFlowType=" + cashFlowType +
                ", money=" + money +
                ", tax=" + tax +
                ", asset=" + asset +
                ", stageNumber=" + stageNumber +
                '}';
    }

    public static class CashFlowItemComp implements Comparator<CashFlowItem> {

        public int compare(CashFlowItem o1, CashFlowItem o2) {
            int result = o1.date.compareTo(o2.date);
            if(result == 0) {
                result = o1.time.compareTo(o2.time);
                if(result == 0) {
                    result = o1.cashFlowType.compareTo(o2.cashFlowType);
                    if(result == 0) {
                        result = Integer.compare(o1.hashCode(), o2.hashCode());
                    }
                }
            }
            return result;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public CashFlowType getCashFlowType() {
        return cashFlowType;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public BigDecimal getTax() {
        return tax;
    }

    @Nullable
    public Asset getAsset() {
        return asset;
    }

    @Nullable
    public Integer getStageNumber() {
        return stageNumber;
    }
}
