package com.codepath.apps.flickmein;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.flickmein.fragments.UserInfoFormFragment;
import com.codepath.apps.flickmein.models.AlbumContributor;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class JoinAlbumActivity extends ActionBarActivity {

    // region Variables
    private AuthorizedAlbum album;
    private UserInfoFormFragment userFragment;
    private TextView tvTitle;
    private TextView tvOwner;
    private ImageView ivPrimary;
    // endregion

    // region Listeners
    private JsonHttpResponseHandler onAlbumInfoReceived = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            String title = "<no title>";
            String owner = "<no owner>";
            String primaryPhoto = null;
            
            try {
                title = response.getJSONObject("photoset").getJSONObject("title").getString("_content");
                primaryPhoto = response.getJSONObject("photoset").getString("primary");
                owner = response.getJSONObject("photoset").getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            album.setTitle(title);
            album.save();
            
            // set to view
            tvTitle.setText(title);
            tvOwner.setText("by " + owner);
            
            if(primaryPhoto != null) {
                PublicFlickrClient.getPhotoSizes(primaryPhoto, onPhotoSizesReceived);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("ALBUM_INFO_ERROR", errorResponse.toString());
        }
    };
    private JsonHttpResponseHandler onPhotoSizesReceived = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            String photoUrl = null;

            try {
                photoUrl = response.getJSONObject("sizes").getJSONArray("size").getJSONObject(1).getString("source");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(photoUrl != null) {
                Picasso.with(JoinAlbumActivity.this).load(photoUrl).resize(0,300).into(ivPrimary);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("PHOTO_SIZES_ERROR", errorResponse.toString());
        }
    };
    // endregion
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        album = (AuthorizedAlbum) getIntent().getSerializableExtra("album");
        setContentView(R.layout.activity_join_album);

        bindUIElements();
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AuthorizedAlbum album = (AuthorizedAlbum) getIntent().getSerializableExtra("album");
        userFragment = new UserInfoFormFragment();
        ft.replace(R.id.flContributor, userFragment);
        ft.commit();
        
        PublicFlickrClient.getAlbumInfo(String.valueOf(album.getPhotosetId()), onAlbumInfoReceived);
    }

    private void bindUIElements() {
        tvTitle = (TextView) findViewById(R.id.tvAlbumTitle);
        tvOwner = (TextView) findViewById(R.id.tvOwner);
        ivPrimary = (ImageView) findViewById(R.id.ivPrimaryPhoto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void joinAlbum(View view) {
        AlbumContributor contributor = userFragment.getAlbumContributor();
        contributor.save();
        album.setContributor(contributor);
        album.save();
        Intent i = new Intent(this, AlbumActivity.class);
        i.putExtra("album", album);
        startActivity(i);
    }
}
