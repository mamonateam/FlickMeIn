package com.codepath.apps.restclienttemplate;

import android.content.Context;

public class FlickrClientApp extends com.activeandroid.app.Application {
	private static Context context;
	
    @Override
    public void onCreate() {
        super.onCreate();
        FlickrClientApp.context = this;
    }
    
    public static FlickrClient getRestClient() {
    	return (FlickrClient) FlickrClient.getInstance(FlickrClient.class, FlickrClientApp.context);
    }
}