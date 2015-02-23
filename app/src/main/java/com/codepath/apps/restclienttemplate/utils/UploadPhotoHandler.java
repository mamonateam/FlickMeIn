package com.codepath.apps.restclienttemplate.utils;

import android.util.Log;

/**
 * Created by jesusft on 2/21/15.
 */
public abstract class UploadPhotoHandler {
    public void onSuccess(String photoID) {
        Log.w("PhotoUploadHandler", "onSuccess(String) was not overriden, but callback was received");
    }

    public void onFailure() {
        Log.w("PhotoUploadHandler", "onFailure() was not overriden, but callback was received");
    }
}
