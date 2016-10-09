package com.github.it89.investordiary.stockmarket;

/**
 * Created by Axel on 08.10.2016.
 */
public class TradeTag {
    private final String tag;

    public TradeTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "TradeTag{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
