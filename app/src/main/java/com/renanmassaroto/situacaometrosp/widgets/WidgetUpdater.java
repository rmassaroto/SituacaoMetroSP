package com.renanmassaroto.situacaometrosp.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.VolleyError;
import com.renanmassaroto.situacaometrosp.LinesListWidgetProvider;
import com.renanmassaroto.situacaometrosp.R;
import com.renanmassaroto.situacaometrosp.api.MetroApi;
import com.renanmassaroto.situacaometrosp.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by renancardosomassaroto on 3/31/15.
 */
public class WidgetUpdater extends Activity implements MetroApi.ApiResponseListener {

    public static final String LOG_TAG = "WIDGET_UPDATER";

    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.widget_lines_list);

        Log.e(LOG_TAG, "Initiated");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        RemoteViews views = new RemoteViews(LinesListWidgetProvider.class.getPackage().getName(), R.layout.widget_lines_list);
//
//        MetroApi.refreshMetroData(this, this, mAppWidgetId, views);
    }

    @Override
    public void onResponseReceived(Context context, String response) {

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void onErrorResponseReceived(Context context, VolleyError error) {

    }
}
