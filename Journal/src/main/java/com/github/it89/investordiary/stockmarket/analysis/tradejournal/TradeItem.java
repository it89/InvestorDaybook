package com.github.it89.investordiary.stockmarket.analysis.tradejournal;

import com.github.it89.investordiary.stockmarket.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

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
    final Set<TradeTag> tradeTags = new HashSet();

    public TradeItem(LocalDate date, LocalTime time, Asset asset, long amount, BigDecimal volume, BigDecimal commission, BigDecimal accumulatedCouponYield) {
        this.date = date;
        this.time = time;
        this.asset = asset;
        this.amount = amount;
        this.volume = volume;
        this.commission = commission;
        this.accumulatedCouponYield = accumulatedCouponYield;
    }

    public TradeItem(Trade trade) {
        TradeOperation operation = trade.getOperation();
        BigDecimal accumulatedCouponYield = null;
        if(trade instanceof TradeBond) {
            accumulatedCouponYield = ((TradeBond) trade).getAccumulatedCouponYield();
        }

        this.date = trade.getDate();
        this.time = trade.getTime();
        this.asset = trade.getAsset();

        if(operation == TradeOperation.BUY)
            this.amount = trade.getAmount();
        else
            this.amount = -trade.getAmount();

        if(operation == TradeOperation.BUY)
            this.volume = trade.getVolume().negate();
        else
            this.volume = trade.getVolume();

        this.commission = trade.getCommission().negate();

        if(accumulatedCouponYield != null)
            if(operation == TradeOperation.BUY)
                this.accumulatedCouponYield = accumulatedCouponYield.negate();
            else
                this.accumulatedCouponYield = accumulatedCouponYield;
        else
            this.accumulatedCouponYield = null;
    }

    public void addTags(Set<TradeTag> tags) {
        tradeTags.addAll(tags);
    }

    public BigDecimal getTotalProfit() {
        BigDecimal totalProfit = volume.add(commission);
        if(accumulatedCouponYield != null)
            totalProfit = totalProfit.add(accumulatedCouponYield);
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

    public Set<TradeTag> getTradeTags() {
        return tradeTags;
    }

    @Nullable
    public BigDecimal getAccumulatedCouponYield() {
        return accumulatedCouponYield;
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
                ", tradeTags=" + tradeTags +
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
