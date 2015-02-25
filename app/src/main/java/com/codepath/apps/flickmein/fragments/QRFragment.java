package com.codepath.apps.flickmein.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.apps.flickmein.R;
import com.codepath.apps.flickmein.models.AuthorizedAlbum;
import com.google.zxing.WriterException;

public class QRFragment extends Fragment {

    private ImageView ivQR;
    private AuthorizedAlbum album;

    public static QRFragment newInstance(AuthorizedAlbum album) {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        args.putSerializable("album", album);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        album = (AuthorizedAlbum) getArguments().getSerializable("album");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qr, container, false);
        bindUIElements(v);

        try {
            ivQR.setImageBitmap(album.toBitmap(600, 600));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return v;
    }

    private void bindUIElements(View v) {
        ivQR = (ImageView) v.findViewById(R.id.ivQR);
    }
}
