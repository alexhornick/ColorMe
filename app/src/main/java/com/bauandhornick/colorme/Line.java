package com.bauandhornick.colorme;

/**
 * Created by Thomas on 3/16/2017.
 */

public class Line {

    private float [] x;
    private float [] y;

    public Line(){
        x = new float[2];
        y = new float[2];
    }

    public Line(float x1, float y1, float x2, float y2){
        x = new float[2];
        x[0] = x1;
        x[1] = x2;
        y = new float[2];
        y[0]=y1;
        y[1]=y2;
    }

}
