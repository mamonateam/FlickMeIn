package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.codepath.apps.restclienttemplate.fragments.NewPicturesFragment;
import com.codepath.apps.restclienttemplate.fragments.PicturesFragment;


public class AlbumActivity extends ActionBarActivity {

    // region Variables
    private FrameLayout flNewPictures;
    private NewPicturesFragment newPicturesFragment;
    // endregion
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        bindUIElements();
        
        // Initialize the pictures fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        String albumId = getIntent().getStringExtra("id");
        PicturesFragment picturesFragment = PicturesFragment.newInstance(albumId);
        ft.replace(R.id.flPictures, picturesFragment);
        ft.commit();
        
        // Initialize new pictures fragment
        ft = getSupportFragmentManager().beginTransaction();
        newPicturesFragment = new NewPicturesFragment();
        ft.replace(R.id.flNewPictures, newPicturesFragment);
        ft.commit();
    }

    private void bindUIElements() {
        flNewPictures = (FrameLayout) findViewById(R.id.flNewPictures); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    public void addPictures(MenuItem item) {
        if(flNewPictures.getVisibility() == View.VISIBLE) {
            flNewPictures.setVisibility(View.GONE);
            newPicturesFragment.restore();
        } else {
            flNewPictures.setVisibility(View.VISIBLE);
        }
    }

    public void ShowQR(MenuItem item) {
    }
}
