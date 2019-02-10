package com.codenjoy.dojo.snakebattle.v4.model;

import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

public class MySnakeV3 {
    private Point head;
    private Point tail;
    private int size;
    private int minSize;
    private List<Point> body;
    private boolean headerUpdated;
    private boolean tailUpdated;

    public MySnakeV3(MySnakeV3 oldSnake) {
        this.head = oldSnake.head;
        this.body = new LinkedList<>(oldSnake.body);
        this.tail = oldSnake.tail;
        this.size = oldSnake.size;
        this.minSize = oldSnake.minSize;
        this.headerUpdated = false;
        this.tailUpdated = false;
    }

    public MySnakeV3() {
        this.head = null;
        this.tail = null;
        this.body = new LinkedList<>();
        this.minSize = 3;
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

    public int getSize() {
        return this.size;
    }


    private void reInitialization(Point head, Point tail) {
        this.body.clear();
//        this.body.add(head);
        this.head = head;
        this.tail = tail;
        this.size = 0;
        this.minSize = 3;
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
        String str;
        if (tail == null) {
            str  = "[null]";
        } else {
            str = tail.toString();
        }
        return str + body + head;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public void setBody(List<Point> body) {
        this.body = body;
    }

    public boolean isHeaderUpdated() {
        return headerUpdated;
    }

    public void setHeaderUpdated(boolean headerUpdated) {
        this.headerUpdated = headerUpdated;
    }

    public boolean isTailUpdated() {
        return tailUpdated;
    }

    public void setTailUpdated(boolean tailUpdated) {
        this.tailUpdated = tailUpdated;
    }
}
