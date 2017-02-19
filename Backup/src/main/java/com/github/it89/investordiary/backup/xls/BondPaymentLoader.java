package com.github.it89.investordiary.backup.xls;

import com.github.it89.investordiary.stockmarket.Asset;
import com.github.it89.investordiary.stockmarket.StockMarketDaybook;
import com.github.it89.investordiary.stockmarket.bondpayment.BondPayment;
import com.github.it89.investordiary.stockmarket.bondpayment.CouponPayment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created by Axel on 19.02.2017.
 */
public class BondPaymentLoader {
    private final StockMarketDaybook daybook;
    private final String filename;

    private final int ROW_NUM_FIRST = 3;
    private final int COLL_DATE = 1;
    private final int COLL_COUPON_RATE = 2;
    private final int COLL_COUPON_PCT = 3;
    private final int COLL_COUPON_PAYMENT = 4;

    public BondPaymentLoader(StockMarketDaybook daybook, String filename) {
        this.daybook = daybook;
        this.filename = filename;
    }

    public HashMap<Asset, TreeSet<BondPayment>> load() throws IOException {
        HashMap<Asset, TreeSet<BondPayment>> assetBondPayments = new HashMap();
        XSSFWorkbook exelBook = exelBook = new XSSFWorkbook(new FileInputStream(filename));
        HashSet<Asset> assets = new HashSet<Asset>();
        assets.addAll(daybook.getAssets().values());

        int numberOfSheets = exelBook.getNumberOfSheets();
        for(int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++) {
            XSSFSheet sheet = exelBook.getSheetAt(sheetNumber);
            Asset asset = findAssetByCaption(assets, sheet.getSheetName());
            if(asset != null) {
                assetBondPayments.put(asset, loadBondPayments(sheet));
            }
        }
        exelBook.close();
        return assetBondPayments;
    }

    @org.jetbrains.annotations.Nullable
    private Asset findAssetByCaption(HashSet<Asset> assets, String assetCaption) {
        for(Asset asset : assets) {
            if(asset.getCaption().equals(assetCaption))
                return asset;
        }
        return null;
    }

    private TreeSet<BondPayment> loadBondPayments(XSSFSheet sheet) {
        TreeSet<BondPayment> bondPayments = new TreeSet();
        int rowNum = ROW_NUM_FIRST;
        XSSFRow row = sheet.getRow(rowNum);
        while(row != null) {
            if(hasRowExists(row) == false)
                break;
            bondPayments.add(new BondPayment(getDate(row), getCouponPayment(row), null));
            row = sheet.getRow(++rowNum);
        }
        return bondPayments;
    }

    private boolean hasRowExists(XSSFRow row) {
        XSSFCell cell = row.getCell(0);
        if(cell == null)
            return false;
        double cellRow = cell.getNumericCellValue();
        if(cellRow == 0)
            return false;
        return true;
    }

    private LocalDate getDate(XSSFRow row) {
        XSSFCell cell = row.getCell(COLL_DATE);
        String dateText = cell.getStringCellValue();
        dateText = dateText.replaceAll("(^\\h*)|(\\h*$)","");
        LocalDate localDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return localDate;
    }

    @org.jetbrains.annotations.Nullable
    private CouponPayment getCouponPayment(XSSFRow row) {
        BigDecimal payment = parseStringToBigDecimal(row.getCell(COLL_COUPON_PAYMENT).getStringCellValue());
        if(payment == null)
            return null;
        BigDecimal couponRate = parseStringToBigDecimal(row.getCell(COLL_COUPON_RATE).getStringCellValue());
        BigDecimal pct = parseStringToBigDecimal(row.getCell(COLL_COUPON_PCT).getStringCellValue());
        return new CouponPayment(couponRate, pct, payment);
    }

    @Nullable
    private BigDecimal parseStringToBigDecimal(String paymentText) {
        String text = paymentText.replace("RUR", "");
        text = text.replace("%", "");
        text = text.replaceAll("(^\\h*)|(\\h*$)","");
        text = text.replace(',', '.');
        if(text.length() == 0)
            return null;
        return new BigDecimal(text);
    }
}
