package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snakebattle.client.Board;

import java.util.*;

public class SnakeUtilsV3 {

    public static List<Point> startBSSBest(Board board, Point header, Point destination, List<Point> additionalPath, List<Point> mySnakeBody) {
        Queue<Point> queue = new LinkedList();
        Queue<Integer> queueLevel = new LinkedList();
        Queue<LinkedList<Point>> queuePath = new LinkedList();
        queue.add(header);
        queueLevel.add(0);
        queuePath.add(new LinkedList<Point>(Arrays.asList(header)));
        int[][] myArr = buildArrayBest(board, additionalPath, mySnakeBody);
        myArr[header.getX()][header.getY()] = 4; //Visited
        List<Point> shortestPath = (List<Point>) recursiveBFSBest(myArr, destination, queue, queuePath, queueLevel, mySnakeBody);
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

    public static List<Point> recursiveBFSBest(int[][] myArr, Point destination, Queue<Point> q, Queue<LinkedList<Point>> qp, Queue<Integer> ql, List<Point> mySnakeBody) {
        if (q.isEmpty()) {
            return null;
        }
        Point curNode = q.remove();
        Integer curLevel = ql.remove();
        List<Point> prevPath = qp.remove();
        //System.out.println("Path:" + prevPath);
        if (compareTwoPoints(curNode, destination)) {
            return prevPath;
        }

        removeSnakeTaileAccordingLevel(myArr, mySnakeBody, curLevel);

        //check node childs
        for (Point child : getEmptyChilsds(myArr, curNode)) {
            q.add(child);
            List<Point> newPath = new LinkedList<>();
            newPath.addAll(prevPath);
            newPath.add(child);
            qp.add((LinkedList<Point>) newPath);
            ql.add(curLevel + 1);
            myArr[child.getX()][child.getY()] = 4; //Visited
        }

        return recursiveBFSBest(myArr, destination, q, qp, ql, mySnakeBody);
    }

    private static List<Point> getEmptyChilsds(int[][] myArr, Point curNode) {
        List<Point> childs = new ArrayList<>();
        if (myArr[curNode.getX() + 1][curNode.getY()] == 0) {
            childs.add(new PointImpl(curNode.getX() + 1, curNode.getY()));
        }
        if (myArr[curNode.getX()][curNode.getY() - 1] == 0) {
            childs.add(new PointImpl(curNode.getX(), curNode.getY() - 1));
        }
        if (myArr[curNode.getX() - 1][curNode.getY()] == 0) {
            childs.add(new PointImpl(curNode.getX() - 1, curNode.getY()));
        }
        if (myArr[curNode.getX()][curNode.getY() + 1] == 0) {
            childs.add(new PointImpl(curNode.getX(), curNode.getY() + 1));
        }
        return childs;
    }

    private static void removeSnakeTaileAccordingLevel(int[][] myArr, List<Point> mySnakeBody, Integer curLevel) {
        for (int i = 0; i < curLevel; i++) {
        }
    }

    public static boolean compareTwoPoints(Point one, Point two) {
        return (one.getX() == two.getX() && one.getY() == two.getY());
    }

    public static int[][] buildArrayBest(Board board, List<Point> additionalPath, List<Point> mySnakeBody) {

        int[][] myCurrentBoard = new int[40][40];
        //      write boarder
        for (Point point : board.getMyWalls()) {
            myCurrentBoard[point.getX()][point.getY()] = 1;
        }
        //      write stones
        for (Point point : board.getMyStones()) {
            myCurrentBoard[point.getX()][point.getY()] = 2;
        }
        //      write snake
        for (Point point : board.getMyBody()) {
            myCurrentBoard[point.getX()][point.getY()] = 3;
        }
        //      add future snake path
        for (Point point : additionalPath) {
            myCurrentBoard[point.getX()][point.getY()] = 4;
        }
        //     remove snake marks from number of points
//        if (additionalPath != null) {
//            for (int i = 0; i < Math.min(additionalPath.size() - 2, mySnakeBody.size() - 1); i++) {
//                myCurrentBoard[mySnakeBody.get(i).getX()][mySnakeBody.get(i).getY()] = 0;
//            }
//        }
        return myCurrentBoard;
    }
}
