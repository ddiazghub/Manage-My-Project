/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectmanagementsoftware.gui;

import projectmanagementsoftware.graph.GraphVertex;
import projectmanagementsoftware.wbs.Deliverable;

/**
 *
 * @author david
 */
public class ScheduleVertexBounds {
    private int x;
    private int y;
    private int width;
    private int height;
    private GraphVertex<Deliverable> vertex;

    public ScheduleVertexBounds(int x, int y, int width, int height, GraphVertex<Deliverable> vertex) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.vertex = vertex;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GraphVertex<Deliverable> getVertex() {
        return vertex;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
