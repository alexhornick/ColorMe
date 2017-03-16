package com.bauandhornick.colorme;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ColorPickActivity extends AppCompatActivity {

    int color=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_pick);

        Intent intent = getIntent();

        if(intent.hasExtra("color")){
            color=intent.getIntExtra("color",0);
        }
        EditText et = (EditText) findViewById(R.id.rbg_editText);
        et.setText(String.valueOf(color));

        Button b = (Button) findViewById(R.id.change_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView im = (ImageView) findViewById(R.id.color_imageView);
                EditText et = (EditText) findViewById(R.id.rbg_editText);
                try{
                color = Color.parseColor(et.getText().toString());
                Log.i("Color","---------"+String.valueOf(color));

                im.setColorFilter(color);}catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Color must be #000000 - #FFFFFF",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void finish() {

        Intent intent = new Intent();
        intent.putExtra("color",color);
        setResult(RESULT_OK,intent);
        super.finish();
    }
}
