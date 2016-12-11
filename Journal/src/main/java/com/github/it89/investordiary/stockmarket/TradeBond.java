package com.github.it89.investordiary.stockmarket;

import java.math.BigDecimal;

/**
 * Created by Axel on 17.09.2016.
 */
public class TradeBond extends Trade{
    private BigDecimal pricePct;
    private double nominal;
    private BigDecimal accumulatedCouponYield;

    public TradeBond(String tradeNumber) {
        super(tradeNumber);
    }

    public BigDecimal getPricePct() {
        return pricePct;
    }

    public void setPricePct(BigDecimal pricePct) {
        this.pricePct = pricePct;
        calcNominal();
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }

    public BigDecimal getAccumulatedCouponYield() {
        return accumulatedCouponYield;
    }

    public void setAccumulatedCouponYield(BigDecimal accumulatedCouponYield) {
        this.accumulatedCouponYield = accumulatedCouponYield;
    }

    @Override
    public String toString() {
        return "TradeBond{" +
                "asset=" + asset +
                ", applicationNumber='" + applicationNumber + '\'' +
                ", tradeNumber='" + tradeNumber + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", operation=" + operation +
                ", amount=" + amount +
                ", currency=" + currency +
                ", volume=" + volume +
                ", commission=" + commission +
                ", stageNumber=" + stageNumber +
                ", pricePct=" + pricePct +
                ", nominal=" + nominal +
                ", accumulatedCouponYield=" + accumulatedCouponYield +
                '}';
    }

    @Override
    public void setVolume(BigDecimal volume) {
        super.setVolume(volume);
        calcNominal();

    }

    @Override
    public void setAmount(long amount) {
        super.setAmount(amount);
        calcNominal();
    }

    private void calcNominal() {
        if(amount == 0 || volume == null || pricePct == null)
            nominal = 0;
        else
            nominal = volume.doubleValue() / (double)amount / pricePct.doubleValue() * 100;
    }
}
