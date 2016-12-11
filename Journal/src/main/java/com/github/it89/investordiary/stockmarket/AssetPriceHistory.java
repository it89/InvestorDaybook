package com.github.it89.investordiary.stockmarket;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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

    public BigDecimal getPrice(Asset asset, LocalDate date) {
        LocalDateTime dateTime = date.atTime(19, 0);
        return getPrice(asset, dateTime);
    }

    public TreeSet<LocalDate> getDates(Set<Asset> assets, LocalDate dateTimeFrom, LocalDate dateTimeTo) {
        TreeSet<LocalDate> treeSet = new TreeSet();
        for(Asset asset : assets) {
            TreeMap<LocalDateTime, BigDecimal> treeDP = treeADP.get(asset);
            if(treeDP == null)
                throw new NullPointerException("Not exists Asset: " + asset.getTicker());
            SortedSet<LocalDateTime> dateTimes = treeDP.navigableKeySet();
            dateTimes = dateTimes.subSet(dateTimeFrom.atTime(0, 0), dateTimeTo.atTime(0, 0));
            for(LocalDateTime dateTime : dateTimes) {
                LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
                treeSet.add(date);
            }

        }
        return treeSet;
    }
}
