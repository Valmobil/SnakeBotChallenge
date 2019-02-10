package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snakebattle.v4.client.Board;
import com.codenjoy.dojo.snakebattle.v4.model.Elements;
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import java.util.*;

class SnakeUtilsV4 {

    static void startBSSBest(Board board, MySnakeV4 mySnake, List<Point> additionalPath, SnakeListV4 otherSnakes, Map<Integer, List<Point>> bestPaths, HashSet<String> targets) {

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

        //Start recursion
        recursiveBFSBest(board, queueSnakes, visited, queue, queuePath, queueLevel, otherSnakes, bestPaths, targets);
    }

    /**
     * Body ot BFS Recursion
     */
    private static void recursiveBFSBest(Board board, Queue<MySnakeV4> queueSnakes, HashSet<Point> visited, Queue<Point> queue, Queue<LinkedList<Point>> queuePath, Queue<Integer> queueLevel, SnakeListV4 otherSnakes, Map<Integer, List<Point>> bestPaths, HashSet<String> targets) {
        if (queue.isEmpty()) {
            return;
        }
        List<Point> prevPath = queuePath.remove();
        Point curNode = queue.remove();
        Integer curLevel = queueLevel.remove();

        //Change snake position
        MySnakeV4 mySnake = queueSnakes.remove();
        MySnakeV4 newMySnake = new MySnakeV4(mySnake);
        newMySnake.setNextStep(curNode);

        //Change tail position after snake recalculation
        HashSet<Point> targetsDirection = new HashSet<>();
        HashSet<Point> targetsCheck = new HashSet<>();
        if (targets.size() > 0) {
            replaceSpecialStringsWithPoints(targets, targetsDirection, targetsCheck, newMySnake);
        }
        boolean skipPoint = false;

        int result = snakeFoundTargetPoint(board, newMySnake, bestPaths, prevPath, otherSnakes, targetsDirection, targetsCheck);
        if (mySnake.getSize() > 2) {
            System.out.print("");
        }
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

    private static void replaceSpecialStringsWithPoints(HashSet<String> targets, HashSet<Point> targetsDirection, HashSet<Point> targetsCheck, MySnakeV4 newMySnake) {
        targetsDirection.clear();
        targetsCheck.clear();
        for (String str : targets) {
            if (str.equals("mySnakeTail")) {
                if (newMySnake.getSize() > 0) {
                    targetsDirection.add(newMySnake.getBody().get(0));
                } else {
                    targetsDirection.add(newMySnake.getHead());
                }
                targetsCheck.add(newMySnake.getTail());
            }
        }
    }

    /**
     * Check for fruitful point
     * //Return    0 - blank Point
     * //          1 - found eatable Point - nead break searching and save result
     * //          2 - found not eatable Point - forbid movement to it
     * //          4 - skip the point
     */
    private static int snakeFoundTargetPoint(Board board, MySnakeV4 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets, HashSet<Point> targetsCheck) {
        int checkResult;

//        if (mySnake.getSize() == 2) {
//        System.out.println();
//        }

        checkResult = isOwnBodyFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, targetsCheck);
        if (checkResult != 4) {
            return checkResult;
        }

        checkResult = isOwnTailFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets);
        if (checkResult != 4) {
            return checkResult;
        }

