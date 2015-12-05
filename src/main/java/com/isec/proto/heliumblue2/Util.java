package com.isec.proto.heliumblue2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Rafael on 25/11/2015.
 */
public class Util {
    public static boolean isOnline(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
