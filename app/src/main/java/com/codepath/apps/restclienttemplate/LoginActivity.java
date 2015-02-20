package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.codepath.apps.restclienttemplate.models.FlickrPhoto;
import com.codepath.apps.restclienttemplate.utils.BlurTransform;
import com.codepath.oauth.OAuthLoginActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends OAuthLoginActivity<FlickrClient> {

    private ImageView ivBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        ivBackground = (ImageView) findViewById(R.id.ivBackground);

        FlickrClientApp.getRestClient().getOneInterestingPhoto(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    FlickrPhoto fp = new FlickrPhoto(response.getJSONObject("photos").getJSONArray("photo").getJSONObject(0));
                    Picasso.with(LoginActivity.this)
                            .load(fp.getUrl())
//                            .centerCrop()
                            .transform(new BlurTransform(LoginActivity.this))
                            .into(ivBackground);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ivBackground.setImageResource(R.drawable.default_background);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ivBackground.setImageResource(R.drawable.default_background);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
    @Override
    public void onLoginSuccess() {
    	Intent i = new Intent(this, PhotosActivity.class);
    	startActivity(i);
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    public void loginToRest(View view) {
        getClient().connect();
    }

}
