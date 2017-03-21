package com.bauandhornick.colorme;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int myColor=0;
    int thickness=0;
    int brushType=0;
    PaintObjectView pov;

    enum startActivity{COLOR,BRUSH};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pov = (PaintObjectView) findViewById(R.id.paint_object_view);
        pov.listOfObjects.setCurrentColor(myColor);
        pov.listOfObjects.setCurrentThickness(thickness);
        pov.listOfObjects.setBrushType(brushType);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                Dialog dialog = new Dialog(MainActivity.this);

                dialog.setContentView(R.layout.activity_color_pick);
                final ColorWheel cw =(ColorWheel) dialog.findViewById(R.id.colorView);
                TextView tv = (TextView) dialog.findViewById(R.id.rbg_textView);
                ImageView im = (ImageView) dialog.findViewById(R.id.example);
                cw.setOutput(tv,im);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        myColor = cw.myColor;
                        pov.listOfObjects.setCurrentColor(myColor);
                    }
                });
                dialog.show();
            }
        });
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
}
