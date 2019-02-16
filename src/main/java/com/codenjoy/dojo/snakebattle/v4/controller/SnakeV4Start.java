package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;
import com.codenjoy.dojo.snakebattle.v4.model.BestPathV4;
import com.codenjoy.dojo.snakebattle.v4.model.Elements;
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import java.text.Format;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeUtilsV4.*;

public class SnakeV4Start {

    /**
     * Need to do:
     * Go to nearest Apple - DONE
     * Avoid obstacles - DONE
     * Build Snake body - DONE
     * Create competition snakes list - DONE
     * Competition snakes list prediction - NOT DONE (not essential)
     * BFS algorithm to one Point - DONE
     * - To nearest Apple - DONE
     * - To Gold, Stones - DONE
     * - To head and tail of competition - DONE
     * - To my body - DONE
     * BFS change direction on each step clockwise and vise versa - NOT DONE (Realy need!!!)
     * --Add ClockWise, Fly, Fury to Method getEmptyChild
     * competition eat my body + 20 points - DONE
     * eat stones +10 points -3 size - DONE
     * eat Fury drug (eat stones and other snakes without body decrease) - DONE
     * eat gold (additional points) - DONE
     * eat apple +1 point - change priority for the rule - DONE
     * snakes incidence - DONE
     * snakes Flying and Fury levels - NOT DONE
     * create stones and walls from stones - NOT DONE
     * you can bit the snake to head and neck!!! - DONE
     * If forecast coincide with real life - use forecast and continue to calculate future - NOT DONE (Realy need!!!)
     * Do manual drive (by selecting next target type - linked to different keys on keyboard) - NOT DONE (
     */


    public static String StartAppV4(Board board, MySnakeV4 mySnake, SnakeListV4 othSnakes) {
        //Time track for
        long startTime = System.nanoTime();

        //Update my Snake body
        Point head = board.getMe();
        mySnake.addToHead(head, board.getMyBody().size(), board.getMyTail().get(0), board);
        System.out.println("Snake=>StartAppV4: " + mySnake);

        //Build Competitive snakes
        othSnakes.changeSnakes(board.getCompetitiveHead(), board.getCompetitiveBody().size(), board.getCompetitiveTails());
        Log.printLog("OtherSnakes: " + othSnakes.toString(), 0);

        //Build fruitful paths
        ConcurrentSkipListMap<Double, BestPathV4> bestPaths = new ConcurrentSkipListMap<>();
        HashSet<String> targets = new HashSet<>();
        startTheBestPathSearch(board, mySnake, othSnakes, bestPaths, targets, startTime);

        //Analyse list of built paths
        if (bestPaths.isEmpty()) {
            System.out.println("Snake=>StartAppV4: Best Path Not Found");
            return Direction.STOP.toString();
        }

        //Print paths for Debug purposes
        bestPaths.forEach((key, value) -> Log.printLog("Key: " + key + "Path:" + value, 2));

        //Put snake by the best path
        List<Point> theBestPath = bestPaths.lastEntry().getValue().getPath();
        bestPaths.clear();
        long endTime = System.nanoTime();
        Log.printLog("Time lapse(ms): " + (endTime - startTime) / 1000000, 2);
        Point nextStep = theBestPath.get(theBestPath.size() > 1 ? 1 : 0);
        checkSnakeFlyFuryStatus(board, mySnake, nextStep);
        return getDirectionToNextPoint(head, nextStep);
    }


    /**
     * Launcher for paths search
     */
    private static void startTheBestPathSearch(Board board, MySnakeV4 mySnake, SnakeListV4 othSnakes, ConcurrentSkipListMap<Double, BestPathV4> bestPaths, HashSet<String> targets, long startTime) {

        // Targets collection can contains further commands for moving objects
        // - mySnakeBitTail - bit my snake tail
        // - mySnakeTail - look for all except own tail

        // Max timeout
        int mazTimeout = 250;

        // Step 1
        Log.printLog("**Step 1 - follow own tail**", 3);
        if (targets.size() == 0) {
            targets.add(mySnake.getTail().toString());
        }
        startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets, 1, null);

