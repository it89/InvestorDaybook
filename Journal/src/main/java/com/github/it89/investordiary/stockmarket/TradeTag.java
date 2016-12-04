package com.github.it89.investordiary.stockmarket;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Axel on 08.10.2016.
 */
public class TradeTag implements Comparable<TradeTag>{
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

    public int compareTo(@NotNull TradeTag o) {
        return tag.compareTo(o.tag);
    }
}
