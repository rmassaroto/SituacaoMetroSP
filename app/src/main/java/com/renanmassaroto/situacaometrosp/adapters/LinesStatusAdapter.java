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

import java.util.ArrayList;

/**
 * Created by renancardosomassaroto on 3/11/15.
 */
public class LinesStatusAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private Context context;

    private ArrayList<String> linesColor = new ArrayList<>();
    private ArrayList<String> linesNumber = new ArrayList<>();
    private ArrayList<String> linesName = new ArrayList<>();
    private ArrayList<String> linesStatus = new ArrayList<>();
    private ArrayList<String> linesStatusMsg = new ArrayList<>();
    private ArrayList<String> linesStatusDesc = new ArrayList<>();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(linesName.get(position));
        mBuilder.setMessage(linesStatusMsg.get(position) + "\n" + linesStatusDesc.get(position));
//        mBuilder.setMessage(linesStatusMsg.get(position));

        AlertDialog mAlertDialog = mBuilder.create();
        mAlertDialog.show();
    }

    public LinesStatusAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return linesName.size();
    }

    @Override
    public Object getItem(int position) {
        return linesName.get(position);
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
        lineColor.setText(linesNumber.get(position));
        lineColor.getBackground().setColorFilter(Color.parseColor(linesColor.get(position)), PorterDuff.Mode.MULTIPLY);

        TextView lineName = (TextView) convertView.findViewById(R.id.text_2);
        lineName.setText(linesName.get(position));

        View lineStatus = convertView.findViewById(R.id.view);
        lineStatus.getBackground().setColorFilter(Color.parseColor(linesStatus.get(position)), PorterDuff.Mode.MULTIPLY);

        return convertView;
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

                notifyDataSetChanged();
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

                    notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: Handle error
        }
    }

    public String getStatusMsg(int position) {
        if (position >= 0 && position < linesStatusMsg.size()) {
            return linesStatusMsg.get(position);
        } else {
            return null;
        }
    }
}
