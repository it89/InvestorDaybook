import com.github.it89.investordiary.stockmarket.Asset;

/**
 * Created by Axel on 17.09.2016.
 */

public class Run {
    public static void main(String[] args) {
        new Run().run();
    }

    private void run() {
        Asset asset = new Asset("test");
        System.out.println(asset);
    }
}
