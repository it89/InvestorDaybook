package com.github.it89.investordiary.stockmarket;

import com.github.it89.investordiary.Analytics;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Axel on 22.01.2017.
 */
@Deprecated
public class StockAnalytics extends Analytics {
    private final @Nullable Asset asset;
    private final @Nullable Integer stageNumber;

    public StockAnalytics(Asset asset, Integer stageNumber) {
        this.asset = asset;
        this.stageNumber = stageNumber;
    }

    @Nullable
    public Asset getAsset() {
        return asset;
    }

    @Nullable
    public Integer getStageNumber() {
        return stageNumber;
    }
}
