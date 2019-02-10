package com.codenjoy.dojo.snakebattle.v4.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;

import java.util.*;

public class bestPathV4 {
    private MySnakeV4 snake;
    private Integer distance;
    private Integer score;
    private LinkedList<Point> path;
    private Board board;
    private Queue<MySnakeV4> queueSnakes;
    private HashSet<Point> visited;
    private SnakeListV4 otherSnakes;
    private Map<Integer, List<Point>> bestPaths;


}
