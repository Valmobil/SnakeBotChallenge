package com.codenjoy.dojo.snakebattle.model;

import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SnakeListV4 {
    private int sumOfBodies;
    private List<MySnakeV4> snakes;

    public SnakeListV4() {
        this.snakes = new ArrayList<>();
    }

    public int getSumOfBodies() {
        return sumOfBodies;
    }

    public void setSumOfBodies(int sumOfBodies) {
        this.sumOfBodies = sumOfBodies;
    }

    public List<MySnakeV4> getSnakes() {
        return snakes;
    }

    public void setSnakes(List<MySnakeV4> snakes) {
        this.snakes = snakes;
    }

    /**
     * Create list of competitive snakes
     */
    public void changeSnakes(List<Point> headers, int size, List<Point> tails) {
        this.setSumOfBodies(size);
        //Clear update status
        for (MySnakeV4 snake : this.snakes) {
            snake.setHeaderUpdated(false);
            snake.setTailUpdated(false);
        }

        //Find respective snake by header
        for (Point header : headers) {
            boolean isHeaderFound = false;
            for (MySnakeV4 snake : this.snakes) {
                if (!snake.isHeaderUpdated()) {
                    Point oldHeader = snake.getHead();
                    if (snake.getHead().itsMe(header)) {
                        //if snake is stopped
                        snake.setHeaderUpdated(true);
                        isHeaderFound = true;
                    } else {
                        if (oldHeader.distance(header) == 1) {
                            snake.getBody().add(oldHeader);
                            snake.setHead(header);
                            snake.setHeaderUpdated(true);
                            isHeaderFound = true;
                        }
                    }
                }
            }
            //If respective header do not found create new snake
            if (!isHeaderFound) {
                MySnakeV4 newSnake = new MySnakeV4();
                newSnake.setHeaderUpdated(true);
                newSnake.setHead(header);
                this.snakes.add(newSnake);
            }
        }

        //Kill not found snakes
        LinkedList<MySnakeV4> snakeForDelete = new LinkedList<>();
        for (MySnakeV4 snake : this.getSnakes()) {
            if (!snake.isHeaderUpdated()) {
                snakeForDelete.add(snake);
            }
        }
        for (MySnakeV4 snake : snakeForDelete) {
            this.getSnakes().remove(snake);
        }

        //Update tails
        for (MySnakeV4 snake : snakes) {
            for (Point tail : tails) {
                //Find respective snake tail
                //- if snake is stop
                if (snake.getTail() == null) {
                    //If tail is null (for new snakes)
                    if (tail.distance(snake.getHead()) == 1 && !snake.isTailUpdated()) {
                        snake.setTail(tail);
                        snake.setTailUpdated(true);
                        break;
                    }
                } else if (snake.getTail().itsMe(tail)) {
                    snake.setTailUpdated(true);
                    break;
                }
                //scan old body for new tail
                int i = -1;
                for (Point point : snake.getBody()) {
                    i++;
                    if (point.itsMe(tail)) {
                        snake.setTail(tail);
                        for (int j = 0; j <= i; j++) {
                            snake.getBody().remove(0);
                        }
                        snake.setSize(snake.getBody().size());
                        break;
                    }
                }
            }
        }
    }

    /**
     * Check if point belong one but one point of competitive snake
     */
    public boolean isLastButOne(Point mySnakeHead) {
        for (MySnakeV4 snake : getSnakes()) {
            Point snakeTailButOne;
            if (snake.getBody().size() == 0) {
                snakeTailButOne = snake.getHead();
            } else {
                snakeTailButOne = snake.getBody().get(0);
            }
            if (mySnakeHead.itsMe(snakeTailButOne)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "SnakeListV4{" +
                "sumOfBodies=" + sumOfBodies +
                ", snakes=" + snakes +
                '}';
    }
}
