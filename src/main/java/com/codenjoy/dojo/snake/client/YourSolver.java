package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.model.Elements;

import java.util.*;

public class YourSolver implements Solver<Board> {

    private Board board;

    @Override
    public String get(Board board) {
        this.board = board;

        Point head = board.getHead();
        List<Point> apples = board.getApples();

        if (head == null) {
            return Direction.UP.toString(); // Якщо змійка не знайдена
        }

        if (apples.isEmpty()) {
            return findSafeDirection(head).toString(); // Якщо яблук немає
        }

        // Знаходимо шлях до яблука за допомогою BFS
        Direction nextMove = bfsToApple(head, apples);
        if (nextMove != null) {
            return nextMove.toString();
        }

        // Якщо BFS не знаходить шлях, вибираємо безпечний напрямок
        return findSafeDirection(head).toString();
    }

    private Direction bfsToApple(Point head, List<Point> apples) {
        Queue<Node> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(new Node(head.getX(), head.getY(), null));

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Якщо дійшли до яблука
            for (Point apple : apples) {
                if (current.x == apple.getX() && current.y == apple.getY()) {
                    return reconstructDirection(current);
                }
            }

            // Додаємо сусідні клітинки до черги
            for (Direction direction : Direction.values()) {
                int nextX = current.x + direction.changeX(1);
                int nextY = current.y + direction.changeY(1);

                String key = nextX + "," + nextY; // Унікальний ключ для клітинки
                if (!visited.contains(key) && isSafe(nextX, nextY)) {
                    visited.add(key);
                    queue.add(new Node(nextX, nextY, direction, current));
                }
            }
        }

        return null; // Якщо шлях до яблука не знайдено
    }

    private boolean isSafe(int x, int y) {
        if (board.isOutOfField(x, y)) {
            return false; // Якщо точка за межами поля
        }

        // Дізнаємось, що знаходиться в клітинці
        Elements element = board.getAt(x, y);

        // Друк для відладки
        System.out.println("Element at (" + x + ", " + y + "): " + element);

        // Перевіряємо, чи клітинка порожня або містить яблуко
        return element == Elements.NONE || element == Elements.GOOD_APPLE; // Замініть GOOD_APPLE на правильну назву
    }

    private Direction findSafeDirection(Point head) {
        for (Direction direction : Direction.values()) {
            int nextX = head.getX() + direction.changeX(1);
            int nextY = head.getY() + direction.changeY(1);
            if (isSafe(nextX, nextY)) {
                return direction;
            }
        }
        return Direction.UP; // Якщо немає безпечного напряму
    }

    private Direction reconstructDirection(Node node) {
        while (node.previous != null && node.previous.previous != null) {
            node = node.previous;
        }
        return node.direction;
    }

    private static class Node {
        int x, y;
        Direction direction;
        Node previous;

        Node(int x, int y, Direction direction) {
            this(x, y, direction, null);
        }

        Node(int x, int y, Direction direction, Node previous) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.previous = previous;
        }
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                "http://178.128.203.65/codenjoy-contest/board/player/dv3h99hkp7mqdg3ad91n?code=4597280563432007708",
                new YourSolver(),
                new Board());
    }
}
