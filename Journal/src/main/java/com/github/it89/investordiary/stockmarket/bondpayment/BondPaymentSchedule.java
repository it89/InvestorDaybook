package com.github.it89.investordiary.stockmarket.bondpayment;

import com.github.it89.investordiary.stockmarket.Asset;

import java.util.TreeSet;

/**
 * Created by Axel on 19.02.2017.
 */
public class BondPaymentSchedule {
    final Asset asset;
    public final TreeSet<BondPayment> bondPayments = new TreeSet<>();

    public BondPaymentSchedule(Asset asset) {
        this.asset = asset;
    }
}
