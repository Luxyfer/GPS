package com.isec.proto.heliumblue2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Rafael on 21/11/2015.
 */
public class Parser {
    private final static String URL_BASE = "http://api.tvmaze.com";
    private final static String URL_PROCURA_MULTIPLA = "/search/shows?q=";
    private final static String URL_PROCURA_ID = "/shows/";

    private boolean online;

    private final static int IDS_MAIS_VISTAS[] = {73, 82, 66, 31, 216, 13, 4, 5, 83, 11, 210, 42, 29, 23, 1, 2, 3, 6, 12, 17};

    public Parser() {
        online = true;
    }

    public Parser(boolean online) {
        this.online = online;
    }

    public ArrayList<Serie> procurarSeries(String entrada) {
        ArrayList<Serie> s = new ArrayList<>();
        String temp = URL_BASE + URL_PROCURA_MULTIPLA + entrada;
        String saida;

        try {
            URL url = new URL(temp);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            temp = uri.toASCIIString();
            saida = readContents(temp);
            JSONArray res = new JSONArray(saida);

            for (int i = 0; i < res.length(); i++) {
                JSONObject jsonobject = res.getJSONObject(i);
                JSONObject temp_json = jsonobject.getJSONObject("show");

                Serie temp_serie = new Serie(temp_json.getString("name"), temp_json.getInt("id"), new URL(temp_json.getJSONObject("image").getString("medium")), temp_json.getString("summary"));
                s.add(temp_serie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return s;
    }

    public Serie procurarSeries(int id) {
        ArrayList<Serie> s = new ArrayList<>();
        String temp = URL_BASE + URL_PROCURA_ID + id;
        String saida;
        Serie temp_serie = null;

        try {
            saida = readContents(temp);

            JSONObject jsonobject = new JSONObject(saida);
            temp_serie = new Serie(jsonobject.getString("name"), id, new URL(jsonobject.getJSONObject("image").getString("medium")), jsonobject.getString("summary"));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return temp_serie;
    }

    private String readContents(String url) throws InterruptedException, ExecutionException {
        return new Conector().execute(url).get();
    }

    public ArrayList<Serie> maisVistas() {
        ArrayList<Serie> series = new ArrayList<>();

        for (int i = 0; i < IDS_MAIS_VISTAS.length; i++) {
            Serie temp = procurarSeries(IDS_MAIS_VISTAS[i]);
            if (temp != null)
                series.add(temp);
        }

        return series;
    }
}
