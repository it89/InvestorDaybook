package com.github.it89.investordiary.stockmarket;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Axel on 11.12.2016.
 */
public class BondNominalHistory {
    private final TreeMap<Asset, TreeMap<LocalDate, BigDecimal>> treeADN = new TreeMap<>();

    public BondNominalHistory(TreeSet<TradeBond> bonds) {
        for(TradeBond bond : bonds) {
            BigDecimal nominal = BigDecimal.valueOf(bond.getNominal());
            nominal = nominal.setScale(2, BigDecimal.ROUND_HALF_UP);
            put(bond.getAsset(), bond.getDate(), nominal);
        }
    }

    public void put(Asset asset, LocalDate date, BigDecimal nominal) {
        if(treeADN.containsKey(asset)) {
            TreeMap<LocalDate, BigDecimal> treeDN = treeADN.get(asset);
            treeDN.put(date, nominal);
        } else {
            TreeMap<LocalDate, BigDecimal> treeDN = new TreeMap<>();
            treeDN.put(date, nominal);
            treeADN.put(asset, treeDN);
        }
    }

    @Nullable
    public BigDecimal getNominal(Asset asset, LocalDate dateTime) {
        BigDecimal nominal = null;
        if(treeADN.containsKey(asset)) {
            TreeMap<LocalDate, BigDecimal> treeDP = treeADN.get(asset);
            nominal = treeDP.floorEntry(dateTime).getValue();
        }
        return nominal;
    }
}
