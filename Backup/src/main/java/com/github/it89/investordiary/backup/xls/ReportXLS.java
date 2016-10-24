package com.github.it89.investordiary.backup.xls;

import com.github.it89.investordiary.stockmarket.TradeTag;
import com.github.it89.investordiary.stockmarket.analysis.CashFlowItem;
import com.github.it89.investordiary.stockmarket.analysis.CashFlowJournal;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Axel on 24.10.2016.
 */
public class ReportXLS {
    public static void ExportCashFlowJournal(CashFlowJournal journal, String filename) throws IOException {
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
}
