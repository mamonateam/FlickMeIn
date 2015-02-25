package com.codepath.apps.flickmein;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.codepath.apps.flickmein.fragments.NewPicturesFragment;
import com.codepath.apps.flickmein.fragments.PicturesFragment;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;


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
        AuthorizedAlbum album = (AuthorizedAlbum) getIntent().getSerializableExtra("album");
        PicturesFragment picturesFragment = PicturesFragment.newInstance(String.valueOf(album.getPhotosetId()));
        ft.replace(R.id.flPictures, picturesFragment);
        ft.commit();
        
        // Initialize new pictures fragment
        ft = getSupportFragmentManager().beginTransaction();
        newPicturesFragment = NewPicturesFragment.newInstance(album);
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
