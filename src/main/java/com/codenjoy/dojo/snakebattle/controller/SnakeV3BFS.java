package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.model.MySnakeV2;
import com.codenjoy.dojo.snakebattle.model.MySnakeV3;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.snakebattle.controller.SnakeUtilsV1.*;
import static com.codenjoy.dojo.snakebattle.controller.SnakeUtilsV3.startBSSBest;

public class SnakeV3BFS {

    /**
     * Need to do:
     * Go to nearest Apple - DONE
     * Avoid obstacles - DONE
     * Build Snake body - DONE
     * BFS algorithm to one Point
     * BFS change direction on each step clockwise and vise versa
     * --Add ClockWize, Fly, Fury to Method private static List<Point> getEmptyChild(Board board, MySnakeV3 mySnake)
     *
     *
     * eat stones +10 points -3 size
     * eat Fury drug (eat stones and other snakes without body decrease)
     * eat gold (additional points)
     * eat apple +1 point - change priority for the rule
     * snakes incidence
     * snakes Flying and Fury levels
     * eat Flying drug (over stones and snakes)
     * create stones and walls from stones
     */


    public static String thirdVersion(Board board, MySnakeV3 mysnakeV3) {
        Point head = board.getMe();
        if (mysnakeV3.getSize() > board.getMyBody().size()) {
            System.out.printf("Snake old size %s => %s%n", mysnakeV3.getSize(), board.getMyBody().size());
            System.out.println("System body: "+ board.getMyBody());
        }

        mysnakeV3.addToHead(head, board.getMyBody().size(), board.getMyTail().get(0));
//        System.out.println(board.toString());
        System.out.println("My snake   : " + mysnakeV3.getTail() + mysnakeV3.getBody() + mysnakeV3.getHead());

//        List<Point> apples = board.getApples();
//        Point nextApple = getNearestApple(head, apples);
//        Point nextStep = buildPathNextStep(board, head, nextApple);

        //calculate the shortest path to Apple
        List<Point> nextPointApplePath = startBSSBest(board, mysnakeV3
                ,new LinkedList<Point>(), new LinkedList<MySnakeV3>());

        return getDirectionToNextPoint(head, nextPointApplePath.get(1));

        //Analyze if snake reach apple and the tail will be reachable (emulate path to apple)
//        List<Point> nextPointTailPath = null;
//        if (null != nextPointApplePath) {
//            System.out.print("To apple + tail: ");
//            nextPointTailPath = startBSSBest(board, nextApple, mysnakeV3.getNextByTail(), nextPointApplePath, mysnakeV3.getBody());
//        }
//        if ((null != nextPointApplePath) && (null != nextPointTailPath)) {
//            If apple is reacheble
//            return getDirectionToNextPoint(head, nextPointApplePath.get(1));
//        }

        //Calculate the shortest path to Tail
//        System.out.print("To tail: ");
//        nextPointTailPath = startBSSBest(board, head, mysnakeV3.getNextByTail(), new LinkedList<Point>(), mysnakeV3.getBody());


//        System.out.println("Next apple: " + nextApple.toString());
//        System.out.println("Head      : " + head.toString());
//        System.out.println("Tail      : " + board.getMyTail().toString());
//        System.out.println("Next Step : " + nextStep.toString());
//        return Direction.UP.toString();
    }


}