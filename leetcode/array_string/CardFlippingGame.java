import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC822: https://leetcode.com/problems/card-flipping-game/
//
// On a table are N cards, with a positive integer printed on the front and back
// of each card. We flip any number of cards, and after we choose one card.
// If the number X on the back of the chosen card is not on the front of any
// card, then this number X is good. What is the smallest number that is good?
// If no number is good, output 0.
// Note:
// 1 <= fronts.length == backs.length <= 1000.
// 1 <= fronts[i] <= 2000.
// 1 <= backs[i] <= 2000.
public class CardFlippingGame {
    // Sorted Set + Set
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats %(36 ms for 169 tests)
    public int flipgame(int[] fronts, int[] backs) {
        TreeSet<Integer> included = new TreeSet<>();
        Set<Integer> excluded = new HashSet<>();
        for (int i = fronts.length - 1; i >= 0; i--) {
            if (fronts[i] == backs[i]) {
                included.remove(fronts[i]);
                excluded.add(fronts[i]);
            } else {
                if (!excluded.contains(fronts[i])) {
                    included.add(fronts[i]);
                }
                if (!excluded.contains(backs[i])) {
                    included.add(backs[i]);
                }
            }
        }
        return included.isEmpty() ? 0 : included.iterator().next();
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // beats %(21 ms for 169 tests)
    public int flipgame2(int[] fronts, int[] backs) {
        Set<Integer> excluded = new HashSet<>();
        for (int i = fronts.length - 1; i >= 0; i--) {
            if (fronts[i] == backs[i]) {
                excluded.add(fronts[i]);
            }
        }
        int res = Integer.MAX_VALUE;
        for (int x : fronts) {
            if (!excluded.contains(x)) {
                res = Math.min(res, x);
            }
        }
        for (int x : backs) {
            if (!excluded.contains(x)) {
                res = Math.min(res, x);
            }
        }
        return res % Integer.MAX_VALUE;
    }

    // time complexity: O(N * MAX_NUM), space complexity: O(N)
    // beats %(30 ms for 169 tests)
    public int flipgame3(int[] fronts, int[] backs) {
        outer : for (int i = 1, n = fronts.length; i <= 2000; i++) {
            boolean found = false;
            for (int j = 0; j < n; j++) {
                if (fronts[j] == i && backs[j] == i) continue outer;

                found |= (backs[j] == i || fronts[j] == i);
            }
            if (found) return i;
        }
        return 0;
    }

    void test(int[] fronts, int[] backs, int expected) {
        assertEquals(expected, flipgame(fronts, backs));
        assertEquals(expected, flipgame2(fronts, backs));
        assertEquals(expected, flipgame3(fronts, backs));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 4, 4, 7}, new int[] {1, 3, 4, 1, 3}, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
