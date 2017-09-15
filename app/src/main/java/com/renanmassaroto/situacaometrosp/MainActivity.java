package com.renanmassaroto.situacaometrosp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.renanmassaroto.situacaometrosp.adapters.LinesStatusAdapter;
import com.renanmassaroto.situacaometrosp.api.MetroApi;
import com.renanmassaroto.situacaometrosp.utils.JSONParser;

import io.fabric.sdk.android.Fabric;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity implements MetroApi.ApiResponseListener {

    public static final String LOG_TAG = "SituacaoMetroSP";

    public static final String SERVICE_URL = "http://www.metro.sp.gov.br/Sistemas/direto-do-metro-via4/diretodoMetroHome.aspx?id=8c583116-4ff7-4205-a1c8-264050698929/";

    private ProgressBar mProgressBar;
    private TextView refreshTimeTextView;
    private FloatingActionButton refreshButton;

    private ListView linesStatusListView;
    private LinesStatusAdapter linesStatusAdapter;

    private boolean isMetroResponse = true;

    private int lastFirstVisibleItem;

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        refreshTimeTextView = (TextView) findViewById(R.id.activity_main_refresh_time);
        refreshButton = (FloatingActionButton) findViewById(R.id.floating_action_button);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMetroData();
//                MetroApi.refreshMetroData(MainActivity.this, MainActivity.this, 0, null);
//                Intent mIntent = new Intent(MainActivity.this, WidgetUpdater.class);
//                startActivity(mIntent);
            }
        });

        linesStatusAdapter = new LinesStatusAdapter(this);
        linesStatusListView = (ListView) findViewById(R.id.activity_main_status_list);
        linesStatusListView.setAdapter(linesStatusAdapter);
        linesStatusListView.setOnItemClickListener(linesStatusAdapter);
        linesStatusListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int currentFirstVisibleItem = linesStatusListView.getFirstVisiblePosition();

                if (currentFirstVisibleItem > lastFirstVisibleItem) {
                    refreshButton.hide();
                } else {
                    refreshButton.show();
                }

                lastFirstVisibleItem = currentFirstVisibleItem;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        refreshMetroData();
//        refreshButton.hide();
//        MetroApi.refreshMetroData(this, this, 0, null);
    }

    @Override
    public void onResponseReceived(Context context, String response) {
        if (isMetroResponse) {
            JSONObject jsonObject = JSONParser.parseJSONFromMetroResponse(response);

            linesStatusAdapter.setMetroLinesStatusData(jsonObject);

            isMetroResponse = false;

            MetroApi.refreshCptmData(this, this);
        } else {
            JSONObject jsonObject = JSONParser.parseJSONFromCptmResponse(response);

            linesStatusAdapter.setCptmLinesStatusData(jsonObject);

            isMetroResponse = true;

            refreshTimeTextView.setText("Atualizado em " + DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
            mProgressBar.setVisibility(View.INVISIBLE);


            refreshButton.show();
        }
    }

    @Override
    public void onErrorResponseReceived(Context context, VolleyError error) {
        Toast.makeText(this, getString(R.string.could_not_get_data_error), Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.INVISIBLE);

        refreshButton.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this ads items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshMetroData() {
        refreshButton.hide();

        if (isConnected()) {
            mProgressBar.setVisibility(View.VISIBLE);

            MetroApi.refreshMetroData(this, this);
        } else {
            refreshButton.show();

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle(getString(R.string.error_title));
            mBuilder.setMessage(getString(R.string.no_internet_connection_error_message));

            AlertDialog errorAlertDialog = mBuilder.create();
            errorAlertDialog.show();
        }
    }
}
