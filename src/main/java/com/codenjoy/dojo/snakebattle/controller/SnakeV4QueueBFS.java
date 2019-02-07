package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.model.SnakeListV4;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import static com.codenjoy.dojo.snakebattle.controller.SnakeUtilsV1.getDirectionToNextPoint;
import static com.codenjoy.dojo.snakebattle.controller.SnakeUtilsV4.startBSSBest;

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

//        if (mySnake.getSize() > board.getMyBody().size()) {
//            System.out.printf("Snake old size %s => %s%n", mySnake.getSize(), board.getMyBody().size());
//            System.out.println("System body: "+ board.getMyBody());
//        }
//        System.out.println("getMyBody System size before change: " + board.getMyBody().size());
//        System.out.println("my Snake         size before change: " + mySnake.getSize());

        //Update my Snake body
        Point head = board.getMe();
        mySnake.addToHead(head, board.getMyBody().size(), board.getMyTail().get(0));

        //Build Competitive snakes
        othSnakes.changeSnakes(board.getCompetitiveHead(), board.getCompetitiveBody().size(), board.getCompetitiveTails());
//        System.out.println("Snake-> " + othSnakes.toString());
//        System.out.println("my Snake         size after  change: " + mySnake.getSize());
//        System.out.println(board.toString());
//        System.out.println("My snake   : " + mySnake.getTail() + mySnake.getBody() + mySnake.getHead());

//        List<Point> apples = board.getApples();
//        Point nextApple = getNearestApple(head, apples);
//        Point nextStep = buildPathNextStep(board, head, nextApple);

        //List of built fruitful paths
        TreeMap<Integer, List<Point>> bestPaths = new TreeMap<>();

        startTheBestPathSearch(board, mySnake, othSnakes, bestPaths);

        if (bestPaths.isEmpty()) {
            System.out.println("Snake=>StartAppV4: Best Path Not Found");
            return Direction.STOP.toString();
        }

        bestPaths.forEach((key, value) -> {
            System.out.printf("Key: %s, %s%n", key, value);
        });

        List<Point> theBestPath = bestPaths.lastEntry().getValue();
        bestPaths.clear();
        if (theBestPath.size() > 1) {
            return getDirectionToNextPoint(head, theBestPath.get(1));
        } else {
            return getDirectionToNextPoint(head, theBestPath.get(0));
        }
        //Analyze if snake reach apple and the tail will be reachable (emulate path to apple)
//        List<Point> nextPointTailPath = null;
//        if (null != nextPointApplePath) {
//            System.out.print("To apple + tail: ");
//            nextPointTailPath = startBSSBest(board, nextApple, mySnake.getNextByTail(), nextPointApplePath, mySnake.getBody());
//        }
//        if ((null != nextPointApplePath) && (null != nextPointTailPath)) {
//            If apple is reacheble
//            return getDirectionToNextPoint(head, nextPointApplePath.get(1));
//        }

        //Calculate the shortest path to Tail
//        System.out.print("To tail: ");
//        nextPointTailPath = startBSSBest(board, head, mySnake.getNextByTail(), new LinkedList<Point>(), mySnake.getBody());


//        System.out.println("Next apple: " + nextApple.toString());
//        System.out.println("Head      : " + head.toString());
//        System.out.println("Tail      : " + board.getMyTail().toString());
//        System.out.println("Next Step : " + nextStep.toString());
//        return Direction.UP.toString();
    }

    private static void startTheBestPathSearch(Board board, MySnakeV4 mySnake, SnakeListV4 othSnakes, TreeMap<Integer, List<Point>> bestPaths) {


        //Step 1 - find own tail
        HashSet<Point> targets = new HashSet<>();
        targets.add(mySnake.getTail());
        startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets);

        //Step 2 - find first fruitful point
        //calculate the shortest path to Fruitful points
        targets.clear();
        startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths, targets);

    }

}