package com.github.it89.investordiary.stockmarket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Axel on 13.11.2016.
 */
@Deprecated
public class AssetPrice implements Comparable<AssetPrice> {
    private final Asset asset;
    private final LocalDateTime dateTime;
    private final BigDecimal price;

    public AssetPrice(Asset asset, LocalDateTime dateTime, BigDecimal price) {
        this.dateTime = dateTime;
        this.asset = asset;
        this.price = price;
    }

    public int compareTo(AssetPrice o) {
        int result = dateTime.compareTo(o.dateTime);
        if(result != 0)
            return result;
        result = asset.compareTo(o.asset);
        return result;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Asset getAsset() {
        return asset;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "AssetPrice{" +
                "asset=" + asset +
                ", dateTime=" + dateTime +
                ", price=" + price +
                '}';
    }
}
