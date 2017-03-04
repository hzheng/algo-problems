import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC293: https://leetcode.com/problems/flip-game
//
// You are playing the following Flip Game with your friend: Given a string that
// contains only these two characters: + and -, you and your friend take turns
// to flip two consecutive "++" into "--". The game ends when a person can no
// longer make a move and therefore the other person will be the winner.
// Write a function to compute all possible states of the string after one valid move.
public class FlipGame {
    // beats 35.17%(1 ms for 25 tests)
    public List<String> generatePossibleNextMoves(String s) {
        List<String> res = new ArrayList<>();
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length - 1; i++) {
            if (cs[i] == '+' && cs[i + 1] == '+') {
                cs[i] = cs[i + 1] = '-';
                res.add(new String(cs));
                cs[i] = cs[i + 1] = '+';
            }
        }
        return res;
    }

    // beats 35.17%(1 ms for 25 tests)
    public List<String> generatePossibleNextMoves2(String s) {
        List<String> res = new ArrayList<>();
        for (int i = -1; (i = s.indexOf("++", i + 1)) >= 0; ) {
            res.add(s.substring(0, i) + "--" + s.substring(i + 2));
        }
        return res;
    }

    void test(String s, String[] expected) {
        assertEquals(Arrays.asList(expected), generatePossibleNextMoves(s));
        assertEquals(Arrays.asList(expected), generatePossibleNextMoves2(s));
    }

    @Test
    public void test() {
        test("--", new String[] {});
        test("++++", new String[] {"--++", "+--+", "++--"});
        test("---+++-+++-+", new String[] {"-----+-+++-+","---+---+++-+","---+++---+-+","---+++-+---+"});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FlipGame");
    }
}
