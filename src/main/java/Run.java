import com.github.it89.investordiary.backup.xls.LoaderXLS;
import com.github.it89.investordiary.stockmarket.*;

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
        for (Map.Entry<String, Asset> entry : daybook.getAssets().entrySet())
            System.out.println(entry.getValue());
        for (Map.Entry<String, TradeStock> entry : daybook.getTradeStocks().entrySet())
            System.out.println(entry.getValue());
        for (Map.Entry<String, TradeBond> entry : daybook.getTradeBonds().entrySet())
            System.out.println(entry.getValue());
        for (CashFlow cashFlow : daybook.getCashFlows()) {
            System.out.println(cashFlow);
        }
    }

}
