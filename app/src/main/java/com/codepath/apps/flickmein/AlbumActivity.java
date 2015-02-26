package com.codepath.apps.flickmein;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.codepath.apps.flickmein.fragments.AlbumFragment;
import com.codepath.apps.flickmein.fragments.FragmentNavigationDrawer;
import com.codepath.apps.flickmein.fragments.QRFragment;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;
import com.tekle.oss.android.animation.AnimationFactory;

import java.util.ArrayList;

public class AlbumActivity extends ActionBarActivity {

    // region Variables
    private AlbumFragment albumFragment;
    private ViewFlipper viewFlipper;
    private FragmentNavigationDrawer dlDrawer;
    // endregion
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // find drawer view
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        if (savedInstanceState == null) {
            AuthorizedAlbum album = (AuthorizedAlbum) getIntent().getSerializableExtra("album");

            albumFragment = AlbumFragment.newInstance(album);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.albumContainer, albumFragment)
                    .commit();

            QRFragment qrFragment = QRFragment.newInstance(album);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.qrContainer, qrFragment)
                    .commit();

            // setup drawer view
            dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), toolbar, R.layout.drawer_nav_item, albumFragment, qrFragment);
            dlDrawer.setTitle(album.getTitle());

            // Add nav items
            ArrayList<AuthorizedAlbum> albums = (ArrayList<AuthorizedAlbum>) AuthorizedAlbum.getAll();
            for (int i = 0; i < albums.size(); i++) {
                dlDrawer.addNavItem(albums.get(i));
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        if (dlDrawer.isDrawerOpen()) {
            menu.findItem(R.id.action_add_pics).setVisible(false);
            menu.findItem(R.id.action_qr).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dlDrawer.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
    }
    
    public void addPictures(MenuItem item) {
        albumFragment.addPicturesState();
    }

    public void ShowQR(MenuItem item) {
        AnimationFactory.flipTransition(viewFlipper, AnimationFactory.FlipDirection.LEFT_RIGHT);
    }
}
