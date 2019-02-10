package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;

import java.util.*;

import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeUtilsV1.*;
import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeUtilsV3.startBSSBest;

public class SnakeV3BFS {

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
     * --Add ClockWise, Fly, Fury to Method private static List<Point> getEmptyChild(Board board, MySnakeV3 mySnake)
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
     */


    public static String thirdVersion(Board board, MySnakeV3 mySnake, SnakeListV3 othSnakes) {
        //List of built fretful paths
        TreeMap<Integer, List<Point>> bestPaths = new TreeMap<>();

        Point head = board.getMe();
//        if (mySnake.getSize() > board.getMyBody().size()) {
//            System.out.printf("Snake old size %s => %s%n", mySnake.getSize(), board.getMyBody().size());
//            System.out.println("System body: "+ board.getMyBody());
//        }
//        System.out.println("getMyBody System size before change: " + board.getMyBody().size());
//        System.out.println("my Snake         size before change: " + mySnake.getSize());

        //Update my Snake body
        mySnake.addToHead(head, board.getMyBody().size(), board.getMyTail().get(0));

        //Build Competitive snakes
        othSnakes.changeSnakes(board.getCompetitiveHead(),board.getCompetitiveBody().size(), board.getCompetitiveTails());
        System.out.println("Snake-> " + othSnakes.toString());
//        System.out.println("my Snake         size after  change: " + mySnake.getSize());
//        System.out.println(board.toString());
//        System.out.println("My snake   : " + mySnake.getTail() + mySnake.getBody() + mySnake.getHead());

//        List<Point> apples = board.getApples();
//        Point nextApple = getNearestApple(head, apples);
//        Point nextStep = buildPathNextStep(board, head, nextApple);

        //calculate the shortest path to Fruitful points
        startBSSBest(board, mySnake, new LinkedList<>(), othSnakes, bestPaths);
        if (bestPaths.isEmpty()) {
            System.out.println("Best Path Not Found");
            return Direction.STOP.toString();
        }
        List<Point> theBestPath = bestPaths.lastEntry().getValue();
        System.out.println(bestPaths);
        bestPaths.clear();
        return getDirectionToNextPoint(head, theBestPath.get(1));

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


}