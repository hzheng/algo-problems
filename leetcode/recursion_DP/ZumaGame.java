import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC488: https://leetcode.com/problems/zuma-game/
//
// You have a row of balls on the table, colored red(R), yellow(Y), blue(B),
// green(G), and white(W). You also have several balls in your hand. Each time,
// you may choose a ball in your hand, and insert it into the row (including the
// leftmost place and rightmost place). Then, if there is a group of 3 or more
// balls in the same color touching, remove these balls. Keep doing this until
// no more balls can be removed.
// Find the minimal balls you have to insert to remove all the balls on the
// table. If you cannot remove all the balls, output -1.
// Note:
// You may assume that the initial row of balls on the table won’t have any 3 or
// more consecutive balls with the same color.
// The number of balls on the table won't exceed 20, and the string represents
// these balls is called "board" in the input.
// The number of balls in your hand won't exceed 5, and the string represents
// these balls is called "hand" in the input.
// Both input strings will be non-empty and only contain characters 'R','Y','B','G','W'.
public class ZumaGame {
    // DFS + Recursion + Hashtable
    // beats 72.73%(20 ms for 106 tests)
    public int findMinStep(String board, String hand) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : hand.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        int[] min = {Integer.MAX_VALUE};
        add(board, map, 0, 0, min);
        return min[0] == Integer.MAX_VALUE ? -1 : min[0];
    }

    private String toString(List<Ball> balls) {
        StringBuilder sb = new StringBuilder();
        for (Ball ball : balls) {
            for (int i = ball.count; i > 0; i--) {
                sb.append(ball.color);
            }
        }
        return sb.toString();
    }

    private List<Ball> toList(String board) {
        List<Ball> balls = new LinkedList<>();
        int len = board.length();
        char lastColor = 0;
        int stream = 1;
        for (int i = 0; i <= len; i++) {
            if (i == len || board.charAt(i) != lastColor) {
                if (i > 0) {
                    balls.add(new Ball(lastColor, stream));
                }
                if (i < len) {
                    lastColor = board.charAt(i);
                }
                stream = 1;
            } else {
                stream++;
            }
        }
        return balls;
    }

    private void add(String board, Map<Character, Integer> hand,
                     int index, int steps, int[] min) {
        if (steps >= min[0]) return;

        if (board.isEmpty()) {
            min[0] = Math.min(min[0], steps);
            return;
        }

        List<Ball> balls = toList(board);
        for (int i = index; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            int available = hand.getOrDefault(ball.color, 0);
            int need = 3 - ball.count;
            if (available < need) continue;

            hand.put(ball.color, available - need);
            List<Ball> tryBalls = toList(board);
            int next = reduce(tryBalls, i);
            add(toString(tryBalls), hand, next, steps + need, min);
            hand.put(ball.color, available); // backtracking
        }
    }

    private int reduce(List<Ball> balls, int index) {
        balls.remove(index);
        while (index > 0 && index < balls.size()) {
            if (balls.get(index - 1).color != balls.get(index).color) break;

            balls.get(index - 1).count += balls.get(index).count;
            balls.remove(index--);
            if (balls.get(index).count < 3) break;
            balls.remove(index);
        }
        return Math.max(0, index - 1);
    }

    class Ball {
        char color;
        int count;
        Ball(char color, int count) {
            this.color = color;
            this.count = count;
        }

        public String toString() {
            return "{" + color + "=" + count + "}";
        }
    }

    // DFS + Recursion + Hashtable
    // beats 85.75%(17 ms for 106 tests)
    public int findMinStep2(String board, String hand) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : hand.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        int[] min = {Integer.MAX_VALUE};
        add2(board, map, 0, 0, min);
        return min[0] == Integer.MAX_VALUE ? -1 : min[0];
    }

    private void add2(String board, Map<Character, Integer> hand,
                     int index, int steps, int[] min) {
        if (steps >= min[0]) return;

        if (board.isEmpty()) {
            min[0] = Math.min(min[0], steps);
            return;
        }
        char last = '\0';
        for (int i = 0, j = 0, len = board.length(); i < len; i++) {
            char c = board.charAt(i);
            if (c == last || j++ < index) continue;

            last = c;
            int count = 1;
            for (++i; i < len && board.charAt(i) == c; i++, count++) {}
            i--;
            int available = hand.getOrDefault(c, 0);
            int need = 3 - count;
            if (available < need) continue;

            hand.put(c, available - need);
            StringBuilder sb = new StringBuilder(board);
            reduce(sb, j - 1);
            add2(sb.toString(), hand, 0, steps + need, min);
            hand.put(c, available); // backtracking
        }
    }

    private void reduce(StringBuilder sb, int index) {
        char last = '\0';
        int i = 0;
        for (int j = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == last) continue;

            last = c;
            if (j++ < index) continue;
            do {
                sb.deleteCharAt(i);
            } while (i < sb.length() && sb.charAt(i) == c);
            break;
        }
        if (i < sb.length() && i > 0 && sb.charAt(i - 1) == sb.charAt(i)) {
            char c = sb.charAt(i);
            int j = i + 1;
            for (; j < sb.length() && sb.charAt(j) == c; j++) {}
            for (i -= 2; i >= 0 && sb.charAt(i) == c; i--) {}
            if (j - i > 3) reduce(sb, index - 1);
        }
    }

    void test(String board, String hand, int expected) {
        assertEquals(expected, findMinStep(board, hand));
        assertEquals(expected, findMinStep2(board, hand));
    }

    void test(String board, int index, String expected) {
        StringBuilder sb = new StringBuilder(board);
        reduce(sb, index);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void test() {
        test("BWYYBBYRRYWB", "BRBW", 4);
        test("RRWWRRW", "RR", 2);
        test("WRRBBW", "RB", -1);
        test("WWRRBBWW", "WRBRW", 2);
        test("G", "GGGGG", 2);
        test("RBYYBBRRB", "YRBGB", 3);
    }

    @Test
    public void testReduce() {
        test("BWYYBBYRRYWB", 5, "BWYYBBYYWB");
        test("BWYYBBYYRRYWB", 5, "BWYYBBWB");
        test("BWYYYBBYYRRYWB", 5, "BWYYYBBWB");
        test("BWYYBBYYRRYBB", 5, "BWYY");
        test("BWYYBBYYRRYBBY", 5, "BW");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ZumaGame");
    }
}
