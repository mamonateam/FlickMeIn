package com.codepath.apps.restclienttemplate.models;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

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

    private String encodeToString() {
        return String.valueOf(photosetId) + ":" + token + ":" + secret;
    }

    public Bitmap toBitmap() throws WriterException {
        // TODO Generate QR from instance
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(this.encodeToString(), BarcodeFormat.QR_CODE, 200, 200, hintMap);
        Bitmap ImageBitmap = Bitmap.createBitmap(180, 40, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < 180; i++) {//width
            for (int j = 0; j < 40; j++) {//height
                ImageBitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        return ImageBitmap;
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
