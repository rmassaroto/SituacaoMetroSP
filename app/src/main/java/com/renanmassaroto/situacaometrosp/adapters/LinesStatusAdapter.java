package com.renanmassaroto.situacaometrosp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.renanmassaroto.situacaometrosp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by renancardosomassaroto on 3/11/15.
 */
public class LinesStatusAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private Context context;
    private String colorList[] = {"#3f51b5", "#009688", "#f44336", "#ffeb3b", "#ab47bc"};
    private String linesList[] = {"Linha 1 - Azul", "Linha 2 - Verde", "Linha 3 - Vermelha", "Linha 4 - Amarela", "Linha 5 - Lilás"};
    private String linesStatus[] = {"#9E9E9E", "#9E9E9E", "#9E9E9E", "#9E9E9E", "#9E9E9E"};
    private String linesStatusMsg[] = {"", "", "", "", ""};
    private String linesStatusDesc[] = {"", "", "", "", ""};

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(linesList[position]);
        mBuilder.setMessage(linesStatusMsg[position] + "\n" + linesStatusDesc[position]);

        AlertDialog mAlertDialog = mBuilder.create();
        mAlertDialog.show();
    }

    public LinesStatusAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return linesList.length;
    }

    @Override
    public Object getItem(int position) {
        return linesList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(R.layout.list_item_1, parent, false);
        }

        TextView lineColor = (TextView) convertView.findViewById(R.id.text_1);
        lineColor.setText(Integer.toString(position + 1));
        lineColor.getBackground().setColorFilter(Color.parseColor(colorList[position]), PorterDuff.Mode.MULTIPLY);

        TextView lineName = (TextView) convertView.findViewById(R.id.text_2);
        lineName.setText(linesList[position]);

        View lineStatus = convertView.findViewById(R.id.view);
        lineStatus.getBackground().setColorFilter(Color.parseColor(linesStatus[position]), PorterDuff.Mode.MULTIPLY);

        return convertView;
    }

    public void setLinesStatusData(JSONObject jsonObject) {
        try {
            JSONArray lines = jsonObject.getJSONArray("lines");
            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);
                String status = line.getString("status");
                if (status.equalsIgnoreCase("lentidão")) {
                    linesStatus[i] = "#ffeb3b";
                    linesStatusMsg[i] = line.getString("msgStatus");
                    linesStatusDesc[i] = line.getString("descricao");
                } else if (status.equalsIgnoreCase("normal")) {
                    linesStatus[i] = "#4caf50";
                    linesStatusMsg[i] = line.getString("msgStatus");
                    linesStatusDesc[i] = line.getString("descricao");
                } else {
                    linesStatus[i] = "#f44336";
                    linesStatusMsg[i] = line.getString("msgStatus");
                    linesStatusDesc[i] = line.getString("descricao");
                }

                notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getStatusMsg(int position) {
        if (position >= 0 && position < linesStatusMsg.length) {
            return linesStatusMsg[position];
        } else {
            return null;
        }
    }
}
