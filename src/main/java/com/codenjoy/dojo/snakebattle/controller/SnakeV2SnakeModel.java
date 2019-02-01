package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.model.MySnake;

import java.util.List;

import static com.codenjoy.dojo.snakebattle.controller.Utils.*;

public class SnakeV2SnakeModel{

    /**Need to do:
     * BFS algorithm
     * eat stones +10 points -3 size
     * eat Fury drug (eat stones and other snakes without body decrease)
     * eat gold (additional points)
     * eat apple +1 point - change priority for the rule
     * snakes incidence
     * snakes Flying and Fury levels
     * eat Flying drug (over stones and snakes)
     * create stones and walls from stones
     * */


    public static String secondVersion(Board board, MySnake mySnake) {
        Point head = board.getMe();


        mySnake.addToHead(head, board.getMyBody().size(),board.getMyTail().get(0));
        System.out.println("My snake   : " +mySnake.getTail() +  mySnake.getBody() + mySnake.getHead());
        System.out.println("System body: "+ board.getMyBody());


        List<Point> apples = board.getApples();
        Point nextApple = getNearestApple(head, apples);
        Point nextStep = buildPathNextStep(board, head, nextApple);

        System.out.println("Next apple: " + nextApple.toString());
        System.out.println("Head      : " + head.toString());
        System.out.println("Next Step : " + nextStep.toString());
        return getDirectionToNextPoint(head, nextStep);
    }



}