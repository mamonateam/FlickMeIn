package com.codepath.apps.flickmein;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PublicFlickrClient {

    private final static String API_BASE = "https://api.flickr.com/services/rest/";
    private final static String API_KEY = "2f02a4b3865e3d5031b751b2938dec5d";

    public static void getOneInterestingPhoto(AsyncHttpResponseHandler handler) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("method", "flickr.interestingness.getList");
        params.put("per_page", "1");
        params.put("page", "1");
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        c.get(API_BASE, params, handler);
    }

    public static void getAlbumPhotos(String albumId, AsyncHttpResponseHandler handler) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("method", "flickr.photosets.getPhotos");
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("photoset_id", albumId);
        params.put("extras", "tags,url_o");
        params.put("api_key", API_KEY);
        c.get(API_BASE, params, handler);
    }

    public static void getAlbumInfo(String albumId, AsyncHttpResponseHandler handler) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("method", "flickr.photosets.getInfo");
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("photoset_id", albumId);
        params.put("api_key", API_KEY);
        c.get(API_BASE, params, handler);
    }
    
    public static void getPhotoSizes(String albumId, AsyncHttpResponseHandler handler) {
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("method", "flickr.photos.getSizes");
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("photo_id", albumId);
        params.put("api_key", API_KEY);
        c.get(API_BASE, params, handler);
    }
}
