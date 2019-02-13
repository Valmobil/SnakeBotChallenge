package com.codenjoy.dojo.snakebattle.v4.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.v4.client.Board;

import java.util.*;

public class BestPathV4 {
    private MySnakeV4 snake;
    private Integer score;
    private LinkedList<Point> path;
    private Board board;
    private HashSet<Point> visited;
    private SnakeListV4 otherSnakes;
    private Map<Integer, List<Point>> bestPaths;
    private HashSet<Point> foundedFruitfulPoints;
    private boolean tailIsReachable;
    private boolean alreadyUsed;

    public BestPathV4(MySnakeV4 snake, Integer score, LinkedList<Point> path, Board board, HashSet<Point> visited, SnakeListV4 otherSnakes, HashSet<Point> foundedFruitfulPoints, boolean tailIsReachable) {
        this.snake = snake;
        this.score = score;
        this.path = path;
        this.board = board;
        this.visited = visited;
        this.otherSnakes = otherSnakes;
        this.foundedFruitfulPoints = foundedFruitfulPoints;
        this.tailIsReachable = tailIsReachable;

    }

    public MySnakeV4 getSnake() {
        return snake;
    }

    public void setSnake(MySnakeV4 snake) {
        this.snake = snake;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LinkedList<Point> getPath() {
        return path;
    }

    public void setPath(LinkedList<Point> path) {
        this.path = path;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public HashSet<Point> getVisited() {
        return visited;
    }

    public void setVisited(HashSet<Point> visited) {
        this.visited = visited;
    }

    public SnakeListV4 getOtherSnakes() {
        return otherSnakes;
    }

    public void setOtherSnakes(SnakeListV4 otherSnakes) {
        this.otherSnakes = otherSnakes;
    }

    public Map<Integer, List<Point>> getBestPaths() {
        return bestPaths;
    }

    public void setBestPaths(Map<Integer, List<Point>> bestPaths) {
        this.bestPaths = bestPaths;
    }

    public boolean isTailIsReachable() {
        return tailIsReachable;
    }

    public void setTailIsReachable(boolean tailIsReachable) {
        this.tailIsReachable = tailIsReachable;
    }

    @Override
    public String toString() {
        return this.path + " Snake: " + this.snake;
    }

    public HashSet<Point> getFoundedFruitfulPoints() {
        return foundedFruitfulPoints;
    }

    public void setFoundedFruitfulPoints(HashSet<Point> foundedFruitfulPoints) {
        this.foundedFruitfulPoints = foundedFruitfulPoints;
    }

    public boolean isAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }
}
