package com.github.it89.investordiary.stockmarket;

/**
 * Created by Axel on 17.09.2016.
 */
public enum AssetType {
    STOCK(false),
    BOND(true);

    private final boolean isBond;

    AssetType(boolean isBond) {
        this.isBond = isBond;
    }

    public boolean isBond() {
        return isBond;
    }
}
