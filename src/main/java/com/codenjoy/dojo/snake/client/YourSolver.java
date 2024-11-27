package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.model.Elements;

import java.util.List;

public class YourSolver implements Solver<Board> {

    @Override
    public String get(Board board) {
        if (board.getHead() == null) {
            System.out.println("Snake is not present on the board. Moving UP by default.");
            return Direction.UP.toString();
        }

        LeeAlgorithmSnake.Point head = new LeeAlgorithmSnake.Point(board.getHead().getX(), board.getHead().getY());
        System.out.println("Head position: " + head.x + ", " + head.y);

        List<Point> apples = board.getApples();
        if (apples.isEmpty()) {
            System.out.println("No apples found on the board.");
            return findSafeDirection(board, head).toString();
        }

        LeeAlgorithmSnake.Point apple = new LeeAlgorithmSnake.Point(apples.get(0).getX(), apples.get(0).getY());
        System.out.println("Nearest apple position: " + apple.x + ", " + apple.y);

        Direction nextMove = LeeAlgorithmSnake.getNextDirection(board, head, apple);
        if (nextMove == null) {
            System.out.println("No valid path to apple. Searching for safe direction.");
            return findSafeDirection(board, head).toString();
        }

        // Перевірка, чи наступний напрямок безпечний
        if (!isSafe(board, head.x + nextMove.changeX(1), head.y + nextMove.changeY(1))) {
            System.out.println("Next move leads to danger. Searching for safe direction.");
            return findSafeDirection(board, head).toString();
        }

        System.out.println("Next move: " + nextMove);
        return nextMove.toString();
    }

    private Direction findSafeDirection(Board board, LeeAlgorithmSnake.Point head) {
        for (Direction direction : Direction.values()) {
            int nextX = head.x + direction.changeX(1);
            int nextY = head.y + direction.changeY(1);

            if (isSafe(board, nextX, nextY)) {
                System.out.println("Safe direction found: " + direction);
                return direction;
            }
        }

        System.out.println("No safe direction found. Moving UP by default.");
        return Direction.UP;
    }


    private boolean isSafe(Board board, int x, int y) {
        if (board.isOutOfField(x, y)) {
            System.out.println("Position (" + x + ", " + y + ") is out of field.");
            return false;
        }

        Elements element = board.getAt(x, y);
        System.out.println("Position (" + x + ", " + y + ") contains: " + element);

        return element == Elements.NONE || element == Elements.GOOD_APPLE;
    }


    public static void main(String[] args) {
        WebSocketRunner.runClient(
                "http://178.128.203.65/codenjoy-contest/board/player/dv3h99hkp7mqdg3ad91n?code=4597280563432007708",
                new YourSolver(),
                new Board());
    }
}
