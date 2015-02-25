package com.codepath.apps.flickmein.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.codepath.apps.flickmein.R;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;
import com.codepath.apps.flickmein.models.FlickrPhoto;

public class AlbumFragment extends Fragment {

    private AuthorizedAlbum album;

    // region Variables
    private FrameLayout flNewPictures;
    private NewPicturesFragment newPicturesFragment;
    private PicturesFragment picturesFragment;
    // endregion

    // region Listeners
    private NewPicturesFragment.OnPictureUploadedListener onPictureUploadedListener = new NewPicturesFragment.OnPictureUploadedListener() {
        @Override
        public void onPictureUploaded(FlickrPhoto pic) {
            picturesFragment.addPicture(pic);
        }
    };
    // endregion

    public static AlbumFragment newInstance(AuthorizedAlbum album) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable("album", album);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album, container, false);

        bindUIElements(v);

        // Initialize the pictures fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        picturesFragment = PicturesFragment.newInstance(String.valueOf(album.getPhotosetId()));
        ft.replace(R.id.flPictures, picturesFragment);
        ft.commit();

        // Initialize new pictures fragment
        ft = getFragmentManager().beginTransaction();
        newPicturesFragment = NewPicturesFragment.newInstance(album);
        newPicturesFragment.setOnPictureUploadedListener(onPictureUploadedListener);
        ft.replace(R.id.flNewPictures, newPicturesFragment);
        ft.commit();

        return v;
    }

    public void addPicturesState() {
        if(flNewPictures.getVisibility() == View.VISIBLE) {
            flNewPictures.setVisibility(View.GONE);
            newPicturesFragment.restore();
        } else {
            flNewPictures.setVisibility(View.VISIBLE);
        }
    }

    private void bindUIElements(View v) {
        flNewPictures = (FrameLayout) v.findViewById(R.id.flNewPictures);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        album = (AuthorizedAlbum) getArguments().getSerializable("album");
    }
}
