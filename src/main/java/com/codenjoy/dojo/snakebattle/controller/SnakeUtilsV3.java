package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.MySnakeV3;

import java.util.*;

class SnakeUtilsV3 {

    static void startBSSBest(Board board, MySnakeV3 mySnake, List<Point> additionalPath, List<MySnakeV3> otherSnakes, Map<Integer, List<Point>> bestPaths) {

        Queue<Point> queue = new LinkedList();
        Queue<Integer> queueLevel = new LinkedList();
        Queue<LinkedList<Point>> queuePath = new LinkedList();
        Queue<MySnakeV3> queueSnakes = new LinkedList<>();
        Point mySnakeHead = mySnake.getHead();
        queue.add(mySnakeHead);
        queueLevel.add(0);
        queuePath.add(new LinkedList<Point>(Arrays.asList(mySnakeHead)));
        queueSnakes.add(mySnake);
        HashSet<Point> visited = new HashSet<>();
//        int[][] myArr = buildArrayBest(board, additionalPath, mySnakeBody);
//        myArr[mySnakeHead.getX()][mySnakeHead.getY()] = 4; //Visited
//        List<Point> shortestPath = recursiveBFSBest(myArr, destination, queue, queuePath, queueLevel, mySnakeBody);
        recursiveBFSBest(board, queueSnakes, visited, queue, queuePath, queueLevel, bestPaths);
//        System.out.println("ShortestPath:" + shortestPath);
//        if (shortestPath == null) {
//            return null;
//        }
//        if (shortestPath.size() > 1) {
//            return shortestPath;
//        } else {
//            return null;
//        }
    }

    private static void recursiveBFSBest(Board board, LinkedList<MySnakeV3> queueSnakes, HashSet<Point> visited, LinkedList<Point> queue, Queue<LinkedList<Point>> queuePath, Queue<Integer> queueLevel, Map<Integer, List<Point>> bestPaths) {
        if (queue.isEmpty()) {
            return;
        }
        Point curNode = queue.remove();
        Integer curLevel = queueLevel.remove();
        List<Point> prevPath = queuePath.remove();
        MySnakeV3 mySnake = queueSnakes.remove();
//        System.out.println("Path:" + prevPath);
        //Change future snake
        MySnakeV3 newMySnake = new MySnakeV3(mySnake);
        newMySnake.setNextStep(curNode);


//        System.out.println("Queue: " + curNode + " => " + queue);
//        System.out.println("Cur level: " + curLevel + " => " + queueLevel);
//        System.out.println("PrevPath: " + prevPath + " => " + queuePath);
//        System.out.println("MySnake: " + mySnake + " => " + queueSnakes);
//        System.out.println("MyNewSnake: " + newMySnake);


//        if (snakeFoundInterestPoint)

//        if (board.isAt(curNode, Elements.APPLE)) {
//            System.out.println();
//        }

        boolean skipPoint = false;

        int result = snakeFoundFruitfulPoint(board, newMySnake, bestPaths, prevPath);
        if (result != 0) {
            if (result == 1) {
                //end search and save result
                System.out.println("Future snake: " + newMySnake.getTail() + newMySnake.getBody() + newMySnake.getHead());
                queue.clear();
                return;
            } else {
                skipPoint = true;
                //we found obstacle, so we should reject this step
            }
        }
        if (!skipPoint) {
            //check node childs
            for (Point child : getEmptyChild(board, newMySnake, visited)) {
                queue.add(child);
                List<Point> newPath = new LinkedList<>(prevPath);
                newPath.add(child);
                queuePath.add((LinkedList<Point>) newPath);
                queueLevel.add(curLevel + 1);
                queueSnakes.add(newMySnake);
                visited.add(child);
            }
        }

        recursiveBFSBest(board, queueSnakes, visited, queue, queuePath, queueLevel, bestPaths);
    }

    private static int snakeFoundFruitfulPoint(Board board, MySnakeV3 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath) {
        //Return    0 - blank Point
        //          1 - found eatable Point - nead break searching and save result
        //          2 - found not eatable Point - forbid movement it it
        Point mySnakeHead = mySnake.getHead();
        if (board.isAt(mySnakeHead, Elements.APPLE)) {
            bestPaths.put(1, prevPath);
            System.out.println("Found Apple: " + mySnakeHead);
            return 1;
        }
        if (board.isAt(mySnakeHead, Elements.GOLD)) {
            bestPaths.put(3, prevPath);
            System.out.println("Found Gold: " + mySnakeHead);
            return 1;
        }

        // other Snakes Tails
//        if (board.isAt(mySnakeHead, Elements.ENEMY_TAIL_END_DOWN, Elements.ENEMY_TAIL_END_LEFT,
//                Elements.ENEMY_TAIL_END_UP, Elements.ENEMY_TAIL_END_RIGHT, Elements.ENEMY_TAIL_INACTIVE)) {
//            bestPaths.put(20, prevPath);
//            System.out.println("Found Gold: " + mySnakeHead);
//            return 1;
//        }

//        System.out.println("Nothing Found");
        if (board.isAt(mySnakeHead, Elements.STONE)) {
            System.out.println("Found Stone:" + mySnakeHead);
            int tempSize = mySnake.getSize();
            System.out.println("Snake size: " + tempSize);
            if (tempSize >= 3) {
                System.out.println("Snake >= 3");
                bestPaths.put(3, prevPath);
                return 1;
            } else {
                System.out.println("Stone!!! Get Away");
                return 2;
            }
        }

        return 0;
    }

    /**
     * Return next available steps - wise, clockwise, fly and fury
     */
    private static List<Point> getEmptyChild(Board board, MySnakeV3 mySnake, HashSet<Point> visited) {
        List<Point> childs = new ArrayList<>();
        Point curPoint = mySnake.getHead();
        Point nextPoint = new PointImpl(curPoint.getX() + 1, curPoint.getY());
        if (visited.contains(nextPoint)) {
//            System.out.println("Already visited");
        } else {
            if (board.isAvailableForNormalSnake(nextPoint)) {
                childs.add(nextPoint);
            }
        }
        nextPoint = new PointImpl(curPoint.getX(), curPoint.getY() - 1);
        if (visited.contains(nextPoint)) {
//            System.out.println("Already visited");
        } else {
            if (board.isAvailableForNormalSnake(nextPoint)) {
                childs.add(nextPoint);
            }
        }
        nextPoint = new PointImpl(curPoint.getX() - 1, curPoint.getY());
        if (visited.contains(nextPoint)) {
//            System.out.println("Already visited");
        } else {
            if (board.isAvailableForNormalSnake(nextPoint)) {
                childs.add(nextPoint);
            }
        }
        nextPoint = new PointImpl(curPoint.getX(), curPoint.getY() + 1);
        if (visited.contains(nextPoint)) {
//            System.out.println("Already visited");
        } else {
            if (board.isAvailableForNormalSnake(nextPoint)) {
                childs.add(nextPoint);
            }
        }
        return childs;
    }
}
