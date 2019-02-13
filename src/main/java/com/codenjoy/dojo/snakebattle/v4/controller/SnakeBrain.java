package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;
import com.codenjoy.dojo.snakebattle.v4.model.BestPathV4;
import com.codenjoy.dojo.snakebattle.v4.model.Elements;
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeUtilsV4.addFoundedFruifulPoints;
import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeUtilsV4.generateBestPathKey;

public class SnakeBrain {

    /**
     * If snake reached the eatable point that was already visited, skip it
     */
    public static int isPointWasAlreadyVisited(Board board, MySnakeV4 mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets, HashSet<Point> targetsCheck, BestPathV4 childPath) {
        Point mySnakeHead = mySnake.getHead();
        if (childPath != null) {
            if (childPath.getFoundedFruitfulPoints() != null) {
                if (childPath.getFoundedFruitfulPoints().contains(mySnakeHead)) {
                    Log.printLog("isPointWasAlreadyVisited=> Point already harvested in previous path. Head:" + mySnake.getHead() + " ChildPath->AlreadyCollected: " + childPath.getFoundedFruitfulPoints(), 2);
                    return 0;
                } else {
                    return 4;
                }
            }
        }
        return 4;
    }

    /**
     * If snake found own body
     * - forbid to use own body
     * - last body point (near tail) can be used as target for BFS
     */
    public static int isOwnBodyFound(Board board, MySnakeV4 mySnake, Map<Double, BestPathV4> bestPaths, List<Point> prevPath, SnakeListV4 otherSnakes, HashSet<Point> targets, HashSet<Point> targetsCheck) {
        Point mySnakeHead = mySnake.getHead();

        // If snake found any part of my snake body - forbid this step
        // !!!We use virtual foretasted snake!!!
        // We can step into last but one snake body point (in order to get tail in reality)

        for (Point bodyPart : mySnake.getBody()) {
            try {
                if (bodyPart != null) {
                    if (mySnakeHead.itsMe(bodyPart)) {
                        if (targetsCheck.contains(mySnakeHead)) {
                            return 4;
                        }
                        Log.printLog("Snake=>Found body. Head:" + mySnakeHead.toString(), 2);
                        return 2;
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("Error is in isOwnBodyFound");
            }
        }
        return 4;
    }


    /**
     * Search for own tail
     * - we use last body point as target for my tail BFS search
     * - but we use targetsCheck for comparision with predicted mySnakeHead
     */
    public static int isOwnTailFound(Board board, MySnakeV4
            mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4
                                             otherSnakes, HashSet<Point> targets, int score, HashSet<Point> visited, Integer mode, BestPathV4 childPath) {
        boolean tailIsFound;

        //If body <2, reject finding tail
//        if (mySnake.getSize() < 3) {
        //If we searching for tail calculate only first 10 future steps
//            if (prevPath.size() > 40 && targets.size() > 0) {
//                tailIsFound = false;
//                bestPaths.put(generateBestPathKey(score, prevPath.size(), null, tailIsFound), new BestPathV4(mySnake, score, prevPath, board, visited, otherSnakes, null, tailIsFound));
//                Log.printLog("Snake=>Tail not Found (10 steps). Head:" + mySnake.getHead() + "Targets: " + targets,2);
//                return 1;
//            }

//        }
        try {
            if (mySnake.getHead().itsMe(mySnake.getTail())) {
//            System.out.println("Utils=>isOwnTailFound!" + mySnake.getHead());
                if (mode != 1) {
                    Log.printLog("Snake=>Tail Avoiding due to Mode <> 1. Head:" + mySnake.getHead() + " Tail: " + mySnake.getTail() + " MySnake: " + mySnake, 2);
                    return 2;
                } else {
                    tailIsFound = true;
                    bestPaths.put(generateBestPathKey(score, prevPath.size(), childPath, tailIsFound), new BestPathV4(mySnake, score, prevPath, board, visited, otherSnakes, addFoundedFruifulPoints(mySnake.getHead(), childPath), tailIsFound));
                    Log.printLog("Snake=>Tail Found. Head:" + mySnake.getHead() + " Tail: " + mySnake.getTail() + " MySnake: " + mySnake, 2);
                    return 1;
                }
            }
        } catch (NullPointerException e) {
            System.out.println();
        }
        return 4;
    }


    public static int isStoneFound(Board board, MySnakeV4
            mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4
                                           otherSnakes, HashSet<Point> targets, int score, Integer mode, HashSet<Point> visited, BestPathV4 childPath) {
        Point mySnakeHead = mySnake.getHead();

        //Find stone
        int tempSize = mySnake.getSize();
        if (board.isAt(mySnakeHead, Elements.STONE)) {
            if (mode == 1) {
                Log.printLog("Snake=>Stone avoiding due to Mode = 1. Head:" + mySnake.getHead(), 2);
                return 2;
            }
//            System.out.println("Utils=> Found Stone:" + mySnakeHead + "Snake size: " + tempSize + "Snake minSize: " + mySnake.getMinSize());
            if (tempSize >= otherSnakes.getSumOfBodies() + 3 + 4) {
                System.out.println("Utils=>isStoneFound: Snake >= Competitions + 7");
                boolean tailIsFound = false;
                bestPaths.put(generateBestPathKey(score, prevPath.size(), null, tailIsFound), new BestPathV4(mySnake, score, prevPath, board, visited, otherSnakes, addFoundedFruifulPoints(mySnake.getHead(), childPath), tailIsFound));
                Log.printLog("Snake=>Stone (eatable) Found. Head:" + mySnake.getHead() + " My snake size: " + mySnake.getSize() + " Other Snakes Size: " + otherSnakes.getSumOfBodies(), 2);
                return 1;
            } else {
                Log.printLog("Snake=>Stone avoiding due to size. Head:" + mySnake.getHead() + " My snake size: " + mySnake.getSize() + " Other Snakes Size: " + otherSnakes.getSumOfBodies(), 2);
//                System.out.println("Stone!!! Get Away");
                return 2;
            }
        }
        return 4;
    }

    /**
     * Search for simple element without any logic
     */
    public static int isAppleOrGoldFound(Board board, MySnakeV4
            mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4
                                                 otherSnakes, HashSet<Point> targets, Elements element, Integer mode, int score, HashSet<Point> visited, BestPathV4 childPath) {
        Point mySnakeHead = mySnake.getHead();
        if (board.isAt(mySnakeHead, element)) {
            if (mode == 1) {
                return 4;
            }
            boolean tailIsFound = false;
            bestPaths.put(generateBestPathKey(score, prevPath.size(), null, tailIsFound), new BestPathV4(mySnake, score, prevPath, board, visited, otherSnakes, addFoundedFruifulPoints(mySnake.getHead(), childPath), tailIsFound));
//            System.out.println("Utils=>isAppleOrGoldFound: Found!" + element + mySnakeHead);
            Log.printLog("Snake=>Apple or Gold found. Head:" + mySnake.getHead() + " Element: " + element + " Mode=" + mode, 2);
            return 1;
        }
        return 4;
    }

    /**
     * Search fury
     */

    public static int isFuryFound(Board board, MySnakeV4
            mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4
                                          otherSnakes, HashSet<Point> targets, int score, Integer mode, HashSet<Point> visited, BestPathV4 childPath) {
        Point mySnakeHead = mySnake.getHead();
        if (board.isAt(mySnakeHead, Elements.FURY_PILL)) {
            if (mode == 1) {
                return 4;
            }
            boolean tailIsFound = false;
            mySnake.setFury(10);
            bestPaths.put(generateBestPathKey(score, prevPath.size(), null, tailIsFound), new BestPathV4(mySnake, score, prevPath, board, visited, otherSnakes, addFoundedFruifulPoints(mySnake.getHead(), childPath), tailIsFound));
//            System.out.println("Utils=>isAppleOrGoldFound: Found!" + element + mySnakeHead);
            Log.printLog("Snake=>Fury found. Head:" + mySnake.getHead() + " Mode=" + mode, 2);
            return 1;
        }
        return 4;
    }

    /**
     * Search fury
     */
    public static int isFlyFound(Board board, MySnakeV4
            mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4
                                         otherSnakes, HashSet<Point> targets, int score, Integer mode, HashSet<Point> visited, BestPathV4 childPath) {
        Point mySnakeHead = mySnake.getHead();
        if (board.isAt(mySnakeHead, Elements.FLYING_PILL)) {
            if (mode == 1) {
                return 4;
            }
            boolean tailIsFound = false;
            mySnake.setFly(10);
            bestPaths.put(generateBestPathKey(score, prevPath.size(), null, tailIsFound), new BestPathV4(mySnake, score, prevPath, board, visited, otherSnakes, addFoundedFruifulPoints(mySnake.getHead(), childPath), tailIsFound));
//            System.out.println("Utils=>isAppleOrGoldFound: Found!" + element + mySnakeHead);
            Log.printLog("Snake=>Fly found. Head:" + mySnake.getHead() + " Mode=" + mode, 2);
            return 1;
        }
        return 4;
    }


    /**
     * Competitive head will be found if mySnake.size > other snakes size
     * If
     */
    public static int isOtherHeadFound(Board board, MySnakeV4
            mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4
                                               otherSnakes, HashSet<Point> targets, int score, Integer mode, HashSet<Point> visited, BestPathV4 childPath) {
        Point mySnakeHead = mySnake.getHead();

        //Find other head
        int tempSize = mySnake.getSize();
        if (myHeaderOtherSnakesReached(mySnakeHead, otherSnakes)) {
            if (mode == 1) {
                Log.printLog("Snake=>Other snakes headers avoiding to Mode = 1. Head:" + mySnake.getHead(), 2);
                return 2;
            }
//            System.out.println("Utils=> Found Stone:" + mySnakeHead + "Snake size: " + tempSize + "Snake minSize: " + mySnake.getMinSize());
            if (tempSize >= otherSnakes.getSumOfBodies() + 3 + 4) {
                boolean tailIsFound = false;
                bestPaths.put(generateBestPathKey(score, prevPath.size(), null, tailIsFound), new BestPathV4(mySnake, score, prevPath, board, visited, otherSnakes, addFoundedFruifulPoints(mySnake.getHead(), childPath), tailIsFound));
                Log.printLog("Snake=>Other head (eatable) Found. Head:" + mySnake.getHead() + " My snake size: " + mySnake.getSize() + " Other Snakes Size: " + otherSnakes.getSumOfBodies(), 2);
                return 1;
            } else {
                Log.printLog("Snake=>Other heads avoiding due to size. Head:" + mySnake.getHead() + " My snake size: " + mySnake.getSize() + " Other Snakes Size: " + otherSnakes.getSumOfBodies(), 2);
//                System.out.println("Stone!!! Get Away");
                return 2;
            }
        }
        return 4;
    }

    private static boolean myHeaderOtherSnakesReached(Point mySnakeHead, SnakeListV4 otherSnakes) {
        for (MySnakeV4 snake : otherSnakes.getSnakes()) {
            if (snake.getHead().itsMe(mySnakeHead)) {
                return true;
            }
        }
        return false;
    }

    public static int isTargetCheckFound(Board board, MySnakeV4
            mySnake, Map<Double, BestPathV4> bestPaths, LinkedList<Point> prevPath, SnakeListV4
                                                 otherSnakes, HashSet<Point> targets, HashSet<Point> targetsCheck, HashSet<Point> visited, int score, BestPathV4 childPath, Integer mode) {

        if (targetsCheck.contains(mySnake.getHead())) {
//            System.out.println("Utils=>isTargetCheckFound: Target point found!" + mySnake.getHead());
            boolean tailIsFound = false;
            if (mode == 1) {
                tailIsFound = true;
            }
            bestPaths.put(generateBestPathKey(score, prevPath.size(), childPath, tailIsFound), new BestPathV4(mySnake, score, prevPath, board, visited, otherSnakes, addFoundedFruifulPoints(mySnake.getHead(), childPath), tailIsFound));
            Log.printLog("Snake=>Targeted element found. Head:" + mySnake.getHead() + " Targets: " + targets + " Targets Check: " + targetsCheck, 2);
            return 1;
        }
        return 4;
    }


}
