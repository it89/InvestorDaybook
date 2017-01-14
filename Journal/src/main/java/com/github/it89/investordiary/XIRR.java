package com.github.it89.investordiary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import static java.time.temporal.ChronoUnit.DAYS;

public class XIRR {
    // TODO: not static realization
    private static final int SEEK_COUNT_LIMIT_NEWTON = 100;
    private static final int SEEK_COUNT_LIMIT_BISECTION = 1000;
    private static final double ACCURACY = 0.000001;

    public static void main(String[] args) {
        TreeMap<LocalDate, BigDecimal> cashFlows = new TreeMap<>();
        cashFlows.put(LocalDate.of(2016, 1, 1), new BigDecimal("100"));
        cashFlows.put(LocalDate.of(2017, 1, 1), new BigDecimal("-1"));
        System.out.println("XIRR = " + getXIRR(cashFlows));
    }

    /**
     * f(xirr) = cashFlow / ((1 + xirr) ^ (dayCount / 365))
     */
    private static double f(double cashFlow, long dayCount, double xirr) {
        double pow = Math.pow(1.0 + xirr, dayCount / 365.0);
        return cashFlow / pow;
    }

    /**
     * f'(xirr) = -(dayCount / 365) * cashFlow * (1.0 + xirr) ^ (-(dayCount / 365) - 1)
     * f'(xirr) = -(dayCount / 365) * cashFlow / (1.0 + xirr) ^ ((dayCount / 365) + 1)
     */
    private static double df(double cashFlow, long dayCount, double xirr) {
        double pow = Math.pow(1.0 + xirr, dayCount / 365.0 + 1.0);
        return (-dayCount / 365.0) * cashFlow / pow;
    }

    private static double getSumF(TreeMap<LocalDate, BigDecimal> cashFlows, double xirr) {
        double result = 0;
        LocalDate firstDate = cashFlows.firstKey();
        for(Map.Entry<LocalDate, BigDecimal> entry : cashFlows.entrySet()) {
            long days = DAYS.between(firstDate, entry.getKey());
            double f = f(entry.getValue().doubleValue(),  days, xirr);
            result += f;
        }

        return result;
    }

    private static double getSumDF(TreeMap<LocalDate, BigDecimal> cashFlows, double xirr) {
        double result = 0;
        LocalDate firstDate = cashFlows.firstKey();
        for(Map.Entry<LocalDate, BigDecimal> entry : cashFlows.entrySet()) {
            long days = DAYS.between(firstDate, entry.getKey());
            double df = df(entry.getValue().doubleValue(), days, xirr);
            result += df;
        }
        return result;
    }

    /** Calculate XIRR*/
    public static double getXIRR(TreeMap<LocalDate, BigDecimal> cashFlows, double guess) {
        double result = newtonMethod(cashFlows, guess);
        if(Double.isNaN(result))
            result = bisectionMethod(cashFlows);
        return result;
    }

    public static double getXIRR(TreeMap<LocalDate, BigDecimal> cashFlows) {
        return getXIRR(cashFlows, 0.1);
    }

    /** Calculate XIRR by Newton's method*/
    private static double newtonMethod(TreeMap<LocalDate, BigDecimal> cashFlows, double guess) {
        double x0 = guess;
        double x1= 0;
        double delta = 0;
        int seekCount = 0;
        do {
            x1 = x0 - getSumF(cashFlows, x0) / getSumDF(cashFlows, x0);
            delta = Math.abs(x0 - x1);
            x0 = x1;
            seekCount++;
            if(seekCount > SEEK_COUNT_LIMIT_NEWTON)
                return Double.NaN;
        } while(delta > ACCURACY);
        return x0;
    }

    /** Calculate XIRR by Bisection method*/
    private static double bisectionMethod(TreeMap<LocalDate, BigDecimal> cashFlows) {
        double leftBound = -1.0 + ACCURACY;
        double rightBound = 1.0;
        double leftValue;
        double rightValue = getSumF(cashFlows, rightBound);
        double xirr = Double.NaN;
        int seekCount;

        seekCount = 0;
        while (rightValue < 0) {
            leftBound = rightBound;
            rightBound = rightBound * 2;
            rightValue = getSumF(cashFlows, rightBound);
            if(seekCount++ > SEEK_COUNT_LIMIT_BISECTION)
                return Double.NaN;
        }
        leftValue = getSumF(cashFlows, leftBound);

        if (leftValue < rightValue) {
            double value;
            seekCount = 0;
            do {
                if(seekCount++ > SEEK_COUNT_LIMIT_BISECTION)
                    return Double.NaN;
                xirr = (leftBound + rightBound) / 2;
                value = getSumF(cashFlows, xirr);
                if (value < 0)
                    leftBound = xirr;
                else
                    rightBound = xirr;
            } while (Math.abs(value) > ACCURACY);

        }
        return xirr;
    }
}
