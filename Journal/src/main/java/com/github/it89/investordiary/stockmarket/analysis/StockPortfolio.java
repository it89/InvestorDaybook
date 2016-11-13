package com.github.it89.investordiary.stockmarket.analysis;

import com.github.it89.investordiary.stockmarket.*;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Клиентский портфель.
 * Сейчас учитывается только прибыль, пополнений нет
 */
public class StockPortfolio {
    /** Прибыль, с которой еще не удержан налог*/
    private BigDecimal sumProfitNotTaxed = new BigDecimal(0);
    /** Прибыль с учетом уже удержанного налога (включая прибыль, которая не облагается налогом*/
    private BigDecimal sumProfitTaxed = new BigDecimal(0);
    /** Сумма налога*/
    private BigDecimal sumTax = new BigDecimal(0);
    /** Спиок акций и их количество*/
    private HashMap<Asset, Long> assetCountMap = new HashMap();

    public BigDecimal getSumProfitNotTaxed() {
        return sumProfitNotTaxed;
    }

    public void setSumProfitNotTaxed(BigDecimal sumProfitNotTaxed) {
        this.sumProfitNotTaxed = sumProfitNotTaxed;
    }

    public HashMap<Asset, Long> getAssetCountMap() {
        return assetCountMap;
    }

    public void setAssetCountMap(HashMap<Asset, Long> assetCountMap) {
        this.assetCountMap = assetCountMap;
    }

    public BigDecimal getSumProfitTaxed() {
        return sumProfitTaxed;
    }

    public void setSumProfitTaxed(BigDecimal sumProfitTaxed) {
        this.sumProfitTaxed = sumProfitTaxed;
    }

    public BigDecimal getSumTax() {
        return sumTax;
    }

    public void setSumTax(BigDecimal sumTax) {
        this.sumTax = sumTax;
    }

    /**
     * Изменяет состояние портфеля.
     * @param trade - сделка
     */
    public void add(Trade trade) {
        BigDecimal volume = trade.getVolume();
        Asset asset = trade.getAsset();
        long amount = trade.getAmount();

        if(trade.getOperation() == TradeOperation.BUY)
            volume = volume.negate();
        else
            amount *= -1;

        sumProfitNotTaxed = sumProfitNotTaxed.add(volume);
        sumProfitNotTaxed = sumProfitNotTaxed.add(trade.getCommission().negate());
        if(trade instanceof TradeBond) {
            sumProfitNotTaxed = sumProfitNotTaxed.add(((TradeBond) trade).getAccumulatedCouponYield().negate());
        }
        if(assetCountMap.containsKey(asset)) {
            assetCountMap.put(asset, assetCountMap.get(asset) + amount);
        } else {
            assetCountMap.put(asset, amount);
        }
    }

    public void Add(AssetIncome assetIncome) {
        BigDecimal volume = assetIncome.getVolume();
        BigDecimal tax = assetIncome.getTax();

        sumProfitTaxed = sumProfitTaxed.add(volume.add(tax.negate()));
        sumTax = sumTax.add(tax);
    }
}
