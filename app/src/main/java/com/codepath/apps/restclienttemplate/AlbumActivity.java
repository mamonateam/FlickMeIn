package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.PicturesFragment;


public class AlbumActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // Initialize the fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        String albumId = getIntent().getStringExtra("id");
        PicturesFragment picturesFragment = PicturesFragment.newInstance(albumId);
        ft.replace(R.id.flPictures, picturesFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    public void addPictures(MenuItem item) {
    }

    public void ShowQR(MenuItem item) {
    }
}
