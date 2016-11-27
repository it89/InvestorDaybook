package com.github.it89.investordiary;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by Axel on 27.11.2016.
 */
public class TaxCalculator {
    public static BigDecimal getTax(BigDecimal sum) {
        BigDecimal tax = sum.multiply(new BigDecimal("0.13")).setScale(0, BigDecimal.ROUND_HALF_UP);
        if(new BigDecimal("0").compareTo(tax) < 0)
            return tax;
        else
            return new BigDecimal("0");
    }
}
