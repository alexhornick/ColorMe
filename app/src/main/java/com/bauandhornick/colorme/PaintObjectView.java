package com.bauandhornick.colorme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alex on 3/14/2017.
 */

public class PaintObjectView extends View implements View.OnTouchListener{

    ObjectsDrawn listOfObjects;

    Paint color;

    public PaintObjectView(Context context) {
        super(context);
        setup();
    }

    public PaintObjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public PaintObjectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }
   
    private void setup() {

        color = new Paint();
        listOfObjects = new ObjectsDrawn();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i=0;i<listOfObjects.getRectangles().size();i++){
         Integer temp = (Integer) listOfObjects.getColors()[0].get(i);
         color.setColor(temp.intValue());
        }

        for (int i = 0; i < listOfObjects.getLines().size(); i++) {
         Integer temp = (Integer) listOfObjects.getColors()[1].get(i);
         color.setColor(temp.intValue());
        }
        for(int i=0;i<listOfObjects.getPoints().size();i++){
         Integer temp = (Integer) listOfObjects.getColors()[2].get(i);
         color.setColor(temp.intValue());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;


    }
}
