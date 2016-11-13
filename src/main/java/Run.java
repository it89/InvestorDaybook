import com.github.it89.investordiary.backup.xls.LoaderXLS;
import com.github.it89.investordiary.stockmarket.AssetPriceHistory;
import com.github.it89.investordiary.stockmarket.StockMarketDaybook;
import com.github.it89.investordiary.stockmarket.analysis.csv.LoadAssetPrice;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Axel on 17.09.2016.
 */

public class Run {
    public static void main(String[] args) throws IOException {
        new Run().run();
    }

    private void run() throws IOException {
        StockMarketDaybook daybook = new StockMarketDaybook();
        String testFileXLS = "F:\\TMP\\Daybook.xls";
        //testFileXLS = "test.xls";
        LoaderXLS loader = new LoaderXLS(daybook, testFileXLS);
        loader.load();
        /*for (Map.Entry<String, Asset> entry : daybook.getAssets().entrySet())
            System.out.println(entry.getValue());
        for (Map.Entry<String, TradeStock> entry : daybook.getTradeStocks().entrySet())
            System.out.println(entry.getValue());
        for (Map.Entry<String, TradeBond> entry : daybook.getTradeBonds().entrySet())
            System.out.println(entry.getValue());
        for (CashFlow cashFlow : daybook.getCashFlows()) {
            System.out.println(cashFlow);
        }
        System.out.println("Analysys-----------------------------------------");*/
        /*CashFlowJournal cashFlowJournal = new CashFlowJournal();
        for (Map.Entry<String, TradeStock> entry : daybook.getTradeStocks().entrySet())
            cashFlowJournal.add(entry.getValue());
        for (Map.Entry<String, TradeBond> entry : daybook.getTradeBonds().entrySet())
            cashFlowJournal.add(entry.getValue());
        for (CashFlow cashFlow : daybook.getCashFlows())
            cashFlowJournal.add(cashFlow);
        CashFlowJournal cashFlowJournalNKNCP = cashFlowJournal.copyByTag(daybook.getTradeTag("NKNCP"));
        CashFlowJournal cashFlowJournalSNGSS = cashFlowJournal.copyByTag(daybook.getTradeTag("SNGS-S"));
        CashFlowJournal cashFlowJournalMGNT = cashFlowJournal.copyByTag(daybook.getTradeTag("MGNT"));


        ReportXLS.exportCashFlowJournal(cashFlowJournal, "F:\\TMP\\Report.xls");*/
        /*TradeJournal tradeJournal = new TradeJournal();
        for (Map.Entry<String, TradeStock> entry : daybook.getTradeStocks().entrySet())
            tradeJournal.add(entry.getValue());
        for (Map.Entry<String, TradeBond> entry : daybook.getTradeBonds().entrySet())
            tradeJournal.add(entry.getValue());

        TradeJournal tradeJournalMGNT = tradeJournal.copyByTag(daybook.getTradeTag("SNGS-S"));*/

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
        AssetPriceHistory assetPriceHistory = new AssetPriceHistory();
        LoadAssetPrice.loadAll(assetPriceHistory, daybook, "F:\\TMP\\AssetPrice");

        System.out.println(assetPriceHistory.getPrice(daybook.getAsset("GAZP"), LocalDateTime.of(2016, 9, 29, 19, 0)));
    }

}
