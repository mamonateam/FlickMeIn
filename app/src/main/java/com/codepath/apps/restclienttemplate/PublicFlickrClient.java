package com.codepath.apps.restclienttemplate;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by jesusft on 2/19/15.
 */
public class PublicFlickrClient {

    private final static String API_BASE = "https://api.flickr.com/services/rest/";
    private final static String API_KEY = "&api_key=2f02a4b3865e3d5031b751b2938dec5d";

    public static void getOneInterestingPhoto(AsyncHttpResponseHandler handler) {
        String apiUrl = API_BASE + "?method=flickr.interestingness.getList&per_page=1&page=1&format=json&nojsoncallback=1" + API_KEY;
        AsyncHttpClient c = new AsyncHttpClient();
        c.get(apiUrl, null, handler);
    }
}
