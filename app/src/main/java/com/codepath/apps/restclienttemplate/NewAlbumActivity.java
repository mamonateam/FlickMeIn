package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.fragments.UserInfoFormFragment;
import com.codepath.apps.restclienttemplate.models.AlbumContributor;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewAlbumActivity extends ActionBarActivity {

    // region Variables
    private static final int GALLERY_IMAGE_REQUEST = 101;
    private static final int CAMERA_IMAGE_REQUEST = 102;

    private UserInfoFormFragment userFragment;
    private ImageView ivPic;
    private TextView tvCamera;
    private TextView tvGallery;
    private ProgressBar progress;
    private EditText etAlbumName;

    // transients
    private String mCurrentPhotoPath;
    private Uri currentPhotoUri;
    // endregion
    
    // region Listeners
    private View.OnClickListener cameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent();
        }
    };

    private View.OnClickListener galleryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pickGalleryPhoto(v);
        }
    };
    
    private JsonHttpResponseHandler usernameHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                userFragment.setName(response.getJSONObject("user").getJSONObject("username").getString("_content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            // TODO - Toast
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    private TextHttpResponseHandler photoUploadHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            if (statusCode == 200) {
                String photoId = android.text.Html.fromHtml(responseString).toString();
                // Create album
                FlickrClientApp.getRestClient().createPhotoSet(etAlbumName.getText().toString(), photoId, photosetHandler);
            } else {
                // TODO - Upload error
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            // TODO - Upload error
        }

        @Override
        public void onProgress(int bytesWritten, int totalSize) {
            progress.setProgress((bytesWritten/totalSize)*100);
        }
    };

    private JsonHttpResponseHandler photosetHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Toast.makeText(NewAlbumActivity.this,"Album created", Toast.LENGTH_LONG).show();
            // TODO - Were do we go if something is good
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            // TODO - photoset error
        }
    };
    // endregion
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album);

        userFragment = new UserInfoFormFragment();

        bindUIElements();
        setupListeners();

        getSupportActionBar().hide();
        
        // resolve flickr user name
        FlickrClientApp.getRestClient().getUsername(usernameHandler);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flUserInfo, userFragment);
        ft.commit();
    }

    private void bindUIElements() {
        ivPic = (ImageView) findViewById(R.id.ivPic);
        tvCamera = (TextView) findViewById(R.id.tvTakePicture);
        tvGallery = (TextView) findViewById(R.id.tvFromGallery);
        progress = (ProgressBar) findViewById(R.id.progress);
        etAlbumName = (EditText) findViewById(R.id.etAlbumName);
    }

    private void setupListeners() {
        tvCamera.setOnClickListener(cameraClickListener);
        tvGallery.setOnClickListener(galleryClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_album, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK) {
                currentPhotoUri = data.getData();
                ivPic.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentPhotoUri));
            } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
                currentPhotoUri = Uri.parse(mCurrentPhotoPath);
                ivPic.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentPhotoUri));
            }
            tvCamera.setVisibility(View.GONE);
            tvGallery.setVisibility(View.GONE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createNewAlbum(View view) {
        // TODO - Check album contributor & currentPhotoUri exists
        // Create album contributor
        AlbumContributor ac = userFragment.getAlbumContributor();
        // Upload photo
        progress.setVisibility(View.VISIBLE);
        FlickrClientApp.getRestClient().uploadPhoto(currentPhotoUri, ac.getTags(), photoUploadHandler);
        // Create album
        // GOTO AlbumListView
    }

    public void goToMain(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }    
    
    /* Image selection methods */

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_IMAGE_REQUEST);
            }
        }
    }

    public void pickGalleryPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_IMAGE_REQUEST);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
