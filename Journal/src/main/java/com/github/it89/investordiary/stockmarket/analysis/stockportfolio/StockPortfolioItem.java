package com.github.it89.investordiary.stockmarket.analysis.stockportfolio;

import com.github.it89.investordiary.stockmarket.Asset;
import com.github.it89.investordiary.stockmarket.analysis.cashflow.CashFlowItem;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Created by Axel on 29.01.2017.
 */
public class StockPortfolioItem implements Comparable<StockPortfolioItem> {
    private final Asset asset;
    private final int stageNumber;
    private final LocalDate date;
    private final long amount;

    public StockPortfolioItem(Asset asset, int stageNumber, LocalDate date, long amount) {
        this.asset = asset;
        this.stageNumber = stageNumber;
        this.date = date;
        this.amount = amount;
    }

    @Override
    public int compareTo(@NotNull StockPortfolioItem o) {
        int result = this.asset.compareTo(o.asset);
        if(result == 0) {
            result = Long.compare(this.stageNumber, o.stageNumber);
            if(result == 0) {
                result = this.date.compareTo(o.date);
                if(result == 0) {
                    result = Integer.compare(this.hashCode(), o.hashCode());
                }
            }
        }
        return result;
    }

    public Asset getAsset() {
        return asset;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getAmount() {
        return amount;
    }
}
