package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FlickrClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = FlickrApi.class;
    public static final String REST_URL = "http://www.flickr.com/services";
    public static final String REST_CONSUMER_KEY = "2f02a4b3865e3d5031b751b2938dec5d";
    public static final String REST_CONSUMER_SECRET = "409f82d889991535";
    public static final String REST_CALLBACK_URL = "oauth://cprest";

    public static final String REST_API_URL = "https://api.flickr.com/services/rest";
    public static final String UPLOAD_API_URL = "https://up.flickr.com/services/upload";

    
    public FlickrClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
        setBaseUrl(REST_API_URL);
    }

    public void getUsername(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?method=flickr.test.login&format=json&nojsoncallback=1");
        client.get(apiUrl, null, handler);
    }

    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?&format=json&nojsoncallback=1&method=flickr.interestingness.getList");
        Log.d("DEBUG", "Sending API call to " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void uploadPhoto(Uri photoUri, String[] tags, AsyncHttpResponseHandler handler) {
        RequestParams params;
        try {
            setBaseUrl(UPLOAD_API_URL);
            params = new RequestParams();
            params.put("photo", new File(photoUri.getPath()));
            params.put("tags", TextUtils.join(" ", tags));
            client.post(getApiUrl(" "), params, handler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            setBaseUrl(REST_API_URL);
        }
    }

}
