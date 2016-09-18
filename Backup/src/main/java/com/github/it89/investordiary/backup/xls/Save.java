package com.github.it89.investordiary.backup.xls;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Axel on 17.09.2016.
 */
public class Save {
    public static void main(String[] args) throws IOException {
        String filename = "test.xls";
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Asset");

        Row row = sheet.createRow(0);
        Cell ticker = row.createCell(0);
        ticker.setCellValue("GAZP");
        Cell caption = row.createCell(1);
        caption.setCellValue("ГАЗПРОМ ао");
        Cell assetType = row.createCell(2);
        assetType.setCellValue("STOCK");

        row = sheet.createRow(1);
        ticker = row.createCell(0);
        ticker.setCellValue("AKRN");
        caption = row.createCell(1);
        caption.setCellValue("Акрон");
        assetType = row.createCell(2);
        assetType.setCellValue("STOCK");

        sheet.autoSizeColumn(1);

        book.write(new FileOutputStream(filename));
        book.close();
    }
}