        checkResult = isStoneFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets);
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

        checkResult = isTargetCheckFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, targetsCheck);
        if (checkResult != 4) {
            return checkResult;
        }
        return 0;
    }

    private static int isTargetCheckFound(Board board, MySnakeV4 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets, HashSet<Point> targetsCheck) {

        if (targetsCheck.contains(mySnake.getHead())) {
            System.out.println("Utils=>isTargetCheckFound: Target point found!" + mySnake.getHead());
            bestPaths.put(generateBestPathKey(0, prevPath.size()), prevPath);
            return 1;
        }
        return 4;


    }

    /**
     * If snake found own body
     * - forbid to use own body
     * - last body point (near tail) can be used as target for BFS
     */
    private static int isOwnBodyFound(Board board, MySnakeV4 mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets, HashSet<Point> targetsCheck) {
        Point mySnakeHead = mySnake.getHead();

        // If snake found any part of my snake body - forbid this step
        // !!!We use virtual foretasted snake!!!
        // We can step into last but one snake body point (in order to get tail in reality)

        for (int i = 0; i < mySnake.getBody().size(); i++) {
            if (mySnakeHead.itsMe(mySnake.getBody().get(i))) {
                if (targetsCheck.contains(mySnakeHead)) {
                    return 4;
                }
                return 2;
            }
        }
        return 4;
    }


    /**
     * Search for simple element without any logic
     */
    private static int isAppleOrGoldFound(Board board, MySnakeV4
            mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4
                                                  otherSnakes, HashSet<Point> targets, Elements element) {
        Point mySnakeHead = mySnake.getHead();
        if (board.isAt(mySnakeHead, element)) {
            if (targets.size() > 0) {
                return 4;
            }
            bestPaths.put(generateBestPathKey(1, prevPath.size()), prevPath);
            System.out.println("Utils=>isAppleOrGoldFound: Found!" + element + mySnakeHead);
            return 1;
        }
        return 4;
    }

    private static int isStoneFound(Board board, MySnakeV4
            mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4
                                            otherSnakes, HashSet<Point> targets) {
        Point mySnakeHead = mySnake.getHead();

        //Find stone
        int tempSize = mySnake.getSize();
        if (board.isAt(mySnakeHead, Elements.STONE)) {
            if (targets.size() > 0) {
                return 2;
            }
//            System.out.println("Utils=> Found Stone:" + mySnakeHead + "Snake size: " + tempSize + "Snake minSize: " + mySnake.getMinSize());
            if (tempSize >= otherSnakes.getSumOfBodies() + 3 + 4) {
                System.out.println("Utils=>isStoneFound: Snake >= Competitions + 7");
                bestPaths.put(generateBestPathKey(3, prevPath.size()), prevPath);
                System.out.println("Utils=>isStoneFound: Stone found!" + mySnakeHead);
                return 1;
            } else {
//                System.out.println("Stone!!! Get Away");
                return 2;
            }
        }
        return 4;
    }


    /**
     * Search for own tail
     * - we use last body point as target for my tail BFS search
     * - but we use targetsCheck for comparision with predicted mySnakeHead
     */
    private static int isOwnTailFound(Board board, MySnakeV4
            mySnake, Map<Integer, List<Point>> bestPaths, List<Point> prevPath, SnakeListV4
                                              otherSnakes, HashSet<Point> targets) {

        //If body <2, reject finding tail
        if (mySnake.getSize() < 3) {
            if (mySnake.getHead().itsMe(mySnake.getTail())) {
                return 2;
            }
            //If we searching for tail calculate only first 10 future steps
            if (prevPath.size() > 10 && targets.size() > 0) {
                bestPaths.put(generateBestPathKey(0, prevPath.size()), prevPath);
                return 1;
            }
        }
        if (mySnake.getHead().itsMe(mySnake.getTail())) {
            bestPaths.put(generateBestPathKey(0, prevPath.size()), prevPath);
            System.out.println("Utils=>isOwnTailFound!" + mySnake.getHead());
            return 1;
        }
        return 4;
    }

    /**
     * Generate direction for one next step to point
     */
    static String getDirectionToNextPoint(Point fromPoint, Point toPoint) {
        if (fromPoint.getY() < toPoint.getY()) {
            return Direction.UP.toString();
        }
        if (fromPoint.getY() > toPoint.getY()) {
            return Direction.DOWN.toString();
        }
        if (fromPoint.getX() < toPoint.getX()) {
            return Direction.RIGHT.toString();
        }
        return Direction.LEFT.toString();
    }

    private static Integer generateBestPathKey(int foundPoints, int pathSize) {
        return (Integer.MAX_VALUE / 100000 - pathSize) * 100000 + foundPoints;
    }

    /**
     * Return next available steps - wise, clockwise for normal, fly and fury
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
