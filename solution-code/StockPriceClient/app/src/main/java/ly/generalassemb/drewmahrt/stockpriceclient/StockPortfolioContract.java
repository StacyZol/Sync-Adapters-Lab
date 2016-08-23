package ly.generalassemb.drewmahrt.stockpriceclient;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by drewmahrt on 8/9/16.
 */
public class StockPortfolioContract {
    public static final String AUTHORITY = "drewmahrt.generalassemb.ly.investingportfolio.MyContentProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Stocks implements BaseColumns {
        public static final String TABLE_STOCKS = "stocks";
        public static final String COLUMN_STOCK_SYMBOL = "symbol";
        public static final String COLUMN_STOCKNAME = "stockname";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_EXCHANGE = "exchange";
        public static final String COLUMN_PRICE = "price";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_STOCKS);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/vnd.ly.generassembly.stocks";

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/vnd.ly.generalassemb.stocks";
    }
}
