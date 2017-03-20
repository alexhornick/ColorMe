package com.bauandhornick.colorme;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.List;
import java.util.Vector;

/**
 * Created by Thomas on 3/16/2017.
 */

public class ObjectsDrawn {
    private List<Rect> rectangles;
    private List<Line> lines;
    private List<Point> points;
    private List [] colors;
    private List [] thickness;

    enum Mode{DRAWING,ERASING};

    private String whatToadd="line";

    public ObjectsDrawn() {
        rectangles = new Vector<Rect>();
        lines = new Vector<>();
        points = new Vector<>();

        colors = new List[3];
        thickness = new List[3];

        for (int i = 0; i < 3; i++) {
            colors[i] = new Vector<Integer>();
            thickness[i] = new Vector<Float>();
        }
    }

    public List<Rect> getRectangles() {
        return rectangles;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List[] getColors() {
        return colors;
    }

    public List[] getThickness() {
        return thickness;
    }

    public String getWhatToadd() {
        return whatToadd;
    }

}
