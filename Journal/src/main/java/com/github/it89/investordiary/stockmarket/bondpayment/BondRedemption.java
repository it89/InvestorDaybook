package com.github.it89.investordiary.stockmarket.bondpayment;

import java.math.BigDecimal;

/**
 * Created by Axel on 19.02.2017.
 */
public class BondRedemption {
    final BigDecimal pct;
    final BigDecimal payment;

    public BondRedemption(BigDecimal pct, BigDecimal payment) {
        this.pct = pct;
        this.payment = payment;
    }
}
