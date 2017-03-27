package com.bauandhornick.colorme;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int myColor= Color.BLACK;
    int myColorOverlay=0;

    int myBackground = Color.WHITE;
    int thickness=4;
    int brushType=2;

    PaintObjectView pov;
    int [] imageList={R.id.colorWheel_imageView,R.id.brush_imageView, R.id.eraserIcon,R.id.clear, R.id.color_filter, R.id.fill_background, R.id.undo};
    boolean display=false;
    boolean eraserMode = false;
    int pastSelected=0;
    protected int brushList[] = {R.id.rectangle,R.id.line,R.id.paintbrush};

    enum startActivity{COLOR,BRUSH};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo);
        toolbar.setPadding(0, 20, 0, 30); //set top, bottom padding for toolbar
        setSupportActionBar(toolbar);


        pov = (PaintObjectView) findViewById(R.id.paint_object_view);
        pov.listOfObjects.setCurrentColor(myColor);
        pov.listOfObjects.setCurrentThickness(thickness);
        pov.listOfObjects.setBrushType(brushType);

        ImageView temp;
        for(int images:imageList){
            temp = (ImageView) findViewById(images);
            temp.setVisibility(View.INVISIBLE);
            temp.setOnClickListener(MainActivity.this);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            if(display==false){
                ImageView temp;
                for(int images:imageList){
                    temp = (ImageView) findViewById(images);
                    temp.setVisibility(View.VISIBLE);
                display=true;
                }
            }
            else
            {
                ImageView temp;
                for(int images:imageList){
                    temp = (ImageView) findViewById(images);
                    temp.setVisibility(View.INVISIBLE);
                    display=false;
                }

            }
        }});
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.colorWheel_imageView) {
            Dialog dialog = new Dialog(MainActivity.this);

            dialog.setContentView(R.layout.activity_color_pick);
            final ColorWheel cw = (ColorWheel) dialog.findViewById(R.id.colorView);
            TextView tv = (TextView) dialog.findViewById(R.id.rbg_textView);
            ImageView im = (ImageView) dialog.findViewById(R.id.example);
            cw.setOutput(tv, im,myColor);

            tv = (TextView) dialog.findViewById(R.id.title_textView);
            tv.setText("Brush Color");

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    myColor = cw.myColor;
                    pov.listOfObjects.setCurrentColor(myColor);
                }
            });

            dialog.show();
        }
        else if(v.getId() == R.id.eraserIcon)
        {
            ImageView im = (ImageView) findViewById(R.id.eraserIcon);

            if(eraserMode) {
                im.setImageResource(R.drawable.eraser_off);
                eraserMode = false;
                pov.listOfObjects.drawMode= ObjectsDrawn.Mode.DRAWING;
            }
            else {
                im.setImageResource(R.drawable.eraser_on);
                eraserMode = true;

                pov.listOfObjects.drawMode= ObjectsDrawn.Mode.ERASING;
            }
        }
        else if (v.getId()==R.id.brush_imageView){
            final Dialog dialog = new Dialog(MainActivity.this);

            dialog.setContentView(R.layout.activity_brush_option);

            SeekBar seek = (SeekBar) dialog.findViewById(R.id.seekBar);
            seek.setMax(20);

            seek.setProgress(thickness);
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

            ImageView im = (ImageView) dialog.findViewById(R.id.line);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrushType(dialog, 1);
                }
            });

            im.setColorFilter(0xff000000);

            im = (ImageView) dialog.findViewById(R.id.rectangle);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrushType(dialog, 0);
                }
            });
            im.setColorFilter(0xff000000);

            im = (ImageView) dialog.findViewById(R.id.paintbrush);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrushType(dialog,2);
                }
            });
            im.setColorFilter(0xff000000);


            setBrushType(dialog,brushType);

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    pov.listOfObjects.setCurrentThickness(thickness);
                    pov.listOfObjects.setBrushType(brushType);
                }
            });
            dialog.show();

        }

        else if(v.getId() == R.id.fill_background)
        {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.activity_color_pick);

            final ColorWheel cwo = (ColorWheel) dialog.findViewById(R.id.colorView);
            TextView tv = (TextView) dialog.findViewById(R.id.rbg_textView);
            ImageView im = (ImageView) dialog.findViewById(R.id.example);
            cwo.setOutput(tv, im, myBackground);
            tv = (TextView) dialog.findViewById(R.id.title_textView);
            tv.setText("Background Color");

          
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    myBackground = cwo.myColor;
                    pov.setBackground(myBackground);
                    pov.invalidate();
                }
            });

            dialog.show();

        }
        else if(v.getId()==R.id.color_filter) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.activity_color_pick);

            final ColorWheel cwo = (ColorWheel) dialog.findViewById(R.id.colorView);
            TextView tv = (TextView) dialog.findViewById(R.id.rbg_textView);
            ImageView im = (ImageView) dialog.findViewById(R.id.example);
            cwo.setOutput(tv, im,myColor);
            tv = (TextView) dialog.findViewById(R.id.title_textView);
            tv.setText("Color filter");

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    myColorOverlay = cwo.myColor;
                    pov.setColorOverlay(myColorOverlay);
                    pov.invalidate();
                }
            });

            dialog.show();

        }

        else if (v.getId() == R.id.clear)
        {
            pov.clear();
        }

        else if(v.getId() == R.id.undo)
        {
            pov.undo();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.action_color){
            Intent intent = new Intent(this,ColorPickActivity.class);
            intent.putExtra("color",myColor);
            startActivityForResult(intent,startActivity.COLOR.ordinal());
        }
        else if(id==R.id.action_brush){

            Intent intent = new Intent(this,BrushOptionActivity.class);
            intent.putExtra("thickness",thickness);
            intent.putExtra("brushType",brushType);
            startActivityForResult(intent,startActivity.BRUSH.ordinal());
        }
        else if(id==R.id.action_about)
        {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==startActivity.COLOR.ordinal()){
            if(resultCode==RESULT_OK){
                if(data.hasExtra("color")){
                  myColor= data.getIntExtra("color",0);
                  pov.listOfObjects.setCurrentColor(myColor);
                    String text="#";
                    text= text+ Integer.toHexString(myColor);
                    if(text.length()>2)
                        text=text.charAt(0)+text.substring(3);
                    else
                        text="#000000";
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
                }
            }
        }
        else if(requestCode==startActivity.BRUSH.ordinal()){
            if(resultCode==RESULT_OK){
                if(data.hasExtra("thickness")){
                    thickness = data.getIntExtra("thickness",1);
                    pov.listOfObjects.setCurrentThickness(thickness);
                }
                if(data.hasExtra("brushType")){
                    brushType = data.getIntExtra("brushType",1);
                    pov.listOfObjects.setBrushType(brushType);
                }
            }
        }
    }
    private void setBrushType(Dialog dialog,int brush)
    {
        brushType = brush;
        ImageView im = (ImageView) dialog.findViewById(brushList[brush]);
        im.setColorFilter(0xff666666);

        if(pastSelected!=brush){
            im = (ImageView) dialog.findViewById(brushList[pastSelected]);
            im.setColorFilter(0xff000000);}
        pastSelected=brush;
    }
}
