package com.bauandhornick.colorme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

    //Setup canvas
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
        canvas.drawPaint(color); //Draw background color

        //Initialize 4 index variables
        int j=0,k=0,l=0,m=0;

        //Draw all objects onto canvas.
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

        //sets color and color filter if applicable
        if(index.equals("0")) {
            temp = (Integer) listOfObjects.getThickness()[0].get(i);
            color.setColor(listOfObjects.getColors()[0].get(i)|colorOverlay); //uses bitwise operation for color filter
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

    /*This function manages all Touch events*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onDown(event.getX(),event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                onMove(event.getX(),event.getY());
                return true;
            case MotionEvent.ACTION_UP:

                return true;
        } return false;

    }
    private void onDown(float x,float y){
        listOfObjects.setStartingPoint(0,(int)x);
        listOfObjects.setStartingPoint(1,(int)y);

        if(listOfObjects.drawMode== ObjectsDrawn.Mode.DRAWING){
            if(listOfObjects.getBrushType()==0) {
                listOfObjects.tempRect=new Rect();
                checkRect(x,y);
                listOfObjects.getPastActions().add("0");
            }

            else if(listOfObjects.getBrushType()==1) {
                listOfObjects.tempLine=new Line();
                listOfObjects.tempLine.set(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1),
                        (int)x,(int)y);
                List<Line> myLine = listOfObjects.getLines();
                myLine.add(listOfObjects.tempLine);
                listOfObjects.getColors()[1].add(listOfObjects.getCurrentColor());
                listOfObjects.getThickness()[1].add(listOfObjects.getCurrentThickness());

                listOfObjects.getPastActions().add("1");
            }

            else if(listOfObjects.getBrushType()==2) {
                listOfObjects.tempPath=new Path();
                listOfObjects.tempPath.moveTo(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1));
                List<Path> myPath = listOfObjects.getPath();
                myPath.add(listOfObjects.tempPath);
                listOfObjects.getColors()[2].add(listOfObjects.getCurrentColor());
                listOfObjects.getThickness()[2].add(listOfObjects.getCurrentThickness());

                listOfObjects.getPastActions().add("2");
            }
        }
        else{
            listOfObjects.tempErasePath=new Path();
            listOfObjects.tempErasePath.moveTo(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1));
            List<Path> myPath = listOfObjects.getErasePaths();
            myPath.add(listOfObjects.tempErasePath);

            listOfObjects.getPastActions().add("3");
            listOfObjects.getThickness()[3].add(listOfObjects.getCurrentThickness());

        }

    }

    /*On Move event, track ending point and draw a new object each time */
    private void onMove(float x, float y){

        if(listOfObjects.drawMode== ObjectsDrawn.Mode.DRAWING){
            if (listOfObjects.getBrushType() == 0) { //when brush type is a rectangle

                List<Rect> myRectangles = listOfObjects.getRectangles();
                myRectangles.remove(listOfObjects.getRectangles().size()-1);

                listOfObjects.getColors()[0].remove(listOfObjects.getColors()[0].size()-1);
                listOfObjects.getThickness()[0].remove(listOfObjects.getThickness()[0].size()-1);
                checkRect(x,y);  //will add rectangle object to the list.

                //Remove last pastAction, replace it with current one.
                listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size()-1);
                listOfObjects.getPastActions().add("0");
            }
            else if(listOfObjects.getBrushType()==1){ //when brush type is a line

                listOfObjects.tempLine.set(listOfObjects.getStartingPoint(0),listOfObjects.getStartingPoint(1),
                        x,y);
                List<Line> myLine = listOfObjects.getLines();
                myLine.remove(myLine.size()-1);
                myLine.add(listOfObjects.tempLine);

                //remove prior color/thickness to be replaced
                listOfObjects.getColors()[1].remove(listOfObjects.getColors()[1].size()-1);
                listOfObjects.getThickness()[1].remove(listOfObjects.getThickness()[1].size()-1);

                //Add current color/thickness
                listOfObjects.getColors()[1].add(listOfObjects.getCurrentColor());
                listOfObjects.getThickness()[1].add(listOfObjects.getCurrentThickness());

                //Remove past action and add current one
                listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size()-1);
                listOfObjects.getPastActions().add("1");
            }

            else if(listOfObjects.getBrushType()==2){ //when brush type is freestyle
                listOfObjects.tempPath.lineTo(x,y);
                List<Path> myPath = listOfObjects.getPath();
                myPath.remove(myPath.size()-1);
                listOfObjects.getColors()[2].remove(listOfObjects.getColors()[2].size()-1);
                listOfObjects.getThickness()[2].remove(listOfObjects.getThickness()[2].size()-1);

                myPath.add(listOfObjects.tempPath);
                listOfObjects.getColors()[2].add(listOfObjects.getCurrentColor());
                listOfObjects.getThickness()[2].add(listOfObjects.getCurrentThickness());


                listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size()-1);
                listOfObjects.getPastActions().add("2");
            }}
        else //when eraser Mode is on.
        {
            listOfObjects.tempErasePath.lineTo(x,y);
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

    private void checkRect(float x,float y){

        List<Rect> myRectangles = listOfObjects.getRectangles();

        if (listOfObjects.getStartingPoint(0) < x &&
                listOfObjects.getStartingPoint(1) < y) {
            listOfObjects.tempRect.set(listOfObjects.getStartingPoint(0), listOfObjects.getStartingPoint(1),
                    (int)x, (int)y);
        } else if (listOfObjects.getStartingPoint(0) > x &&
                listOfObjects.getStartingPoint(1) < y) {
            listOfObjects.tempRect.set((int)x, listOfObjects.getStartingPoint(1),
                    listOfObjects.getStartingPoint(0), (int)y);

        } else if (listOfObjects.getStartingPoint(0) < x &&
                listOfObjects.getStartingPoint(1) > y) {
            listOfObjects.tempRect.set(listOfObjects.getStartingPoint(0), (int)y,
                    (int)x, listOfObjects.getStartingPoint(1));

          } else {
            listOfObjects.tempRect.set((int)x, (int)y,
                    listOfObjects.getStartingPoint(0), listOfObjects.getStartingPoint(1));
        }
        listOfObjects.getColors()[0].add(listOfObjects.getCurrentColor());
        listOfObjects.getThickness()[0].add(listOfObjects.getCurrentThickness());
        myRectangles.add(listOfObjects.tempRect);
    }

    public void setColorOverlay(int colorOverlay) { this.colorOverlay = colorOverlay; }
    public void setBackground(int background) { this.background = background; }

    /*This function resets and clears every object on the canvas*/
    public void clear(){
        background = Color.WHITE; //set background to white
        listOfObjects.getLines().clear();
        listOfObjects.getRectangles().clear();
        listOfObjects.getPath().clear();
        listOfObjects.getErasePaths().clear();
        listOfObjects.getPastActions().clear();

        //Remove thickness and colors list
        for(int i=0;i<4;i++)
            listOfObjects.getThickness()[i].clear();

        for(int i=0;i<3;i++)
            listOfObjects.getColors()[i].clear();

        invalidate(); //redraw the now-cleared canvas
    }
    public void undo(){
        int size = listOfObjects.getPastActions().size();

        //Make sure to not call undo when canvas is empty.
        if(size > 0) {

            //pastAction can be 0 (Rectangle), 1(Line), 2(Path), or 3(Erase path)
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

            //Remove latest past action
            listOfObjects.getPastActions().remove(listOfObjects.getPastActions().size() - 1);
            invalidate(); //redraw canvas
        }

    }



}

