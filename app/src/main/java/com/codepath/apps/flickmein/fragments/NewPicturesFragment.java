package com.codepath.apps.flickmein.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.flickmein.FlickrClientApp;
import com.codepath.apps.flickmein.R;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;
import com.codepath.apps.flickmein.models.FlickrPhoto;
import com.codepath.apps.flickmein.utils.UploadPhotoHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewPicturesFragment extends Fragment {

    // region Constants
    private static final int GALLERY_IMAGE_REQUEST = 101;
    private static final int CAMERA_IMAGE_REQUEST = 102;
    // endregion

    // region Variables
    private TextView tvCamera;
    private TextView tvGallery;
    private String mCurrentPhotoPath;
    private HorizontalScrollView hsvPicsScroll;
    private LinearLayout llPicsContainer;
    private ArrayList<Uri> imagesArray;
    private AuthorizedAlbum album;
    private OnPictureUploadedListener pictureUploadedListener;
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
            pickGalleryPhoto();
        }
    };

    private UploadPhotoHandler uploadPhotoHandler = new UploadPhotoHandler() {
        @Override
        public void onSuccess(String photoID) {
            Log.i("PhotoUploader-Success", "New photo id is " + photoID);
            FlickrClientApp.getRestClient().addToAlbum(photoID, album, addToAlbumHandler);
        }

        @Override
        public void onFailure() {
            Toast.makeText(getActivity(), "Photo upload could not be done", Toast.LENGTH_LONG).show();
            Log.e("PhotoUpload-Failure", "Could not upload photo");
            restore();
        }
    };

    private JsonHttpResponseHandler addToAlbumHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            // Call activity's listener
            if(pictureUploadedListener != null) {
                // ToDo: Get real tags
                FlickrPhoto newPhoto = new FlickrPhoto();
                newPhoto.setColor("ff00ff");
                newPhoto.setUser("sheniff");
                newPhoto.setUri(imagesArray.get(0));
                pictureUploadedListener.onPictureUploaded(newPhoto);
            }

            imagesArray.remove(0);
            llPicsContainer.removeViewAt(0);
            
            if (imagesArray.size() > 0) {
                uploadPicture(imagesArray.get(0));
                // Show progress bar in first child now...
                llPicsContainer.getChildAt(0).findViewById(R.id.progress_circular).setVisibility(View.VISIBLE);
            } else {
                restore();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Toast.makeText(getActivity(), "Could not add photo to album", Toast.LENGTH_LONG).show();
            Log.e("Add-to-album failure", "Could not add photo to album");
        }
    };
    // endregion

    public interface OnPictureUploadedListener {
        public void onPictureUploaded(FlickrPhoto pic);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_pictures, container, false);
        bindUIElements(v);
        setUpListeners();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        album = (AuthorizedAlbum) getActivity().getIntent().getSerializableExtra("album");
    }

    private void bindUIElements(View v) {
        tvCamera = (TextView) v.findViewById(R.id.tvTakePicture);
        tvGallery = (TextView) v.findViewById(R.id.tvFromGallery);
        hsvPicsScroll = (HorizontalScrollView) v.findViewById(R.id.hsvNewPictures);
        llPicsContainer = (LinearLayout) v.findViewById(R.id.llNewPictures);
    }

    private void setUpListeners() {
        tvCamera.setOnClickListener(cameraClickListener);
        tvGallery.setOnClickListener(galleryClickListener);
    }

    public static NewPicturesFragment newInstance(AuthorizedAlbum album) {
        NewPicturesFragment fragment = new NewPicturesFragment();
        Bundle args = new Bundle();
        args.putSerializable("album", album);
        fragment.setArguments(args);

        return fragment;
    }

    public void setOnPictureUploadedListener(OnPictureUploadedListener listener) {
        this.pictureUploadedListener = listener;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            imagesArray = new ArrayList<>();

            if (requestCode == GALLERY_IMAGE_REQUEST) {
                ClipData clips = data.getClipData();

                if (clips != null) {
                    for (int i = 0; i < clips.getItemCount(); i++) {
                        ClipData.Item item = clips.getItemAt(i);
                        imagesArray.add(item.getUri());
                    }
                } else {
                    imagesArray.add(data.getData());
                }
            } else if (requestCode == CAMERA_IMAGE_REQUEST) {
                imagesArray.add(Uri.parse(mCurrentPhotoPath));
            }

            hsvPicsScroll.setVisibility(View.VISIBLE);

            llPicsContainer.removeAllViews();
            populateGallery(imagesArray);
            uploadPicture(imagesArray.get(0));
        } else {
            restore();
        }
    }

    // ToDo: Refactor all this crap to make it more efficient... (use an horizontal list view)
    private void populateGallery(ArrayList<Uri> gallery) {
        int size = gallery.size();
        int height = getResources().getDimensionPixelSize(R.dimen.horizontal_gallery_height);
        
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        layoutParams.gravity = Gravity.LEFT;

        for (int i = 0; i < size; i++) {
            RelativeLayout rlWrapper = new RelativeLayout(this.getActivity());

            rlWrapper.setTag(i);
            rlWrapper.setLayoutParams(layoutParams);
            rlWrapper.setPadding(0, 0, 5, 0);

            ImageView image = new ImageView(this.getActivity());
            image.setLayoutParams(layoutParams);
            image.setAdjustViewBounds(true);
            
            rlWrapper.addView(image);
                        
            try {
                Bitmap bitmap = decodeSampledBitmapFromUri(getActivity().getContentResolver(), gallery.get(i), height);
                image.setImageBitmap(bitmap);

                // loader image
                ProgressBar pb = new ProgressBar(this.getActivity());
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        bitmap.getWidth(),
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

                pb.setLayoutParams(lp);
                pb.setPadding(50, 50, 50, 50);
                pb.setId(R.id.progress_circular);
                if(i > 0) {
                    pb.setVisibility(View.GONE);
                }
                pb.setBackgroundColor(Color.parseColor("#44666666"));
                rlWrapper.addView(pb);
                llPicsContainer.addView(rlWrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPicture(Uri imageUri) {
        InputStream photoStream;
        try {
            photoStream = this.getActivity().getContentResolver().openInputStream(imageUri);
            // ToDo: Get real tags
            FlickrClientApp.getRestClient().uploadPhoto(album, photoStream, new String[]{"fmiusersheniff", "fmicolorff00ff"}, uploadPhotoHandler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* Image selection methods */

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void pickGalleryPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), GALLERY_IMAGE_REQUEST);
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

    public void restore() {
        // tvCamera.setVisibility(View.VISIBLE);
        // tvGallery.setVisibility(View.VISIBLE);
        hsvPicsScroll.setVisibility(View.GONE);
        llPicsContainer.removeAllViews();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight) {

            final int halfHeight = height / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromUri(ContentResolver cr, Uri uri, int reqHeight) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        InputStream stream = cr.openInputStream(uri);
        BitmapFactory.decodeStream(stream, null, options);
        stream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        stream = cr.openInputStream(uri);
        return BitmapFactory.decodeStream(stream, null, options);
    }

    public void changeAlbum(AuthorizedAlbum album) {
        this.album = album;
    }
}
