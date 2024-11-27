package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.snake.model.Elements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class LeeAlgorithmSnake {

    private static final int[][] directions = {
            {0, 1},   // right
            {1, 0},   // down
            {0, -1},  // left
            {-1, 0}   // up
    };

    public static class Point {
        public int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static boolean isValid(Board board, int x, int y) {
        if (board.isOutOfField(x, y)) {
            System.out.println("Position (" + x + ", " + y + ") is out of field.");
            return false;
        }

        Elements element = board.getAt(x, y);
        System.out.println("Position (" + x + ", " + y + ") contains: " + element);

        // Перевіряємо, чи клітинка вільна або містить яблуко
        boolean isAccessible = element == Elements.NONE || element == Elements.GOOD_APPLE;

        // Додатково перевіряємо, чи клітинка не є частиною змійки
        if (element.toString().startsWith("HEAD") || element.toString().startsWith("TAIL")) {
            System.out.println("Position (" + x + ", " + y + ") is part of the snake.");
            return false;
        }

        if (!isAccessible) {
            System.out.println("Position (" + x + ", " + y + ") is not accessible.");
        }

        return isAccessible;
    }




    public static Direction getNextDirection(Board board, Point head, Point apple) {
        int rows = board.size();
        int columns = board.size();
        int[][] distance = new int[rows][columns];
        for (int[] row : distance) Arrays.fill(row, -1);

        Queue<Point> queue = new LinkedList<>();
        queue.add(head);
        distance[head.x][head.y] = 0;

        System.out.println("Starting BFS from head: " + head.x + ", " + head.y);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            System.out.println("Processing point: " + current.x + ", " + current.y);

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (isValid(board, newX, newY) && distance[newX][newY] == -1) {
                    queue.add(new Point(newX, newY));
                    distance[newX][newY] = distance[current.x][current.y] + 1;
                    System.out.println("Added to queue: " + newX + ", " + newY);

                    if (newX == apple.x && newY == apple.y) {
                        System.out.println("Path to apple found!");
                        return reconstructDirection(distance, head, apple);
                    }
                }
            }
        }

        System.out.println("No path to apple found.");
        return null; // Шлях не знайдено
    }

    private static Direction reconstructDirection(int[][] distance, Point head, Point apple) {
        Point current = apple;
        System.out.println("Reconstructing path from apple to head...");

        // Рухаємося від яблука назад до голови
        while (distance[current.x][current.y] > 1) {
            for (int[] dir : directions) {
                int prevX = current.x - dir[0];
                int prevY = current.y - dir[1];
                if (prevX >= 0 && prevX < distance.length && prevY >= 0 && prevY < distance[0].length
                        && distance[prevX][prevY] == distance[current.x][current.y] - 1) {
                    current = new Point(prevX, prevY);
                    break;
                }
            }
        }

        System.out.println("Next step from head is: " + current.x + ", " + current.y);

        // Визначаємо напрямок руху від голови до наступної клітинки
        if (current.x == head.x && current.y == head.y - 1) {
            return Direction.LEFT;
        } else if (current.x == head.x && current.y == head.y + 1) {
            return Direction.RIGHT;
        } else if (current.x == head.x - 1 && current.y == head.y) {
            return Direction.UP;
        } else if (current.x == head.x + 1 && current.y == head.y) {
            return Direction.DOWN;
        }

        // Якщо напрямок не знайдено
        System.out.println("Could not determine direction from head to next step.");
        System.out.println("Head position: " + head.x + ", " + head.y);
        System.out.println("Reconstructed next step: " + current.x + ", " + current.y);

        return null;
    }






    // Метод для друку шляху
    private static void printPath(int[][] distance, Point start, Point target) {
        System.out.println("Tracing path from apple to head:");
        Point current = target;
        while (distance[current.x][current.y] > 0) {
            System.out.println("Step: " + current.x + ", " + current.y);
            for (int[] dir : directions) {
                int prevX = current.x - dir[0];
                int prevY = current.y - dir[1];
                if (prevX >= 0 && prevX < distance.length && prevY >= 0 && prevY < distance[0].length
                        && distance[prevX][prevY] == distance[current.x][current.y] - 1) {
                    current = new Point(prevX, prevY);
                    break;
                }
            }
        }
    }

}
