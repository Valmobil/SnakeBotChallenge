package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.MySnakeV3;

import java.util.*;

public class SnakeUtilsV3 {

    public static List<Point> startBSSBest(Board board, MySnakeV3 mySnake, List<Point> additionalPath, List<MySnakeV3> otherSnakes) {

        Queue<Point> queue = new LinkedList();
        Queue<Integer> queueLevel = new LinkedList();
        Queue<LinkedList<Point>> queuePath = new LinkedList();
        Point mySnakeHead = mySnake.getHead();
        queue.add(mySnakeHead);
        queueLevel.add(0);
        queuePath.add(new LinkedList<Point>(Arrays.asList(mySnakeHead)));
        HashSet<Point> visited = new HashSet<>();
//        int[][] myArr = buildArrayBest(board, additionalPath, mySnakeBody);
//        myArr[mySnakeHead.getX()][mySnakeHead.getY()] = 4; //Visited
//        List<Point> shortestPath = recursiveBFSBest(myArr, destination, queue, queuePath, queueLevel, mySnakeBody);
        List<Point> shortestPath = recursiveBFSBest(board, mySnake, visited, queue, queuePath, queueLevel);
        System.out.println("ShortestPath:" + shortestPath);
        if (shortestPath == null) {
            return null;
        }
        if (shortestPath.size() > 1) {
            return shortestPath;
        } else {
            return null;
        }
    }

    private static List<Point> recursiveBFSBest(Board board, MySnakeV3 mySnake, HashSet<Point> visited, Queue<Point> queue, Queue<LinkedList<Point>> queuePath, Queue<Integer> queueLevel) {
        if (queue.isEmpty()) {
            return null;
        }
        Point curNode = queue.remove();
        Integer curLevel = queueLevel.remove();
        List<Point> prevPath = queuePath.remove();
//        System.out.println("Path:" + prevPath);
        //Change future snake
        MySnakeV3 newMySnake = new MySnakeV3(mySnake);
        mySnake.setNextStep(curNode);

        if (board.isAt(curNode, Elements.APPLE)) {
            return prevPath;
        }

        //check node childs
        for (Point child : getEmptyChild(board, mySnake)) {
            queue.add(child);
            List<Point> newPath = new LinkedList<>(prevPath);
            newPath.add(child);
            queuePath.add((LinkedList<Point>) newPath);
            queueLevel.add(curLevel + 1);
            visited.add(child);
        }

        return recursiveBFSBest(board, newMySnake, visited, queue, queuePath, queueLevel);

    }

    /** Return next four available steps - wize, clockwize, fly and fury*/
    private static List<Point> getEmptyChild(Board board, MySnakeV3 mySnake) {
        List<Point> childs = new ArrayList<>();
        Point curPoint = mySnake.getHead();
        Point nextPoint = new PointImpl(curPoint.getX() + 1, curPoint.getY());
        if (board.isAvailableForNormalSnake(nextPoint)) {
            childs.add(nextPoint);
        }
        nextPoint = new PointImpl(curPoint.getX(), curPoint.getY() - 1);
        if (board.isAvailableForNormalSnake(nextPoint)) {
            childs.add(nextPoint);
        }
        nextPoint = new PointImpl(curPoint.getX() - 1, curPoint.getY());
        if (board.isAvailableForNormalSnake(nextPoint)) {
            childs.add(nextPoint);
        }
        nextPoint = new PointImpl(curPoint.getX(), curPoint.getY() + 1);
        if (board.isAvailableForNormalSnake(nextPoint)) {
            childs.add(nextPoint);
        }
        return childs;
    }
}
