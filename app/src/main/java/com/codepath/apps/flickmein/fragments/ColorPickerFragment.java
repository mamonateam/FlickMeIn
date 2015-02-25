package com.codepath.apps.flickmein.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.apps.flickmein.R;
import com.larswerkman.holocolorpicker.ColorPicker;

public class ColorPickerFragment extends android.support.v4.app.DialogFragment {

    // region Variables
    private Button btnChoose;
    private ColorPicker colorPicker;
    private int color;
    private OnColorPicked colorPickedListener;
    // endregion

    // region Listeners
    private View.OnClickListener onChooseColorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            color = colorPicker.getColor();
            if(colorPickedListener != null) {
                colorPickedListener.onColorPicked(color);
            }
            getDialog().dismiss();
        }
    };
    // endregion

    public interface OnColorPicked {
        public void onColorPicked(int color);
    }

    public ColorPickerFragment() {
    }

    public static ColorPickerFragment newInstance(int color) {
        ColorPickerFragment fragment = new ColorPickerFragment();
        Bundle args = new Bundle();
        args.putInt("color", color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_color_picker, container);
        bindUIElements(v);
        setUpListeners();
        color = getArguments().getInt("color");
        colorPicker.setOldCenterColor(color);
        getDialog().setTitle("Pick a color");
        return v;
    }

    private void bindUIElements(View v) {
        btnChoose = (Button) v.findViewById(R.id.btnChoose);
        colorPicker = (ColorPicker) v.findViewById(R.id.colorPicker);
    }

    private void setUpListeners() {
        btnChoose.setOnClickListener(onChooseColorListener);
    }
    
    public void setOnColorPickedListener(OnColorPicked listener) {
        this.colorPickedListener = listener;
    }
}
