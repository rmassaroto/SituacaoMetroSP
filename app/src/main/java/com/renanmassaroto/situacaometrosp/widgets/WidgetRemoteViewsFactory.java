package com.renanmassaroto.situacaometrosp.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.renanmassaroto.situacaometrosp.LinesListWidgetProvider;
import com.renanmassaroto.situacaometrosp.R;

import java.util.ArrayList;

/**
 * Created by renancardosomassaroto on 9/10/15.
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Intent intent;

    private ArrayList<String> linesColor = new ArrayList<>();
    private ArrayList<String> linesNumber = new ArrayList<>();
    private ArrayList<String> linesName = new ArrayList<>();
    private ArrayList<String> linesStatus = new ArrayList<>();
    private ArrayList<String> linesStatusMsg = new ArrayList<>();
    private ArrayList<String> linesStatusDesc = new ArrayList<>();

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;

        Bundle extras = intent.getExtras();

        linesNumber = extras.getStringArrayList(LinesListWidgetProvider.EXTRA_LINE_NUMBERS_ARRAY);
        linesName = extras.getStringArrayList(LinesListWidgetProvider.EXTRA_LINE_NAMES_ARRAY);
        linesColor = extras.getStringArrayList(LinesListWidgetProvider.EXTRA_LINE_COLORS_ARRAY);
        linesStatus = extras.getStringArrayList(LinesListWidgetProvider.EXTRA_LINE_STATUS_ARRAY);
        linesStatusMsg = extras.getStringArrayList(LinesListWidgetProvider.EXTRA_LINE_STATUS_MSG_ARRAY);
        linesStatusDesc = extras.getStringArrayList(LinesListWidgetProvider.EXTRA_LINE_STATUS_DESC_ARRAY);

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return linesName.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_list_1);

        row.setInt(R.id.color_1, "setBackgroundColor", Color.parseColor(linesColor.get(position)));

        String text = linesName.get(position) + " - " + linesStatusMsg.get(position);

        row.setTextViewText(R.id.text_1, text);
        row.setTextColor(R.id.text_1, Color.BLACK);

//        Intent intent = new Intent();
//        Bundle extras = new Bundle();
//
//        extras.putString(LinesListWidgetProvider.EXTRA_LINE_NUMBER, linesNumber.get(position));
//        extras.putString(LinesListWidgetProvider.EXTRA_LINE_NAME, linesName.get(position));
//        extras.putString(LinesListWidgetProvider.EXTRA_LINE_COLOR, linesColor.get(position));

//        intent.putExtras(extras);

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
