package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.StockPortfolio;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by Axel on 23.10.2016.
 */
public class StockPortfolioTest {
    @org.junit.Test
    public void testAdd() throws Exception {
        // Add Trade
        Asset assetA = new Asset("A");
        TradeStock tradeStock = new TradeStock("A1");
        tradeStock.setAsset(assetA);
        tradeStock.setOperation(TradeOperation.BUY);
        tradeStock.setVolume(new BigDecimal("3480"));
        tradeStock.setAmount(100);
        tradeStock.setCommission(new BigDecimal("1.98"));

        StockPortfolio sp = new StockPortfolio();
        sp.add(tradeStock);
        sp.add(tradeStock);
        tradeStock.setOperation(TradeOperation.SELL);
        tradeStock.setVolume(new BigDecimal("3005"));
        tradeStock.setCommission(new BigDecimal("1.71"));
        sp.add(tradeStock);
        tradeStock.setOperation(TradeOperation.BUY);
        tradeStock.setVolume(new BigDecimal("3069"));
        tradeStock.setCommission(new BigDecimal("1.75"));
        sp.add(tradeStock);
        assertEquals("100 + 100 - 100 + 100 must be 200", new Long(200), sp.getAssetCountMap().get(assetA));
        assertEquals("-3480 - 1.98 - 3480 - 1.98 + 3005 - 1.71 - 3069 - 1.75 must be -7031.42", new BigDecimal("-7031.42"), sp.getSumProfitNotTaxed());

        Asset assetB = new Asset("B");
        TradeBond tradeBond = new TradeBond("B1");
        tradeBond.setAsset(assetA);
        tradeBond.setOperation(TradeOperation.BUY);
        tradeBond.setVolume(new BigDecimal("3000"));
        tradeBond.setAmount(3);
        tradeBond.setCommission(new BigDecimal("3"));
        tradeBond.setAccumulatedCouponYield(new BigDecimal("50"));
        sp.add(tradeBond);
        assertEquals("-7031.42 - 3000 - 3 - 50 must be -10084.42", new BigDecimal("-10084.42"), sp.getSumProfitNotTaxed());

        // Add AssetIncome
        BigDecimal sumProfitNotTaxed = sp.getSumProfitNotTaxed();
        AssetIncome assetIncome = new AssetIncome();
        assetIncome.setVolume(new BigDecimal("197.25"));
        assetIncome.setTax(new BigDecimal("26"));
        sp.Add(assetIncome);
        assetIncome.setVolume(new BigDecimal("7612"));
        assetIncome.setTax(new BigDecimal("989"));
        sp.Add(assetIncome);
        assertEquals("sumProfitNotTaxed not changed", sumProfitNotTaxed, sp.getSumProfitNotTaxed());
        assertEquals("197.25 - 26 + 7612 - 989 = 6794.25", new BigDecimal("6794.25"), sp.getSumProfitTaxed());
        assertEquals("26 + 989 = 1015", new BigDecimal("1015"), sp.getSumTax());
    }
}