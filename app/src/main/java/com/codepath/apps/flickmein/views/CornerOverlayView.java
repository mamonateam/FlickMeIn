package com.codepath.apps.flickmein.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class CornerOverlayView extends View {

    // defines paint and canvas
    private int color;
    private Paint drawPaint;
    
    public CornerOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    public CornerOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupPaint();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CornerOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupPaint();
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPaint.setColor(color);
        drawPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, 20, drawPaint);
    }
    
    public void setCornerColor(int color) {
        this.color = color;
        invalidate();
    }
}
