package com.codepath.apps.flickmein;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewFlipper;

import com.codepath.apps.flickmein.fragments.AlbumFragment;
import com.codepath.apps.flickmein.fragments.QRFragment;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;
import com.tekle.oss.android.animation.AnimationFactory;


public class AlbumActivity extends ActionBarActivity {

    private AuthorizedAlbum album;

    private AlbumFragment albumFragment;
    private QRFragment qrFragment;

    private ViewFlipper viewFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        if (savedInstanceState == null) {
            album = (AuthorizedAlbum)getIntent().getSerializableExtra("album");
            albumFragment = AlbumFragment.newInstance(album);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.albumContainer, albumFragment)
                    .commit();

            qrFragment = QRFragment.newInstance(album);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.qrContainer, qrFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    public void addPictures(MenuItem item) {
        albumFragment.addPicturesState();
    }

    public void ShowQR(MenuItem item) {
        AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
    }
}
