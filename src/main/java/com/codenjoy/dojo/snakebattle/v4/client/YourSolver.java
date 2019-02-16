package com.codenjoy.dojo.snakebattle.v4.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.snakebattle.v4.controller.Log;
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;

import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeV4Start.StartAppV4;


/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    private MySnakeV4 mySnake = new MySnakeV4();
    private SnakeListV4 othSnakes = new SnakeListV4();

    YourSolver(Dice dice) {
        this.dice = dice;
    }

    public static int currentRun = 0;


    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) {
            mySnake = new MySnakeV4();
            return "";
        }
        Log.printLog(board.toString(),0);
        return StartAppV4(board, mySnake, othSnakes);
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                "https://game3.epam-bot-challenge.com.ua/codenjoy-contest/board/player/6ndtvc6yvpd4w6zwmuer?code=6889565379839031161",
                // paste here board page url from browser after registration
                new YourSolver(new RandomDice()),
                new Board());
    }
}
