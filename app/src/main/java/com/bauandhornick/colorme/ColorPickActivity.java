package com.bauandhornick.colorme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.colorpickerview.ColorPickerView;

public class ColorPickActivity extends AppCompatActivity {

    int myColor=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_pick);

        Intent intent = getIntent();

        if(intent.hasExtra("color")){
            myColor=intent.getIntExtra("color",0);
        }

        TextView tv = (TextView) findViewById(R.id.rbg_textView);
        String text="#";
        text= text+ Integer.toHexString(myColor);
        if(text.length()>2)
            text=text.charAt(0)+text.substring(3);
        else
            text="#000000";
        tv.setText(text);

        //ColorPickerView cpv = (ColorPickerView) findViewById(R.id.colorPickerView);



        /*cpv.setColorListener(new ColorPickerView.ColorListener() {
            @Override
            public void onColorSelected(int color) {
              TextView tv = (TextView) findViewById(R.id.rbg_textView);

                String text="#";
                text= text+ Integer.toHexString(color);

                if(text.length()>2)
                    text=text.charAt(0)+text.substring(3);
                else
                    text="#000000";
                tv.setText(text);

                    myColor = color;
            }
        });*/

        final ImageView im = (ImageView) findViewById(R.id.colorView);
        im.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){


                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        Matrix invertMatrix = new Matrix();
                        im.getImageMatrix().invert(invertMatrix);

                        float[] mappedPoints = new float[]{event.getX(),event.getY()};
                        invertMatrix.mapPoints(mappedPoints);

                        if (im.getDrawable() != null && im.getDrawable() instanceof BitmapDrawable &&
                            mappedPoints[0] > 0 && mappedPoints[1] > 0 &&
                            mappedPoints[0] < im.getDrawable().getIntrinsicWidth() && mappedPoints[1] < im.getDrawable().getIntrinsicHeight())

                        myColor=((BitmapDrawable) im.getDrawable()).getBitmap().getPixel((int) mappedPoints[0], (int) mappedPoints[1]);

                        TextView tv = (TextView) findViewById(R.id.rbg_textView);
                        String text="#";
                        text= text+ Integer.toHexString(myColor);
                        if(text.length()>2)
                            text=text.charAt(0)+text.substring(3);
                        else
                            text="#000000";
                        tv.setText(text);

                        ImageView im2 = (ImageView) findViewById(R.id.example);
                        im2.setColorFilter(myColor);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void finish() {

        Intent intent = new Intent();
        intent.putExtra("color",myColor);
        setResult(RESULT_OK,intent);
        super.finish();
    }
}
