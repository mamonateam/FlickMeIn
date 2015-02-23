package com.codepath.apps.restclienttemplate.models;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.security.Signature;
import java.util.Hashtable;
import java.util.IllegalFormatException;
import java.util.StringTokenizer;

/**
 * Created by jesusft on 2/22/15.
 */
public class AuthorizedAlbum {

    private static final String QR_SIGNATURE = "FlickMeInOrNot";
    private static final String QR_END = "JAMON";

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
        return QR_SIGNATURE + ":" + String.valueOf(photosetId) + ":" + token + ":" + secret + ":" + QR_END;
    }

    public Bitmap toBitmap(int height, int width) throws WriterException {
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(this.encodeToString(), BarcodeFormat.QR_CODE, width, height, hintMap);


        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, byteMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    public static AuthorizedAlbum fromQRInfo(String qrInfo) throws IllegalFormatException {
        if (!qrInfo.startsWith(QR_SIGNATURE)) {
            throw new IllegalArgumentException("Could not find proper signature");
        }
        if (!qrInfo.endsWith(QR_END)) {
            throw new IllegalArgumentException("QR Code is incomplete");
        }

        StringTokenizer st = new StringTokenizer(qrInfo, ":");

        int tokenCount = 0;
        long photosetId = -1;
        String token = "";
        String secret = "";
        while(st.hasMoreTokens()) {
            String t = st.nextToken();
            switch (tokenCount) {
                case 0:
                    // Intro token
                    break;
                case 1:
                    photosetId = Long.valueOf(t);
                    break;
                case 2:
                    token = t;
                    break;
                case 3:
                    secret = t;
                    break;
                case 4:
                    // Outro token
                    break;
            }
            tokenCount++;
        }
        return new AuthorizedAlbum(photosetId, token, secret);
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
