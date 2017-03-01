import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC277: https://leetcode.com/problems/find-the-celebrity/
//
// Suppose you are at a party with n people and among them, there may exist one
// celebrity. A celebrity is that all the other n - 1 people know him but he does
// not know any of them. Now you want to find out who the celebrity is. The only
// thing you are allowed to do is to ask questions like: "Hi, A. Do you know B?"
// to get information of whether A knows B. You need to find out the celebrity
// by asking as few questions as possible(in the asymptotic sense).
// You are given a helper function bool knows(a, b) which tells you whether A
// knows B. Implement a function int findCelebrity(n), your function should
// minimize the number of calls to knows.
// Note: There will be exactly one celebrity if he is in the party. Return the
// celebrity's label. If there is no celebrity, return -1.
public class FindCelebrity {
    private int[][] relations;

    boolean knows(int a, int b) { //  helper method
        return a == b || relations[a][b] == 1;
    }

    // beats 85.85%(13 ms for 171 tests)
    public int findCelebrity(int n) {
        int candidate = 0;
        int[][] relationMatrix = new int[n][n];
        for (int i = 1; i < n; i++) {
            if (knows(candidate, i)) {
                relationMatrix[candidate][i] = 1;
                candidate = i;
            } else {
                relationMatrix[candidate][i] = -1;
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == candidate) continue;
            if (relationMatrix[candidate][i] == 0 && knows(candidate, i)) return -1;
            if (relationMatrix[i][candidate] == 0 && !knows(i, candidate)) return -1;
        }
        return candidate;
    }

    // beats 61.51%(15 ms for 171 tests)
    public int findCelebrity2(int n) {
        int candidate = 0;
        for (int i = 1; i < n; i++) {
            if (knows(candidate, i)) {
                candidate = i;
            }
        }
        for (int i = 0; i < n; i++) { // may ask some repeated questions
            if (i != candidate && (knows(candidate, i) || !knows(i, candidate))) return -1;
        }
        return candidate;
    }

    // beats 61.51%(15 ms for 171 tests)
    public int findCelebrity2_2(int n) {
        int candidate = 0;
        for (int i = 1; i < n; i++) {
            if (knows(candidate, i)) {
                candidate = i;
            }
        }
        for (int i = 0; i < n; i++) {
            if (i < candidate && knows(candidate, i) || !knows(i, candidate)) return -1;
            if (i > candidate && !knows(i, candidate)) return -1;
        }
        return candidate;
    }

    // beats 15.42%(23 ms for 171 tests)
    public int findCelebrity2_3(int n) {
        int candidate = 0;
        for (int i = 1; i < n; i++) {
            if (knows(candidate, i)) {
                candidate = i;
            }
        }
        for (int i = 0; i < candidate; i++) {
            if (knows(candidate, i)) return -1;
        }
        for (int i = 0; i < n; i++) {
            if (i != candidate && !knows(i, candidate)) return -1;
        }
        return candidate;
    }

    void test(int[][] relations, int n, int expected) {
        this.relations = relations;
        assertEquals(expected, findCelebrity(n));
        assertEquals(expected, findCelebrity2(n));
        assertEquals(expected, findCelebrity2_2(n));
        assertEquals(expected, findCelebrity2_3(n));
    }

    @Test
    public void test() {
        test(new int[][] { {0, 0}, {0, 0}}, 2, -1);
        test(new int[][] { {0, 1, 1}, {0, 0, 0}, {0, 1, 0}}, 3, 1);
        test(new int[][] { {0, 1, 1}, {0, 0, 1}, {0, 1, 0}}, 3, -1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindCelebrity");
    }
}
