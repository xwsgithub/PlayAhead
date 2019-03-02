package com.example.y.playahead.PA;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;

public class PARoundRectButton extends AppCompatImageButton {
    private float width, height;
    private int deg = 50;

    public PARoundRectButton(Context context) {
        this(context, null);
    }

    public PARoundRectButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PARoundRectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width > 2 * deg && height > 2 * deg) {
            Path path = new Path();
            path.moveTo(deg, 0);
            path.lineTo(width - deg, 0);
            path.quadTo(width, 0, width, deg);
            path.lineTo(width, height - deg);
            path.quadTo(width, height, width - deg, height);
            path.lineTo(deg, height);
            path.quadTo(0, height, 0, height - deg);
            path.lineTo(0, deg);
            path.quadTo(0, 0, deg, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}
