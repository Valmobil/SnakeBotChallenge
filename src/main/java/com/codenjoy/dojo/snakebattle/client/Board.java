package com.codenjoy.dojo.snakebattle.client;

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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.model.Elements;

import java.util.List;

import static com.codenjoy.dojo.snakebattle.model.Elements.*;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, WALL, START_FLOOR, ENEMY_HEAD_SLEEP, ENEMY_TAIL_INACTIVE, TAIL_INACTIVE);
    }

    public boolean isAvailableForNormalSnake(Point point) {
        return isAt(point, NONE, APPLE, STONE, FLYING_PILL, FURY_PILL,
                TAIL_END_DOWN, TAIL_END_LEFT, TAIL_END_UP, TAIL_END_RIGHT, TAIL_INACTIVE,
                // змейки противников
                ENEMY_HEAD_DOWN, ENEMY_HEAD_LEFT, ENEMY_HEAD_RIGHT, ENEMY_HEAD_UP,
                ENEMY_HEAD_DEAD,ENEMY_HEAD_EVIL,ENEMY_HEAD_FLY, ENEMY_HEAD_SLEEP,
                // хвосты змеек противников
                ENEMY_TAIL_END_DOWN, ENEMY_TAIL_END_LEFT, ENEMY_TAIL_END_UP, ENEMY_TAIL_END_RIGHT,
                ENEMY_TAIL_INACTIVE
                );
    }


    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    public Point getMe() {
        return getMyHead().get(0);
    }

    public boolean isGameOver() {
        return getMyHead().isEmpty();
    }

    public List<Point> getMyWalls() {
        return get(WALL);
    }

    public List<Point> getMyStones() {
        return get(STONE);
    }

    private List<Point> getMyHead() {
        return get(HEAD_DOWN, HEAD_LEFT, HEAD_RIGHT, HEAD_UP, HEAD_SLEEP, HEAD_EVIL, HEAD_FLY);
    }

    public List<Point> getMyBody() {
        return get(Elements.BODY_HORIZONTAL, Elements.BODY_LEFT_DOWN, Elements.BODY_LEFT_UP, Elements.BODY_RIGHT_DOWN, Elements.BODY_RIGHT_UP, Elements.BODY_VERTICAL);
    }

    public List<Point> getMyTail() {
        return get(Elements.TAIL_INACTIVE, Elements.TAIL_END_DOWN, Elements.TAIL_END_LEFT, Elements.TAIL_END_RIGHT, Elements.TAIL_END_UP);
    }

    public List<Point> getApples() {
        return get(APPLE);
    }

    public boolean isStoneAt(int x, int y) {
        return isAt(x, y, STONE);
    }

}
