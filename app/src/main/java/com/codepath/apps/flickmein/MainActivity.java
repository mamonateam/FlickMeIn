package com.codepath.apps.flickmein;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.flickmein.models.AuthorizedAlbum;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


// Important ToDos
// ToDo: show loader while pic is uploaded
// ToDo: Use color picker
// ToDo: Redirect to QR view when album is created
// ToDo: Scan QR and select own name and color for uploading pics
// ToDo: List of albums...
// ToDo: Add Loader while album is created (do not allow to change anything or click send again)
// ToDo: Settings like "filter by uploader"
// ToDo: Make Push notifications using Parse
// ToDo: Link albums user belongs to to Google account (for additional persistence)
// ToDo: moar styling

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void createNewAlbum(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void joinExistingAlbum(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    // ToDo: Temporary access to an album
    public void goToAlbumView(View view) {
        Intent i = new Intent(this, AlbumActivity.class);
        i.putExtra("id", "72157651012673115");
        // ToDo: we need to pass the oauth token to this call to make it available for not signed in users...
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            AuthorizedAlbum album = AuthorizedAlbum.fromQRInfo(scanResult.getContents());
            album.save();
            Log.d("QRParse", album.toString());
        } else {
            Toast.makeText(this, "Could not capture any QR", Toast.LENGTH_LONG).show();
        }
    }
    
}