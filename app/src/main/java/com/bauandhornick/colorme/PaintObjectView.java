package com.bauandhornick.colorme;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Alex on 3/14/2017.
 */

public class PaintObjectView extends View {

    public PaintObjectView(Context context) {
        super(context);
    }

    public PaintObjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintObjectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setup(AttributeSet attrs) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
