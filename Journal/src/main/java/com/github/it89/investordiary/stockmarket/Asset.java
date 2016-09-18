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
        return String.format("Asset{ticker='%s', caption='%s'}", ticker, caption);
    }

}
