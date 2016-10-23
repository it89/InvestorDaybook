import com.github.it89.investordiary.backup.xls.LoaderXLS;
import com.github.it89.investordiary.stockmarket.*;
import com.github.it89.investordiary.stockmarket.analysis.CashFlowItem;
import com.github.it89.investordiary.stockmarket.analysis.CashFlowJournal;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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
        CashFlowJournal cashFlowJournal = new CashFlowJournal();
        for (Map.Entry<String, TradeStock> entry : daybook.getTradeStocks().entrySet())
            cashFlowJournal.add(entry.getValue());
        for (Map.Entry<String, TradeBond> entry : daybook.getTradeBonds().entrySet())
            cashFlowJournal.add(entry.getValue());
        for (CashFlow cashFlow : daybook.getCashFlows())
            cashFlowJournal.add(cashFlow);
        CashFlowJournal cashFlowJournalNKNCP = cashFlowJournal.copyByTag(daybook.getTradeTag("NKNCP"));

        for (CashFlowItem cashFlowItem : cashFlowJournalNKNCP.getItems()) {
            System.out.println(cashFlowItem);
        }

        CashFlowJournal cashFlowJournalSNGSS = cashFlowJournal.copyByTag(daybook.getTradeTag("SNGS-S"));

        for (CashFlowItem cashFlowItem : cashFlowJournalSNGSS.getItems()) {
            System.out.println(cashFlowItem);
        }
    }

}
