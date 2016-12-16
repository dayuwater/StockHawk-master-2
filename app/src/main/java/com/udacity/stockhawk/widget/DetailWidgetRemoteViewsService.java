package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.Contract.Quote;
import com.udacity.stockhawk.data.StockProvider;

import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.R;

import com.udacity.stockhawk.ui.StockHistoryActivity;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * RemoteViewsService controlling the data being shown in the scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {

    private DecimalFormat dollarFormatWithPlus=new DecimalFormat();
    private DecimalFormat dollarFormat=new DecimalFormat();
    private DecimalFormat percentageFormat=new DecimalFormat();
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();

                Uri quoteUri = Quote.uri;

                data = getContentResolver().query(quoteUri,
                        Contract.Quote.QUOTE_COLUMNS,
                        null,
                        null,
                        Contract.Quote.COLUMN_SYMBOL);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);

                String stockName = data.getString(data.getColumnIndex(Quote.COLUMN_SYMBOL));
                double price = data.getDouble(data.getColumnIndex(Quote.COLUMN_PRICE));
                double change=data.getDouble(data.getColumnIndex(Quote.COLUMN_ABSOLUTE_CHANGE));
                double percentageChange=data.getDouble(data.getColumnIndex(Quote.COLUMN_PERCENTAGE_CHANGE));
                String history=data.getString(data.getColumnIndex(Quote.COLUMN_HISTORY));

                dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                dollarFormatWithPlus.setPositivePrefix("+$");
                percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
                percentageFormat.setMaximumFractionDigits(2);
                percentageFormat.setMinimumFractionDigits(2);
                percentageFormat.setPositivePrefix("+");

                String changeInFormat = dollarFormatWithPlus.format(change);
                String percentage = percentageFormat.format(percentageChange / 100);
                String priceInFormat=dollarFormat.format(price);

                // set the background color of the change block in the widget
                if(percentageChange>0){
                    views.setTextColor(R.id.widget_change,getColor(R.color.material_green_700));

                }
                else{
                    views.setTextColor(R.id.widget_change,getColor(R.color.material_red_700));

                }

                views.setTextViewText(R.id.widget_change,percentage);
                views.setTextViewText(R.id.widget_bid_price,priceInFormat);
                views.setTextViewText(R.id.widget_stock_symbol,stockName);



                final Intent fillInIntent = new Intent();
                // TODO: Put real Uri into intent

                fillInIntent.putExtra(getString(R.string.stock_history_extra),history);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            //TODO: Content Description
//            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
//            private void setRemoteContentDescription(RemoteViews views, String description) {
//                views.setContentDescription(R.id.widget_icon, description);
//            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(data.getColumnIndex(Quote._ID));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}