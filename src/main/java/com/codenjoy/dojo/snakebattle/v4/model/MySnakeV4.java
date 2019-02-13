package com.codenjoy.dojo.snakebattle.v4.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;

import java.util.LinkedList;
import java.util.List;

public class MySnakeV4 {
    private Point head;
    private Point tail;
    private int size;
    private int minSize;
    private List<Point> body;
    private boolean headerUpdated;
    private boolean tailUpdated;
    private int fury;
    private int fly;


    public MySnakeV4(MySnakeV4 oldSnake) {
        this.head = oldSnake.head;
        this.body = new LinkedList<>(oldSnake.body);
        this.tail = oldSnake.tail;
        this.size = oldSnake.size;
        this.minSize = oldSnake.minSize;
        this.headerUpdated = false;
        this.tailUpdated = false;
        this.fury = oldSnake.fury;
        this.fly = oldSnake.fly;
    }

    public MySnakeV4() {
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
        this.minSize = 3;
//        this.fly = 0;
//        this.fury = 0;
        this.size = 0;
    }

    private int prevSize;

    public void addToHead(Point newHead, int newSize, Point tail, Board board) {

        if (prevSize == 0) {
            prevSize=newSize;
        }
        if (Math.abs(newSize - prevSize) > 3 ) {
            System.out.println("Error");
        } else {
            prevSize=newSize;
        }


        if (newSize == 0) {
            reInitialization(newHead, tail);
            return;
        }
        int oldSize = this.body.size();

        this.body.add(this.head);
        if (newHead != null) {
            this.head = newHead;
        }

        if (tail != null) {
            this.tail = tail;
        }
        if (oldSize < newSize) {
            System.out.println("SnakeObj=>addToHead: Snake increase:" + this.getSize() + "=>" + newSize);
        } else if (oldSize == newSize) {
            this.body.remove(0);
        } else {
            //Decrease after stone
            System.out.println("SnakeObj=>addToHead: Snake decrease:" + this.getSize() + "=>" + newSize);
            for (int i = 0; i <= oldSize - newSize; i++) {
                this.body.remove(0);
            }
        }
        this.setSize(this.getBody().size());

        //Decrease after collision
        //Check for errors
        //Check for errors
        if (newHead == null) {
            System.out.println("Snake error!");
        }
        if (tail == null) {
            System.out.println("Snake error");
        }
        if (this.size != this.body.size()) {
            System.out.println("Snake error");
        }
        if (Math.abs(board.getMyBody().size() - this.size) > 1) {
            System.out.println("Snake error");
        }
    }

    public void setNextStep(Point curNode) {
        if (curNode.itsMe(this.tail)) {
            this.head = curNode;
        } else {
            if (!curNode.itsMe(this.head)) {
                this.body.add(this.head);
                this.head = curNode;
                this.tail = this.body.get(0);
                this.body.remove(0);
            }
        }
        this.fury = this.fury > 0 ? this.fury - 1 : this.fury;
        this.fly = this.fly > 0 ? this.fly - 1 : this.fly;
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

    public int getFury() {
        return fury;
    }

    public void setFury(int fury) {
        this.fury = fury;
    }

    public int getFly() {
        return fly;
    }

    public void setFly(int fly) {
        this.fly = fly;
    }

    @Override
    public String toString() {
        String str;
        if (tail == null) {
            str = "[null]";
        } else {
            str = tail.toString();
        }
        return "Fury:"+ fury + " Fly:" + fly + " Snake:" +  str + body + head;
    }
}
