import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC544: https://leetcode.com/problems/output-contest-matches/
//
// During the NBA playoffs, we always arrange the rather strong team to play with
// the rather weak team, like make the rank 1 team play with the rank nth team,
// which is a good strategy to make the contest more interesting. Now, you're given
// n teams, you need to output their final contest matches in the form of a string.
public class FindContestMatches {
    // Deque
    // beats 37.50%(10 ms for 12 tests)
    public String findContestMatch(int n) {
        Deque<String> matches = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) {
            matches.offer(String.valueOf(i));
        }
        while (matches.size() > 1) {
            Deque<String> nextRound = new ArrayDeque<>();
            while (!matches.isEmpty()) {
                String first = matches.pollFirst();
                String second = matches.pollLast();
                nextRound.offer("(" + first + "," + second + ")");
            }
            matches = nextRound;
        }
        return matches.peek();
    }

    // Array
    // beats 91.93%(7 ms for 12 tests)
    public String findContestMatch2(int n) {
        String[] matches = new String[n];
        for (int i = 1; i <= n; i++) {
            matches[i - 1] = String.valueOf(i);
        }
        for (int i = n; i > 1; i /= 2) {
            for (int j = 0; j < i / 2; j++) {
                matches[j] = "(" + matches[j] + "," + matches[i - 1 - j] + ")";
            }
        }
        return matches[0];
    }

    // Recursion
    // beats 37.50%(10 ms for 12 tests)
    public String findContestMatch3(int n) {
        List<String> matches = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            matches.add(String.valueOf(i));
        }
        contestMatch(matches, n);
        return matches.get(0);
    }

    private void contestMatch(List<String> matches, int n) {
        if (n == 1) return;

        for (int i = 0, j = n - 1; i < j; i++, j--) {
            matches.set(i, "(" + matches.get(i) + "," + matches.get(j) + ")");
        }
        contestMatch(matches, n / 2);
    }

    void test(int n, String expected) {
        assertEquals(expected, findContestMatch(n));
        assertEquals(expected, findContestMatch2(n));
        assertEquals(expected, findContestMatch3(n));
    }

    @Test
    public void test() {
        test(2, "(1,2)");
        test(4, "((1,4),(2,3))");
        test(8, "(((1,8),(4,5)),((2,7),(3,6)))");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
