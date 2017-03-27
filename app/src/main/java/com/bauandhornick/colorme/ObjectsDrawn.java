package com.bauandhornick.colorme;

import android.graphics.Path;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 3/16/2017.
 */

public class ObjectsDrawn {
    private List<Rect> rectangles;
    private List<Line> lines;
    private List [] colors;
    private List [] thickness;
    private List<Path> paths;

    public List<Path> getErasePaths() {
        return erasePaths;
    }

    public void setErasePaths(List<Path> erasePaths) {
        this.erasePaths = erasePaths;
    }

    private List<Path> erasePaths;

    public List<String> getPastActions() {
        return pastActions;
    }

    public void setPastActions(List<String> pastActions) {
        this.pastActions = pastActions;
    }

    private List<String> pastActions;
    private int currentColor;
    private int currentThickness;
    private int brushType;

    Path tempPath;
    Path tempErasePath;
    Line tempLine;
    Rect tempRect;


    enum Mode{DRAWING,ERASING}

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
        rectangles = new ArrayList<>();
        lines = new ArrayList<>();
        paths = new ArrayList<>();
        startingPoint = new int[2];
        endingPoint = new int[2];
        tempEndingPoint = new int[2];

        colors = new List[3];
        thickness = new List[4];
        pastActions = new ArrayList<>();
        erasePaths = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            colors[i] = new ArrayList();
            thickness[i] = new ArrayList();
        }
        thickness[3]=new ArrayList();
    }

}