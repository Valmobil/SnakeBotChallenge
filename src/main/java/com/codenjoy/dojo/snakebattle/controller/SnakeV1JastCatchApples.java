package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.client.Board;

import java.util.List;

import static com.codenjoy.dojo.snakebattle.controller.SnakeUtilsV1.*;

public class SnakeV1JastCatchApples {

    public static String firstVersion(Board board) {
        Point head = board.getMe();
        List<Point> apples = board.getApples();
        Point nextApple = getNearestApple(head, apples);
        Point nextStep = buildPathNextStep(board, head, nextApple);

        System.out.println("Next apple: " + nextApple.toString());
        System.out.println("Head      : " + head.toString());
        System.out.println("Next Step : " + nextStep.toString());
        return getDirectionToNextPoint(head, nextStep);
    }



}
