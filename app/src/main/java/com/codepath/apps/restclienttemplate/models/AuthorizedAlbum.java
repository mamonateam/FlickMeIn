package com.codepath.apps.restclienttemplate.models;

import android.graphics.Bitmap;

/**
 * Created by jesusft on 2/22/15.
 */
public class AuthorizedAlbum {

    private long photosetId;
    private String token;
    private String secret;

    public AuthorizedAlbum(long photosetId, String token, String secret) {
        this.photosetId = photosetId;
        this.token = token;
        this.secret = secret;
    }

    public long getPhotosetId() {
        return photosetId;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public Bitmap toBitmap() {
        // TODO Generate QR from instance
        return null;
    }

    public static AuthorizedAlbum fromQRInfo(String qrInfo) {
        // TODO Parse album instance from QR info
        return null;
    }

    @Override
    public String toString() {
        return "AuthorizedAlbum{" +
                "photosetId=" + photosetId +
                ", token='" + token + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
