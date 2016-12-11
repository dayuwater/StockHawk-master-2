package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.udacity.stockhawk.R;

public class StockHistoryActivity extends Activity {

    private String mHistoryString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        mHistoryString=getIntent().getStringExtra(getString(R.string.stock_history_extra));
        TextView textView=(TextView)findViewById(R.id.history_text);
        textView.setText(mHistoryString);
    }

}
