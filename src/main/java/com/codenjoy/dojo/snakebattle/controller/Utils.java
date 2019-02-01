package com.codenjoy.dojo.snakebattle.controller;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snakebattle.client.Board;

import java.util.LinkedList;
import java.util.List;

class Utils {

    /** Find nearest element from list */
    static Point getNearestApple(Point head, List<Point> element) {
        double minDistance = Integer.MAX_VALUE;
        Point nearestPoint = null;
        for (Point p : element) {
            //Calculate distance
            double tempDistance = head.distance(p);
            if (minDistance > tempDistance) {
                minDistance = tempDistance;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }

    /** Generate direction for one next step to point */
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

    /** Check if nextPoint has no barriers - is available for next step*/
    static boolean checkIfAvailableForNextStep (Board board, Point nextPoint) {
        if (board.isBarrierAt(nextPoint.getX(),nextPoint.getY())) {
            System.out.println("Barier at: "+ nextPoint.toString());
        }
        return !board.isBarrierAt(nextPoint.getX(),nextPoint.getY());
    }

    /**Calculate next one step to Point  */
    static Point buildPathNextStep(Board board, Point startPoint, Point endPoint) {
        List<Point> list = new LinkedList<>();
        Point nextStep = new PointImpl();
        list.add(nextStep);
        if (startPoint.getX() < endPoint.getX()) {
            nextStep.setX(startPoint.getX()+1);
            nextStep.setY(startPoint.getY());
            if (checkIfAvailableForNextStep(board,nextStep)) {
                return nextStep;
            }
        }
        if (startPoint.getY() < endPoint.getY()) {
            nextStep.setX(startPoint.getX());
            nextStep.setY(startPoint.getY()+1);
            if (checkIfAvailableForNextStep(board,nextStep)) {
                return nextStep;
            }

        }
        if (startPoint.getX() > endPoint.getX()) {
            nextStep.setX(startPoint.getX()-1);
            nextStep.setY(startPoint.getY());
            if (checkIfAvailableForNextStep(board,nextStep)) {
                return nextStep;
            }
        }
        if (startPoint.getY() > endPoint.getY()) {
            nextStep.setX(startPoint.getX());
            nextStep.setY(startPoint.getY()-1);
            if (checkIfAvailableForNextStep(board,nextStep)) {
                return nextStep;
            }
        }
        return nextStep;
    }
}
