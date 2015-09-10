package com.renanmassaroto.situacaometrosp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.VolleyError;
import com.renanmassaroto.situacaometrosp.api.MetroApi;
import com.renanmassaroto.situacaometrosp.utils.JSONParser;
import com.renanmassaroto.situacaometrosp.widgets.WidgetUpdater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by renancardosomassaroto on 3/31/15.
 */
public class LinesListWidgetProvider extends AppWidgetProvider implements MetroApi.ApiResponseListener {

    public static final String EXTRA_LINE_NUMBERS_ARRAY = "br.com.renanmassaroto.situacaometrosp.widgets.EXTRA_LINE_NUMBERS_ARRAY";
    public static final String EXTRA_LINE_NAMES_ARRAY = "br.com.renanmassaroto.situacaometrosp.widgets.EXTRA_LINE_NAMES_ARRAY";
    public static final String EXTRA_LINE_COLORS_ARRAY = "br.com.renanmassaroto.situacaometrosp.widgets.EXTRA_LINE_COLORS_ARRAY";
    public static final String EXTRA_LINE_STATUS_ARRAY = "br.com.renanmassaroto.situacaometrosp.widgets.EXTRA_LINE_STATUS_ARRAY";
    public static final String EXTRA_LINE_STATUS_MSG_ARRAY = "br.com.renanmassaroto.situacaometrosp.widgets.EXTRA_LINE_STATUS_MSG_ARRAY";
    public static final String EXTRA_LINE_STATUS_DESC_ARRAY = "br.com.renanmassaroto.situacaometrosp.widgets.EXTRA_LINE_STATUS_DESC_ARRAY";

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    private ArrayList<String> linesColor;
    private ArrayList<String> linesNumber;
    private ArrayList<String> linesName;
    private ArrayList<String> linesStatus;
    private ArrayList<String> linesStatusMsg;
    private ArrayList<String> linesStatusDesc;

    private boolean isMetroResponse = true;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        // Refresh data
        isMetroResponse = true;
        MetroApi.refreshMetroData(context, this);
    }

    @Override
    public void onResponseReceived(Context context, String response) {
        if (isMetroResponse) {
            JSONObject jsonObject = JSONParser.parseJSONFromMetroResponse(response);
            setMetroLinesStatusData(jsonObject);

            isMetroResponse = false;
            MetroApi.refreshCptmData(context, this);
        } else {
            isMetroResponse = true;

            JSONObject jsonObject = JSONParser.parseJSONFromCptmResponse(response);
            setCptmLinesStatusData(jsonObject);

            int numberOfWidgets = appWidgetIds.length;

            // Perform this loop procedure for each App Widget that belongs to this provider
            for (int i = 0; i < numberOfWidgets; i++) {
                int appWidgetId = appWidgetIds[i];

                // Create an Intent to update widget
                Intent intent = new Intent(context, WidgetService.class);

                Bundle extras = new Bundle();

                extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                extras.putStringArrayList(EXTRA_LINE_NUMBERS_ARRAY, linesNumber);
                extras.putStringArrayList(EXTRA_LINE_NAMES_ARRAY, linesName);
                extras.putStringArrayList(EXTRA_LINE_COLORS_ARRAY, linesColor);
                extras.putStringArrayList(EXTRA_LINE_STATUS_ARRAY, linesStatus);
                extras.putStringArrayList(EXTRA_LINE_STATUS_MSG_ARRAY, linesStatusMsg);
                extras.putStringArrayList(EXTRA_LINE_STATUS_DESC_ARRAY, linesStatusDesc);

                intent.putExtras(extras);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                // Get the layout for the App Widget and attach an on-click listener
                // to the button
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_lines_list);

                views.setRemoteAdapter(R.id.widget_lines_list, intent);
//            views.setRemoteAdapter(appWidgetId, R.id.widget_lines_list, intent);


                // Tell the AppWidgetManager to perform an update on the current app widget_lines_list
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    @Override
    public void onErrorResponseReceived(Context context, VolleyError error) {
//        Log.e(LOG_TAG, error.getMessage());
//        Toast.makeText(this, getString(R.string.could_not_get_data_error), Toast.LENGTH_SHORT).show();
    }

    public void setMetroLinesStatusData(JSONObject jsonObject) {
        linesColor = new ArrayList<>();
        linesNumber = new ArrayList<>();
        linesName = new ArrayList<>();
        linesStatus = new ArrayList<>();
        linesStatusMsg = new ArrayList<>();
        linesStatusDesc = new ArrayList<>();

        try {
            JSONArray lines = jsonObject.getJSONArray("lines");
            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);

                linesNumber.add(line.getString("lineNumber"));
                linesName.add(line.getString("lineName"));
                linesColor.add(line.getString("lineColor"));

                String status = line.getString("status");
                if (status.equalsIgnoreCase("lentidão")) {
                    linesStatus.add("#ffeb3b");
                } else if (status.equalsIgnoreCase("normal")) {
                    linesStatus.add("#4caf50");
                } else {
                    linesStatus.add("#f44336");
                }

                linesStatusMsg.add(line.getString("msgStatus"));
                linesStatusDesc.add(line.getString("descricao"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setCptmLinesStatusData(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                JSONArray lines = jsonObject.getJSONArray("lines");
                for (int i = 0; i < lines.length(); i++) {
                    JSONObject line = lines.getJSONObject(i);

                    linesNumber.add(line.getString("lineNumber"));
                    linesName.add(line.getString("lineName"));
                    linesColor.add(line.getString("lineColor"));

                    String status = line.getString("status");
                    if (status.equalsIgnoreCase("lentidão") || status.equalsIgnoreCase("velocidade reduzida")) {
                        linesStatusMsg.add(line.optString("msgStatus", "Velocidade Reduzida"));
                        linesStatus.add("#ffeb3b");
                    } else if (status.equalsIgnoreCase("normal")) {
                        linesStatusMsg.add(line.optString("msgStatus", "Operação Normal"));
                        linesStatus.add("#4caf50");
                    } else {
                        linesStatusMsg.add(line.optString("msgStatus", "N/D"));
                        linesStatus.add("#f44336");
                    }


                    linesStatusDesc.add(line.optString("descricao", ""));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: Handle error
        }
    }
}