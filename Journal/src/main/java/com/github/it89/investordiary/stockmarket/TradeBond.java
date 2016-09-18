package com.github.it89.investordiary.stockmarket;

import java.math.BigDecimal;

/**
 * Created by Axel on 17.09.2016.
 */
public class TradeBond extends Trade{
    private BigDecimal pricePct;
    private double nominal;

    public TradeBond(String tradeNumber) {
        super(tradeNumber);
    }
}
