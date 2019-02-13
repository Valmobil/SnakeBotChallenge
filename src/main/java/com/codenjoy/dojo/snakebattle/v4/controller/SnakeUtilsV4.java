package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snakebattle.v4.client.Board;
import com.codenjoy.dojo.snakebattle.v4.model.BestPathV4;
import com.codenjoy.dojo.snakebattle.v4.model.Elements;
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import java.util.*;

import static com.codenjoy.dojo.snakebattle.v4.controller.Log.printLog;
import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeBrain.*;

class SnakeUtilsV4 {

    static void startBSSBest(Board board, MySnakeV4 mySnake, LinkedList<Point> additionalPath, SnakeListV4 otherSnakes, Map<Double, BestPathV4> bestPaths, HashSet<String> targets, Integer mode, BestPathV4 childPath) {


        // Mode can be:
        // - 0 way to any fruitful point
        // - 1 way directly to point (eating apple&gold and avoiding stones etc)

        Queue<Point> queue = new LinkedList<>();
        Queue<Integer> queueLevel = new LinkedList<>();
        Queue<LinkedList<Point>> queuePath = new LinkedList<>();
        Queue<MySnakeV4> queueSnakes = new LinkedList<>();


        Point mySnakeHead = mySnake.getHead();
        queue.add(mySnakeHead);
        queueLevel.add(0);
        if (additionalPath.size() > 0) {
            queuePath.add(additionalPath);
        } else {
            queuePath.add(new LinkedList<>(Arrays.asList(mySnakeHead)));
        }
        queueSnakes.add(mySnake);
        HashSet<Point> visited = new HashSet<>();

        //Start recursion
        recursiveBFSBest(board, queueSnakes, visited, queue, queuePath, queueLevel, otherSnakes, bestPaths, targets, mode, childPath);
    }

