package com.codepath.apps.flickmein.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codepath.apps.flickmein.R;
import com.codepath.apps.flickmein.models.FlickrPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoArrayAdapter extends ArrayAdapter<FlickrPhoto> {
	public PhotoArrayAdapter(Context context, List<FlickrPhoto> photoList) {
		super(context, R.layout.photo_item, photoList);
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		FlickrPhoto photo = this.getItem(position);
		LinearLayout itemView;
		ImageView ivImage;
        if (convertView == null) {
    		LayoutInflater inflator = LayoutInflater.from(getContext());
    		itemView = (LinearLayout) inflator.inflate(R.layout.photo_item, parent, false);
        } else {
            itemView = (LinearLayout) convertView;
        }
        ivImage = (ImageView) itemView.findViewById(R.id.ivPhoto);
        ivImage.setImageResource(android.R.color.transparent);
        // set border based on user's color
        if(photo.getColor() != null && !photo.getColor().equals("")) {
            ivImage.setBackgroundColor(Color.parseColor("#" + photo.getColor()));
        }
        if(photo.getUrl() != null) {
            Picasso.with(getContext()).load(photo.getUrl()).resize(500,0).into(ivImage);
        } else if(photo.getUri() != null) {
            Picasso.with(getContext()).load(photo.getUri()).resize(500,0).into(ivImage);
        }
        return itemView;
	}
}
