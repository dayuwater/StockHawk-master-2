package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StockHistoryActivity extends Activity {

    private String mHistoryString;
    private LineChart mLineChart;
    private ArrayList<String> xAxes=new ArrayList<>();
    private ArrayList<Entry> yAxes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);


        mHistoryString=getIntent().getStringExtra(getString(R.string.stock_history_extra));


        mLineChart=(LineChart)findViewById(R.id.history);


        convertHistoryTextIntoMapWithCorrectTimeFormat();
        drawLineChart();

    }

    private void drawLineChart(){



        String[] xaxes = new String[xAxes.size()];
        for(int i=0; i<xAxes.size();i++){
            xaxes[i] = xAxes.get(i).toString();
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();



        LineDataSet lineDataSet2 = new LineDataSet(yAxes,"sin");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.RED);


        lineDataSets.add(lineDataSet2);

        mLineChart.setData(new LineData(lineDataSets));
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.setAutoScaleMinMaxEnabled(true);

        mLineChart.setVisibleXRangeMaximum(65f);



    }

    private void convertHistoryTextIntoMapWithCorrectTimeFormat(){

        String[] splited=mHistoryString.split("\n");

        float count=0;
        // preprocess the time data

        long minTime=Long.parseLong(splited[splited.length-1].split(",")[0].trim())>>29;
        for(int i=splited.length-1; i>=0; i--)
        {
            String[] parts=splited[i].split(",");


            //xAxes.add(Float.toString(count*1000));
            //xAxes.add(parts[0].trim())
            //yAxes.add(new Entry(Long.parseLong(parts[0].trim())>>29,Float.parseFloat(parts[1].trim())));
            yAxes.add(new Entry(count/2,Float.parseFloat(parts[1].trim())));
            count++;



        }




    }

    private String convertMilisecondsIntoNormalDateFormat(String millis){
        return "";
    }


}
