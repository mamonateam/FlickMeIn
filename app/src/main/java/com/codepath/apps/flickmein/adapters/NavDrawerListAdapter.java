package com.codepath.apps.flickmein.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codepath.apps.flickmein.R;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    // region Variables
    private Context context;
    private ArrayList<AuthorizedAlbum> albums;
    // endregion


    public NavDrawerListAdapter(Context context, ArrayList<AuthorizedAlbum> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position - 2);
    }

    @Override
    public long getItemId(int position) {
        return position - 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.drawer_nav_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtDrawerItemAlbumTitle);

        if(position == 0) {
            // Special case: create new album
            txtTitle.setText("Create New Album");
            txtTitle.setTextColor(convertView.getResources().getColor(R.color.flickr_pink));
        } else if (position == 1) {
            // Special case: join to album
            txtTitle.setText("Join To Album");
            txtTitle.setTextColor(convertView.getResources().getColor(R.color.flickr_blue));
        } else {
            txtTitle.setText(albums.get(position - 2).getTitle());
            txtTitle.setBackgroundColor(Color.TRANSPARENT);
            txtTitle.setTextColor(Color.BLACK);
        }
        
        return convertView;
    }
}
