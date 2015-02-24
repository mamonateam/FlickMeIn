package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.AlbumContributor;

public class UserInfoFormFragment extends Fragment {

    private EditText etName;
    private EditText etColor;

    public static UserInfoFormFragment newInstance(String suggestedName) {
        UserInfoFormFragment ui = new UserInfoFormFragment();
        Bundle args = new Bundle();
        args.putString("name", suggestedName);
        ui.setArguments(args);

        return ui;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_info_form, container, false);

        bindAllElements(v);
        if (getArguments() != null) {
            etName.setText(getArguments().getString("name"));
        }

        return v;
    }

    private void bindAllElements(View v) {
        etName = (EditText) v.findViewById(R.id.etName);
        etColor = (EditText) v.findViewById(R.id.etColor);
    }

    public void setName(String name) {
        etName.setText(name);
    }

    public AlbumContributor getAlbumContributor() {
        return new AlbumContributor(etName.getText().toString(), etColor.getText().toString());
    }
}
