package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.Asset;
import com.github.it89.investordiary.stockmarket.CashFlowType;
import com.github.it89.investordiary.stockmarket.TradeTag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Axel on 23.10.2016.
 */
public class CashFlowItem {
    private LocalDate date;
    private LocalTime time;
    private CashFlowType cashFlowType;
    private BigDecimal money;
    private BigDecimal tax;
    final Set<TradeTag> tradeTags = new HashSet();

    public CashFlowItem(LocalDate date, LocalTime time, CashFlowType cashFlowType, BigDecimal money, BigDecimal tax) {
        this.date = date;
        this.time = time;
        this.cashFlowType = cashFlowType;
        this.money = money;
        this.tax = tax;
    }

    public void addTags(Set<TradeTag> tags) {
        tradeTags.addAll(tags);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public CashFlowType getCashFlowType() {
        return cashFlowType;
    }

    public void setCashFlowType(CashFlowType cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Set<TradeTag> getTradeTags() {
        return tradeTags;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CashFlowItem{" +
                "date=" + date +
                ", time=" + time +
                ", cashFlowType=" + cashFlowType +
                ", money=" + money +
                ", tax=" + tax +
                ", tradeTags=" + tradeTags +
                '}';
    }

    public int compareTo(Object o) {
        System.out.println(this.hashCode() + " <> " + o.hashCode());
        if(this.hashCode() > o.hashCode())
            return 1;
        else if(this.hashCode() < o.hashCode())
            return -1;
        return 0;
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
}
