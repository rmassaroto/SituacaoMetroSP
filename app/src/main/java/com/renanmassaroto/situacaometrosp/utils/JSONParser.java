package com.renanmassaroto.situacaometrosp.utils;

import android.util.Log;

import com.renanmassaroto.situacaometrosp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by renancardosomassaroto on 3/11/15.
 */
public class JSONParser {

    // KEY NAMES
    public static final String LINE_NUMBER = "lineNumber";
    public static final String LINE_NAME = "lineName";
    public static final String LINE_COLOR = "lineColor";

    // BLUE LINE
    public static final String LINE_NUMBER_BLUE = "1";
    public static final String LINE_NAME_BLUE = "Linha 1 - Azul";
    public static final String LINE_COLOR_BLUE = "#3f51b5";

    // GREEN LINE
    public static final String LINE_NUMBER_GREEN = "2";
    public static final String LINE_NAME_GREEN = "Linha 2 - Verde";
    public static final String LINE_COLOR_GREEN = "#009688";

    // RED LINE
    public static final String LINE_NUMBER_RED = "3";
    public static final String LINE_NAME_RED = "Linha 3 - Vermelha";
    public static final String LINE_COLOR_RED = "#f44336";

    // YELLOW LINE
    public static final String LINE_NUMBER_YELLOW = "4";
    public static final String LINE_NAME_YELLOW = "Linha 4 - Amarela";
    public static final String LINE_COLOR_YELLOW = "#ffeb3b";

    // SILVER LINE
    public static final String LINE_NUMBER_SILVER = "15";
    public static final String LINE_NAME_SILVER = "Linha 15 - Prata";
    public static final String LINE_COLOR_SILVER = "#717A7D";

    // LILAC LINE
    public static final String LINE_NUMBER_LILAC = "5";
    public static final String LINE_NAME_LILAC = "Linha 5 - Lilás";
    public static final String LINE_COLOR_LILAC = "#ab47bc";


    // RUBI LINE
    public static final String LINE_NUMBER_RUBI = "7";
    public static final String LINE_NAME_RUBI = "Linha 7 - Rubi";
    public static final String LINE_COLOR_RUBI = "#A01239";

    // DIAMANTE LINE
    public static final String LINE_NUMBER_DIAMANTE = "8";
    public static final String LINE_NAME_DIAMANTE = "Linha 8 - Diamante";
    public static final String LINE_COLOR_DIAMANTE = "#A0957D";

    // ESMERALDA LINE
    public static final String LINE_NUMBER_ESMERALDA = "9";
    public static final String LINE_NAME_ESMERALDA = "Linha 9 - ESMERALDA";
    public static final String LINE_COLOR_ESMERALDA = "#1A9E93";

    // TURQUESA LINE
    public static final String LINE_NUMBER_TURQUESA = "10";
    public static final String LINE_NAME_TURQUESA = "Linha 10 - TURQUESA";
    public static final String LINE_COLOR_TURQUESA = "#00708B";

    // CORAL LINE
    public static final String LINE_NUMBER_CORAL = "11";
    public static final String LINE_NAME_CORAL = "Linha 11 - CORAL";
    public static final String LINE_COLOR_CORAL = "#EC4D13";

    // SAFIRA LINE
    public static final String LINE_NUMBER_SAFIRA = "12";
    public static final String LINE_NAME_SAFIRA = "Linha 12 - SAFIRA";
    public static final String LINE_COLOR_SAFIRA = "#0B486E";


    public static JSONObject parseJSONFromMetroResponse(String response) {

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
        response = response.replace("&#234;", "ê");
        response = response.replace("&#245;", "õ");


        try {
            JSONObject result = new JSONObject();
            JSONArray resultJsonArray = new JSONArray();

            JSONObject tempJson = new JSONObject(response);
            JSONArray lines = tempJson.getJSONArray("lines");
            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);

                switch (i) {
                    case 0:
                        // BLUE LINE
                        line.put(LINE_NUMBER, LINE_NUMBER_BLUE);
                        line.put(LINE_NAME, LINE_NAME_BLUE);
                        line.put(LINE_COLOR, LINE_COLOR_BLUE);
                        break;
                    case 1:
                        // GREEN LINE
                        line.put(LINE_NUMBER, LINE_NUMBER_GREEN);
                        line.put(LINE_NAME, LINE_NAME_GREEN);
                        line.put(LINE_COLOR, LINE_COLOR_GREEN);
                        break;
                    case 2:
                        // RED LINE
                        line.put(LINE_NUMBER, LINE_NUMBER_RED);
                        line.put(LINE_NAME, LINE_NAME_RED);
                        line.put(LINE_COLOR, LINE_COLOR_RED);
                        break;
                    case 3:
                        // YELLOW LINE
                        line.put(LINE_NUMBER, LINE_NUMBER_YELLOW);
                        line.put(LINE_NAME, LINE_NAME_YELLOW);
                        line.put(LINE_COLOR, LINE_COLOR_YELLOW);
                        break;
                    case 4:
                        // SILVER LINE
                        line.put(LINE_NUMBER, LINE_NUMBER_SILVER);
                        line.put(LINE_NAME, LINE_NAME_SILVER);
                        line.put(LINE_COLOR, LINE_COLOR_SILVER);
                        break;
                    case 5:
                        // LILAC LINE
                        line.put(LINE_NUMBER, LINE_NUMBER_LILAC);
                        line.put(LINE_NAME, LINE_NAME_LILAC);
                        line.put(LINE_COLOR, LINE_COLOR_LILAC);
                        break;
                }

                resultJsonArray.put(line);
            }

