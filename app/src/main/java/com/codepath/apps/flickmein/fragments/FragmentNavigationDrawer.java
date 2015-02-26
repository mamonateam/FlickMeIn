package com.codepath.apps.flickmein.fragments;
 
/*
** FragmentNavigationDrawer object for use with support-v7 library
** using compatibility fragments and support actionbar
*/

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.flickmein.R;
import com.codepath.apps.flickmein.adapters.NavDrawerListAdapter;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;

import java.util.ArrayList;

public class FragmentNavigationDrawer extends DrawerLayout {
    private ActionBarDrawerToggle drawerToggle;
    private ListView lvDrawer;
    private Toolbar toolbar;
    private NavDrawerListAdapter drawerAdapter;
    private ArrayList<AuthorizedAlbum> albumNavItems;
    private AlbumFragment albumFragment;
    private QRFragment qrFragment;

    public FragmentNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FragmentNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentNavigationDrawer(Context context) {
        super(context);
    }

    public void setupDrawerConfiguration(ListView drawerListView, Toolbar drawerToolbar, int drawerItemRes, AlbumFragment albumFragment, QRFragment qrFragment) {
        // Setup navigation items array
        albumNavItems = new ArrayList<>();
        this.albumFragment = albumFragment;
        this.qrFragment = qrFragment;
        // Setup drawer list view and related adapter
        lvDrawer = drawerListView;
        // Setup toolbar
        toolbar = drawerToolbar;
        // Setup item listener
        lvDrawer.setOnItemClickListener(new FragmentDrawerItemListener());
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar icon
        drawerToggle = setupDrawerToggle();
        setDrawerListener(drawerToggle);
        // Setup action buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void addNavItem(AuthorizedAlbum album) {
        albumNavItems.add(album);
        drawerAdapter = new NavDrawerListAdapter(getActivity(), albumNavItems);
        lvDrawer.setAdapter(drawerAdapter);
    }

    /**
     * Swaps fragments in the main content view
     */
    public void selectDrawerItem(int position) {
        AuthorizedAlbum currentAlbum = albumNavItems.get(position);

        albumFragment.changeAlbum(currentAlbum);
        qrFragment.changeAlbum(currentAlbum);

        // Highlight the selected item, update the title, and close the drawer
        lvDrawer.setItemChecked(position, true);
        setTitle(currentAlbum.getTitle());        
        closeDrawer(lvDrawer);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) getContext();
    }

    private ActionBar getSupportActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    private class FragmentDrawerItemListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectDrawerItem(position);
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(getActivity(), this, toolbar, R.string.app_name, R.string.app_name);
    }

    public boolean isDrawerOpen() {
        return isDrawerOpen(lvDrawer);
    }
}