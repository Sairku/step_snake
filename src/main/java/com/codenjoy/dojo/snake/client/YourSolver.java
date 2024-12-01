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
        List<Point> barriers = board.getBarriers();

        if (apples.isEmpty()) {
            return getSafeDirection(head, barriers, Direction.UP); // Якщо яблук немає, обираємо безпечний напрямок
        }

        Point apple = apples.get(0); // Беремо перше яблуко
        Direction direction = calculateDirection(head, apple, barriers);

        return direction != null ? direction.toString() : getSafeDirection(head, barriers, Direction.UP);
    }

    private Direction calculateDirection(Point from, Point to, List<Point> barriers) {
        // Перевіряємо можливість руху у всіх напрямках
        if (from.getX() < to.getX() && isSafe(from.getX() + 1, from.getY(), barriers)) {
            return Direction.RIGHT;
        }
        if (from.getX() > to.getX() && isSafe(from.getX() - 1, from.getY(), barriers)) {
            return Direction.LEFT;
        }
        if (from.getY() < to.getY() && isSafe(from.getX(), from.getY() + 1, barriers)) {
            return Direction.UP;
        }
        if (from.getY() > to.getY() && isSafe(from.getX(), from.getY() - 1, barriers)) {
            return Direction.DOWN;
        }
        return null; // Якщо немає безпечного напрямку
    }

    private boolean isSafe(int x, int y, List<Point> barriers) {
        for (Point barrier : barriers) {
            if (barrier.getX() == x && barrier.getY() == y) {
                return false;
            }
        }
        return true;
    }

    private String getSafeDirection(Point head, List<Point> barriers, Direction defaultDirection) {
        // Спробуємо рухатись у безпечному напрямку
        for (Direction direction : Direction.values()) {
            int newX = head.getX() + direction.changeX(1);
            int newY = head.getY() + direction.changeY(1);
            if (isSafe(newX, newY, barriers)) {
                return direction.toString();
            }
        }
        return defaultDirection.toString(); // Якщо безпечного напрямку немає, обираємо за замовчуванням
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                "http://178.128.203.65/codenjoy-contest/board/player/dv3h99hkp7mqdg3ad91n?code=4597280563432007708",
                new YourSolver(),
                new Board());
    }
}
