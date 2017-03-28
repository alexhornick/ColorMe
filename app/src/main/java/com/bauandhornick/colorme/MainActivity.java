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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/*
Features:

Color filter:
    Variables:
        int myColorOverlay in MainActivity
        int colorOverlay in PaintObjectView
    Methods:
        OnClick in MainActivity under R.id.color_filter
        Creates dialog that uses activity_color_pick.xml to
        pick color
        onDraw in PaintObjectView

Clear button:
     Variables:
        clears all of the lists in the ObjectsDrawn object
        in PaintObjectView
     Methods
        OnClick in MainActivity under R.id.clear
        clear in PaintObjectView

Undo button:
       Variables:
         List<String> pastActions in ObjectsDrawn
         all of the lists in the ObjectsDrawn object
         in PaintObjectView
       Methods:
         OnClick in MainActivity under R.id.undo
         undo in PaintObjectView

Fill background:
       Variables:
         int myBackground in MainActivity
         int background in PaintObjectView
       Methods:
         OnClick in MainActivity under R.id.fill_background
         Creates dialog that uses activity_color_pick.xml to
         pick color
         setBackground in PaintObjectView

Freestyle:
       Variables:
          Path tempPath
          List<Path> Paths
       Methods:
          onTouch in PaintObjectView
          onDraw in PaintObjectView

Eraser:
       Variables:
          boolean eraserMode in MainActivity
          Mode drawMode in ObjectsDrawn
          Path tempErasePath
          List<Path> erasePaths
       Methods:
          onClick in MainActivity under R.id.eraserIcon
          onTouch in PaintObjectView
          onDraw in PaintObjectView
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int myColor= Color.BLACK;
    int myColorOverlay=0;

    int myBackground = Color.WHITE;
    int thickness=4;
    int brushType=2;

    PaintObjectView pov;
    final int [] imageList={R.id.colorWheel_imageView,R.id.brush_imageView, R.id.eraserIcon,R.id.clear, R.id.color_filter, R.id.fill_background, R.id.undo};
    boolean display=false;
    boolean eraserMode = false;
    int pastSelected=0;
    protected final int[] brushList = {R.id.rectangle,R.id.line,R.id.paintbrush};

    enum startActivity{COLOR,BRUSH}
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

                if(!display){
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
            Button b = (Button) dialog.findViewById(R.id.reset);
            cw.setOutput(tv, im, b, myColor);

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
            Button b = (Button) dialog.findViewById(R.id.reset);
            cwo.setOutput(tv, im, b, myBackground);

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
            Button b = (Button) dialog.findViewById(R.id.reset);
            cwo.setOutput(tv, im, b, myColorOverlay);
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

        if(id==R.id.action_about)
        {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
