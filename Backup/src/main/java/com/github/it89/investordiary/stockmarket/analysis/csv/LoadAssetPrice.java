package com.github.it89.investordiary.stockmarket.analysis.csv;

import com.github.it89.investordiary.stockmarket.Asset;
import com.github.it89.investordiary.stockmarket.AssetPriceHistory;
import com.github.it89.investordiary.stockmarket.StockMarketDaybook;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Axel on 13.11.2016.
 */
public class LoadAssetPrice {
    /*public static TreeSet<AssetPrice> load(StockMarketDaybook stockMarketDaybook, String fileName) throws IOException {
        TreeSet<AssetPrice> set = new TreeSet<AssetPrice>();
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("<TICKER>", "<PER>", "<DATE>", "<TIME>", "<CLOSE>").parse(in);
        LocalTime timeCloseDay = stockMarketDaybook.getTimeCloseDay();
        for (CSVRecord record : records) {
            String ticker = record.get("<TICKER>");
            if(!"<TICKER>".equals(ticker)) {
                Asset asset = stockMarketDaybook.getAsset(ticker);
                String stringDate = record.get("<DATE>");
                int year = Integer.valueOf(stringDate.substring(0, 4));
                int month = Integer.valueOf(stringDate.substring(4, 6));
                int day = Integer.valueOf(stringDate.substring(6, 8));
                LocalDateTime dateTime = LocalDateTime.of(year, month, day, timeCloseDay.getHour(), timeCloseDay.getMinute());
                AssetPrice assetPrice = new AssetPrice(asset, dateTime, new BigDecimal(record.get("<CLOSE>")));
                set.add(assetPrice);
            }
        }
        return set;
    }*/

    public static void load(AssetPriceHistory assetPriceHistory, StockMarketDaybook stockMarketDaybook, String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader("<TICKER>", "<PER>", "<DATE>", "<TIME>", "<CLOSE>").parse(in);
        LocalTime timeCloseDay = stockMarketDaybook.getTimeCloseDay();
        for (CSVRecord record : records) {
            String ticker = record.get("<TICKER>");
            if(!"<TICKER>".equals(ticker)) {
                Asset asset = stockMarketDaybook.getAsset(ticker);
                String stringDate = record.get("<DATE>");
                int year = Integer.valueOf(stringDate.substring(0, 4));
                int month = Integer.valueOf(stringDate.substring(4, 6));
                int day = Integer.valueOf(stringDate.substring(6, 8));
                LocalDateTime dateTime = LocalDateTime.of(year, month, day, timeCloseDay.getHour(), timeCloseDay.getMinute());

                assetPriceHistory.put(asset, dateTime, new BigDecimal(record.get("<CLOSE>")));
            }
        }
    }

    public static void loadAll(AssetPriceHistory assetPriceHistory, StockMarketDaybook stockMarketDaybook, String folderName) throws IOException {
        File folder = new File(folderName);
        String[] files = folder.list(new FilenameFilter() {
            public boolean accept(File folder, String name) {
                if(name.toLowerCase().endsWith(".csv"))
                    return true;
                return false;
            }
        });
        for(String fileName : files)
            load(assetPriceHistory, stockMarketDaybook, folderName + "\\" + fileName);
    }
}
