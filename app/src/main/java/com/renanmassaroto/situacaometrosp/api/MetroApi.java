package com.renanmassaroto.situacaometrosp.api;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.renanmassaroto.situacaometrosp.R;
import com.renanmassaroto.situacaometrosp.requests.CustomStringRequest;

/**
 * Created by renancardosomassaroto on 3/31/15.
 */
public class MetroApi {

    public static final String METRO_SERVICE_URL = "http://www.metro.sp.gov.br/Sistemas/direto-do-metro-via4/diretodoMetroHome.aspx?id=8c583116-4ff7-4205-a1c8-264050698929/";
    public static final String CPTM_SERVICE_URL = "http://www.cptm.sp.gov.br/Pages/Home.aspx";

    public interface ApiResponseListener {
        void onResponseReceived(Context context, String response);
        void onErrorResponseReceived(Context context, VolleyError error);
    }

    public static void refreshMetroData(final Context context, final ApiResponseListener listener) {

        if (isConnected(context)) {
            RequestQueue queue = Volley.newRequestQueue(context);

            final CustomStringRequest customStringRequest = new CustomStringRequest(Request.Method.GET, METRO_SERVICE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    listener.onResponseReceived(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onErrorResponseReceived(context, error);
                }
            });

            queue.add(customStringRequest);

        } else {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            mBuilder.setTitle(context.getString(R.string.error_title));
            mBuilder.setMessage(context.getString(R.string.no_internet_connection_error_message));

            AlertDialog errorAlertDialog = mBuilder.create();
            errorAlertDialog.show();
        }
    }

    public static void refreshCptmData(final Context context, final ApiResponseListener listener) {

        if (isConnected(context)) {
            RequestQueue queue = Volley.newRequestQueue(context);

            final CustomStringRequest customStringRequest = new CustomStringRequest(Request.Method.GET, CPTM_SERVICE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    listener.onResponseReceived(context, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onErrorResponseReceived(context, error);
                }
            });

            queue.add(customStringRequest);

        } else {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            mBuilder.setTitle(context.getString(R.string.error_title));
            mBuilder.setMessage(context.getString(R.string.no_internet_connection_error_message));

            AlertDialog errorAlertDialog = mBuilder.create();
            errorAlertDialog.show();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