            result.put("lines", resultJsonArray);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static JSONObject parseJSONFromCptmResponse(String response) {

        int startIndex = response.indexOf("<h5>Situação das linhas​​​​</h5>");
        int lastIndex = response.indexOf("<div class='ultima_atualizacao'>| Atualizado em:");

        response = response.substring(startIndex, lastIndex);
        response = response.replace("<h5>Situação das linhas​​​​</h5>", "");

        response = response.replace("</span></div>", "\" },");



        // RUBI
        // Case normal speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 rubi'><span class='nome_linha'>RUBI</span><span data-placement='bottom' title=''   class='status_normal'>Operação Normal",
                "{ \"lines\": [ { \"lineNumber\" : " + LINE_NUMBER_RUBI + ", \"lineName\" : \"" + LINE_NAME_RUBI + "\", \"lineColor\" : \"" + LINE_COLOR_RUBI + "\", \"descricao\" : \"Operação Normal\", \"status\": \"normal");
//        response = response.replace("'   class='status_normal'>Operação Normal", "\",\n \"status\": \"normal");
        // Case reduced speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 rubi'><span class='nome_linha'>RUBI</span><span data-placement='bottom' title=''  data-original-title='",
                "{ \"lines\": [\n { \"lineNumber\" : " + LINE_NUMBER_RUBI + ", \"lineName\" : \"" + LINE_NAME_RUBI + "\", \"lineColor\" : \"" + LINE_COLOR_RUBI + "\", \"descricao\" : \"");
        response = response.replace("'   class='status_reduzida'>", "\",\n \"status\": \"");
        response = response.replace("'   class='status_parcial'>", "\",\n \"status\": \"");


        // DIAMANTE
        // Case normal speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 diamante'><span class='nome_linha'>DIAMANTE</span><span data-placement='bottom' title=''   class='status_normal'>Operação Normal",
                "{ \"lineNumber\" : " + LINE_NUMBER_DIAMANTE + ", \"lineName\" : \"" + LINE_NAME_DIAMANTE + "\", \"lineColor\" : \"" + LINE_COLOR_DIAMANTE + "\", \"descricao\" : \"Operação Normal\", \"status\": \"normal");
//        response = response.replace("'   class='status_normal'>Operação Normal", "\",\n \"status\": \"normal");
        // Case reduced speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 diamante'><span class='nome_linha'>DIAMANTE</span><span data-placement='bottom' title=''  data-original-title='",
                "{ \"lineNumber\" : " + LINE_NUMBER_DIAMANTE + ", \"lineName\" : \"" + LINE_NAME_DIAMANTE + "\", \"lineColor\" : \"" + LINE_COLOR_DIAMANTE + "\", \"descricao\" : \"");
        response = response.replace("'   class='status_reduzida'>", "\",\n \"status\": \"");
        response = response.replace("'   class='status_parcial'>", "\",\n \"status\": \"");

        // ESMERALDA
        // Case normal speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 esmeralda'><span class='nome_linha'>ESMERALDA</span><span data-placement='bottom' title=''   class='status_normal'>Operação Normal",
                "{ \"lineNumber\" : " + LINE_NUMBER_ESMERALDA + ", \"lineName\" : \"" + LINE_NAME_ESMERALDA + "\", \"lineColor\" : \"" + LINE_COLOR_ESMERALDA + "\", \"descricao\" : \"Operação Normal\", \"status\": \"normal");
