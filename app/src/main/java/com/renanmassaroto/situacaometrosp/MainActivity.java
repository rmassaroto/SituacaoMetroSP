package com.renanmassaroto.situacaometrosp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melnykov.fab.FloatingActionButton;
import com.renanmassaroto.situacaometrosp.adapters.LinesStatusAdapter;
import com.renanmassaroto.situacaometrosp.requests.CustomStringRequest;
import com.renanmassaroto.situacaometrosp.utils.JSONParser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    public static final String LOG_TAG = "SituacaoMetroSP";

    public static final String SERVICE_URL = "http://www.metro.sp.gov.br/Sistemas/direto-do-metro-via4/diretodoMetroHome.aspx?id=8c583116-4ff7-4205-a1c8-264050698929/";

    private ProgressBar mProgressBar;
    private TextView refreshTimeTextView;
    private ImageButton refreshButton;

    private ListView linesStatusListView;
    private LinesStatusAdapter linesStatusAdapter;

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        refreshTimeTextView = (TextView) findViewById(R.id.activity_main_refresh_time);
        refreshButton = (ImageButton) findViewById(R.id.floating_action_button);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });

        linesStatusAdapter = new LinesStatusAdapter(this);
        linesStatusListView = (ListView) findViewById(R.id.activity_main_status_list);
        linesStatusListView.setAdapter(linesStatusAdapter);
        linesStatusListView.setOnItemClickListener(linesStatusAdapter);

        refreshData();

    }

    public void onResponseReceived(String response) {
        JSONObject jsonObject = JSONParser.parseJSONFromResponse(response);
        linesStatusAdapter.setLinesStatusData(jsonObject);

        refreshTimeTextView.setText("Atualizado em " + DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        mProgressBar.setVisibility(View.INVISIBLE);

        refreshButton.setEnabled(true);
    }

    public void onErrorResponseReceived(VolleyError error) {
        Log.e(LOG_TAG, error.getMessage());
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

    public void refreshData() {
        refreshButton.setEnabled(false);
        if (isConnected()) {

            mProgressBar.setVisibility(View.VISIBLE);

            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            final CustomStringRequest customStringRequest = new CustomStringRequest(Request.Method.GET, SERVICE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    onResponseReceived(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    onErrorResponseReceived(error);
                }
            });

            queue.add(customStringRequest);

        } else {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle(getString(R.string.error_title));
            mBuilder.setMessage(getString(R.string.no_internet_connection_error_message));

            AlertDialog errorAlertDialog = mBuilder.create();
            errorAlertDialog.show();
        }
    }
}
