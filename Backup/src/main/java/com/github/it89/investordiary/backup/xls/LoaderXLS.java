package com.github.it89.investordiary.backup.xls;

import com.github.it89.investordiary.stockmarket.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by Axel on 17.09.2016.
 */
public class LoaderXLS {
    private final StockMarketDaybook daybook;
    private final String filename;
    private HSSFWorkbook exelBook;

    private static final int CELL_ASSET_TICKER = 0;
    private static final int CELL_ASSET_CAPTION = 1;
    private static final int CELL_ASSET_ASSET_TYPE = 2;

    private static final int CELL_TRADE_TRADE_NUMBER = 3;
    private static final int CELL_TRADE_ASSET_TICKER = 1;
    private static final int CELL_TRADE_APPLICATION_NUMBER = 2;
    private static final int CELL_TRADE_DATE = 4;
    private static final int CELL_TRADE_TIME = 5;
    private static final int CELL_TRADE_OPERATION = 6;
    private static final int CELL_TRADE_AMOUNT = 7;
    private static final int CELL_TRADE_CURRENCY = 8;
    private static final int CELL_TRADE_PRICE = 9;
    private static final int CELL_TRADE_VOLUME = 10;
    private static final int CELL_TRADE_COMMISSION = 11;

    public LoaderXLS(StockMarketDaybook stockMarketDaybook, String filename) {
        this.daybook = stockMarketDaybook;
        this.filename = filename;
    }

    public void load() throws IOException {
        exelBook = new HSSFWorkbook(new FileInputStream(filename));

        loadAsset();
        loadTradeStock();

        exelBook.close();
    }

    public void loadAsset() {
        HSSFSheet sheet = exelBook.getSheet("Asset");

        int rowNum = 1;
        HSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            Asset asset = new Asset(row.getCell(CELL_ASSET_TICKER).getStringCellValue());
            asset.setCaption(row.getCell(CELL_ASSET_CAPTION).getStringCellValue());
            asset.setAssetType(AssetType.valueOf(row.getCell(CELL_ASSET_ASSET_TYPE).getStringCellValue()));

            daybook.addAsset(asset);
            row = sheet.getRow(++rowNum);
        }
    }

    public void loadTradeStock() {
        HSSFSheet sheet = exelBook.getSheet("TradeStock");

        int rowNum = 1;
        HSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            row.getCell(CELL_TRADE_TRADE_NUMBER).setCellType(Cell.CELL_TYPE_STRING);
            TradeStock tradeStock = new TradeStock(row.getCell(CELL_TRADE_TRADE_NUMBER).getStringCellValue());

            tradeStock.setAsset(daybook.getAsset(row.getCell(CELL_TRADE_ASSET_TICKER).getStringCellValue()));

            row.getCell(CELL_TRADE_APPLICATION_NUMBER).setCellType(Cell.CELL_TYPE_STRING);
            tradeStock.setApplicationNumber(row.getCell(CELL_TRADE_APPLICATION_NUMBER).getStringCellValue());

            Date date = row.getCell(CELL_TRADE_DATE).getDateCellValue();
            tradeStock.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            date = row.getCell(CELL_TRADE_TIME).getDateCellValue();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            LocalTime time = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            tradeStock.setTime(time);

            tradeStock.setOperation(TradeOperation.valueOf(row.getCell(CELL_TRADE_OPERATION).getStringCellValue()));
            tradeStock.setAmount((long)row.getCell(CELL_TRADE_AMOUNT).getNumericCellValue());
            tradeStock.setCurrency(Currency.getInstance(row.getCell(CELL_TRADE_CURRENCY).getStringCellValue()));

            row.getCell(CELL_TRADE_PRICE).setCellType(Cell.CELL_TYPE_STRING);
            tradeStock.setPrice(new BigDecimal(row.getCell(CELL_TRADE_PRICE).getStringCellValue()));

            row.getCell(CELL_TRADE_VOLUME).setCellType(Cell.CELL_TYPE_STRING);
            tradeStock.setVolume(new BigDecimal(row.getCell(CELL_TRADE_VOLUME).getStringCellValue()));

            row.getCell(CELL_TRADE_COMMISSION).setCellType(Cell.CELL_TYPE_STRING);
            tradeStock.setCommission(new BigDecimal(row.getCell(CELL_TRADE_COMMISSION).getStringCellValue()));

            daybook.addTradeStock(tradeStock);
            row = sheet.getRow(++rowNum);
        }

    }
}