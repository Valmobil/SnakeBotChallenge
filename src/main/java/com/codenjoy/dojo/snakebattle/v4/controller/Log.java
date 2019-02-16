package com.codenjoy.dojo.snakebattle.v4.controller;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;
import com.codenjoy.dojo.snakebattle.v4.model.BestPathV4;
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Log {
    private static boolean logIsEnabled = false;
    private static int destination = 1;
    //0 - to screen
    //1 - to file
    private static int logLevel = 1;
    public static int step = 0;
    private static PrintWriter outToFile;

    static {
        // Instantiate a Date object
        DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        try {
            outToFile = new PrintWriter("LogFile" +(timeStampPattern.format(java.time.LocalDateTime.now())) + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void printLog(Board board, Queue<MySnakeV4> queueSnakes, HashSet<Point> visited, Queue<Point> queue, Queue<LinkedList<Point>> queuePath, Queue<Integer> queueLevel, SnakeListV4 otherSnakes, Map<Double, BestPathV4> bestPaths, HashSet<String> targets, int mode, int level) {

        if (logIsEnabled) {
            if (level >= logLevel) {
                System.out.println("Log: " + step++ + "**************************");
                System.out.println("Log: My snake->" + queueSnakes.peek());
                System.out.println("Log: Queue->" + queue.toString());
                System.out.println("Log: Path->" + queuePath.peek());
                System.out.println("Log: Target->" + targets);
                if (logLevel < 1) {
                    System.out.println("Log: BestPath->" + bestPaths);
                    System.out.println("Log: Visited->" + visited);
                }
            }
        }
        if (destination == 1) {
            if (logIsEnabled) {
                if (level >= logLevel) {
                    outToFile.println("Log: " + step++ + "**************************");
                    outToFile.println("Log: My snake->" + queueSnakes.peek());
                    outToFile.println("Log: Queue->" + queue.toString());
                    outToFile.println("Log: Path->" + queuePath.peek());
                    outToFile.println("Log: Target->" + targets);
                    if (logLevel < 1) {
                        outToFile.println("Log: BestPath->" + bestPaths);
                        outToFile.println("Log: Visited->" + visited);
                    }
                }
            }
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
        if (destination == 1) {
            if (logIsEnabled) {
                outToFile.println("Log: " + s);
            }
        }
    }
}
