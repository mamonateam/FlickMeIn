package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.FlickrClient;
import com.codepath.apps.restclienttemplate.FlickrClientApp;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.PhotoArrayAdapter;
import com.codepath.apps.restclienttemplate.models.FlickrPhoto;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PicturesFragment extends Fragment {
    
    // region Variables
    private FlickrClient client;
    private StaggeredGridView gvPics;
    private ArrayList<FlickrPhoto> pictures;
    private PhotoArrayAdapter adapter;
    // endregion
    
    // region Listeners
    private JsonHttpResponseHandler photosHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                adapter.addAll(FlickrPhoto.fromJSONArray(response.getJSONObject("photoset").getJSONArray("photo")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.d("ERROR_PHOTOS_FETCH", Integer.toString(statusCode));
        }
    };
    // endregion
    
    public static PicturesFragment newInstance(String albumId) {
        PicturesFragment fragment = new PicturesFragment();
        Bundle args = new Bundle();
        args.putString("id", albumId);
        fragment.setArguments(args);
        
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pictures, container, false);
        bindUIElements(v);
        pictures = new ArrayList<>();
        adapter = new PhotoArrayAdapter(this.getActivity(), pictures);
        gvPics.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = FlickrClientApp.getRestClient();
        // fetch album pics
        client.getAlbumPhotos(getArguments().getString("id"), photosHandler);
    }

    private void bindUIElements(View v) {
        gvPics = (StaggeredGridView) v.findViewById(R.id.gvPics);
    }
}
