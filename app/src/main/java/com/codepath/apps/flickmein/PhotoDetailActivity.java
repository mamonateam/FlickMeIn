package com.codepath.apps.flickmein;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.codepath.apps.flickmein.models.FlickrPhoto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class PhotoDetailActivity extends ActionBarActivity {

    private FlickrPhoto result;
    private ImageView ivImageResult;
    private ShareActionProvider miShareAction;

    private FrameLayout flImageContainer;
    private ImageView ivLoaderImage;
    private RelativeLayout rlLoader;
    private Animation animRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ivImageResult = (ImageView) findViewById(R.id.ivImageResult);

        flImageContainer = (FrameLayout) findViewById(R.id.flImageContainer);
        ivLoaderImage = (ImageView) findViewById(R.id.ivLoaderImage);
        rlLoader = (RelativeLayout) findViewById(R.id.rlLoader);

        result = (FlickrPhoto) getIntent().getSerializableExtra("result");

        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        ivLoaderImage.setAnimation(animRotate);

        Picasso.with(this).load(result.getUrl()).into(ivImageResult, new Callback() {
            @Override
            public void onSuccess() {
                if(ivImageResult.getVisibility() != FrameLayout.VISIBLE) {
                    ivImageResult.setVisibility(FrameLayout.VISIBLE);
                    rlLoader.setVisibility(FrameLayout.GONE);
                }
                setupShareIntent();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void setupShareIntent() {
        // Fetch Bitmap Uri locally
        ImageView ivImage = (ImageView) findViewById(R.id.ivImageResult);
        Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        miShareAction.setShareIntent(shareIntent);
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
