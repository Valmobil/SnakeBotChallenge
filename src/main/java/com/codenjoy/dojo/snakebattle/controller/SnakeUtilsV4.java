package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.model.SnakeListV4;

import java.util.*;

class SnakeUtilsV4 {

    static void startBSSBest(Board board, MySnakeV4 mySnake, List<Point> additionalPath, SnakeListV4 otherSnakes, Map<Integer, List<Point>> bestPaths, HashSet<Point> targets) {

        Queue<Point> queue = new LinkedList<>();
        Queue<Integer> queueLevel = new LinkedList<>();
        Queue<LinkedList<Point>> queuePath = new LinkedList<>();
        Queue<MySnakeV4> queueSnakes = new LinkedList<>();
        Point mySnakeHead = mySnake.getHead();
        queue.add(mySnakeHead);
        queueLevel.add(0);
        queuePath.add(new LinkedList<>(Arrays.asList(mySnakeHead)));
        queueSnakes.add(mySnake);
        HashSet<Point> visited = new HashSet<>();
//        int[][] myArr = buildArrayBest(board, additionalPath, mySnakeBody);
//        myArr[mySnakeHead.getX()][mySnakeHead.getY()] = 4; //Visited
//        List<Point> shortestPath = recursiveBFSBest(myArr, destination, queue, queuePath, queueLevel, mySnakeBody);
        recursiveBFSBest(board, queueSnakes, visited, queue, queuePath, queueLevel, otherSnakes, bestPaths, targets);
    }

    private static void recursiveBFSBest(Board board, Queue<MySnakeV4> queueSnakes, HashSet<Point> visited, Queue<Point> queue, Queue<LinkedList<Point>> queuePath, Queue<Integer> queueLevel, SnakeListV4 otherSnakes, Map<Integer, List<Point>> bestPaths, HashSet<Point> targets) {
        if (queue.isEmpty()) {
//            bestPaths.put(0, prevPath);
//            System.out.println("Utils=>recursiveBFSBest: Target not found!!! Get MAX Path)");
            return;
        }
        List<Point> prevPath = queuePath.remove();
        Point curNode = queue.remove();
        Integer curLevel = queueLevel.remove();
        MySnakeV4 mySnake = queueSnakes.remove();
//        System.out.println("Path:" + prevPath);
        //Change future snake
        MySnakeV4 newMySnake = new MySnakeV4(mySnake);
        newMySnake.setNextStep(curNode);
        //Change tail position after snake recalculation
        if (targets.size() > 0) {
            targets.clear();
            if (newMySnake.getSize() > 0) {
                targets.add(newMySnake.getBody().get(0));
            } else {
                targets.add(newMySnake.getTail());
            }
        }

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


        int result = snakeFoundTargetPoint(board, newMySnake, bestPaths, prevPath, otherSnakes, targets);
//        int result = snakeFoundFruitfulPoint(board, newMySnake, bestPaths, prevPath, otherSnakes);
        if (result != 0) {
            if (result == 1) {
                //end search and save result
//                System.out.println("Future snake: " + newMySnake.getTail() + newMySnake.getBody() + newMySnake.getHead());
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
                LinkedList<Point> newPath = new LinkedList<>(prevPath);
                newPath.add(child);
                queuePath.add(newPath);
                queueLevel.add(curLevel + 1);
                queueSnakes.add(newMySnake);
                visited.add(child);
            }
        }

        recursiveBFSBest(board, queueSnakes, visited, queue, queuePath, queueLevel, otherSnakes, bestPaths, targets);
    }

    /**
     * Check for fruitful point
     * //Return    0 - blank Point
     * //          1 - found eatable Point - nead break searching and save result
     * //          2 - found not eatable Point - forbid movement it it
     * //          4 - skip the point
     */
    private static int snakeFoundTargetPoint(Board board, MySnakeV4 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets) {
        int checkResult;

        checkResult = isOwnTailFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets);
        if (checkResult != 4) {
            return checkResult;
        }

