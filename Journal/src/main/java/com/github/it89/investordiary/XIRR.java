package com.github.it89.investordiary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import static java.time.temporal.ChronoUnit.DAYS;

public class XIRR {
    public static void main(String[] args) {
        LocalDate d0 = LocalDate.of(2016, 1, 1);
        LocalDate d1 = LocalDate.of(2016, 9, 1);
        //Period p = d0.until(d1, )
        System.out.println(DAYS.between(d0, d1));
        TreeMap<LocalDate, BigDecimal> cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(-10000));
        cashFlow.put(LocalDate.of(2017, 5, 5), BigDecimal.valueOf(11000));
        System.out.println(getXIRR(cashFlow, 0.1));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(10000));
        cashFlow.put(LocalDate.of(2017, 5, 5), BigDecimal.valueOf(-11000));
        System.out.println(getXIRR(cashFlow, 0.1));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(10000));
        cashFlow.put(LocalDate.of(2017, 5, 5), BigDecimal.valueOf(11000));
        System.out.println(getXIRR(cashFlow, 0.1));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(-10000));
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(11000));
        System.out.println(getXIRR(cashFlow));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(-10000));
        cashFlow.put(LocalDate.of(2016, 2, 1), BigDecimal.valueOf(4000));
        cashFlow.put(LocalDate.of(2016, 5, 11), BigDecimal.valueOf(-1000));
        System.out.println(getXIRR(cashFlow));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(-10000));
        cashFlow.put(LocalDate.of(2016, 2, 1), BigDecimal.valueOf(4000));
        cashFlow.put(LocalDate.of(2016, 5, 11), BigDecimal.valueOf(-1000));
        cashFlow.put(LocalDate.of(2017, 5, 5), new BigDecimal("5500.32"));
        System.out.println(getXIRR(cashFlow));
    }

    private static double f(double cashFlow, long dayCount, double xirr) {
        return cashFlow * Math.pow(1.0 + xirr, dayCount / 365.0);
    }

    private static double df(double cashFlow, long dayCount, double xirr) {
        return (1.0 / 365.0) * dayCount * cashFlow * Math.pow(xirr + 1.0, dayCount / 365.0 - 1.0);
    }

    private static double getSumF(TreeMap<LocalDate, BigDecimal> cashFlows, double xirr) {
        double result = 0;
        LocalDate firstDate = cashFlows.firstKey();
        for(Map.Entry<LocalDate, BigDecimal> entry : cashFlows.entrySet())
            result += f(entry.getValue().doubleValue(), DAYS.between(entry.getKey(), firstDate), xirr);
        return result;
    }

    private static double getSumFD(TreeMap<LocalDate, BigDecimal> cashFlows, double xirr) {
        double result = 0;
        LocalDate firstDate = cashFlows.firstKey();
        for(Map.Entry<LocalDate, BigDecimal> entry : cashFlows.entrySet())
            result += df(entry.getValue().doubleValue(), DAYS.between(entry.getKey(), firstDate), xirr);
        return result;
    }

    /** Calculate XIRR by Newton's method*/
    public static double getXIRR(TreeMap<LocalDate, BigDecimal> cashFlows, double guess) {
        double x0 = guess;
        double x1= 0;
        double delta = 0;
        int i = 0;
        do {
            x1 = x0 - getSumF(cashFlows, x0) / getSumFD(cashFlows, x0);
            delta = Math.abs(x0 - x1);
            x0 = x1;
            i++;
            if(i > 100)
                return Double.NaN;
        } while(delta > 0.000001);
        return x0;
    }

    public static double getXIRR(TreeMap<LocalDate, BigDecimal> cashFlows) {
        return getXIRR(cashFlows, 0.1);
    }
}
