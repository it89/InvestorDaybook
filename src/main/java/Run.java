import com.github.it89.investordiary.backup.xls.LoaderXLS;
import com.github.it89.investordiary.stockmarket.Asset;
import com.github.it89.investordiary.stockmarket.StockMarketDaybook;

import java.io.IOException;

/**
 * Created by Axel on 17.09.2016.
 */

public class Run {
    public static void main(String[] args) throws IOException {
        new Run().run();
    }

    private void run() throws IOException {
        StockMarketDaybook daybook = new StockMarketDaybook();
        LoaderXLS loader = new LoaderXLS(daybook, "test.xls");
        loader.load();
        System.out.println(daybook);
    }
}
