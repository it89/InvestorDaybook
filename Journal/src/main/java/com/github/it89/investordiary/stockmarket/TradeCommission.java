package com.github.it89.investordiary.stockmarket;

import java.math.BigDecimal;

/**
 * Created by Axel on 18.09.2016.
 */
public class TradeCommission {
    private BigDecimal commission;

    public TradeCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "TradeCommission{" +
                "commission=" + commission +
                '}';
    }
}
