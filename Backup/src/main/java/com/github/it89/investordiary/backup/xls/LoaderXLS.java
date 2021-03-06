package com.github.it89.investordiary.backup.xls;

import com.github.it89.investordiary.stockmarket.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private static final int CELL_TRADE_STAGE_NUMBER = 19;

    private static final int CELL_CASH_FLOW_DATE = 0;
    private static final int CELL_CASH_FLOW_VOLUME = 2;
    private static final int CELL_CASH_FLOW_COMMENT = 3;
    private static final int CELL_CASH_FLOW_TYPE = 4;
    private static final int CELL_CASH_FLOW_ASSET_TICKER = 5;
    private static final int CELL_CASH_FLOW_STAGE_NUMBER = 6;
    private static final int CELL_CASH_FLOW_TAX = 7;
    private static final int CELL_CASH_FLOW_EBT = 8;

    private static final int CELL_PORTFOLIO_COST = 14;
    private static final int CELL_PORTFOLIO_ASSET_TICKER = 16;
    private static final int CELL_PORTFOLIO_ACCUMULATED_COUPON = 12;

    public LoaderXLS(StockMarketDaybook stockMarketDaybook, String filename) {
        this.daybook = stockMarketDaybook;
        this.filename = filename;
    }

    public void load() throws IOException {
        exelBook = new HSSFWorkbook(new FileInputStream(filename));

        loadAsset();
        loadTrade();
        loadCashFlow();

        exelBook.close();
    }

    public void loadAsset() {
        HSSFSheet sheet = exelBook.getSheet("Asset");

        int rowNum = 1;
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
            String ticker = cell.getStringCellValue();
            if(ticker.length() == 0)
                break;
            Asset asset = daybook.getAsset(ticker);
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
                tradeBond.setAccumulatedCouponYield(new BigDecimal(cell.getStringCellValue()));
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

            trade.setStageNumber((int)row.getCell(CELL_TRADE_STAGE_NUMBER).getNumericCellValue());

            row = sheet.getRow(++rowNum);
        }
    }

    public void loadCashFlow() {
        HSSFSheet sheet = exelBook.getSheet("CashFlow");

        int rowNum = 1;
        HSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            CashFlow cashFlow;
            HSSFCell cell = row.getCell(CELL_CASH_FLOW_COMMENT);
            if(cell == null)
                break;
            String comment = cell.getStringCellValue();

            cell = row.getCell(CELL_CASH_FLOW_TYPE);
            String typeCode = null;
            if(cell != null) {
                typeCode = cell.getStringCellValue();
                if(typeCode != null)
                    typeCode = typeCode.trim();
            }
            if(typeCode != null && !typeCode.isEmpty()) {
                CashFlowType cashFlowType = CashFlowType.valueOf(typeCode);
                if(cashFlowType.isAssetIncome()) {
                    AssetIncome assetIncome = new AssetIncome();
                    assetIncome.setAsset(daybook.getAsset(row.getCell(CELL_CASH_FLOW_ASSET_TICKER).getStringCellValue()));

                    cell = row.getCell(CELL_CASH_FLOW_TAX);
                    if(cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().isEmpty())
                            assetIncome.setTax(new BigDecimal(cell.getStringCellValue()));
                    }

                    cell = row.getCell(CELL_CASH_FLOW_EBT);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    assetIncome.setVolume(new BigDecimal(cell.getStringCellValue()));

                    assetIncome.setStageNumber((int)row.getCell(CELL_CASH_FLOW_STAGE_NUMBER).getNumericCellValue());
                    cashFlow = assetIncome;
                } else {
                    cashFlow = new CashFlow();
                    cell = row.getCell(CELL_CASH_FLOW_VOLUME);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cashFlow.setVolume(new BigDecimal(cell.getStringCellValue()));
                }

                cashFlow.setCashFlowType(cashFlowType);

                Date date = row.getCell(CELL_CASH_FLOW_DATE).getDateCellValue();
                cashFlow.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                cashFlow.setComment(comment);

                daybook.addCashFlow(cashFlow);
            }
            row = sheet.getRow(++rowNum);
        }
    }

    public HashMap<Asset, BigDecimal> getPortfolioCostMap() throws IOException {
        exelBook = new HSSFWorkbook(new FileInputStream(filename));
        HSSFSheet sheet = exelBook.getSheet("Portfolio");
        HashMap<Asset, BigDecimal> portfolio = new HashMap<Asset, BigDecimal>();

        int rowNum = 1;
        HSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            HSSFCell cell = row.getCell(0);
            if (cell == null)
                break;
            cell = row.getCell(CELL_PORTFOLIO_ASSET_TICKER);
            if(cell == null) {
                row = sheet.getRow(++rowNum);
                continue;
            }
            String ticker = cell.getStringCellValue();
            if(ticker.length() == 0){
                row = sheet.getRow(++rowNum);
                continue;
            }
            Asset asset = daybook.getAsset(ticker);
            cell = row.getCell(CELL_PORTFOLIO_COST);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            BigDecimal cost = new BigDecimal(cell.getStringCellValue());

            cell = row.getCell(CELL_PORTFOLIO_ACCUMULATED_COUPON);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String cellText = cell.getStringCellValue();
            if(!cellText.isEmpty())
                cost = cost.add(new BigDecimal(cellText));

            portfolio.put(asset, cost);

            row = sheet.getRow(++rowNum);
        }
        exelBook.close();
        return portfolio;
    }
}