package com.codepath.apps.flickmein.utils;

import android.util.Log;

public abstract class UploadPhotoHandler {
    public void onSuccess(String photoID) {
        Log.w("PhotoUploadHandler", "onSuccess(String) was not overriden, but callback was received");
    }

    public void onFailure() {
        Log.w("PhotoUploadHandler", "onFailure() was not overriden, but callback was received");
    }
}
