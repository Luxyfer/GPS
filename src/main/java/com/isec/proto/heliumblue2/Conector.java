package com.isec.proto.heliumblue2;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Rafael on 22/11/2015.
 */
public class Conector extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        if (params.length > 0) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(params[0])
                    .build();
            Response responses = null;

            try {
                responses = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return "ERRO";
            }

            try {
                return responses.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "ERRO";
    }
}