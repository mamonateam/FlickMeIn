package com.codepath.apps.flickmein.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.codepath.apps.flickmein.R;
import com.codepath.apps.flickmein.models.AlbumContributor;

import java.util.Random;

public class UserInfoFormFragment extends Fragment {

    // region Constants
    private static final String[] DEFAULT_COLORS = {"#00abbd", "#055499", "#9bca3c", "#ff5a00", "#cc0000", "#ff921f", "#e91365", "#98dde4"};
    // endregion

    // region Variables
    private EditText etName;
    private ImageView ivColorSelector;
    private int color = Color.parseColor(DEFAULT_COLORS[new Random().nextInt(DEFAULT_COLORS.length)]);
    // endregion

    // region Listeners
    private View.OnClickListener onSelectColorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            ColorPickerFragment colorDialog = ColorPickerFragment.newInstance(color);
            colorDialog.setOnColorPickedListener(onColorPicked);
            colorDialog.show(fm, "fragment_color");
        }
    };
    private ColorPickerFragment.OnColorPicked onColorPicked = new ColorPickerFragment.OnColorPicked() {
        @Override
        public void onColorPicked(int c) {
            color = c;
            ivColorSelector.setBackgroundColor(color);
        }
    };
    // endregion

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
        setUpListeners();

        ivColorSelector.setBackgroundColor(color);

        return v;
    }

    private void bindAllElements(View v) {
        etName = (EditText) v.findViewById(R.id.etName);
        ivColorSelector = (ImageView) v.findViewById(R.id.ivColorSelector);
    }

    private void setUpListeners() {
        ivColorSelector.setOnClickListener(onSelectColorListener);
    }

    public void setName(String name) {
        etName.setText(name);
    }

    public AlbumContributor getAlbumContributor() {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return new AlbumContributor(etName.getText().toString(), hexColor);
    }
}
