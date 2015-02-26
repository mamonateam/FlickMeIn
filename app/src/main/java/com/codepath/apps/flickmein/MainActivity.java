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

import java.util.ArrayList;


// Important ToDos
// ToDo: ReStyle pictures to change border for a simple triangle in the corner (bottom right)
// ToDo: Redirect to QR view when album is created {jesus}
// ToDo: moar styling
// ToDo: Add Loader while album is created (do not allow to change anything or click send again)
// ToDo: (Stretch) Make Push notifications using Parse
// ToDo: (Stretch) Make horizontal scroll view more efficient using a list view :)
// ToDo: (Stretch) Settings like "filter by uploader"
// ToDo: (Stretch) Link albums user belongs to to Google account (for additional persistence)

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        ArrayList<AuthorizedAlbum> albums = (ArrayList<AuthorizedAlbum>) AuthorizedAlbum.getAll(); 
        
        if(albums.size() > 0) {
            Intent i = new Intent(this, AlbumActivity.class);
            AuthorizedAlbum album = albums.get(0);
            i.putExtra("album", album);
            startActivity(i);
            finish();
        }
    }

    public void createNewAlbum(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void joinExistingAlbum(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            AuthorizedAlbum album = AuthorizedAlbum.fromQRInfo(scanResult.getContents());
            Log.d("QRParse", album.toString());
            // Go to JoinAlbumActivity
            Intent i = new Intent(this, JoinAlbumActivity.class);
            i.putExtra("album", album);
            startActivity(i);
        } else {
            Toast.makeText(this, "Could not capture any QR", Toast.LENGTH_LONG).show();
        }
    }
    
}
