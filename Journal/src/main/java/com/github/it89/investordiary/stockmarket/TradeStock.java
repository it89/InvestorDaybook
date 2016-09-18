package com.github.it89.investordiary.stockmarket;

import java.math.BigDecimal;

/**
 * Created by Axel on 17.09.2016.
 */
public class TradeStock extends Trade {
    private BigDecimal price;

    public TradeStock(String tradeNumber) {
        super(tradeNumber);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TradeStock{" +
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
                ", price=" + price +
                '}';
    }
}
