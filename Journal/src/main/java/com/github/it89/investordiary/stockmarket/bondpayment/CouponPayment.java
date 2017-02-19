package com.github.it89.investordiary.stockmarket.bondpayment;

import java.math.BigDecimal;

/**
 * Created by Axel on 19.02.2017.
 */
public class CouponPayment {
    final BigDecimal couponRate;
    final BigDecimal pct;
    final BigDecimal payment;

    public CouponPayment(BigDecimal couponRate, BigDecimal pct, BigDecimal payment) {
        this.couponRate = couponRate;
        this.pct = pct;
        this.payment = payment;
    }
}
