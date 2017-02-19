package com.github.it89.investordiary.stockmarket.bondpayment;

import com.github.it89.investordiary.stockmarket.Asset;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

/**
 * Created by Axel on 19.02.2017.
 */
public class BondPayment implements Comparable<BondPayment> {
    final LocalDate date;
    final CouponPayment couponPayment;
    final BondRedemption bondRedemption;

    public BondPayment(LocalDate date, CouponPayment couponPayment, BondRedemption bondRedemption) {
        this.date = date;
        this.couponPayment = couponPayment;
        this.bondRedemption = bondRedemption;
    }

    @Override
    public int compareTo(@NotNull BondPayment o) {
        return date.compareTo(o.date);
    }
}
