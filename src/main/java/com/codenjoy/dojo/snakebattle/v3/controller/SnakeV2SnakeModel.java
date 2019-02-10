package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;

import java.util.List;

import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeUtilsV1.*;

public class SnakeV2SnakeModel {

    /**
     * Need to do:
     * Go to nearest Apple - DONE
     * Avoid obstacles - DONE
     * Build Snake body - DONE
     * BFS algorithm to one Point
     * BFS change direction on each step clockwise and vise versa
     * eat stones +10 points -3 size
     * eat Fury drug (eat stones and other snakes without body decrease)
     * eat gold (additional points)
     * eat apple +1 point - change priority for the rule
     * snakes incidence
     * snakes Flying and Fury levels
     * eat Flying drug (over stones and snakes)
     * create stones and walls from stones
     */


    public static String secondVersion(Board board, MySnakeV2 mySnakeV2) {
        Point head = board.getMe();
        if (mySnakeV2.getSize() > board.getMyBody().size()) {
            System.out.printf("Snake old size %s => %s%n", mySnakeV2.getSize(), board.getMyBody().size());
            System.out.println("System body: "+ board.getMyBody());
        }
        mySnakeV2.addToHead(head, board.getMyBody().size(), board.getMyTail().get(0));
//        System.out.println(board.toString());
        System.out.println("My snake   : " + mySnakeV2.getTail() + mySnakeV2.getBody() + mySnakeV2.getHead());


        List<Point> apples = board.getApples();
        Point nextApple = getNearestApple(head, apples);
        Point nextStep = buildPathNextStep(board, head, nextApple);

        System.out.println("Next apple: " + nextApple.toString());
//        System.out.println("Head      : " + head.toString());
//        System.out.println("Tail      : " + board.getMyTail().toString());
        System.out.println("Next Step : " + nextStep.toString());
        return getDirectionToNextPoint(head, nextStep);
    }


}