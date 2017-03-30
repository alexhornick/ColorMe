package com.bauandhornick.colorme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Thomas on 3/21/2017.
 */

public class ColorWheel extends ImageView implements View.OnClickListener {

    int myColor;
    TextView tv;
    ImageView im2;
    Button b; //reset button

    public ColorWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public ColorWheel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    public ColorWheel(Context context) {
        super(context);
        setUp();}

    public void setUp(){
        final ImageView im = this;

        /*This code allows the user to select the color. It will get the color of the pixel the user selected*/
        /*Code below inspired by the following StackOverflow question: http://stackoverflow.com/questions/23511682/find-the-pixel-of-bitmap-irrespective-imageview-size-and-scaletype*/
        im.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        Matrix invertMatrix = new Matrix();
                        im.getImageMatrix().invert(invertMatrix);

                        float[] mappedPoints = new float[]{event.getX(), event.getY()};
                        invertMatrix.mapPoints(mappedPoints);

                        if (im.getDrawable() != null && im.getDrawable() instanceof BitmapDrawable &&
                                mappedPoints[0] > 0 && mappedPoints[1] > 0 &&
                                mappedPoints[0] < im.getDrawable().getIntrinsicWidth() && mappedPoints[1] < im.getDrawable().getIntrinsicHeight())

                            myColor = ((BitmapDrawable) im.getDrawable()).getBitmap().getPixel((int) mappedPoints[0], (int) mappedPoints[1]);

                        setColorOutput();
                        return true;
                }
                return false;
            }

        });


    }

    //Set variables and output such as the color, hex value, etc.
    public void setOutput(TextView tv, ImageView im, Button b, int color) {
        this.tv = tv;
        this.im2 = im;
        this.b = b;
        this.myColor = color;

        setColorOutput();
        b.setOnClickListener(this);
    }

    //Used for reset button
    public void onClick(View v) {
        if(myColor==Color.BLACK)
            myColor = Color.WHITE;
        else
            myColor=Color.BLACK;
        setColorOutput();
    }

    //Display hex of color selected, and changes color of image view.
    public void setColorOutput()
    {
        String text = "#";
        text = text + Integer.toHexString(myColor);

        if (text.length() > 2)
            text = text.charAt(0) + text.substring(3);
        else
            text = "#000000";

        this.tv.setText(text);
        im2.setColorFilter(myColor);
    }


}

