package com.github.it89.investordiary.stockmarket.analysis.tradejournal;

import com.github.it89.investordiary.stockmarket.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

/**
 * Created by Axel on 12.11.2016.
 */
public class TradeItem {
    private final LocalDate date;
    private final LocalTime time;
    private final Asset asset;
    private final long amount;
    private final BigDecimal volume;
    private final BigDecimal commission;
    private @Nullable final BigDecimal accumulatedCouponYield;
    private @Nullable final Integer stageNumber;

    public TradeItem(LocalDate date, LocalTime time, Asset asset, long amount, BigDecimal volume, BigDecimal commission, BigDecimal accumulatedCouponYield, Integer stageNumber) {
        this.date = date;
        this.time = time;
        this.asset = asset;
        this.amount = amount;
        this.volume = volume;
        this.commission = commission;
        this.accumulatedCouponYield = accumulatedCouponYield;
        this.stageNumber = stageNumber;
    }

    public TradeItem(Trade trade) {
        TradeOperation operation = trade.getOperation();

        this.date = trade.getDate();
        this.time = trade.getTime();
        this.asset = trade.getAsset();
        this.stageNumber = trade.getStageNumber();

        if(operation == TradeOperation.BUY)
            this.amount = trade.getAmount();
        else
            this.amount = -trade.getAmount();

        BigDecimal accumulatedCouponYield = null;
        if(trade instanceof TradeBond)
            accumulatedCouponYield = ((TradeBond) trade).getAccumulatedCouponYield();
        if(accumulatedCouponYield != null && operation == TradeOperation.BUY)
            accumulatedCouponYield = accumulatedCouponYield.negate();
        this.accumulatedCouponYield = accumulatedCouponYield;

        BigDecimal volume = trade.getVolume();
        if(operation == TradeOperation.BUY)
            volume = volume.negate();
        if(accumulatedCouponYield != null)
            volume = volume.add(accumulatedCouponYield.negate());
        this.volume = volume;

        this.commission = trade.getCommission().negate();


    }

    public BigDecimal getTotalProfit() {
        BigDecimal totalProfit = volume.add(commission);
        return totalProfit;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Asset getAsset() {
        return asset;
    }

    public long getAmount() {
        return amount;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    @Nullable
    public BigDecimal getAccumulatedCouponYield() {
        return accumulatedCouponYield;
    }

    @Nullable
    public Integer getStageNumber() {
        return stageNumber;
    }

    @Override
    public String toString() {
        return "TradeItem{" +
                "date=" + date +
                ", time=" + time +
                ", asset=" + asset +
                ", amount=" + amount +
                ", volume=" + volume +
                ", commission=" + commission +
                ", accumulatedCouponYield=" + accumulatedCouponYield +
                ", asset=" + asset +
                ", stageNumber=" + stageNumber +
                '}';
    }

    public static class CashFlowItemComp implements Comparator<TradeItem> {
        public int compare(TradeItem o1, TradeItem o2) {
            int result = o1.date.compareTo(o2.date);
            if(result == 0) {
                result = o1.time.compareTo(o2.time);
                if(result == 0) {
                    result = Integer.compare(o1.hashCode(), o2.hashCode());
                }
            }
            return result;
        }
    }


}
