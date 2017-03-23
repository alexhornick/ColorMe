package com.bauandhornick.colorme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Alex on 3/14/2017.
 */

public class PaintObjectView extends View implements View.OnTouchListener{

    ObjectsDrawn listOfObjects;

    Paint color;
    PorterDuffColorFilter filter;
    PorterDuffColorFilter linefilter;
    PorterDuffColorFilter pathfilter;
    final PorterDuff.Mode mode = PorterDuff.Mode.DARKEN;
    int colorOverlay;


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
        this.setOnTouchListener(this);
        color.setAntiAlias(true);
        color.setStyle(Paint.Style.STROKE);
        color.setStrokeJoin(Paint.Join.ROUND);
        color.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float strokeWidth=0;

        Integer temp;
        color.setStyle(Paint.Style.FILL);
        filter = new PorterDuffColorFilter(colorOverlay, mode);
        linefilter = new PorterDuffColorFilter(colorOverlay, PorterDuff.Mode.DST_ATOP);
        pathfilter = new PorterDuffColorFilter(colorOverlay, PorterDuff.Mode.SRC_ATOP);
        color.setColorFilter(filter);

        for(int i=0;i<listOfObjects.getRectangles().size();i++){

         color.setColor(listOfObjects.getColors()[0].get(i));
         temp = (Integer)listOfObjects.getThickness()[0].get(i);
            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp,dm);
            color.setStrokeWidth(strokeWidth);

