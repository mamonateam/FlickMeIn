package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void createNewAlbum(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void joinExistingAlbum(View view) {
        // TODO - Join Album flow start point
    }

    // ToDo: Temporary access to an album
    public void goToAlbumView(View view) {
        Intent i = new Intent(this, AlbumActivity.class);
        i.putExtra("id", "72157647876707511");
        // ToDo: we need to pass the oauth token to this call to make it available for not signed in users...
        startActivity(i);
    }
    
}
