package com.codenjoy.dojo.snakebattle.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.List;

public class MySnakeV2 {
    private Point head;
    private Point tail;
    private Point nextByTail;
    //Size not include head and tail
    private int size;
    private List<Point> body;
//    private int maxPathToTail;
//    private List<Point> mustPath;

    public MySnakeV2() {
        this.head = null;
        this.tail = null;
        this.nextByTail = null;
        this.body = new ArrayList<>();
//        this.mustPath = new ArrayList<>();
    }

    public Point getHead() {
        return head;
    }

    public void setHead(Point head) {
        this.head = head;
    }

    public Point getTail() {
        return tail;
    }

    public void setTail(Point tail) {
        this.tail = tail;
    }

    public List<Point> getBody() {
        return body;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Point getNextByTail() {
        return nextByTail;
    }

//    public List<Point> getMustPath() {
//        return mustPath;
//    }

    public int getSize() {
        this.size = this.body.size();
        return this.size;
    }


    public void reInitialization(Point head, Point tail) {
        this.body.clear();
//        this.body.add(head);
        this.head = head;
        this.tail = tail;
//        this.nextByTail = tail;
//        this.maxPathToTail = 10;
    }

    public void addToHead(Point newHead, int newSize, Point tail) {
        if (newSize == 0) {
            reInitialization(newHead, tail);
            return;
        }
        this.body.add(this.head);
        this.head = newHead;
        this.tail = tail;
        if (this.size < newSize) {
            this.size++;
        } else if (this.size == newSize) {
//            this.nextByTail = new PointImpl(this.tail);
            this.body.remove(0);
//            this.tail = this.body.get(0);
        } else {
            //Decrease after stone
            int oldSize = this.size;
            for (int i = 0; i < newSize - oldSize; i++) {
                this.body.remove(0);
            }
            this.size = newSize;
//            this.nextByTail = this.tail;
//            this.tail = this.body.get(0);
        }
    }
}
