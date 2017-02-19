import com.github.it89.investordiary.backup.xls.BondPaymentLoader;
import com.github.it89.investordiary.backup.xls.LoaderXLS;
import com.github.it89.investordiary.backup.xls.ReportXLS;
import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.cashflow.CashFlowJournal;
import com.github.it89.investordiary.stockmarket.analysis.ProfitResult;
import com.github.it89.investordiary.stockmarket.analysis.csv.LoadAssetPrice;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistory;
import com.github.it89.investordiary.stockmarket.analysis.tradejournal.TradeJournal;
import com.github.it89.investordiary.stockmarket.bondpayment.BondPayment;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Axel on 17.09.2016.
 */

public class Run {
    public static void main(String[] args) throws IOException {
        new Run().run();
    }

    private void run() throws IOException {
        StockMarketDaybook daybook = loadStockMarketDaybook();

        //cashFlowJournal(daybook);
        //tradeJournal(daybook);
        //AssetPriceHistory assetPriceHistory = assetPriceHistory(daybook);

        //TreeSet<TradeBond> tradeBondSet = new TreeSet();
        //tradeBondSet.addAll(daybook.getTradeBonds().values());
        //BondNominalHistory bondNominalHistory = new BondNominalHistory(tradeBondSet);

        //stockPortfolioJournal(daybook);
        //profitHistory(daybook, assetPriceHistory, bondNominalHistory);
        //profitResult(daybook, assetPriceHistory, bondNominalHistory);
        //profitResult(daybook);
        //portfolioCostMap(daybook);
        bondPayment(daybook);
    }

    private StockMarketDaybook loadStockMarketDaybook() throws IOException {
        StockMarketDaybook daybook = new StockMarketDaybook();
        String testFileXLS = "F:\\TMP\\Daybook.xls";
        //testFileXLS = "test.xls";
        LoaderXLS loader = new LoaderXLS(daybook, testFileXLS);
        loader.load();

        /*for(TradeBond bond : daybook.getTradeBonds().values()) {
            System.out.println(bond);
        }*/
        return daybook;
    }

    private void cashFlowJournal(StockMarketDaybook daybook) throws IOException {
        CashFlowJournal cashFlowJournal = new CashFlowJournal();
        cashFlowJournal.fill(daybook);

        ReportXLS.exportCashFlowJournal(cashFlowJournal, "F:\\TMP\\CashFlowJournal.xls");
    }

    private void tradeJournal(StockMarketDaybook daybook) throws IOException {
        TradeJournal tradeJournal = new TradeJournal();
        for (Map.Entry<String, TradeStock> entry : daybook.getTradeStocks().entrySet())
            tradeJournal.add(entry.getValue());
        for (Map.Entry<String, TradeBond> entry : daybook.getTradeBonds().entrySet())
            tradeJournal.add(entry.getValue());
        for(CashFlow cashFlow : daybook.getCashFlows())
            tradeJournal.add(cashFlow);

        TradeJournal tradeJournalFlt = tradeJournal.copyByAsset(daybook.getAsset("RU000A0JWLX8"), null);

        ReportXLS.exportTradeJournal(tradeJournalFlt, "F:\\TMP\\TradeJournal.xls");
    }

    private AssetPriceHistory assetPriceHistory(StockMarketDaybook daybook) throws IOException {
        AssetPriceHistory assetPriceHistory = new AssetPriceHistory();
        LoadAssetPrice.loadAll(assetPriceHistory, daybook, "F:\\TMP\\AssetPrice");
        return assetPriceHistory;
    }

    private void profitHistory(StockMarketDaybook daybook,
                               AssetPriceHistory assetPriceHistory,
                               BondNominalHistory bondNominalHistory) throws IOException {
        ProfitHistory profitHistory = new ProfitHistory(assetPriceHistory, bondNominalHistory);
        Asset asset = daybook.getAsset("VTRS");
        int stageNumber = 1;
        profitHistory.fill(daybook, asset, stageNumber);
        ReportXLS.exportProfitHistory(profitHistory, "F:\\TMP\\ReportProfitHistory.xls");
    }

    private void profitResult(StockMarketDaybook daybook,
                              AssetPriceHistory assetPriceHistory,
                              BondNominalHistory bondNominalHistory) throws IOException {
        /*TreeMap<Asset, TreeSet<Integer>> combinations = ProfitResult.getAssetStageCombinations(daybook);
        TreeSet<ProfitResult> results = new TreeSet<ProfitResult>();
        for(Map.Entry<Asset, TreeSet<Integer>> entry : combinations.entrySet()) {
            for(Integer stageNumber : entry.getValue()) {
                //System.out.println(entry.getKey().getTicker() + " [" + stageNumber + "]");
                ProfitHistory profitHistory = new ProfitHistory(assetPriceHistory, bondNominalHistory);
                profitHistory.fill(daybook, entry.getKey(), stageNumber);
                //System.out.println(new ProfitResult(profitHistory));
                results.add(new ProfitResult(profitHistory, entry.getKey(), stageNumber));
            }
        }
        ReportXLS.exportProfitResult(results, "F:\\TMP\\ReportProfitResult.xls");*/
    }

    private void stockPortfolioJournal(StockMarketDaybook daybook) {
        /*StockPortfolioJournal journal = new StockPortfolioJournal();
        TreeSet<Trade> trades = new TreeSet<Trade>();
        trades.addAll(daybook.getTradeStocks().values());
        trades.addAll(daybook.getTradeBonds().values());
        trades = Trade.filterTreeSetByAsset(trades, daybook.getAsset("SNGSP"));
        journal.addTrades(trades);
        System.out.println(journal.getAmountSum(LocalDate.of(2016, 1, 1), LocalDate.of(2017, 1, 5)));*/
    }

    private void profitResult(StockMarketDaybook daybook) throws IOException {
        String testFileXLS = "F:\\TMP\\Daybook.xls";
        LoaderXLS loader = new LoaderXLS(daybook, testFileXLS);
        HashMap<Asset, BigDecimal> portfolio = loader.getPortfolioCostMap();

        TreeSet<ProfitResult> results = ProfitResult.generateByCombinatons(daybook, portfolio);
        /*for(ProfitResult result : results)
            System.out.println(result);*/
        ReportXLS.exportProfitResult(results, "F:\\TMP\\ReportProfitResult.xls");
    }

    private void portfolioCostMap(StockMarketDaybook daybook) throws IOException {
        String testFileXLS = "F:\\TMP\\Daybook.xls";
        LoaderXLS loader = new LoaderXLS(daybook, testFileXLS);
        HashMap<Asset, BigDecimal> portfolio = loader.getPortfolioCostMap();
        for(Map.Entry<Asset, BigDecimal> entry : portfolio.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    private void bondPayment(StockMarketDaybook daybook) throws IOException {
        String fileXLSX = "F:\\TMP\\BondPays.xlsx";
        BondPaymentLoader loader = new BondPaymentLoader(daybook, fileXLSX);
        HashMap<Asset, TreeSet<BondPayment>> assetBondPayments = loader.load();
        int i = 0;
    }
}
