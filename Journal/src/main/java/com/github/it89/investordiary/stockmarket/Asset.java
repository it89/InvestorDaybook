package com.github.it89.investordiary.stockmarket;

import java.util.TreeMap;

/**
 * Created by Axel on 17.09.2016.
 */
public class Asset implements Comparable<Asset> {
    private final String ticker;
    private String caption;
    private AssetType assetType;

    public Asset(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public int compareTo(Asset o) {
        return ticker.compareTo(o.ticker);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "ticker='" + ticker + '\'' +
                ", caption='" + caption + '\'' +
                ", assetType=" + assetType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Asset asset = (Asset) o;

        return ticker.equals(asset.ticker);
    }

    @Override
    public int hashCode() {
        return ticker.hashCode();
    }
}
