package com.github.it89.investordiary;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XIRRTest {
    @Test
    public void testGetXIRR() throws Exception {
        TreeMap<LocalDate, BigDecimal> cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(-10000));
        cashFlow.put(LocalDate.of(2017, 5, 5), BigDecimal.valueOf(11000));
        double xirr = XIRR.getXIRR(cashFlow);
        assertEquals("Test", 73577, Math.round(xirr * 1000000));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(10000));
        cashFlow.put(LocalDate.of(2017, 5, 5), BigDecimal.valueOf(-11000));
        xirr = XIRR.getXIRR(cashFlow);
        assertEquals("Test", 73577, Math.round(xirr * 1000000));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(10000));
        cashFlow.put(LocalDate.of(2017, 5, 5), BigDecimal.valueOf(11000));
        xirr = XIRR.getXIRR(cashFlow);
        assertTrue("Is negative infinity", Double.isInfinite(xirr));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(-10000));
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(11000));
        xirr = XIRR.getXIRR(cashFlow);
        assertTrue("Is negative infinity", Double.isInfinite(xirr));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(-10000));
        cashFlow.put(LocalDate.of(2016, 2, 1), BigDecimal.valueOf(4000));
        cashFlow.put(LocalDate.of(2016, 5, 11), BigDecimal.valueOf(-1000));
        xirr = XIRR.getXIRR(cashFlow);
        assertTrue("Is NaN", Double.isNaN(xirr));

        cashFlow = new TreeMap<>();
        cashFlow.put(LocalDate.of(2016, 1, 1), BigDecimal.valueOf(-10000));
        cashFlow.put(LocalDate.of(2016, 2, 1), BigDecimal.valueOf(4000));
        cashFlow.put(LocalDate.of(2016, 5, 11), BigDecimal.valueOf(-1000));
        cashFlow.put(LocalDate.of(2017, 5, 5), new BigDecimal("5500.32"));
        xirr = XIRR.getXIRR(cashFlow);
        assertEquals("Test", -164849, Math.round(xirr * 1000000));
    }
}
