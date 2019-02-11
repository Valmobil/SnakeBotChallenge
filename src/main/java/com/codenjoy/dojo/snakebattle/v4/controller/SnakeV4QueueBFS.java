package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeUtilsV4.getDirectionToNextPoint;
import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeUtilsV4.startBSSBest;

public class SnakeV4QueueBFS {

    /**
     * Need to do:
     * Go to nearest Apple - DONE
     * Avoid obstacles - DONE
     * Build Snake body - DONE
     * Create competition snakes list - DONE
     * Competition snakes list prediction
     * BFS algorithm to one Point
     * - To nearest Apple - DONE
     * - To Gold, Stones - DONE
     * - To head and tail of competition
     * - To my tail
     * BFS change direction on each step clockwise and vise versa
     * --Add ClockWise, Fly, Fury to Method private static List<Point> getEmptyChild(Board board, MySnakeV4 mySnake)
     * competition eat my body + 20 points
     * eat stones +10 points -3 size
     * <p>
     * eat Fury drug (eat stones and other snakes without body decrease)
     * eat gold (additional points)
     * eat apple +1 point - change priority for the rule
     * snakes incidence
     * snakes Flying and Fury levels
     * eat Flying drug (over stones and snakes)
     * create stones and walls from stones
     * You can bit the snake to head and neck!!!
     * If forecast coincide with real life - use forecast and continue to calculate future
     */


    public static String StartAppV4(Board board, MySnakeV4 mySnake, SnakeListV4 othSnakes) {
        System.out.println("Snake=>StartAppV4: " + mySnake);

        //Update my Snake body
        Point head = board.getMe();
        mySnake.addToHead(head, board.getMyBody().size(), board.getMyTail().get(0));

        //Build Competitive snakes
        othSnakes.changeSnakes(board.getCompetitiveHead(), board.getCompetitiveBody().size(), board.getCompetitiveTails());

        //Build fruitful paths
        TreeMap<Double, List<Point>> bestPaths = new TreeMap<>();
        startTheBestPathSearch(board, mySnake, othSnakes, bestPaths);

        //Analyse list of built paths
        if (bestPaths.isEmpty()) {
            System.out.println("Snake=>StartAppV4: Best Path Not Found");
            return Direction.STOP.toString();
        }

        //Print paths for Debug purposes
        bestPaths.forEach((key, value) -> {
            System.out.printf("Key: %s, %s%n", key, value);
        });

        //Put snake by the best path
        List<Point> theBestPath = bestPaths.lastEntry().getValue();
        bestPaths.clear();
        return getDirectionToNextPoint(head, theBestPath.get(theBestPath.size() > 1 ? 1 : 0));
    }


    /**
     * Launcher for paths search
     */
    private static void startTheBestPathSearch(Board board, MySnakeV4 mySnake, SnakeListV4 othSnakes, TreeMap<Double, List<Point>> bestPaths) {

        // Targets collection can contains further commands for moving objects
        // - mySnakeBitTail - bit my snake tail
        // - mySnakeTail - look for all except own tail

        HashSet<String> targets = new HashSet<>();

        //Step 1 - follow own tail
        targets.add(mySnake.getTail().toString());
        startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets, 1);

        //Step 2 - find first fruitful point
        //calculate the shortest path to Fruitful points
        targets.clear();
        startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets, 0);

        //Step 3 - bite own tail
//        targets.clear();
//        targets = new HashSet<>();
//        targets.add("mySnakeTail");
//        startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets);

    }

}