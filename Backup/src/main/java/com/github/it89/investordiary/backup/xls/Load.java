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
public class Load {
    public static void main(String[] args) throws IOException {
        StockMarketDaybook daybook = load();

        for(Map.Entry<String, Asset> entry: daybook.getAssets().entrySet()) {
            System.out.println(entry.getValue());
        }

        for(Map.Entry<String, TradeStock> entry: daybook.getTradeStocks().entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public static StockMarketDaybook load() throws IOException {
        String filename = "test.xls";
        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(filename));
        StockMarketDaybook daybook = new StockMarketDaybook();

        loadAsset(myExcelBook, daybook);
        loadTradeStock(myExcelBook, daybook);

        myExcelBook.close();
        return daybook;
    }

    public static void loadAsset(HSSFWorkbook workbook, StockMarketDaybook daybook) {
        HSSFSheet sheet = workbook.getSheet("Asset");

        int rowNum = 0;
        HSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            Asset asset = new Asset(row.getCell(0).getStringCellValue());
            asset.setCaption(row.getCell(1).getStringCellValue());
            asset.setAssetType(AssetType.valueOf(row.getCell(2).getStringCellValue()));

            daybook.addAsset(asset);
            row = sheet.getRow(++rowNum);
        }
    }

    public static void loadTradeStock(HSSFWorkbook workbook, StockMarketDaybook daybook) {
        HSSFSheet sheet = workbook.getSheet("TradeStock");

        int rowNum = 0;
        HSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
            TradeStock tradeStock = new TradeStock(row.getCell(3).getStringCellValue());

            tradeStock.setAsset(daybook.getAsset(row.getCell(1).getStringCellValue()));

            row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
            tradeStock.setApplicationNumber(row.getCell(2).getStringCellValue());

            Date date = row.getCell(4).getDateCellValue();
            tradeStock.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            date = row.getCell(5).getDateCellValue();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            LocalTime time = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            tradeStock.setTime(time);

            tradeStock.setOperation(TradeOperation.valueOf(row.getCell(6).getStringCellValue()));
            tradeStock.setAmount((long)row.getCell(7).getNumericCellValue());
            tradeStock.setCurrency(Currency.getInstance(row.getCell(8).getStringCellValue()));

            row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
            tradeStock.setPrice(new BigDecimal(row.getCell(9).getStringCellValue()));

            row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
            tradeStock.setVolume(new BigDecimal(row.getCell(10).getStringCellValue()));

            row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
            tradeStock.AddComission(new TradeCommission(new BigDecimal(row.getCell(11).getStringCellValue())));

            daybook.addTradeStock(tradeStock);
            row = sheet.getRow(++rowNum);
        }

    }
}