    /**
     * Body ot BFS Recursion
     */
    private static void recursiveBFSBest(Board board, Queue<MySnakeV4> queueSnakes, HashSet<Point> visited, Queue<Point> queue, Queue<LinkedList<Point>> queuePath, Queue<Integer> queueLevel, SnakeListV4 otherSnakes, Map<Double, BestPathV4> bestPaths, HashSet<String> targets, Integer mode, BestPathV4 childPath) {
        if (queue.isEmpty()) {
            return;
        }

        //For debug purposes
        printLog(board, queueSnakes, visited, queue, queuePath, queueLevel, otherSnakes, bestPaths, targets, mode);

        LinkedList<Point> prevPath = queuePath.remove();
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
//        if (mySnake.getTail().getX() == 0) {
//            System.out.println();
//        }
        int result = snakeFoundTargetPoint(board, newMySnake, bestPaths, prevPath, otherSnakes, targetsDirection, targetsCheck, mode, visited, childPath);
        Log.printLog("Result: " + result, 0);

        if (result != 0) {
            if (result == 1) {
                //end search and save result
                Log.printLog("~~~~~", 2);
                Log.printLog("recursiveBFSBest=> BestPathes: " + bestPaths.toString(), 1);
                queue.clear();
                return;
            } else {
                skipPoint = true;
                //we found obstacle, so we should reject this step
            }
        }
        if (!skipPoint) {
            //check child node
            Log.printLog("recursiveBFSBest=> Child nodes for queue: " + getEmptyChild(board, newMySnake, visited), 2);
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
        recursiveBFSBest(board, queueSnakes, visited, queue, queuePath, queueLevel, otherSnakes, bestPaths, targets, mode, childPath);
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
            } else {
                Point p = stringToPoint(str);
                targetsDirection.add(p);
                targetsCheck.add(p);
            }
        }
    }

    private static Point stringToPoint(String str) {
        //Valid string (str) example
        int x = Integer.parseInt(str.substring(1, str.indexOf(",")));
        int y = Integer.parseInt(str.substring(str.indexOf(",") + 1, str.indexOf("]")));
        return new PointImpl(x, y);
    }

    /**
     * Check for fruitful point
     * //Return    0 - blank Point
     * //          1 - found eatable Point - nead break searching and save result
     * //          2 - found not eatable Point - forbid movement to it
     * //          4 - skip the point
     */
    private static int snakeFoundTargetPoint(Board board, MySnakeV4 mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets, HashSet<Point> targetsCheck, Integer mode, HashSet<Point> visited, BestPathV4 childPath) {
        int checkResult;

//        if (mySnake.getSize() == 2) {
//        System.out.println();
//        }
        checkResult = isPointWasAlreadyVisited(board, mySnake, bestPaths, prevPath, otherSnakes, targets, targetsCheck, childPath);
        if (checkResult != 4) {
            return checkResult;
        }

        checkResult = isOwnBodyFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, targetsCheck);
        if (checkResult != 4) {
            return checkResult;
        }
        int score = 0;
        checkResult = isOwnTailFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, score, visited, mode, childPath);
        if (checkResult != 4) {
            return checkResult;
        }

        score = 5;
        checkResult = isStoneFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, score, mode, visited, childPath);
        if (checkResult != 4) {
            return checkResult;
        }

        score = 1;
        checkResult = isAppleOrGoldFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, Elements.APPLE, mode, score, visited, childPath);
        if (checkResult != 4) {
            return checkResult;
        }

        score = 10;
        checkResult = isAppleOrGoldFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, Elements.GOLD, mode, score, visited, childPath);
        if (checkResult != 4) {
            return checkResult;
        }

        score = 0;
        checkResult = isTargetCheckFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, targetsCheck, visited, score, childPath, mode);
        if (checkResult != 4) {
            return checkResult;
        }

        score = 20;
        checkResult = isOtherHeadFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, score, mode, visited, childPath);
        if (checkResult != 4) {
            return checkResult;
        }

        score = 20;
        checkResult = isFuryFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, score, mode, visited, childPath);
        if (checkResult != 4) {
            return checkResult;
        }

        score = 1;
        checkResult = isFlyFound(board, mySnake, bestPaths, prevPath, otherSnakes, targets, score, mode, visited, childPath);
        if (checkResult != 4) {
            return checkResult;
        }


        return 0;
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

    public static Double generateBestPathKey(int foundPoints, int pathSize, BestPathV4 childPath, boolean tailIsFound) {
        if (childPath != null) {
            return (Double.valueOf(childPath.getScore()) / childPath.getPath().size()) + (tailIsFound ? 100 : 0);
        }
        return (Double.valueOf(foundPoints) / pathSize) + (tailIsFound ? 100 : 0);
    }

    /**
     * Return next available steps - wise, clockwise for normal, fly and fury
     */
    private static List<Point> getEmptyChild(Board board, MySnakeV4 mySnake, HashSet<Point> visited) {
        List<Point> childs = new ArrayList<>();
        Point curPoint = mySnake.getHead();

        Point nextPoint = new PointImpl(curPoint.getX() + 1, curPoint.getY());
        getEmptyChildAddNextPoint(board, visited, childs, nextPoint, mySnake);

        nextPoint = new PointImpl(curPoint.getX(), curPoint.getY() - 1);
        getEmptyChildAddNextPoint(board, visited, childs, nextPoint, mySnake);

        nextPoint = new PointImpl(curPoint.getX() - 1, curPoint.getY());
        getEmptyChildAddNextPoint(board, visited, childs, nextPoint, mySnake);

        nextPoint = new PointImpl(curPoint.getX(), curPoint.getY() + 1);
        getEmptyChildAddNextPoint(board, visited, childs, nextPoint, mySnake);

        return childs;
    }

    private static void getEmptyChildAddNextPoint(Board
                                                          board, HashSet<Point> visited, List<Point> childs, Point nextPoint, MySnakeV4 mySnake) {
        if (!visited.contains(nextPoint)) {
            //If not visited yet
            if (board.isAvailableForNormalSnake(nextPoint)) {
                //if we can enter to this point
                if (!(mySnake.getSize() == 0 && nextPoint.itsMe(mySnake.getTail()))) {
                    //Snake without body cannot get tail
                    try {
                        try {
                            if (!snakeTryToGetOwnNeck(mySnake, nextPoint)) {
                                childs.add(nextPoint);
                            } else {
                                Log.printLog("getEmptyChildAddNextPoint=> Snake body > 0, try bite own neck. Next point: " + nextPoint, 0);
                            }
                        } catch (NullPointerException e) {
                            System.out.println("Error !!!");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Error!!!");
                    }
                } else {
                    Log.printLog("getEmptyChildAddNextPoint=> Snake body == 0, try turn around. Next point: " + nextPoint, 0);
                }
            } else {
                Log.printLog("getEmptyChildAddNextPoint=> Standard obstacles. Next point: " + nextPoint, 0);
            }
        } else {
            Log.printLog("getEmptyChildAddNextPoint=> Already Visited. Next point: " + nextPoint, 0);
        }
    }

    private static boolean snakeTryToGetOwnNeck(MySnakeV4 mySnake, Point nextPoint) {
        if (mySnake.getBody() != null) {
            if (mySnake.getBody().size() > 0) {
                if (mySnake.getBody().get(mySnake.getBody().size() - 1) != null) {
                    if (nextPoint.itsMe(mySnake.getBody().get(mySnake.getBody().size() - 1))) {
                        //If Snake try to reach own neck
                        return true;
                    }

                }
            }
        }
        return false;
    }


    public static HashSet<Point> addFoundedFruifulPoints(Point head, BestPathV4 childPath) {
        HashSet<Point> foundedFruitfulPoints;
        if (childPath != null) {
            foundedFruitfulPoints = new HashSet<>(childPath.getFoundedFruitfulPoints());
        } else {
            foundedFruitfulPoints = new HashSet<>();
        }
        foundedFruitfulPoints.add(head);
        return foundedFruitfulPoints;
    }
}


