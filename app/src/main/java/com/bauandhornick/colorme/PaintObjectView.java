package com.bauandhornick.colorme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

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

        color.setStyle(Paint.Style.FILL);
        for(int i=0;i<listOfObjects.getRectangles().size();i++){
         Integer temp = (Integer) listOfObjects.getColors()[0].get(i);
         color.setColor(temp);
         temp = (Integer) listOfObjects.getThickness()[0].get(i);
            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp,dm);
            color.setStrokeWidth(strokeWidth);

            canvas.drawRect(listOfObjects.getRectangles().get(i),color);
        }
        color.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < listOfObjects.getLines().size(); i++) {
         Integer temp = listOfObjects.getColors()[1].get(i);
         color.setColor(temp);

         temp = (Integer) listOfObjects.getThickness()[1].get(i);
         strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp,dm);
         color.setStrokeWidth(strokeWidth);
            canvas.drawLine(listOfObjects.getLines().get(i).getX()[0],listOfObjects.getLines().get(i).getY()[0],
                 listOfObjects.getLines().get(i).getX()[1],listOfObjects.getLines().get(i).getY()[1],color);
        }
        for(int i=0;i<listOfObjects.getPath().size();i++){
         Integer temp = (Integer) listOfObjects.getColors()[2].get(i);
         color.setColor(temp);

            temp = (Integer) listOfObjects.getThickness()[2].get(i);
            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp,dm);
            color.setStrokeWidth(strokeWidth);

         canvas.drawPath(listOfObjects.getPath().get(i),color);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                listOfObjects.setStartingPoint(0,(int)event.getX());
                listOfObjects.setStartingPoint(1,(int)event.getY());

                listOfObjects.setEndingPoint(0,(int)event.getX());
                listOfObjects.setEndingPoint(1,(int)event.getY());

                if(listOfObjects.getBrushType()==2) {
                listOfObjects.tempPath=new Path();
                //listOfObjects.tempPath.setFillType(Path.FillType.WINDING);
                listOfObjects.tempPath.moveTo(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1));
                    List<Path> mylist = listOfObjects.getPath();
                    mylist.add(listOfObjects.tempPath);
                    listOfObjects.getColors()[2].add(new Integer(listOfObjects.getCurrentColor()));
                    listOfObjects.getThickness()[2].add(new Integer(listOfObjects.getCurrentThickness()));

                }
                return true;
            case MotionEvent.ACTION_MOVE:
                listOfObjects.setEndingPoint(0,(int)event.getX());
                listOfObjects.setEndingPoint(1,(int)event.getY());

                if(listOfObjects.getBrushType()==2){
                    listOfObjects.tempPath.lineTo(listOfObjects.getEndingPoint(0),listOfObjects.getEndingPoint(1));
                //    listOfObjects.tempPath.moveTo(listOfObjects.getEndingPoint(0),listOfObjects.getEndingPoint(1));
                    List<Path> mylist = listOfObjects.getPath();
                    mylist.remove(mylist.size()-1);
                    listOfObjects.getColors()[2].remove(listOfObjects.getColors()[2].size()-1);
                    listOfObjects.getThickness()[2].remove(listOfObjects.getThickness()[2].size()-1);

                    mylist.add(listOfObjects.tempPath);
                    listOfObjects.getColors()[2].add(new Integer(listOfObjects.getCurrentColor()));
                    listOfObjects.getThickness()[2].add(new Integer(listOfObjects.getCurrentThickness()));

                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if(listOfObjects.drawMode.equals(ObjectsDrawn.Mode.DRAWING)) {
                    if (listOfObjects.getBrushType() == 0) {
                        List<Rect> mylist = listOfObjects.getRectangles();

                        if(listOfObjects.getStartingPoint(0)<listOfObjects.getEndingPoint(0)&&
                                listOfObjects.getStartingPoint(1)<listOfObjects.getEndingPoint(1)) {
                            Rect myRect = new Rect(listOfObjects.getStartingPoint(0), listOfObjects.getStartingPoint(1),
                                    listOfObjects.getEndingPoint(0), listOfObjects.getEndingPoint(1));

                            mylist.add(myRect);
                        }
                        else if(listOfObjects.getStartingPoint(0)>listOfObjects.getEndingPoint(0)&&
                                listOfObjects.getStartingPoint(1)<listOfObjects.getEndingPoint(1)) {
                            Rect myRect = new Rect(listOfObjects.getEndingPoint(0), listOfObjects.getStartingPoint(1),
                                    listOfObjects.getStartingPoint(0), listOfObjects.getEndingPoint(1));

                            mylist.add(myRect);
                        }

                        else if(listOfObjects.getStartingPoint(0)<listOfObjects.getEndingPoint(0)&&
                                listOfObjects.getStartingPoint(1)>listOfObjects.getEndingPoint(1)) {
                            Rect myRect = new Rect(listOfObjects.getStartingPoint(0), listOfObjects.getEndingPoint(1),
                                    listOfObjects.getEndingPoint(0), listOfObjects.getStartingPoint(1));

                            mylist.add(myRect);
                        }
                        else{
                            Rect myRect = new Rect(listOfObjects.getEndingPoint(0), listOfObjects.getStartingPoint(1),
                                    listOfObjects.getStartingPoint(0), listOfObjects.getEndingPoint(1));
                            mylist.add(myRect);

                        }
                        listOfObjects.getColors()[0].add(new Integer(listOfObjects.getCurrentColor()));
                        listOfObjects.getThickness()[0].add(new Integer(listOfObjects.getCurrentThickness()));

                        invalidate();

                    }
                    else if (listOfObjects.getBrushType() == 1) {
                        List<Line> mylist = listOfObjects.getLines();
                        Line myline = new Line((float) listOfObjects.getStartingPoint(0), (float) listOfObjects.getStartingPoint(1),
                                (float) listOfObjects.getEndingPoint(0), (float) listOfObjects.getEndingPoint(1));
                        mylist.add(myline);

                        listOfObjects.getColors()[1].add(new Integer(listOfObjects.getCurrentColor()));
                        listOfObjects.getThickness()[1].add(new Integer(listOfObjects.getCurrentThickness()));

                        invalidate();
                    }

                else if(listOfObjects.getBrushType()==2) {
                        List<Path> mylist = listOfObjects.getPath();


                        mylist.add(listOfObjects.tempPath);
                        //listOfObjects.tempPath = null;

                        listOfObjects.getColors()[2].add(new Integer(listOfObjects.getCurrentColor()));
                        listOfObjects.getThickness()[2].add(new Integer(listOfObjects.getCurrentThickness()));

                        invalidate();

                    }}
                return true;
        }
        return false;

    }
}
