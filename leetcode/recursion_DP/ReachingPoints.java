import org.junit.Test;
import static org.junit.Assert.*;

// LC780: https://leetcode.com/problems/reaching-points/
//
// A move consists of taking a point (x, y) and transforming it to either
// (x, x+y) or (x+y, y). Given a starting point (sx, sy) and a target point
// (tx, ty), return True if and only if a sequence of moves exists to transform
// the point (sx, sy) to (tx, ty). Otherwise, return False.
public class ReachingPoints {
    // Recursion
    // time complexity: O(log(max(tx,ty))
    // beats %(16 ms for 189 tests)
    public boolean reachingPoints(int sx, int sy, int tx, int ty) {
        if (sx > tx || sy > ty) return false;

        if (sx == tx) return (ty - sy) % tx == 0;

        if (sy == ty) return (tx - sx) % ty == 0;

        return (tx >= ty) ? reachingPoints(sx, sy, tx % ty, ty)
               : reachingPoints(sx, sy, tx, ty % tx);
    }

    // time complexity: O(log(max(tx,ty))
    // beats %(14 ms for 189 tests)
    public boolean reachingPoints2(int sx, int sy, int tx, int ty) {
        while (true) {
            if (sx > tx || sy > ty) return false;

            if (sx == tx) return (ty - sy) % tx == 0;

            if (sy == ty) return (tx - sx) % ty == 0;

            if (tx >= ty) {
                tx %= ty;
            } else {
                ty %= tx;
            }
        }
    }

    // time complexity: O(log(max(tx,ty))
    // beats %(14 ms for 189 tests)
    public boolean reachingPoints3(int sx, int sy, int tx, int ty) {
        while (tx >= sx && ty >= sy && tx != ty) {
            if (tx > ty) {
                if (ty > sy) {
                    tx %= ty;
                } else return (tx - sx) % ty == 0;
            } else {
                if (tx > sx) {
                    ty %= tx;
                } else return (ty - sy) % tx == 0;
            }
        }
        return (tx == sx && ty == sy);
    }

    // time complexity: O(log(max(tx,ty))
    // beats %(15 ms for 189 tests)
    public boolean reachingPoints4(int sx, int sy, int tx, int ty) {
        while (sx < tx && sy < ty) {
            if (tx < ty) {
                ty %= tx;
            } else {
                tx %= ty;
            }
        }
        return sx == tx && (ty - sy) % sx == 0
               || sy == ty && (tx - sx) % sy == 0;
    }

    void test(int sx, int sy, int tx, int ty, boolean expected) {
        assertEquals(expected, reachingPoints(sx, sy, tx, ty));
        assertEquals(expected, reachingPoints2(sx, sy, tx, ty));
        assertEquals(expected, reachingPoints3(sx, sy, tx, ty));
        assertEquals(expected, reachingPoints4(sx, sy, tx, ty));
    }

    @Test
    public void test() {
        test(1, 1, 3, 5, true);
        test(1, 1, 2, 2, false);
        test(1, 1, 1, 1, true);
        test(9, 10, 9, 19, true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
