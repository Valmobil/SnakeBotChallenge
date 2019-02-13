package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;
import com.codenjoy.dojo.snakebattle.v4.model.BestPathV4;
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Log {
    public static boolean logIsEnabled = true;
    public static int logLevel = 1;
    public static int step = 0;


    public static void printLog(Board board, Queue<MySnakeV4> queueSnakes, HashSet<Point> visited, Queue<Point> queue, Queue<LinkedList<Point>> queuePath, Queue<Integer> queueLevel, SnakeListV4 otherSnakes, Map<Double, BestPathV4> bestPaths, HashSet<String> targets, int mode) {
        if (logIsEnabled) {
            System.out.println("Log: " + step++ + "**************************");
            System.out.println("Log: My snake->" + queueSnakes.peek());
            System.out.println("Log: Queue->" + queue.toString());
            System.out.println("Log: Path->" + queuePath.peek());
            System.out.println("Log: Target->" + targets);
            if (logLevel < 1) {
                System.out.println("Log: BestPath->" + bestPaths);
                System.out.println("Log: Visited->" + visited);
            }
//        System.out.println("Log: " + );
//        System.out.println("Log: " + );
//        System.out.println("Log: " + );
        }
    }

    public static void printLog(String s, int level) {
        // level = 0 - show all notices
        // level = 1.. - less details
        if (level >= logLevel) {
            if (logIsEnabled) {
                System.out.println("Log: " + s);
            }
        }
    }
}
