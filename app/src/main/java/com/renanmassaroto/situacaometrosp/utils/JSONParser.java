package com.renanmassaroto.situacaometrosp.utils;

import android.util.Log;

import com.renanmassaroto.situacaometrosp.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by renancardosomassaroto on 3/11/15.
 */
public class JSONParser {

    public static JSONObject parseJSONFromResponse(String response) {

        int startIndex = response.indexOf("var objArrLinhas = [");
        int lastIndex = response.indexOf("function abreDetalheLinha(codigo)");

        response = response.substring(startIndex, lastIndex);
        response = response.replace("var objArrLinhas = [", "");

        startIndex = response.indexOf("]");
        lastIndex = response.indexOf("var objArrL4 = [");

        response = "{ \"lines\" : [\n" + response.substring(0, startIndex) + ", \n" + response.substring(lastIndex + 16, response.length() - 10) + "}";

        response = response.replace("&#224;", "à");
        response = response.replace("&#225;", "á");
        response = response.replace("&#227;", "ã");
        response = response.replace("&#231;", "ç");
        response = response.replace("#234;", "ê");
        response = response.replace("&#245;", "õ");


        try {
            JSONObject result = new JSONObject(response);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }

}