        checkResult = isAppleOrGoldFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, Elements.APPLE);
        if (checkResult != 4) {
            return checkResult;
        }

        checkResult = isAppleOrGoldFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, Elements.GOLD);
        if (checkResult != 4) {
            return checkResult;
        }
        return 0;
    }

    /**
     * Search for simple element without any logic
     */
    private static int isAppleOrGoldFound(Board board, MySnakeV4 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets, Elements element) {
        Point mySnakeHead = mySnake.getHead();
        if (board.isAt(mySnakeHead, element)) {
            if (targets.size() > 0) {
                return 4;
            }
            bestPaths.put(generateBestPathKey(1, prevPath.size()), prevPath);
            System.out.println("Found " + element + mySnakeHead);
            return 1;
        }
        return 4;
    }

    private static int isStoneFound(Board board, MySnakeV4 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets, Elements element) {
        Point mySnakeHead = mySnake.getHead();

        //Find stone
        int tempSize = mySnake.getSize();
        if (board.isAt(mySnakeHead, Elements.STONE)) {
            if (targets.size() > 0) {
                return 2;
            }
            System.out.println("Utils=> Found Stone:" + mySnakeHead + "Snake size: " + tempSize + "Snake minSize: " + mySnake.getMinSize());
            if (tempSize >= otherSnakes.getSumOfBodies() + 3 + 4) {
//                System.out.println("Snake >= 3");
                bestPaths.put(generateBestPathKey(3, prevPath.size()), prevPath);
                return 1;
            } else {
//                System.out.println("Stone!!! Get Away");
                return 2;
            }
        }
        return 4;
    }

    private static Integer generateBestPathKey(int foundPoints, int pathSize) {
        return (Integer.MAX_VALUE / 100000 - pathSize) * 100000 + foundPoints;
    }

    /**
     * Search for own tail
     */
    private static int isOwnTailFound(Board board, MySnakeV4 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets) {
        //If body <2, reject finding tail
        if (mySnake.getSize() < 2) {
            if (targets.contains(mySnake.getHead())) {
                return 2;
            }
            //If we searching for tail calculate only first 10 steps
            if (prevPath.size() > 10 && targets.size() > 0) {
                bestPaths.put(generateBestPathKey(0, prevPath.size()), prevPath);
                System.out.println("Tail not Found: " + mySnake.getHead());
                return 1;
            }
        }

        if (targets.contains(mySnake.getHead())) {
            bestPaths.put(generateBestPathKey(0, prevPath.size()), prevPath);
            System.out.println("Found Tail: " + mySnake.getHead());
            return 1;
        }
        return 4;


    }

    private static int snakeFoundFruitfulPoint(Board board, MySnakeV4 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes) {


        Point mySnakeHead = mySnake.getHead();


        // eat Other Snakes Tails (last but one point)
//        if (board.isAt(mySnakeHead, Elements.ENEMY_TAIL_END_DOWN, Elements.ENEMY_TAIL_END_LEFT,
//                Elements.ENEMY_TAIL_END_UP, Elements.ENEMY_TAIL_END_RIGHT, Elements.ENEMY_TAIL_INACTIVE)) {
        int tempSize = mySnake.getSize();
        if (otherSnakes.isLastButOne(mySnakeHead)) {
            if (tempSize >= otherSnakes.getSumOfBodies() + 4) {
                bestPaths.put(generateBestPathKey(20, prevPath.size()), prevPath);
                System.out.println("Found Tail: " + mySnakeHead);
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }

    /**
     * Return next available steps - wise, clockwise, fly and fury
     */
    private static List<Point> getEmptyChild(Board board, MySnakeV4 mySnake, HashSet<Point> visited) {
        List<Point> childs = new ArrayList<>();
        Point curPoint = mySnake.getHead();

        Point nextPoint = new PointImpl(curPoint.getX() + 1, curPoint.getY());
        getEmptyChildAddNextPoint(board, visited, childs, nextPoint);

        nextPoint = new PointImpl(curPoint.getX(), curPoint.getY() - 1);
        getEmptyChildAddNextPoint(board, visited, childs, nextPoint);

        nextPoint = new PointImpl(curPoint.getX() - 1, curPoint.getY());
        getEmptyChildAddNextPoint(board, visited, childs, nextPoint);

        nextPoint = new PointImpl(curPoint.getX(), curPoint.getY() + 1);
        getEmptyChildAddNextPoint(board, visited, childs, nextPoint);

        return childs;
    }

    private static void getEmptyChildAddNextPoint(Board board, HashSet<Point> visited, List<Point> childs, Point nextPoint) {
        if (!visited.contains(nextPoint)) {
            if (board.isAvailableForNormalSnake(nextPoint)) {
                childs.add(nextPoint);
            }
        }
    }
}
