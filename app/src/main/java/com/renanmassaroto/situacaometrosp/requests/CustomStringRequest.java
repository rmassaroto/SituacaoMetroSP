package com.renanmassaroto.situacaometrosp.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renancardosomassaroto on 3/11/15.
 */
public class CustomStringRequest extends StringRequest {

    public CustomStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-agent", "Custom user agent");

        return headers;
    }
}
