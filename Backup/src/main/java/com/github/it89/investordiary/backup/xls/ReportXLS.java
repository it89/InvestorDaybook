package com.github.it89.investordiary.backup.xls;

import com.github.it89.investordiary.stockmarket.TradeTag;
import com.github.it89.investordiary.stockmarket.analysis.CashFlowItem;
import com.github.it89.investordiary.stockmarket.analysis.CashFlowJournal;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistory;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistoryItem;
import com.github.it89.investordiary.stockmarket.analysis.tradejournal.TradeItem;
import com.github.it89.investordiary.stockmarket.analysis.tradejournal.TradeJournal;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by Axel on 24.10.2016.
 */
public class ReportXLS {
    public static void exportCashFlowJournal(CashFlowJournal journal, String filename) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("CashFlowJournal");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Date");
        row.createCell(1).setCellValue("Time");
        row.createCell(2).setCellValue("Type");
        row.createCell(3).setCellValue("Money");
        row.createCell(4).setCellValue("Tax");
        row.createCell(5).setCellValue("Tags");

        int rowNumber = 1;
        for(CashFlowItem item : journal.getItems()) {
            row = sheet.createRow(rowNumber);
            row.createCell(0).setCellValue(item.getDate().toString());
            row.createCell(1).setCellValue(item.getTime().toString());
            row.createCell(2).setCellValue(item.getCashFlowType().toString());
            row.createCell(3).setCellValue(item.getMoney().doubleValue());
            BigDecimal tax = item.getTax();
            if(tax != null)
                row.createCell(4).setCellValue(tax.doubleValue());
            rowNumber++;
            int tagNum = 0;
            for(TradeTag tag : item.getTradeTags()) {
                row.createCell(5 + tagNum++).setCellValue(tag.getTag());
            }
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);

        book.write(new FileOutputStream(filename));
        book.close();
    }

    public static void exportTradeJournal(TradeJournal journal, String filename) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("TradeJournal");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Date");
        row.createCell(1).setCellValue("Time");
        row.createCell(2).setCellValue("Asset");
        row.createCell(3).setCellValue("Amount");
        row.createCell(4).setCellValue("Volume");
        row.createCell(5).setCellValue("Commission");
        row.createCell(6).setCellValue("Accumulated Coupon Yield");
        row.createCell(7).setCellValue("Total Profit");
        row.createCell(8).setCellValue("Tags");

        int rowNumber = 1;
        for(TradeItem item : journal.getTradeItems()) {
            row = sheet.createRow(rowNumber);
            row.createCell(0).setCellValue(item.getDate().toString());
            row.createCell(1).setCellValue(item.getTime().toString());
            row.createCell(2).setCellValue(item.getAsset().getCaption());
            row.createCell(3).setCellValue(item.getAmount());
            row.createCell(4).setCellValue(item.getVolume().doubleValue());
            row.createCell(5).setCellValue(item.getCommission().doubleValue());

            BigDecimal accumulatedCouponYield = item.getAccumulatedCouponYield();
            if(accumulatedCouponYield != null)
                row.createCell(6).setCellValue(accumulatedCouponYield.doubleValue());

            row.createCell(7).setCellValue(item.getTotalProfit().doubleValue());

            rowNumber++;
            int tagNum = 0;
            for(TradeTag tag : item.getTradeTags()) {
                row.createCell(8 + tagNum++).setCellValue(tag.getTag());
            }
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);


        book.write(new FileOutputStream(filename));
        book.close();
    }

    public static void exportProfitHistory(ProfitHistory profitHistory, String filename) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("ProfitHistory");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Date");
        row.createCell(1).setCellValue("Profit not taxed");
        row.createCell(2).setCellValue("Profit taxed");
        row.createCell(3).setCellValue("Tax");
        row.createCell(4).setCellValue("Paper profit");

        int rowNumber = 1;
        for(Map.Entry<LocalDate, ProfitHistoryItem> entry : profitHistory.getItems().entrySet()) {
            row = sheet.createRow(rowNumber);
            row.createCell(0).setCellValue(entry.getKey().toString());
            row.createCell(1).setCellValue(entry.getValue().getSumProfitNotTaxed().doubleValue());
            row.createCell(2).setCellValue(entry.getValue().getSumProfitTaxed().doubleValue());
            row.createCell(3).setCellValue(entry.getValue().getSumTax().doubleValue());

            BigDecimal paperProfit = entry.getValue().getPaperProfit();
            if(paperProfit != null)
                row.createCell(4).setCellValue(paperProfit.doubleValue());

            rowNumber++;
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);

        book.write(new FileOutputStream(filename));
        book.close();
    }
}
