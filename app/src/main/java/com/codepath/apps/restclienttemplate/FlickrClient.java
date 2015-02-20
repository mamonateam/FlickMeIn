package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;

public class FlickrClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = FlickrApi.class;
    public static final String REST_URL = "http://www.flickr.com/services";
    public static final String REST_CONSUMER_KEY = "2f02a4b3865e3d5031b751b2938dec5d";
    public static final String REST_CONSUMER_SECRET = "409f82d889991535";
    public static final String REST_CALLBACK_URL = "oauth://cprest";
    
    public FlickrClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
        setBaseUrl("https://api.flickr.com/services/rest");
    }

    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?&format=json&nojsoncallback=1&method=flickr.interestingness.getList");
        Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    // Unauthorized call
    public void getOneInterestingPhoto(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?method=flickr.interestingness.getList&per_page=1&page=1&format=json&nojsoncallback=1&api_key=2f02a4b3865e3d5031b751b2938dec5d");
        Log.d("DEBUG", "Sending API call to " + apiUrl);
        AsyncHttpClient c = new AsyncHttpClient();
        c.get(apiUrl, null, handler);
    }

}
