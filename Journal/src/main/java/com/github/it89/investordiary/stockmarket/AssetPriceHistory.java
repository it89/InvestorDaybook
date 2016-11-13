package com.github.it89.investordiary.stockmarket;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.TreeMap;

/**
 * Created by Axel on 13.11.2016.
 */
public class AssetPriceHistory {
    private final TreeMap<Asset, TreeMap<LocalDateTime, BigDecimal>> treeADP = new TreeMap<Asset, TreeMap<LocalDateTime, BigDecimal>>();

    public void put(Asset asset, LocalDateTime dateTime, BigDecimal price) {
        if(treeADP.containsKey(asset)) {
            TreeMap<LocalDateTime, BigDecimal> treeDP = treeADP.get(asset);
            treeDP.put(dateTime, price);
        } else {
            TreeMap<LocalDateTime, BigDecimal> treeDP = new TreeMap<LocalDateTime, BigDecimal>();
            treeDP.put(dateTime, price);
            treeADP.put(asset, treeDP);
        }
    }

    @Nullable
    public BigDecimal getPrice(Asset asset, LocalDateTime dateTime) {
        BigDecimal price = null;
        if(treeADP.containsKey(asset)) {
            TreeMap<LocalDateTime, BigDecimal> treeDP = treeADP.get(asset);
            if(treeDP.containsKey(dateTime))
                price = treeDP.get(dateTime);
        }
        return price;
    }
}
