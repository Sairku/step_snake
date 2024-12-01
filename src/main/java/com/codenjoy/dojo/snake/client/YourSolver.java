package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.client.WebSocketRunner;

import java.util.List;

public class YourSolver implements Solver<Board> {

    @Override
    public String get(Board board) {
        if (board.isGameOver()) {
            return Direction.UP.toString(); // Якщо гра закінчилась, обираємо будь-який напрямок
        }

        Point head = board.getHead();
        List<Point> apples = board.getApples();

        if (apples.isEmpty()) {
            return Direction.UP.toString(); // Якщо яблук немає, змійка рухається вгору
        }

        Point apple = apples.get(0); // Беремо перше яблуко з списку
        Direction direction = calculateDirection(head, apple);

        return direction != null ? direction.toString() : Direction.UP.toString();
    }

    private Direction calculateDirection(Point from, Point to) {
        if (from.getX() < to.getX()) {
            return Direction.RIGHT;
        }
        if (from.getX() > to.getX()) {
            return Direction.LEFT;
        }
        if (from.getY() < to.getY()) {
            return Direction.UP;
        }
        if (from.getY() > to.getY()) {
            return Direction.DOWN;
        }
        return null;
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                "http://178.128.203.65/codenjoy-contest/board/player/dv3h99hkp7mqdg3ad91n?code=4597280563432007708",
                new YourSolver(),
                new Board());
    }
}
