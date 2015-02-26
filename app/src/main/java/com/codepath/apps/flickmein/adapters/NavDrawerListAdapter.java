package com.codepath.apps.flickmein.adapters;

import android.content.Context;
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
        return albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.drawer_nav_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtDrawerItemAlbumTitle);
        txtTitle.setText(albums.get(position).getTitle());
        
        return convertView;
    }
}
