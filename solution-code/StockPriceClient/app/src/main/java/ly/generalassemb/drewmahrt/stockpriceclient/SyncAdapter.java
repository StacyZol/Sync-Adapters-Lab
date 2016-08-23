package ly.generalassemb.drewmahrt.stockpriceclient;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by drewmahrt on 3/7/16.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter{
    private static final String TAG = "SyncAdapter";
    ContentResolver mResolver;
    public static final Uri CONTENT_URI = StockPortfolioContract.Stocks.CONTENT_URI;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(SyncAdapter.class.getName(),"Starting sync");
        getPortfolioStocks();
    }

    private void getPortfolioStocks(){
        Cursor cursor = mResolver.query(CONTENT_URI,null,null,null,null);
        while(cursor != null && cursor.moveToNext()) {
            updateStockInfo(cursor.getString(cursor.getColumnIndex(StockPortfolioContract.Stocks.COLUMN_STOCK_SYMBOL)));
        }
    }


    public void updateStockInfo(final String symbol){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String stockUrl = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol="+symbol;

        JsonObjectRequest stockJsonRequest = new JsonObjectRequest
                (Request.Method.GET, stockUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(SyncAdapter.class.getName(),response.toString());
                        try {
                            Log.d(TAG, "onResponse: Symbol: "+response.getString("LastPrice")+" symbol: "+symbol);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("price", response.getString("LastPrice"));
                            mResolver.update(CONTENT_URI, contentValues, StockPortfolioContract.Stocks.COLUMN_STOCK_SYMBOL+"=?",new String[]{symbol});
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        queue.add(stockJsonRequest);
    }
}


