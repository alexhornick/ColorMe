package com.bauandhornick.colorme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    
    int colorOverlay;
    int background = 0xffffffff;

    DisplayMetrics dm;
    float strokeWidth;
    Integer temp;

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
        dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        color.setColor(background);
        color.setStyle(Paint.Style.FILL);
        canvas.drawPaint(color);
       
        int j=0,k=0,l=0,m=0;

        for(int i=0;i<listOfObjects.getPastActions().size();i++){

               if(listOfObjects.getPastActions().get(i).equals("0")){
                   setColorandThickness(listOfObjects.getPastActions().get(i),j);
                    canvas.drawRect(listOfObjects.getRectangles().get(j),color);
                    j++;}
                else if(listOfObjects.getPastActions().get(i).equals("1")){
                   setColorandThickness(listOfObjects.getPastActions().get(i),l);
                   canvas.drawLine(listOfObjects.getLines().get(l).getX()[0],listOfObjects.getLines().get(l).getY()[0],
                            listOfObjects.getLines().get(l).getX()[1],listOfObjects.getLines().get(l).getY()[1],color);
                     l++;}
                else if(listOfObjects.getPastActions().get(i).equals("2")){
                    setColorandThickness(listOfObjects.getPastActions().get(i),k);
                    canvas.drawPath(listOfObjects.getPath().get(k),color);
                k++;}
                else if(listOfObjects.getPastActions().get(i).equals("3")) {
                   setColorandThickness(listOfObjects.getPastActions().get(i),m);
                   canvas.drawPath(listOfObjects.getErasePaths().get(m), color);
                m++;
                }
            }
    }

    private void setColorandThickness(String index,int i){
        if(index.equals("0"))
            color.setStyle(Paint.Style.FILL);
        else
            color.setStyle(Paint.Style.STROKE);

        if(index.equals("0")) {
            temp = (Integer) listOfObjects.getThickness()[0].get(i);
            color.setColor(listOfObjects.getColors()[0].get(i)|colorOverlay);
        }else if(index.equals("1")) {
            temp = (Integer) listOfObjects.getThickness()[1].get(i);
            color.setColor(listOfObjects.getColors()[1].get(i)|colorOverlay);
        }else if(index.equals("2")) {
            temp = (Integer) listOfObjects.getThickness()[2].get(i);
            color.setColor(listOfObjects.getColors()[2].get(i)|colorOverlay);
        }else if(index.equals("3")) {
            temp = (Integer) listOfObjects.getThickness()[3].get(i);
            color.setColor(background);
        }

        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp,dm);
        color.setStrokeWidth(strokeWidth);
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

        if(listOfObjects.drawMode== ObjectsDrawn.Mode.DRAWING){
        if(listOfObjects.getBrushType()==0) {
            listOfObjects.tempRect=new Rect();
            checkRect(event);
            listOfObjects.getPastActions().add("0");
        }

        else if(listOfObjects.getBrushType()==1) {
            listOfObjects.tempLine=new Line();
            listOfObjects.tempLine.set(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1),
                    event.getX(),event.getY());
            List<Line> myLine = listOfObjects.getLines();
            myLine.add(listOfObjects.tempLine);
            listOfObjects.getColors()[1].add(new Integer(listOfObjects.getCurrentColor()));
            listOfObjects.getThickness()[1].add(new Integer(listOfObjects.getCurrentThickness()));

            listOfObjects.getPastActions().add("1");
        }

        else if(listOfObjects.getBrushType()==2) {
            listOfObjects.tempPath=new Path();
            listOfObjects.tempPath.moveTo(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1));
            List<Path> myPath = listOfObjects.getPath();
            myPath.add(listOfObjects.tempPath);
            listOfObjects.getColors()[2].add(new Integer(listOfObjects.getCurrentColor()));
            listOfObjects.getThickness()[2].add(new Integer(listOfObjects.getCurrentThickness()));

            listOfObjects.getPastActions().add("2");
        }

        }
        else{
            listOfObjects.tempErasePath=new Path();
            listOfObjects.tempErasePath.moveTo(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1));
            List<Path> myPath = listOfObjects.getErasePaths();
            myPath.add(listOfObjects.tempErasePath);

            listOfObjects.getPastActions().add("3");
            listOfObjects.getThickness()[3].add(new Integer(listOfObjects.getCurrentThickness()));

        }

    }
    private void onMove(MotionEvent event){
        listOfObjects.setEndingPoint(0,(int)event.getX());
        listOfObjects.setEndingPoint(1,(int)event.getY());

        if(listOfObjects.drawMode== ObjectsDrawn.Mode.DRAWING){
        if (listOfObjects.getBrushType() == 0) {

            List<Rect> myRectangles = listOfObjects.getRectangles();
            myRectangles.remove(listOfObjects.getRectangles().size()-1);

            listOfObjects.getColors()[0].remove(listOfObjects.getColors()[0].size()-1);
            listOfObjects.getThickness()[0].remove(listOfObjects.getThickness()[0].size()-1);
            checkRect(event);

            listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size()-1);
            listOfObjects.getPastActions().add("0");
        }
        else if(listOfObjects.getBrushType()==1){

            listOfObjects.tempLine.set(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1),
                    event.getX(),event.getY());
            List<Line> myLine = listOfObjects.getLines();
            myLine.remove(myLine.size()-1);
            myLine.add(listOfObjects.tempLine);

            listOfObjects.getColors()[1].remove(listOfObjects.getColors()[1].size()-1);
            listOfObjects.getThickness()[1].remove(listOfObjects.getThickness()[1].size()-1);

            listOfObjects.getColors()[1].add(new Integer(listOfObjects.getCurrentColor()));
            listOfObjects.getThickness()[1].add(new Integer(listOfObjects.getCurrentThickness()));


            listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size()-1);
            listOfObjects.getPastActions().add("1");
        }

        else if(listOfObjects.getBrushType()==2){
            listOfObjects.tempPath.lineTo(listOfObjects.getEndingPoint(0),listOfObjects.getEndingPoint(1));
            List<Path> myPath = listOfObjects.getPath();
            myPath.remove(myPath.size()-1);
            listOfObjects.getColors()[2].remove(listOfObjects.getColors()[2].size()-1);
            listOfObjects.getThickness()[2].remove(listOfObjects.getThickness()[2].size()-1);

            myPath.add(listOfObjects.tempPath);
            listOfObjects.getColors()[2].add(new Integer(listOfObjects.getCurrentColor()));
            listOfObjects.getThickness()[2].add(new Integer(listOfObjects.getCurrentThickness()));


            listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size()-1);
            listOfObjects.getPastActions().add("2");
        }}
        else
        {
            listOfObjects.tempErasePath.lineTo(listOfObjects.getEndingPoint(0),listOfObjects.getEndingPoint(1));
            List<Path> myPath = listOfObjects.getErasePaths();
            myPath.remove(myPath.size()-1);

            myPath.add(listOfObjects.tempErasePath);

            listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size()-1);
            listOfObjects.getPastActions().add("3");

            listOfObjects.getThickness()[3].remove(listOfObjects.getThickness()[3].size()-1);

            listOfObjects.getThickness()[3].add(listOfObjects.getCurrentThickness());
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
            listOfObjects.tempRect.set(listOfObjects.getEndingPoint(0), listOfObjects.getEndingPoint(1),
                    listOfObjects.getStartingPoint(0), listOfObjects.getStartingPoint(1));
            myRectangles.add(listOfObjects.tempRect);
        }
        listOfObjects.getColors()[0].add(new Integer(listOfObjects.getCurrentColor()));
        listOfObjects.getThickness()[0].add(new Integer(listOfObjects.getCurrentThickness()));
    }

    public void setColorOverlay(int colorOverlay) { this.colorOverlay = colorOverlay; }
    public void setBackground(int background) { this.background = background; }
    public void clear(){
        background = Color.WHITE;
        listOfObjects.getLines().clear();
        listOfObjects.getRectangles().clear();
        listOfObjects.getPath().clear();
        listOfObjects.getErasePaths().clear();
        listOfObjects.getPastActions().clear();

        for(int i=0;i<4;i++)
            listOfObjects.getThickness()[i].clear();

        for(int i=0;i<3;i++)
            listOfObjects.getColors()[i].clear();

        invalidate();
    }
    public void undo(){
        int size = listOfObjects.getPastActions().size();

        //Make sure to not call undo when canvas is empty.
        if(size > 0) {
            String pastAction = listOfObjects.getPastActions().get(size - 1);

            if (pastAction.equals("0")) {
                listOfObjects.getThickness()[0].remove(listOfObjects.getThickness()[0].size() - 1);
                listOfObjects.getColors()[0].remove(listOfObjects.getColors()[0].size() - 1);
                listOfObjects.getRectangles().remove(listOfObjects.getRectangles().size() - 1);
            } else if (pastAction.equals("1")) {
                listOfObjects.getThickness()[1].remove(listOfObjects.getThickness()[1].size() - 1);
                listOfObjects.getColors()[1].remove(listOfObjects.getColors()[1].size() - 1);
                listOfObjects.getLines().remove(listOfObjects.getLines().size() - 1);

            } else if (pastAction.equals("2")) {
                listOfObjects.getThickness()[2].remove(listOfObjects.getThickness()[2].size() - 1);
                listOfObjects.getColors()[2].remove(listOfObjects.getColors()[2].size() - 1);
                listOfObjects.getPath().remove(listOfObjects.getPath().size() - 1);

            } else if (pastAction.equals("3")) {
                listOfObjects.getThickness()[3].remove(listOfObjects.getThickness()[3].size() - 1);
                listOfObjects.getErasePaths().remove(listOfObjects.getErasePaths().size() - 1);
            }

            listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size() - 1);
            invalidate();
        }

    }



}

