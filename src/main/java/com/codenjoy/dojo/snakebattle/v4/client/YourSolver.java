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
import com.codenjoy.dojo.snakebattle.v4.model.MySnakeV4;
import com.codenjoy.dojo.snakebattle.v4.model.SnakeListV4;

import static com.codenjoy.dojo.snakebattle.v4.controller.SnakeV4QueueBFS.StartAppV4;


/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    private MySnakeV4 mySnakeV4 = new MySnakeV4();
    private SnakeListV4 othSnakes = new SnakeListV4();

    YourSolver(Dice dice) {
        this.dice = dice;
    }

    public static int currentRun = 0;


    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) {
//            mySnakeV2 = new MySnakeV2();
            mySnakeV4 = new MySnakeV4();
            return "";
        }
//        return firstVersion(board);
//        return secondVersion(board, mySnakeV2);
//        return thirdVersion(board, mySnakeV3, othSnakes);
        return StartAppV4(board, mySnakeV4, othSnakes);

    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                "https://game3.epam-bot-challenge.com.ua/codenjoy-contest/board/player/jyzt9po8lt2qeo1y45qp?code=5984052072816257893",
//                "https://snakebattle.tk/codenjoy-contest/board/player/val_mobil@mail.ru?code=7484974391366816317",
                // paste here board page url from browser after registration
//                "https://game2.epam-bot-challenge.com.ua/codenjoy-contest/board/player/valmobil@gmail.com?code=2039282413817115302",
//                "https://game3.epam-bot-challenge.com.ua/codenjoy-contest/board/player/valmobil@gmail.com?code=2039282413817115302",
                new YourSolver(new RandomDice()),
                new Board());
    }
}