            canvas.drawRect(listOfObjects.getRectangles().get(i),color);
        }
        color.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < listOfObjects.getLines().size(); i++) {

         color.setColor(listOfObjects.getColors()[1].get(i));
            temp = (Integer)listOfObjects.getThickness()[1].get(i);

         color.setColorFilter(linefilter);
         color.setAlpha(150);
         temp = (Integer) listOfObjects.getThickness()[1].get(i);
       
         strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp,dm);
         color.setStrokeWidth(strokeWidth);
            canvas.drawLine(listOfObjects.getLines().get(i).getX()[0],listOfObjects.getLines().get(i).getY()[0],
                 listOfObjects.getLines().get(i).getX()[1],listOfObjects.getLines().get(i).getY()[1],color);
        }
        for(int i=0;i<listOfObjects.getPath().size();i++){
         temp = (Integer) listOfObjects.getThickness()[2].get(i);
         color.setColor(listOfObjects.getColors()[2].get(i));

         color.setColorFilter(pathfilter);
         color.setAlpha(150);
            temp = (Integer)listOfObjects.getThickness()[2].get(i);

            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp,dm);
            color.setStrokeWidth(strokeWidth);

         canvas.drawPath(listOfObjects.getPath().get(i),color);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onDown(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                onMove(event);
                return true;
            case MotionEvent.ACTION_UP:
                return true;
        } return false;

    }
    private void onDown(MotionEvent event){
        listOfObjects.setStartingPoint(0,(int)event.getX());
        listOfObjects.setStartingPoint(1,(int)event.getY());

        listOfObjects.setEndingPoint(0,(int)event.getX());
        listOfObjects.setEndingPoint(1,(int)event.getY());

        if(listOfObjects.getBrushType()==0) {
            listOfObjects.tempRect=new Rect();
            checkRect(event);
        }

        if(listOfObjects.getBrushType()==1) {
            listOfObjects.tempLine=new Line();
            listOfObjects.tempLine.set(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1),
                    event.getX(),event.getY());
            List<Line> myLine = listOfObjects.getLines();
            myLine.add(listOfObjects.tempLine);
            listOfObjects.getColors()[1].add(new Integer(listOfObjects.getCurrentColor()));
            listOfObjects.getThickness()[1].add(new Integer(listOfObjects.getCurrentThickness()));
        }

        else if(listOfObjects.getBrushType()==2) {
            listOfObjects.tempPath=new Path();
            listOfObjects.tempPath.moveTo(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1));
            List<Path> mylist = listOfObjects.getPath();
            mylist.add(listOfObjects.tempPath);
            listOfObjects.getColors()[2].add(new Integer(listOfObjects.getCurrentColor()));
            listOfObjects.getThickness()[2].add(new Integer(listOfObjects.getCurrentThickness()));
        }

    }
    private void onMove(MotionEvent event){
        listOfObjects.setEndingPoint(0,(int)event.getX());
        listOfObjects.setEndingPoint(1,(int)event.getY());

        if (listOfObjects.getBrushType() == 0) {

            List<Rect> myRectangles = listOfObjects.getRectangles();
            myRectangles.remove(listOfObjects.getRectangles().size()-1);

            listOfObjects.getColors()[0].remove(listOfObjects.getColors()[0].size()-1);
            listOfObjects.getThickness()[0].remove(listOfObjects.getThickness()[0].size()-1);
            checkRect(event);
        }
        if(listOfObjects.getBrushType()==1){

            listOfObjects.tempLine.set(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1),
                    event.getX(),event.getY());
            List<Line> myLine = listOfObjects.getLines();
            myLine.remove(myLine.size()-1);
            myLine.add(listOfObjects.tempLine);

            listOfObjects.getColors()[1].remove(listOfObjects.getColors()[1].size()-1);
            listOfObjects.getThickness()[1].remove(listOfObjects.getThickness()[1].size()-1);

            listOfObjects.getColors()[1].add(new Integer(listOfObjects.getCurrentColor()));
            listOfObjects.getThickness()[1].add(new Integer(listOfObjects.getCurrentThickness()));

        }

        else if(listOfObjects.getBrushType()==2){
            listOfObjects.tempPath.lineTo(listOfObjects.getEndingPoint(0),listOfObjects.getEndingPoint(1));
            //    listOfObjects.tempPath.moveTo(listOfObjects.getEndingPoint(0),listOfObjects.getEndingPoint(1));
            List<Path> mylist = listOfObjects.getPath();
            mylist.remove(mylist.size()-1);
            listOfObjects.getColors()[2].remove(listOfObjects.getColors()[2].size()-1);
            listOfObjects.getThickness()[2].remove(listOfObjects.getThickness()[2].size()-1);

            mylist.add(listOfObjects.tempPath);
            listOfObjects.getColors()[2].add(new Integer(listOfObjects.getCurrentColor()));
            listOfObjects.getThickness()[2].add(new Integer(listOfObjects.getCurrentThickness()));

        }
        invalidate();
    }

    private void checkRect(MotionEvent event){

        List<Rect> myRectangles = listOfObjects.getRectangles();

        if (listOfObjects.getStartingPoint(0) < listOfObjects.getEndingPoint(0) &&
                listOfObjects.getStartingPoint(1) < listOfObjects.getEndingPoint(1)) {
            listOfObjects.tempRect.set(listOfObjects.getStartingPoint(0), listOfObjects.getStartingPoint(1),
                    listOfObjects.getEndingPoint(0), listOfObjects.getEndingPoint(1));

            myRectangles.add(listOfObjects.tempRect);
        } else if (listOfObjects.getStartingPoint(0) > listOfObjects.getEndingPoint(0) &&
                listOfObjects.getStartingPoint(1) < listOfObjects.getEndingPoint(1)) {
            listOfObjects.tempRect.set(listOfObjects.getEndingPoint(0), listOfObjects.getStartingPoint(1),
                    listOfObjects.getStartingPoint(0), listOfObjects.getEndingPoint(1));

            myRectangles.add(listOfObjects.tempRect);
        } else if (listOfObjects.getStartingPoint(0) < listOfObjects.getEndingPoint(0) &&
                listOfObjects.getStartingPoint(1) > listOfObjects.getEndingPoint(1)) {
            listOfObjects.tempRect.set(listOfObjects.getStartingPoint(0), listOfObjects.getEndingPoint(1),
                    listOfObjects.getEndingPoint(0), listOfObjects.getStartingPoint(1));

            myRectangles.add(listOfObjects.tempRect);
        } else {
            listOfObjects.tempRect.set(listOfObjects.getEndingPoint(0), listOfObjects.getStartingPoint(1),
                    listOfObjects.getStartingPoint(0), listOfObjects.getEndingPoint(1));
            myRectangles.add(listOfObjects.tempRect);
        }
        listOfObjects.getColors()[0].add(new Integer(listOfObjects.getCurrentColor()));
        listOfObjects.getThickness()[0].add(new Integer(listOfObjects.getCurrentThickness()));
    }

    public void setColorOverlay(int colorOverlay) { this.colorOverlay = colorOverlay; }
}

