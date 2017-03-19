package com.bauandhornick.colorme;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class BrushOptionActivity extends AppCompatActivity implements View.OnClickListener {

    protected int thickness;
    protected int brushType;
    final SeekBar seek= (SeekBar) findViewById(R.id.seekBar);
    //protected int brushList[] = {R.id.line, R.id.rectangle, R.id.paintbrush};
    //enum BRUSH{LINE,RECTANGLE,SPEED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush_option);

        Intent intent=getIntent();
        if(intent.hasExtra("thickness"))
        {
            thickness = intent.getIntExtra("thickness", 0);
        }

        if(intent.hasExtra("paintbrush"))
        {
            brushType = intent.getIntExtra("brushType", 0);
        }

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                thickness=progress;
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.line)
        {
            setBrushType(R.id.line);
        }

        else if(v.getId() == R.id.rectangle)
        {
            setBrushType(R.id.rectangle);
        }

        else if(v.getId() == R.id.paintbrush)
        {
            setBrushType(R.id.paintbrush);
        }
    }

    private void setBrushType(int brush)
    {
        brushType = brush;
        ImageView im = (ImageView) findViewById(brush);
    }

}
