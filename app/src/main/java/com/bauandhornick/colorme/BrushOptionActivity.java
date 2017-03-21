package com.bauandhornick.colorme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class BrushOptionActivity extends AppCompatActivity implements View.OnClickListener {

    protected int thickness=1;
    protected int brushType=0;
    SeekBar seek;
    private int pastSelected=0;
    protected int brushList[] = {R.id.rectangle,R.id.line,R.id.paintbrush};
    //enum BRUSH{LINE,RECTANGLE,SPEED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush_option);

        thickness=0;
        Intent intent=getIntent();
        if(intent.hasExtra("thickness"))
        {
            thickness = intent.getIntExtra("thickness", 0);
        }

        if(intent.hasExtra("brushType"))
        {
            brushType = intent.getIntExtra("brushType", 0);
        }

        seek = (SeekBar) findViewById(R.id.seekBar);
        seek.setMax(20);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),String.valueOf(thickness),Toast.LENGTH_LONG).show();
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
        ImageView im = (ImageView) findViewById(R.id.line);
        im.setOnClickListener(this);
        im.setColorFilter(0xff000000);

        im = (ImageView) findViewById(R.id.rectangle);
        im.setOnClickListener(this);
        im.setColorFilter(0xff000000);

        im = (ImageView) findViewById(R.id.paintbrush);
        im.setOnClickListener(this);
        im.setColorFilter(0xff000000);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.rectangle)
        {
            setBrushType(0);
        }
        else if(v.getId() == R.id.line)
        {
            setBrushType(1);
        }


        else if(v.getId() == R.id.paintbrush)
        {
            setBrushType(2);
        }
    }

    private void setBrushType(int brush)
    {
        brushType = brush;
        ImageView im = (ImageView) findViewById(brushList[brush]);
        im.setColorFilter(0xff666666);

        if(pastSelected!=brush){
        im = (ImageView) findViewById(brushList[pastSelected]);
        im.setColorFilter(0xff000000);}
        pastSelected=brush;
    }

    @Override
    public void finish() {

        Intent intent = new Intent();
        intent.putExtra("brushType",brushType);
        intent.putExtra("thickness",thickness);
        setResult(RESULT_OK,intent);

        super.finish();
    }
}
