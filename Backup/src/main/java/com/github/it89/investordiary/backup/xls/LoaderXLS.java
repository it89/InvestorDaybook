package com.github.it89.investordiary.backup.xls;

import com.github.it89.investordiary.stockmarket.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
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

    private static final int CELL_ASSET_TICKER = 1;
    private static final int CELL_ASSET_CAPTION = 0;
    private static final int CELL_ASSET_ASSET_TYPE = 2;

    private static final int CELL_TRADE_TRADE_NUMBER = 3;
    private static final int CELL_TRADE_APPLICATION_NUMBER = 2;
    private static final int CELL_TRADE_DATE = 4;
    private static final int CELL_TRADE_TIME = 5;
    private static final int CELL_TRADE_AMOUNT_BUY = 7;
    private static final int CELL_TRADE_AMOUNT_SELL = 8;
    private static final int CELL_TRADE_CURRENCY = 9;
    private static final int CELL_TRADE_PRICE = 10;
    private static final int CELL_TRADE_VOLUME = 12;
    private static final int CELL_TRADE_DAY_COUNT_CONVENTION = 13;
    private static final int CELL_TRADE_COMMISSION = 14;
    private static final int CELL_TRADE_ASSET_TICKER = 18;

    public LoaderXLS(StockMarketDaybook stockMarketDaybook, String filename) {
        this.daybook = stockMarketDaybook;
        this.filename = filename;
    }

    public void load() throws IOException {
        exelBook = new HSSFWorkbook(new FileInputStream(filename));

        loadAsset();
        loadTrade();

        exelBook.close();
    }

    public void loadAsset() {
        HSSFSheet sheet = exelBook.getSheet("Asset");

        int rowNum = 0;
        HSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            HSSFCell cell = row.getCell(CELL_ASSET_TICKER);
            if(cell == null)
                break;
            Asset asset = new Asset(cell.getStringCellValue());
            asset.setCaption(row.getCell(CELL_ASSET_CAPTION).getStringCellValue());
            asset.setAssetType(AssetType.valueOf(row.getCell(CELL_ASSET_ASSET_TYPE).getStringCellValue()));

            daybook.addAsset(asset);
            row = sheet.getRow(++rowNum);
        }
    }

    public void loadTrade() {
        HSSFSheet sheet = exelBook.getSheet("Trade");

        int rowNum = 1;
        HSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            HSSFCell cell = row.getCell(CELL_TRADE_ASSET_TICKER);
            if(cell == null)
                break;
            Asset asset = daybook.getAsset(cell.getStringCellValue());
            boolean isAssetStock = (asset.getAssetType() == AssetType.STOCK);

            cell = row.getCell(CELL_TRADE_TRADE_NUMBER);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String tradeNumber = cell.getStringCellValue();
            Trade trade;
            if(isAssetStock) {
                TradeStock tradeStock = new TradeStock(tradeNumber);
                daybook.addTradeStock(tradeStock);
                trade = tradeStock;

                cell = row.getCell(CELL_TRADE_PRICE);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                tradeStock.setPrice(new BigDecimal(cell.getStringCellValue()));
            } else {
                TradeBond tradeBond = new TradeBond(tradeNumber);
                daybook.addTradeBond(tradeBond);
                trade = tradeBond;

                cell = row.getCell(CELL_TRADE_PRICE);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                tradeBond.setPricePct(new BigDecimal(cell.getStringCellValue()));

                cell = row.getCell(CELL_TRADE_DAY_COUNT_CONVENTION);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                tradeBond.setDayCountConvention(new BigDecimal(cell.getStringCellValue()));
            }
            trade.setAsset(asset);

            cell = row.getCell(CELL_TRADE_APPLICATION_NUMBER);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            trade.setApplicationNumber(cell.getStringCellValue());

            Date date = row.getCell(CELL_TRADE_DATE).getDateCellValue();
            trade.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            date = row.getCell(CELL_TRADE_TIME).getDateCellValue();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            LocalTime time = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            trade.setTime(time);

            cell = row.getCell(CELL_TRADE_AMOUNT_BUY);
            long amount = 0;
            if(cell != null)
                amount = (long)cell.getNumericCellValue();
            if(amount != 0) {
                trade.setOperation(TradeOperation.BUY);
                trade.setAmount(amount);
            } else {
                trade.setOperation(TradeOperation.SELL);
                trade.setAmount((long)row.getCell(CELL_TRADE_AMOUNT_SELL).getNumericCellValue());
            }

            trade.setCurrency(Currency.getInstance(row.getCell(CELL_TRADE_CURRENCY).getStringCellValue()));

            cell = row.getCell(CELL_TRADE_VOLUME);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            trade.setVolume(new BigDecimal(cell.getStringCellValue()));

            cell = row.getCell(CELL_TRADE_COMMISSION);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            trade.setCommission(new BigDecimal(cell.getStringCellValue()));

            row = sheet.getRow(++rowNum);
        }

    }
}