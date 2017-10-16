package com.renanmassaroto.situacaometrosp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.renanmassaroto.situacaometrosp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by renan on 09/10/17.
 */

public class LinesStatusesAdapter extends RecyclerView.Adapter<LinesStatusesAdapter.LineStatusViewHolder> {

    @NonNull
    private final Context mContext;

    @NonNull
    private final OnLineClickListener mListener;

    @NonNull
    private ArrayList<Line> mLines = new ArrayList<>();

    public LinesStatusesAdapter(@NonNull Context context, @NonNull OnLineClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void clearDataSet() {
        mLines = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setMetroLinesStatusData(@NonNull JSONObject jsonObject) {
        try {
            JSONArray lines = jsonObject.getJSONArray("lines");
            for (int i = 0; i < lines.length(); i++) {
                JSONObject lineJsonObject = lines.getJSONObject(i);

                Line line = new Line();
                line.number = lineJsonObject.getString("lineNumber");
                line.name = lineJsonObject.getString("lineName");
                line.color = lineJsonObject.getString("lineColor");

                String status = lineJsonObject.getString("status");
                if (status.equalsIgnoreCase("lentidão")) {
                    line.statusColor = "#ffeb3b";
                    line.statusMsg = lineJsonObject.optString("msgStatus", "Velocidade Reduzida");
                } else if (status.equalsIgnoreCase("normal")) {
                    line.statusColor = "#4caf50";
                    line.statusMsg = lineJsonObject.optString("msgStatus", "Operação Normal");
                } else if (status.equalsIgnoreCase("parcial")) {
                    line.statusColor = "#ffeb3b";
                    line.statusMsg = lineJsonObject.optString("msgStatus", "Operação Parcial");
                } else {
                    line.statusColor = "#f44336";
                    line.statusMsg = lineJsonObject.optString("msgStatus", "N/D");
                }

                line.statusDesc = lineJsonObject.optString("descricao", "N/D");
                if (line.statusDesc.isEmpty())
                    line.statusDesc = line.statusMsg;

                mLines.add(line);
            }
        } catch (Exception exception) {
            Crashlytics.logException(exception);
        }

        notifyDataSetChanged();
    }

    public void setCptmLinesStatusData(@NonNull JSONObject jsonObject) {
        try {
            JSONArray lines = jsonObject.getJSONArray("lines");
            for (int i = 0; i < lines.length(); i++) {
                JSONObject lineJsonObject = lines.getJSONObject(i);

                Line line = new Line();
                line.number = lineJsonObject.getString("lineNumber");
                line.name = lineJsonObject.getString("lineName");
                line.color = lineJsonObject.getString("lineColor");

                String status = lineJsonObject.getString("status");
                if (status.equalsIgnoreCase("lentidão") || status.equalsIgnoreCase("velocidade reduzida")) {
                    line.statusColor = "#ffeb3b";
                    line.statusMsg = lineJsonObject.optString("msgStatus", "Velocidade Reduzida");
                } else if (status.equalsIgnoreCase("normal") || status.equalsIgnoreCase("Operação Normal")) {
                    line.statusColor = "#4caf50";
                    line.statusMsg = lineJsonObject.optString("msgStatus", "Operação Normal");
                } else if (status.equalsIgnoreCase("Operação Parcial")) {
                    line.statusColor = "#ffeb3b";
                    line.statusMsg = lineJsonObject.optString("msgStatus", "Operação Parcial");
                } else {
                    line.statusColor = "#f44336";
                    line.statusMsg = lineJsonObject.optString("msgStatus", "N/D");
                }

                line.statusDesc = lineJsonObject.optString("descricao", "N/D");
                if (line.statusDesc.isEmpty())
                    line.statusDesc = line.statusMsg;

                mLines.add(line);
            }
        } catch (Exception exception) {
            Crashlytics.logException(exception);
        }

        notifyDataSetChanged();
    }

    @Override
    public LineStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        return new LineStatusViewHolder(layoutInflater.inflate(R.layout.list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(LineStatusViewHolder holder, int position) {
        holder.clearData();

        Line line = mLines.get(position);

        if (line.expanded) {
            holder.lineStatusMsgTextView.setVisibility(View.VISIBLE);
            holder.lineStatusDescTextView.setVisibility(View.VISIBLE);
        }

        holder.lineNumberTextView.getBackground().setColorFilter(Color.parseColor(line.color), PorterDuff.Mode.MULTIPLY);
        holder.lineStatusColorView.getBackground().setColorFilter(Color.parseColor(line.statusColor), PorterDuff.Mode.MULTIPLY);

        holder.lineNumberTextView.setText(line.number);
        holder.lineNameTextView.setText(line.name);
        holder.lineStatusMsgTextView.setText(line.statusMsg);
        holder.lineStatusDescTextView.setText(line.statusDesc);
    }

    @Override
    public int getItemCount() {
        return mLines.size();
    }

    public class LineStatusViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final View lineStatusColorView;
        final TextView lineNumberTextView;
        final TextView lineNameTextView;
        final TextView lineStatusMsgTextView;
        final TextView lineStatusDescTextView;

        public LineStatusViewHolder(View itemView) {
            super(itemView);

            lineStatusColorView = itemView.findViewById(R.id.view);
            lineNumberTextView = (TextView) itemView.findViewById(R.id.text_1);
            lineNameTextView = (TextView) itemView.findViewById(R.id.text_2);
            lineStatusMsgTextView = (TextView) itemView.findViewById(R.id.text_3);
            lineStatusDescTextView = (TextView) itemView.findViewById(R.id.text_4);

            itemView.setOnClickListener(this);
        }

        public void clearData() {
            lineStatusMsgTextView.setVisibility(View.GONE);
            lineStatusDescTextView.setVisibility(View.GONE);

            lineNumberTextView.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            lineStatusColorView.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

            lineNumberTextView.setText(null);
            lineNameTextView.setText(null);
            lineStatusMsgTextView.setText(null);
            lineStatusDescTextView.setText(null);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() > -1) {
                Line line = mLines.get(getAdapterPosition());

                line.expanded = !line.expanded;
                lineStatusMsgTextView.setVisibility((line.expanded ? View.VISIBLE : View.GONE));
                lineStatusDescTextView.setVisibility((line.expanded ? View.VISIBLE : View.GONE));

                mListener.onLineClick(getAdapterPosition());
            }
        }
    }

    private class Line {

        @NonNull
        String color;

        @NonNull
        String number;

        @NonNull
        String name;

        @NonNull
        String statusColor;

        @NonNull
        String statusMsg;

        @NonNull
        String statusDesc;

        boolean expanded = false;

        public Line() {

        }

    }

    public interface OnLineClickListener {
        void onLineClick(int position);
    }
}
