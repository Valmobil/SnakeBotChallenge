package com.codenjoy.dojo.snakebattle.model;

import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalLong;

public class MySnakeV3 {
    private Point head;
    private Point tail;
    //    private Point nextByTail;
    //Size does not include head and tail
    private int size;
    private List<Point> body;
//    private int maxPathToTail;
//    private List<Point> mustPath;

    public MySnakeV3(MySnakeV3 oldSnake) {
        this.head = oldSnake.head;
        this.body = new LinkedList<>(oldSnake.body);
        this.tail = oldSnake.tail;
        this.size = oldSnake.size;
    }

    public MySnakeV3() {
        this.head = null;
        this.tail = null;
//        this.nextByTail = null;
        this.body = new LinkedList<>();
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

//    public Point getNextByTail() {
//        return nextByTail;
//    }

//    public List<Point> getMustPath() {
//        return mustPath;
//    }

    public int getSize() {
        return this.size;
    }


    public void reInitialization(Point head, Point tail) {
        this.body.clear();
//        this.body.add(head);
        this.head = head;
        this.tail = tail;
        this.size = 0;
//        this.nextByTail = tail;
//        this.maxPathToTail = 10;
    }

    public void addToHead(Point newHead, int newSize, Point tail) {
        if (newSize == 0) {
            reInitialization(newHead, tail);
            return;
        }
        int oldSize = this.getSize();
        this.body.add(this.head);
        this.head = newHead;
        this.tail = tail;
        if (oldSize < newSize) {
            System.out.println("Snake increase:" + this.getSize() + "=>" + newSize);
            this.size++;
            System.out.println("getSize: " + this.getSize());
        } else if (oldSize == newSize) {
//            this.nextByTail = new PointImpl(this.tail);
            this.body.remove(0);
//            this.tail = this.body.get(0);
        } else {
            //Decrease after stone
            System.out.println("Snake decrease:" + this.getSize() + "=>" + newSize);
            for (int i = 0; i < oldSize - newSize; i++) {
                this.body.remove(0);
            }
            this.size = newSize;
            System.out.println("getSize: " + this.getSize());
//            this.nextByTail = this.tail;
//            this.tail = this.body.get(0);
        }
    }

    public void setNextStep(Point curNode) {
        if (!curNode.itsMe(this.head)) {
            this.body.add(this.head);
            this.head = curNode;
            this.tail = this.body.get(0);
            this.body.remove(0);
        }
    }

    @Override
    public String toString() {
        return tail.toString() + body + head;
    }
}
