import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC353: https://leetcode.com/problems/design-snake-game
//
// Design a Snake game that is played on a device with screen size = width x height.
// The snake is initially positioned at the top left corner (0,0) with length = 1 unit.
// You are given a list of food's positions in row-column order. When a snake eats
// the food, its length and the game's score both increase by 1.
// Each food appears one by one on the screen. When a food does appear on the screen,
// it is guaranteed that it will not appear on a block occupied by the snake.
public class SnakeGame {
    interface ISnakeGame {
        /** Moves the snake.
           @param direction - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down
           @return The game's score after the move. Return -1 if game over.
           Game over when snake crosses the screen boundary or bites its body. */
        public int move(String direction);
    }

    // Queue + Deque
    // beats 96.76%(235 ms for 539 tests)
    static class SnakeGame1 implements ISnakeGame {
        private int score;
        private int width;
        private int height;
        private Deque<int[]> snake = new LinkedList<>();
        private Queue<int[]> foodQueue = new LinkedList<>();

        public SnakeGame1(int width, int height, int[][] food) {
            this.width = width;
            this.height = height;
            for (int[] f : food) {
                foodQueue.offer(f);
            }
            snake.add(new int[2]);
        }

        public int move(String direction) {
            // if (score < 0) return -1;
            int[] head = snake.peekFirst();
            int x = head[0];
            int y = head[1];
            switch (direction) {
            case "L": y--; break;
            case "R": y++; break;
            case "D": x++; break;
            case "U": x--; break;
            }
            if (x < 0 || x >= height || y < 0 || y >= width) return -1;

            int[] newHead = new int[] {x, y};
            if (!foodQueue.isEmpty() && Arrays.equals(newHead, foodQueue.peek())) {
                foodQueue.poll();
                snake.offerFirst(newHead);
                return ++score;
            }
            snake.pollLast();
            for (int[] pos : snake) {
                if (Arrays.equals(newHead, pos)) return -1;
            }
            snake.offerFirst(newHead);
            return score;
        }
    }

    // Queue + Deque + Set
    // beats 52.70%(282 ms for 539 tests)
    static class SnakeGame2 implements ISnakeGame {
        private Set<Integer> occupied = new HashSet<>();
        private Deque<Integer> snake = new LinkedList<>();
        private int score;
        private int[][] food;
        private int foodIndex;
        private int width;
        private int height;

        public SnakeGame2(int width, int height, int[][] food) {
            this.width = width;
            this.height = height;
            this.food = food;
            occupied.add(0);
            snake.offer(0);
        }

        public int move(String direction) {
            // if (score < 0) return -1;
            int head = snake.peekFirst();
            int headRow = head / width;
            int headCol = head % width;
            switch (direction) {
            case "U": headRow--; break;
            case "D": headRow++; break;
            case "L": headCol--; break;
            case "R": headCol++; break;
            }
            int newHead = headRow * width + headCol;
            occupied.remove(snake.peekLast());
            if (headRow < 0 || headRow == height || headCol < 0 || headCol == width
                || occupied.contains(newHead)) {
                return score = -1;
            }
            occupied.add(newHead);
            snake.offerFirst(newHead);
            if (foodIndex < food.length && headRow == food[foodIndex][0] && headCol == food[foodIndex][1]) {
                occupied.add(snake.peekLast()); // add back
                foodIndex++;
                return ++score;
            }
            snake.pollLast();
            return score;
        }
    }

    private void test1(ISnakeGame snake) {
        assertEquals(0, snake.move("R"));
        assertEquals(0, snake.move("D"));
        assertEquals(1, snake.move("R"));
        assertEquals(1, snake.move("U"));
        assertEquals(2, snake.move("L"));
        assertEquals(-1, snake.move("U"));
    }

    private void test2(ISnakeGame snake) {
        assertEquals(0, snake.move("D"));
        assertEquals(1, snake.move("D"));
        assertEquals(1, snake.move("R"));
        assertEquals(1, snake.move("U"));
        assertEquals(1, snake.move("U"));
        assertEquals(2, snake.move("L"));
        assertEquals(2, snake.move("D"));
        assertEquals(2, snake.move("R"));
        assertEquals(2, snake.move("R"));
        assertEquals(3, snake.move("U"));
        assertEquals(3, snake.move("L"));
        assertEquals(3, snake.move("D"));
    }

    private void test1(String className, int width, int height, int[][] food) {
        try {
            Class<?> clazz = Class.forName("SnakeGame$" + className);
            Constructor<?> ctor = clazz.getConstructor(int.class, int.class, int[][].class);
            test1((ISnakeGame)ctor.newInstance(new Object[] {width, height, food}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test1(int width, int height, int[][] food) {
        test1("SnakeGame1", width, height, food);
        test1("SnakeGame2", width, height, food);
    }

    private void test2(String className, int width, int height, int[][] food) {
        try {
            Class<?> clazz = Class.forName("SnakeGame$" + className);
            Constructor<?> ctor = clazz.getConstructor(int.class, int.class, int[][].class);
            test2((ISnakeGame)ctor.newInstance(new Object[] {width, height, food}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test2(int width, int height, int[][] food) {
        test2("SnakeGame1", width, height, food);
        test2("SnakeGame2", width, height, food);
    }

    @Test
    public void test() {
        test1(3, 2, new int[][] {{1, 2}, {0, 1}});
        test2(3, 3, new int[][] {{2, 0}, {0, 0}, {0, 2}, {2, 2}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SnakeGame");
    }
}
