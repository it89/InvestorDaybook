import com.github.it89.investordiary.TaxCalculator;
import com.github.it89.investordiary.backup.xls.LoaderXLS;
import com.github.it89.investordiary.backup.xls.ReportXLS;
import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.CashFlowJournal;
import com.github.it89.investordiary.stockmarket.analysis.ProfitResult;
import com.github.it89.investordiary.stockmarket.analysis.StockPortfolio;
import com.github.it89.investordiary.stockmarket.analysis.csv.LoadAssetPrice;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistory;
import com.github.it89.investordiary.stockmarket.analysis.tradejournal.TradeJournal;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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
        AssetPriceHistory assetPriceHistory = assetPriceHistory(daybook);
        profitHistory(daybook, assetPriceHistory);
        profitResult(daybook, assetPriceHistory);

        /*TreeSet<Trade> tradeSet = new TreeSet<Trade>();
        tradeSet.addAll(daybook.getTradeStocks().values());
        tradeSet.addAll(daybook.getTradeBonds().values());
        tradeSet = Trade.filterTreeSetByTag(tradeSet, daybook.getTradeTag("MGNT"));
        TradeJournal tradeJournalMGNT = new TradeJournal();
        for(Trade trade : tradeSet)
            tradeJournalMGNT.add(trade);

        ReportXLS.exportTradeJournal(tradeJournalMGNT, "F:\\TMP\\Report.xls");
        for(Trade trade : tradeSet)
            System.out.println(trade.getDate() + " " + trade.getTime());*/

        //TreeSet<AssetPrice> assetPriceTreeSet = LoadAssetPrice.loadAll(daybook, "F:\\TMP\\AssetPrice");
        //System.out.println(AssetPrice.getPrice(assetPriceTreeSet, daybook.getAsset("GAZP"), LocalDateTime.of(2016, 11, 11, 19, 0)));


        //System.out.println(assetPriceHistory.getPrice(daybook.getAsset("GAZP"), LocalDateTime.of(2016, 9, 29, 19, 0)));

        /*TreeSet<Trade> tradeSet = new TreeSet<Trade>();
        tradeSet.addAll(daybook.getTradeStocks().values());
        tradeSet = Trade.filterTreeSetByTag(tradeSet, daybook.getTradeTag("GAZP"));

        StockPortfolio stockPortfolio = new StockPortfolio();
        for(Trade trade: tradeSet) {
            stockPortfolio.add(trade);
        }

        System.out.println(stockPortfolio);*/
    }

    private StockMarketDaybook loadStockMarketDaybook() throws IOException {
        StockMarketDaybook daybook = new StockMarketDaybook();
        String testFileXLS = "F:\\TMP\\Daybook.xls";
        //testFileXLS = "test.xls";
        LoaderXLS loader = new LoaderXLS(daybook, testFileXLS);
        loader.load();
        return daybook;
    }

    private void cashFlowJournal(StockMarketDaybook daybook) throws IOException {
        CashFlowJournal cashFlowJournal = new CashFlowJournal();
        for (Map.Entry<String, TradeStock> entry : daybook.getTradeStocks().entrySet())
            cashFlowJournal.add(entry.getValue());
        for (Map.Entry<String, TradeBond> entry : daybook.getTradeBonds().entrySet())
            cashFlowJournal.add(entry.getValue());
        for (CashFlow cashFlow : daybook.getCashFlows())
            cashFlowJournal.add(cashFlow);
        /*CashFlowJournal cashFlowJournalNKNCP = cashFlowJournal.copyByTag(daybook.getTradeTag("NKNCP"));
        CashFlowJournal cashFlowJournalSNGSS = cashFlowJournal.copyByTag(daybook.getTradeTag("SNGS-S"));
        CashFlowJournal cashFlowJournalMGNT = cashFlowJournal.copyByTag(daybook.getTradeTag("MGNT"));*/


        ReportXLS.exportCashFlowJournal(cashFlowJournal, "F:\\TMP\\Report.xls");
    }

    private void tradeJournal(StockMarketDaybook daybook) throws IOException {
        /*TradeJournal tradeJournal = new TradeJournal();
        for (Map.Entry<String, TradeStock> entry : daybook.getTradeStocks().entrySet())
            tradeJournal.add(entry.getValue());
        for (Map.Entry<String, TradeBond> entry : daybook.getTradeBonds().entrySet())
            tradeJournal.add(entry.getValue());

        TradeJournal tradeJournalMGNT = tradeJournal.copyByTag(daybook.getTradeTag("NKNCP"));

        ReportXLS.exportTradeJournal(tradeJournalMGNT, "F:\\TMP\\Report.xls");*/
    }

    private AssetPriceHistory assetPriceHistory(StockMarketDaybook daybook) throws IOException {
        AssetPriceHistory assetPriceHistory = new AssetPriceHistory();
        LoadAssetPrice.loadAll(assetPriceHistory, daybook, "F:\\TMP\\AssetPrice");
        return assetPriceHistory;
    }

    private void profitHistory(StockMarketDaybook daybook, AssetPriceHistory assetPriceHistory) throws IOException {
        ProfitHistory profitHistory = new ProfitHistory(assetPriceHistory);
        Asset asset = daybook.getAsset("RU000A0JVBN2");
        int stageNumber = 1;
        profitHistory.fill(daybook, asset, stageNumber);
        ReportXLS.exportProfitHistory(profitHistory, "F:\\TMP\\ReportProfitHistory.xls");


    }

    private void profitResult(StockMarketDaybook daybook, AssetPriceHistory assetPriceHistory) {
        TreeMap<Asset, TreeSet<Integer>> combinations = ProfitResult.getAssetStageCombinations(daybook);
        for(Map.Entry<Asset, TreeSet<Integer>> entry : combinations.entrySet()) {
            for(Integer stageNumber : entry.getValue()) {
                System.out.println(entry.getKey().getTicker() + " [" + stageNumber + "]");
                ProfitHistory profitHistory = new ProfitHistory(assetPriceHistory);
                profitHistory.fill(daybook, entry.getKey(), stageNumber);
                System.out.println(new ProfitResult(profitHistory));
            }
        }
    }
}
