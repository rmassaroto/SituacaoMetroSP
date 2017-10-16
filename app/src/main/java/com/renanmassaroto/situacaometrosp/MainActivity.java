package com.renanmassaroto.situacaometrosp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.renanmassaroto.situacaometrosp.adapters.LinesStatusesAdapter;
import com.renanmassaroto.situacaometrosp.api.MetroApi;
import com.renanmassaroto.situacaometrosp.utils.JSONParser;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements MetroApi.ApiResponseListener,
        LinesStatusesAdapter.OnLineClickListener {

    private TextView mRefreshTimeTextView;

    @NonNull
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @NonNull
    private RecyclerView mRecyclerView;

    @NonNull
    private LinesStatusesAdapter mAdapter;

    private boolean isMetroResponse = true;

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        prepareViews();
    }

    private void prepareViews() {
        mRefreshTimeTextView = (TextView) findViewById(R.id.activity_main_refresh_time);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new LinesStatusesAdapter(this, this);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                attemptDataLoad();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                attemptDataLoad();
            }
        });
    }

    private void attemptDataLoad() {
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter.clearDataSet();

        if (isConnected()) {
            MetroApi.refreshMetroData(this, this);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle(getString(R.string.error_title));
            mBuilder.setMessage(getString(R.string.no_internet_connection_error_message));

            AlertDialog errorAlertDialog = mBuilder.create();
            errorAlertDialog.show();
        }
    }

    @Override
    public void onResponseReceived(String response) {
        if (isMetroResponse) {
            JSONObject jsonObject = JSONParser.parseJSONFromMetroResponse(response);

            if (jsonObject != null)
                mAdapter.setMetroLinesStatusData(jsonObject);

            isMetroResponse = false;

            MetroApi.refreshCptmData(this, this);
        } else {
            JSONObject jsonObject = JSONParser.parseJSONFromCptmResponse(response);

            if (jsonObject != null)
                mAdapter.setCptmLinesStatusData(jsonObject);

            isMetroResponse = true;

            mRefreshTimeTextView.setText(
                    getString(
                            R.string.updated_at,
                            DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()
                            )
                    )
            );

            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onErrorResponseReceived(VolleyError error) {
        Toast.makeText(this, getString(R.string.could_not_get_data_error), Toast.LENGTH_SHORT).show();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLineClick(int position) {
        mRecyclerView.smoothScrollToPosition(position + 1);
    }
}
