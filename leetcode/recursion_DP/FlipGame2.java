import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC294: https://leetcode.com/problems/flip-game-ii
//
// You are playing the following Flip Game with your friend: Given a string that
// contains only these two characters: + and -, you and your friend take turns
// to flip two consecutive "++" into "--". The game ends when a person can no
// longer make a move and therefore the other person will be the winner.
// Write a function to determine if the starting player can guarantee a win.
public class FlipGame2 {
    // DFS + Backtracking + Recursion
    // beats 72.40%(24 ms for 33 tests)
    public boolean canWin(String s) {
        return canWin(s.toCharArray());
    }

    private boolean canWin(char[] cs) {
        for (int i = 0; i < cs.length - 1; i++) {
            if (cs[i] == '+' && cs[i + 1] == '+') {
                cs[i] = cs[i + 1] = '-';
                boolean opponentWins = canWin(cs);
                cs[i] = cs[i + 1] = '+';
                if (!opponentWins) return true;
            }
        }
        return false;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // beats 88.02%(20 ms for 33 tests)
    public boolean canWin2(String s) {
        return canWin(s, new HashMap<>());
    }

    public boolean canWin(String s, Map<String, Boolean> memo) {
        Boolean res = memo.get(s);
        if (res != null) return res;

        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == '+' && s.charAt(i + 1) == '+') {
                // one '-' is enough
                String next = s.substring(0, i) + "-" + s.substring(i + 2);
                if (!canWin(next, memo)) {
                    memo.put(s, true);
                    return true;
                }
            }
        }
        memo.put(s, false);
        return false;
    }

    // TODO: Dynamic Programming(Bottom-Up) time complexity: O(N ^ 2)
    // https://discuss.leetcode.com/topic/27282/theory-matters-from-backtracking-128ms-to-dp-0ms

    void test(String s, boolean expected) {
        assertEquals(expected, canWin(s));
        assertEquals(expected, canWin2(s));
    }

    @Test
    public void test() {
        test("++++", true);
        test("+++++", false);
        test("+---++++", true);
        test("+---++++--++", true);
        test("++++------+---++++++-----+++--++-------++++", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FlipGame2");
    }
}
