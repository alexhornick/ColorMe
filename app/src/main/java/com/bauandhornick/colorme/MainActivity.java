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

    //Set default brush, background and color filter colors.
    int myColor= Color.BLACK;
    int myColorOverlay=0;
    int myBackground = Color.WHITE;

    //Default thickness of line, default brush is 2/freestyle.
    int thickness=4;
    int brushType=2;

    PaintObjectView pov;
    final int [] imageList={R.id.colorWheel_imageView,R.id.brush_imageView, R.id.eraserIcon,R.id.clear, R.id.color_filter, R.id.fill_background, R.id.undo};
    boolean display=false;
    boolean eraserMode = false; //If user is in draw vs. erase mode
    int pastSelected=0;
    protected final int[] brushList = {R.id.rectangle,R.id.line,R.id.paintbrush};

    enum startActivity{COLOR,BRUSH}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo); //set logo for toolbar
        toolbar.setPadding(0, 20, 0, 30); //set top, bottom padding for toolbar
        setSupportActionBar(toolbar);


        pov = (PaintObjectView) findViewById(R.id.paint_object_view);
        pov.listOfObjects.setCurrentColor(myColor);
        pov.listOfObjects.setCurrentThickness(thickness);
        pov.listOfObjects.setBrushType(brushType);


        //Set images on sidebar to be invisible by default
        ImageView temp;
        for(int images:imageList){
            temp = (ImageView) findViewById(images);
            temp.setVisibility(View.INVISIBLE);
            temp.setOnClickListener(MainActivity.this);
        }

        //FloatingActionButton will toggle the visibility of the sidebar images
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

    /* onClick handles clicks on all the sidebar images */
    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.colorWheel_imageView) { //When user selects color wheel to select paint color.

            //Create dialog box, get Image & Text View from box.
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.activity_color_pick);
            final ColorWheel cw = (ColorWheel) dialog.findViewById(R.id.colorView);
            TextView tv = (TextView) dialog.findViewById(R.id.rbg_textView);
            ImageView im = (ImageView) dialog.findViewById(R.id.example);
            Button b = (Button) dialog.findViewById(R.id.reset);
            cw.setOutput(tv, im, b, myColor);

            //Set title of dialog to Brush Color
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
        else if(v.getId() == R.id.eraserIcon) //Toggle eraser mode
        {
            ImageView im = (ImageView) findViewById(R.id.eraserIcon);

            //switch image resource to on or off
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
        else if (v.getId()==R.id.brush_imageView){ //Brush options (line, rectangle, freestyle, thickness)

            final Dialog dialog = new Dialog(MainActivity.this); //create new Dialog box
            dialog.setContentView(R.layout.activity_brush_option); //set layout to the dialog box

            SeekBar seek = (SeekBar) dialog.findViewById(R.id.seekBar);
            seek.setMax(20);

            seek.setProgress(thickness); //set thickness, taken from the seekbar
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


            /*Set OnClick listeners for the three Brush options, and set Brush type accordingly*/
            ImageView im = (ImageView) dialog.findViewById(R.id.line);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrushType(dialog, 1);
                }
            });

            im.setColorFilter(0xff000000); //reset color filter to 000, which is black (non-selected)

            im = (ImageView) dialog.findViewById(R.id.rectangle);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrushType(dialog, 0);
                }
            });
            im.setColorFilter(0xff000000); //reset color filter to 000, which is black (non-selected)

            im = (ImageView) dialog.findViewById(R.id.paintbrush);
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBrushType(dialog,2);
                }
            });
            im.setColorFilter(0xff000000); //reset color filter to 000, which is black (non-selected)
            setBrushType(dialog,brushType);

            //When user exits dialog, set BrushType and Thickness in the listOfObject in the POV class.
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    pov.listOfObjects.setCurrentThickness(thickness);
                    pov.listOfObjects.setBrushType(brushType);
                }
            });
            dialog.show();

        }

        else if(v.getId() == R.id.fill_background) //when user selects the fill background icon (paint bucket)
        {
            //Create new Dialog box with the color pick activity
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.activity_color_pick);

            //Create ColorWheel object to get value user selected.
            final ColorWheel cwo = (ColorWheel) dialog.findViewById(R.id.colorView);
            TextView tv = (TextView) dialog.findViewById(R.id.rbg_textView);
            ImageView im = (ImageView) dialog.findViewById(R.id.example);
            Button b = (Button) dialog.findViewById(R.id.reset);
            cwo.setOutput(tv, im, b, myBackground);

            //Set title of the dialog box so user knows what the color is for.
            tv = (TextView) dialog.findViewById(R.id.title_textView);
            tv.setText("Background Color");

            //When user exits the dialog box.
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    myBackground = cwo.myColor;
                    pov.setBackground(myBackground); //set background color in POV class
                    pov.invalidate(); //redraw canvas with new background
                }
            });

            dialog.show();

        }
        else if(v.getId()==R.id.color_filter) { //Color filter selected from sidebar

            //Create new dialog box
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.activity_color_pick);

            //Create ColorWheel object to get color selected
            final ColorWheel cwo = (ColorWheel) dialog.findViewById(R.id.colorView);
            TextView tv = (TextView) dialog.findViewById(R.id.rbg_textView);
            ImageView im = (ImageView) dialog.findViewById(R.id.example);
            Button b = (Button) dialog.findViewById(R.id.reset);
            cwo.setOutput(tv, im, b, myColorOverlay);
            tv = (TextView) dialog.findViewById(R.id.title_textView);
            tv.setText("Color filter");

            //When user exits the dialog
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    myColorOverlay = cwo.myColor;
                    pov.setColorOverlay(myColorOverlay);
                    pov.invalidate(); //redraw canvas with filter overlay
                }
            });

            dialog.show();

        }

        //Clears canvas
        else if (v.getId() == R.id.clear)
        {
            pov.clear();
        }

        //Undoes last action
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

    //Sets brushType and changes the ImageView by applying a filter over to show it's selected.
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
