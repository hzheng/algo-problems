import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC874: https://leetcode.com/problems/walking-robot-simulation/
//
// A robot on an infinite grid starts at point (0, 0) and faces north.  It can
// receive one of three possible types of commands:
// -2: turn left 90 degrees
// -1: turn right 90 degrees
// 1 <= x <= 9: move forward x units
// Some of the grid squares are obstacles. If the robot would try to move onto
// them, the robot stays on the previous grid square instead. Return the square
// of the maximum Euclidean distance that the robot will be from the origin.
public class RobotSimulation {
    // Set
    // beats 92.76%(22 ms for 62 tests)
    public int robotSim(int[] commands, int[][] obstacles) {
        int res = 0;
        int x = 0;
        int y = 0;
        int dx = 0;
        int dy = 1;
        Set<Long> os = new HashSet<>();
        for (int[] o : obstacles) {
            os.add(hash(o[0], o[1]));
        }
        for (int c : commands) {
            if (c == -1) {
                int tmp = dx;
                dx = dy;
                dy = -tmp;
            } else if (c == -2) {
                int tmp = dx;
                dx = -dy;
                dy = tmp;
            } else {
                for (int i = 0; i < c; i++) {
                    x += dx;
                    y += dy;
                    if (os.contains(hash(x, y))) {
                        x -= dx;
                        y -= dy;
                        break;
                    }
                    res = Math.max(x * x + y * y, res);
                }
            }
        }
        return res;
    }

    private long hash(long a, long b) {
        // return a + "," + b;
        return ((a + Integer.MAX_VALUE) << 32) | (b + Integer.MAX_VALUE);
    }

    // Set
    // beats 94.63%(21 ms for 62 tests)
    public int robotSim2(int[] commands, int[][] obstacles) {
        int[] dx = new int[]{0, 1, 0, -1};
        int[] dy = new int[]{1, 0, -1, 0};
        int x = 0;
        int y = 0;
        int dir = 0;
        Set<Long> os = new HashSet<>();
        for (int[] o : obstacles) {
            os.add(hash(o[0], o[1]));
        }
        int res = 0;
        for (int c : commands) {
            if (c == -2) {
                dir = (dir + 3) % 4;
            } else if (c == -1) {
                dir = (dir + 1) % 4;
            } else {
                for (int i = 0; i < c; i++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];
                    if (os.contains(hash(nx, ny))) break;

                    x = nx;
                    y = ny;
                    res = Math.max(res, x * x + y * y);
                }
            }
        }
        return res;
    }

    void test(int[] commands, int[][] obstacles, int expected) {
        assertEquals(expected, robotSim(commands, obstacles));
        assertEquals(expected, robotSim2(commands, obstacles));
    }

    @Test
    public void test() {
        test(new int[] { 4, -1, 3 }, new int[][] {}, 25);
        test(new int[] { 4, -1, 4, -2, 4 }, new int[][] { { 2, 4 } }, 65);
        test(new int[] { -2, 8, 5, 5, 6, 3, 1, 9, 8, 7, -1, 7, 3, 8, 3, -1, 2,
                         -1, -1, 3, 7, 6, -2, -2, 7, 3, 6, 4, -1,
                         -2, -1, 6, 2, 6, -1, 5, 6, -1, -1, 2, -2, 8, -2, -2,
                         -2, -2, -1, 8, 2, -1, -2, 3, -2, 2, 4, 4, -1, 7,
                         -1, -2, 1, 1, 4, 8, 1, 2, 6, 9, 1, 3, 9, 2, -2, 2, 1,
                         3, 7, 5, 7, 1, 3, 3, 5, -2, 6, 1, 3, 7, 7, 4, 6,
                         -1, 7, 8, 1, -2, 7, -1, -1, 3, -2, -2, 3, 9, 8, 8, 6,
                         -2, 5, 3, 5, 8, -1, 9, 3, 2, 5, 9, 2, 4, 5, 4, 6,
                         -2, -1, 6, 1, 2, 1, 3 },
             new int[][] { { 2, 4 }, { -35, -24 }, { 39, -23 }, { 14, 11 },
                           { 17, 21 }, { -17, 16 }, { 20, 54 },
                           { -30, 12 } },
             18133);
        test(new int[] { 2, -1, 8, -1, 6 }, 
             new int[][] { { 1, 5 }, { -5, -5 }, { 0, 4 }, { -1, -1 }, { 4, 5 },
                      { -5, -3 }, { -2, 1 }, { -2, -5 }, { 0, 5 }, { 0, -1 } },
             80);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
