package com.github.it89.investordiary.backup.xls;

import com.github.it89.investordiary.XIRR;
import com.github.it89.investordiary.stockmarket.analysis.cashflow.CashFlowItem;
import com.github.it89.investordiary.stockmarket.analysis.cashflow.CashFlowJournal;
import com.github.it89.investordiary.stockmarket.analysis.ProfitResult;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistory;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistoryItem;
import com.github.it89.investordiary.stockmarket.analysis.tradejournal.TradeItem;
import com.github.it89.investordiary.stockmarket.analysis.tradejournal.TradeJournal;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
        row.createCell(5).setCellValue("Asset");
        row.createCell(6).setCellValue("Stage number");

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
            if(item.getAsset() != null)
                row.createCell(5).setCellValue(item.getAsset().getCaption());
            if(item.getStageNumber() != null)
                row.createCell(6).setCellValue(item.getStageNumber());
        }

        for(int i = 0; i < 6; i++)
            sheet.autoSizeColumn(i);

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
        row.createCell(8).setCellValue("Tax");
        row.createCell(9).setCellValue("Stage number");

        int rowNumber = 1;
        Iterator<TradeItem> tradeItemIterator = journal.getTradeItems().iterator();
        Iterator<CashFlowItem> cashFlowItemIterator = journal.getCashFlowItems().iterator();
        TradeItem tradeItem = null;
        CashFlowItem cashFlowItem = null;
        if(tradeItemIterator.hasNext())
            tradeItem = tradeItemIterator.next();
        if(cashFlowItemIterator.hasNext())
            cashFlowItem = cashFlowItemIterator.next();

        while(tradeItem != null || cashFlowItem != null) {
            boolean isTradeItemRow = false;
            if(cashFlowItem == null)
                isTradeItemRow = true;
            else
                if(tradeItem != null)
                    if(tradeItem.getDate().compareTo(cashFlowItem.getDate()) <= 0)
                        isTradeItemRow = true;

            row = sheet.createRow(rowNumber);
            if(isTradeItemRow) {
                row.createCell(0).setCellValue(tradeItem.getDate().toString());
                row.createCell(1).setCellValue(tradeItem.getTime().toString());
                row.createCell(2).setCellValue(tradeItem.getAsset().getCaption());
                row.createCell(3).setCellValue(tradeItem.getAmount());
                row.createCell(4).setCellValue(tradeItem.getVolume().doubleValue());
                row.createCell(5).setCellValue(tradeItem.getCommission().doubleValue());

                BigDecimal accumulatedCouponYield = tradeItem.getAccumulatedCouponYield();
                if(accumulatedCouponYield != null)
                    row.createCell(6).setCellValue(accumulatedCouponYield.doubleValue());

                row.createCell(7).setCellValue(tradeItem.getTotalProfit().doubleValue());

                Integer stageNumber = tradeItem.getStageNumber();
                if(stageNumber != null)
                    row.createCell(9).setCellValue(stageNumber);

                if(tradeItemIterator.hasNext())
                    tradeItem = tradeItemIterator.next();
                else
                    tradeItem = null;
            } else {
                row.createCell(0).setCellValue(cashFlowItem.getDate().toString());
                row.createCell(2).setCellValue(cashFlowItem.getAsset().getCaption());
                row.createCell(7).setCellValue(cashFlowItem.getMoney().doubleValue());
                row.createCell(8).setCellValue(cashFlowItem.getTax().doubleValue());
                Integer stageNumber = cashFlowItem.getStageNumber();
                if(stageNumber != null)
                    row.createCell(9).setCellValue(stageNumber);

                if(cashFlowItemIterator.hasNext())
                    cashFlowItem = cashFlowItemIterator.next();
                else
                    cashFlowItem = null;
            }
            rowNumber++;
        }

        for(int i = 0; i <= 9; i++)
            sheet.autoSizeColumn(i);

        book.write(new FileOutputStream(filename));
        book.close();
    }

    public static void exportProfitHistory(ProfitHistory profitHistory, String filename) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("ProfitHistory");

        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Date");
        row.createCell(1).setCellValue("Profit not taxed");
        row.createCell(2).setCellValue("Profit taxed");
        row.createCell(3).setCellValue("Tax");
        row.createCell(4).setCellValue("Paper profit");
        row.createCell(5).setCellValue("Total profit not taxed");
        row.createCell(6).setCellValue("Total profit taxed");
        row.createCell(7).setCellValue("Total paper profit not taxed");
        row.createCell(8).setCellValue("Total paper profit taxed");
        row.createCell(9).setCellValue("Total paper profit XIRR");

        int rowNumber = 1;
        for(Map.Entry<LocalDate, ProfitHistoryItem> entry : profitHistory.getItems().entrySet()) {
            row = sheet.createRow(rowNumber);
            Date date = Date.from(entry.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Cell cell = row.createCell(0);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(date);
            row.createCell(1).setCellValue(entry.getValue().getSumProfitNotTaxed().doubleValue());
            row.createCell(2).setCellValue(entry.getValue().getSumProfitTaxed().doubleValue());
            row.createCell(3).setCellValue(entry.getValue().getSumTax().doubleValue());

            BigDecimal paperProfit = entry.getValue().getPaperProfit();
            if(paperProfit != null)
                row.createCell(4).setCellValue(paperProfit.doubleValue());

            row.createCell(5).setCellValue(entry.getValue().getTotalProfitNotTaxed().doubleValue());
            row.createCell(6).setCellValue(entry.getValue().getTotalProfitTaxed().doubleValue());
            row.createCell(7).setCellValue(entry.getValue().getTotalPaperProfitNotTaxed().doubleValue());
            row.createCell(8).setCellValue(entry.getValue().getTotalPaperProfitTaxed().doubleValue());
            TreeMap<LocalDate, BigDecimal> cashFlows = entry.getValue().getCashFlows();
            if(paperProfit != null) {
                if(cashFlows.containsKey(entry.getKey()))
                    cashFlows.put(entry.getKey(), paperProfit.add(cashFlows.get(entry.getKey())));
                else
                    cashFlows.put(entry.getKey(), paperProfit);
            }

            double xirr = XIRR.getXIRR(cashFlows);
            if(Double.isFinite(xirr))
                row.createCell(9).setCellValue(xirr);

            rowNumber++;
        }

        for(int i = 0; i < 9; i++)
            sheet.autoSizeColumn(i);

        book.write(new FileOutputStream(filename));
        book.close();
    }

    public static void exportProfitResult(TreeSet<ProfitResult> items, String filename) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("ProfitResult");

        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Asset");
        row.createCell(1).setCellValue("Stage number");
        row.createCell(2).setCellValue("Begin");
        row.createCell(3).setCellValue("End");
        row.createCell(4).setCellValue("Profit not taxed");

        int rowNumber = 1;
        for(ProfitResult item : items) {
            row = sheet.createRow(rowNumber);
            row.createCell(0).setCellValue(item.getAsset().getCaption());
            row.createCell(1).setCellValue(item.getStageNumber());

            Date begin = Date.from(item.getDateBegin().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Cell cell = row.createCell(2);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(begin);

            if(item.getDateEnd() != null) {
                Date end = Date.from(item.getDateEnd().atStartOfDay(ZoneId.systemDefault()).toInstant());
                cell = row.createCell(3);
                cell.setCellStyle(dateStyle);
                cell.setCellValue(end);
            }

            row.createCell(4).setCellValue(item.getProfitNotTaxed().doubleValue());
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
