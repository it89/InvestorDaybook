import com.github.it89.investordiary.backup.xls.LoaderXLS;
import com.github.it89.investordiary.backup.xls.ReportXLS;
import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.cashflow.CashFlowJournal;
import com.github.it89.investordiary.stockmarket.analysis.ProfitResult;
import com.github.it89.investordiary.stockmarket.analysis.csv.LoadAssetPrice;
import com.github.it89.investordiary.stockmarket.analysis.profithistory.ProfitHistory;

import java.io.IOException;
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
        cashFlowJournal(daybook);
        //tradeJournal(daybook);
        AssetPriceHistory assetPriceHistory = assetPriceHistory(daybook);

        TreeSet<TradeBond> tradeBondSet = new TreeSet();
        tradeBondSet.addAll(daybook.getTradeBonds().values());
        BondNominalHistory bondNominalHistory = new BondNominalHistory(tradeBondSet);

        profitHistory(daybook, assetPriceHistory, bondNominalHistory);
        //profitResult(daybook, assetPriceHistory, bondNominalHistory);

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

    private void profitHistory(StockMarketDaybook daybook,
                               AssetPriceHistory assetPriceHistory,
                               BondNominalHistory bondNominalHistory) throws IOException {
        ProfitHistory profitHistory = new ProfitHistory(assetPriceHistory, bondNominalHistory);
        Asset asset = daybook.getAsset("SNGSP");
        int stageNumber = 1;
        profitHistory.fill(daybook, asset, stageNumber);
        ReportXLS.exportProfitHistory(profitHistory, "F:\\TMP\\ReportProfitHistory.xls");
    }

    private void profitResult(StockMarketDaybook daybook,
                              AssetPriceHistory assetPriceHistory,
                              BondNominalHistory bondNominalHistory) throws IOException {
        TreeMap<Asset, TreeSet<Integer>> combinations = ProfitResult.getAssetStageCombinations(daybook);
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
        ReportXLS.exportProfitResult(results, "F:\\TMP\\ReportProfitResult.xls");
    }
}