        // Step 2
        Log.printLog("**Step 2 - if tail is not available - eat own body**", 3);
        long endTime = System.nanoTime();
        Log.printLog("Time: " + (endTime - startTime) / 1000000, 2);
        if (bestPaths.size() == 0) {
            targets.clear();
            for (Point point : mySnake.getBody()) {
                if (point != null) {
                    targets.add(point.toString());
                }
            }
            startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets, 2, null);
        }

        // Step 3
        Log.printLog("**Step 3 - if active fury mode only - find nearest other snake**", 3);
        endTime = System.nanoTime();
        Log.printLog("Time: " + (endTime - startTime) / 1000000, 2);
        if (mySnake.getFury() > 0 && mySnake.getFly() == 0) {
            targets.clear();
            for (MySnakeV4 othSnake : othSnakes.getSnakes()) {
                targets.add(othSnake.getHead().toString());
                targets.add(othSnake.getTail().toString());
                for (Point point : othSnake.getBody()) {
                    targets.add(point.toString());
                }
            }
            startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets, 2, null);
        }

        // Step 4
        Log.printLog("**Step 4 - find first fruitful point**", 3);
        endTime = System.nanoTime();
        Log.printLog("Time: " + (endTime - startTime) / 1000000, 2);
        //calculate the shortest path to Fruitful points
        targets.clear();
        startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets, 0, null);

        // Step 5
        endTime = System.nanoTime();
        if ((endTime - startTime) / 1000000 > mazTimeout) {
            Log.printLog("!!!Time out!!! > 600 - ignore Gold", 2);
        } else {
            Log.printLog("**Step 5 - Look for Gold**", 3);
            Log.printLog("Time: " + (endTime - startTime) / 1000000, 2);
            targets.clear();
            for (Point point : board.getMyGold()) {
                targets.add(point.toString());
            }
            if (targets.size() > 0) {
                startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets, 3, null);
            }
        }

        // Step 6
        Log.printLog("**Step 6 - check if after apple the tail is reachable", 3);
        endTime = System.nanoTime();
        Log.printLog("Time: " + (endTime - startTime) / 1000000, 2);
        Set<Double> mapKeys = bestPaths.keySet();
        for (Double key : mapKeys) {
            BestPathV4 myEntry = bestPaths.get(key);
            if (!myEntry.isTailIsReachable()) {
                targets.clear();
                Board boardNextStep = myEntry.getBoard();
                MySnakeV4 snakeNextStep = myEntry.getSnake();
                LinkedList<Point> pathNextStep = myEntry.getPath();
                SnakeListV4 otherSnakesNextStep = myEntry.getOtherSnakes();
                HashSet<String> targetsNextStep = new HashSet<>();
                targetsNextStep.add(snakeNextStep.getTail().toString());
                myEntry.setAlreadyUsed(true);
                startBSSBest(boardNextStep, snakeNextStep, pathNextStep, otherSnakesNextStep, bestPaths, targetsNextStep, 1, myEntry);
            }
        }

        // Step 7
        endTime = System.nanoTime();
        if ((endTime - startTime) / 1000000 > mazTimeout) {
            Log.printLog("!!!Time out!!! >600 - Ignore Fury", 2);
        } else {
            Log.printLog("**Step 7 - look for Fury**", 3);
            Log.printLog("Time: " + (endTime - startTime) / 1000000, 2);
            targets.clear();
            for (Point point : board.getMyFury()) {
                targets.add(point.toString());
            }
            if (targets.size() > 0) {
                startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets, 3, null);
            }
        }

        // Step 8
        Log.printLog("**Step 8 - check if snakes with fury is available, check if other snakes reachable**", 3);
        endTime = System.nanoTime();
        Log.printLog("Time: " + (endTime - startTime) / 1000000, 2);
        if ((endTime - startTime) / 1000000 > mazTimeout) {
            Log.printLog("!!!Time out!!! >600 - Ignore Fury", 2);
        } else {
            mapKeys = bestPaths.keySet();
            for (Double key : mapKeys) {
                BestPathV4 myEntry = bestPaths.get(key);
                if (!myEntry.isTailIsReachable()) {
                    targets.clear();
                    Board boardNextStep = myEntry.getBoard();
                    MySnakeV4 snakeNextStep = myEntry.getSnake();
                    LinkedList<Point> pathNextStep = myEntry.getPath();
                    SnakeListV4 otherSnakesNextStep = myEntry.getOtherSnakes();
                    HashSet<String> targetsNextStep = new HashSet<>();
                    targetsNextStep.add(snakeNextStep.getTail().toString());
                    myEntry.setAlreadyUsed(true);
                    startBSSBest(boardNextStep, snakeNextStep, pathNextStep, otherSnakesNextStep,
                            bestPaths, targetsNextStep, 1, myEntry);
                }
            }
        }
    }
}
