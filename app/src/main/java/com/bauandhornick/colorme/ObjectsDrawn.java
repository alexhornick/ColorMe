package com.bauandhornick.colorme;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.shapes.OvalShape;

import java.util.List;
import java.util.Vector;

/**
 * Created by Thomas on 3/16/2017.
 */

public class ObjectsDrawn {
    private List<Rect> rectangles;
    private List<Line> lines;
     private List [] colors;
    private List [] thickness;
    private List<Path> paths;

    private int currentColor;
    private int currentThickness;
    private int brushType;

    Path tempPath;
    enum Mode{DRAWING,ERASING};

    Mode drawMode = Mode.DRAWING;

    public List<Rect> getRectangles() {
        return rectangles;
    }

    public void setRectangles(List<Rect> rectangles) {
        this.rectangles = rectangles;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Path> getPath() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public List<Integer>[] getColors() {
        return colors;
    }

    public void setColors(List[] colors) {
        this.colors = colors;
    }

    public List[] getThickness() {
        return thickness;
    }

    public void setThickness(List[] thickness) {
        this.thickness = thickness;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    public int getCurrentThickness() {
        return currentThickness;
    }

    public void setCurrentThickness(int currentThickness) {
        this.currentThickness = currentThickness;
    }

    public int getBrushType() {
        return brushType;
    }

    public void setBrushType(int brushType) {

        this.brushType = brushType;
    }

    public int getStartingPoint(int index) {
        return startingPoint[index];
    }

    public void setStartingPoint(int index, int value) {
        this.startingPoint[index] = value;
    }

    public int getEndingPoint(int index) {
        return endingPoint[index];
    }

    public void setEndingPoint(int index, int value) {
        this.endingPoint[index] = value;
    }

    private int [] startingPoint;
    private int [] endingPoint;
    private int [] tempEndingPoint;

    public ObjectsDrawn() {
        rectangles = new Vector<Rect>();
        lines = new Vector<>();
        paths = new Vector<>();
        startingPoint = new int[2];
        endingPoint = new int[2];
        tempEndingPoint = new int[2];

        colors = new List[3];
        thickness = new List[3];
        tempPath = new Path();

        for (int i = 0; i < 3; i++) {
            colors[i] = new Vector<Integer>();
            thickness[i] = new Vector<Float>();
        }
    }

  }
