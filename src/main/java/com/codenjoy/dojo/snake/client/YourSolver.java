package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.client.WebSocketRunner;

import java.util.*;

public class YourSolver implements Solver<Board> {
        private Dice dice;
        private Board board;

    @Override
    public String get(Board board) {
        this.board = board;

        Point head = board.getHead();
        Point apple = board.getApples().get(0);

        List<Point> obstacles = new ArrayList<>();
        obstacles.addAll(board.getSnake());
        obstacles.addAll(board.getWalls());
        obstacles.addAll(board.getStones());

        List<Point> path = findShortestPath(head, apple, obstacles);

        if (path != null && path.size() > 1) {
            Point nextPoint = path.get(1);
            return getDirection(head, nextPoint);
        }

        for (Direction direction : Direction.values()) {
            Point nextPoint = direction.change(head);
            if (!obstacles.contains(nextPoint)) {
                return direction.toString();
            }
        }

        return Direction.UP.toString();
    }

    private List<Point> findShortestPath(Point start, Point goal, List<Point> obstacles) {
        Queue<List<Point>> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();

        queue.add(Collections.singletonList(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            List<Point> path = queue.poll();
            Point current = path.get(path.size() - 1);

            if (current.equals(goal)) {
                return path;
            }

            for (Direction direction : Direction.values()) {
                Point next = direction.change(current);
                if (!visited.contains(next) && !obstacles.contains(next)) {
                    visited.add(next);
                    List<Point> newPath = new ArrayList<>(path);
                    newPath.add(next);
                    queue.add(newPath);
                }
            }
        }

        return null;
    }
    private String getDirection(Point head, Point nextPoint) {
        if (nextPoint.getX() > head.getX()) {
            return Direction.RIGHT.toString();
        } else if (nextPoint.getX() < head.getX()) {
            return Direction.LEFT.toString();
        } else if (nextPoint.getY() > head.getY()) {
            return Direction.UP.toString();
        } else if (nextPoint.getY() < head.getY()) {
            return Direction.DOWN.toString();
        }
        return Direction.UP.toString();
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                "http://178.128.203.65/codenjoy-contest/board/player/dv3h99hkp7mqdg3ad91n?code=4597280563432007708",
                new YourSolver(),
                new Board());
    }
}