//        response = response.replace("'   class='status_normal'>Operação Normal", "\",\n \"status\": \"normal");
        // Case reduced speed or partial operation
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 esmeralda'><span class='nome_linha'>ESMERALDA</span><span data-placement='bottom' title=''  data-original-title='",
                "{ \"lineNumber\" : " + LINE_NUMBER_ESMERALDA + ", \"lineName\" : \"" + LINE_NAME_ESMERALDA + "\", \"lineColor\" : \"" + LINE_COLOR_ESMERALDA + "\", \"descricao\" : \"");
        response = response.replace("'   class='status_reduzida'>", "\",\n \"status\": \"");
        response = response.replace("'   class='status_parcial'>", "\",\n \"status\": \"");

        // TURQUESA
        // Case normal speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 turquesa'><span class='nome_linha'>TURQUESA</span><span data-placement='bottom' title=''   class='status_normal'>Operação Normal",
                "{ \"lineNumber\" : " + LINE_NUMBER_TURQUESA + ", \"lineName\" : \"" + LINE_NAME_TURQUESA + "\", \"lineColor\" : \"" + LINE_COLOR_TURQUESA + "\", \"descricao\" : \"Operação Normal\", \"status\": \"normal");
//        response = response.replace("'   class='status_normal'>Operação Normal", "\",\n \"status\": \"normal");
        // Case reduced speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 turquesa'><span class='nome_linha'>TURQUESA</span><span data-placement='bottom' title=''  data-original-title='",
                "{ \"lineNumber\" : " + LINE_NUMBER_TURQUESA + ", \"lineName\" : \"" + LINE_NAME_TURQUESA + "\", \"lineColor\" : \"" + LINE_COLOR_TURQUESA + "\", \"descricao\" : \"");
        response = response.replace("'   class='status_reduzida'>", "\",\n \"status\": \"");
        response = response.replace("'   class='status_parcial'>", "\",\n \"status\": \"");

        // CORAl
        // Case normal speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 coral'><span class='nome_linha'>CORAL</span><span data-placement='bottom' title=''   class='status_normal'>Operação Normal",
                "{ \"lineNumber\" : " + LINE_NUMBER_CORAL + ", \"lineName\" : \"" + LINE_NAME_CORAL + "\", \"lineColor\" : \"" + LINE_COLOR_CORAL + "\", \"descricao\" : \"Operação Normal\", \"status\": \"normal");
//        response = response.replace("'   class='status_normal'>Operação Normal", "\",\n \"status\": \"normal");
        // Case reduced speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 coral'><span class='nome_linha'>CORAL</span><span data-placement='bottom' title=''  data-original-title='",
                "{ \"lineNumber\" : " + LINE_NUMBER_CORAL + ", \"lineName\" : \"" + LINE_NAME_CORAL + "\", \"lineColor\" : \"" + LINE_COLOR_CORAL + "\", \"descricao\" : \"");
        response = response.replace("'   class='status_reduzida'>", "\",\n \"status\": \"");
        response = response.replace("'   class='status_parcial'>", "\",\n \"status\": \"");

        // SAFIRA
        // Case normal speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 safira'><span class='nome_linha'>SAFIRA</span><span data-placement='bottom' title=''   class='status_normal'>Operação Normal",
                "{ \"lineNumber\" : " + LINE_NUMBER_SAFIRA + ", \"lineName\" : \"" + LINE_NAME_SAFIRA + "\", \"lineColor\" : \"" + LINE_COLOR_SAFIRA + "\", \"descricao\" : \"Operação Normal\", \"status\": \"normal");
//        response = response.replace("'   class='status_normal'>Operação Normal", "\",\n \"status\": \"normal");
        // Case reduced speed
        response = response.replace("<div class='col-xs-4 col-sm-4 col-md-2 safira'><span class='nome_linha'>SAFIRA</span><span data-placement='bottom' title=''  data-original-title='",
                "{ \"lineNumber\" : " + LINE_NUMBER_SAFIRA + ", \"lineName\" : \"" + LINE_NAME_SAFIRA + "\", \"lineColor\" : \"" + LINE_COLOR_SAFIRA + "\", \"descricao\" : \"");
        response = response.replace("'   class='status_reduzida'>", "\",\n \"status\": \"");
        response = response.replace("'   class='status_parcial'>", "\",\n \"status\": \"");

        response = response.substring(0, response.length() - 1);
        response = response + "]}";

        response = response.replace("&#224;", "à");
        response = response.replace("&#225;", "á");
        response = response.replace("&#227;", "ã");
        response = response.replace("&#231;", "ç");
        response = response.replace("&#234;", "ê");
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
