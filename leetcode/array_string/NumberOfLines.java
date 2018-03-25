import org.junit.Test;
import static org.junit.Assert.*;

// LC806: https://leetcode.com/problems/number-of-lines-to-write-string/
//
// We are to write the letters of a given string S, from left to right into
// lines. Each line has maximum width 100 units, and if writing a letter would
// cause the width of the line to exceed 100 units, it is written on the next
// line. We are given an array widths, an array where widths[0] is the width of
// 'a', widths[1] is the width of 'b', ..., and widths[25] is the width of 'z'.
// Now answer two questions: how many lines have at least one character from S,
// and what is the width used by the last such line? Return your answer as an
// integer list of length 2.
public class NumberOfLines {
    // beats %(3 ms for 26 tests)
    public int[] numberOfLines(int[] widths, String S) {
        int lines = 1;
        int cur = 0;
        final int max = 100;
        for (char c : S.toCharArray()) {
            int w = widths[c - 'a'];
            cur += w;
            if (cur > max) {
                cur = w;
                lines++;
            }
        }
        return new int[] {lines, cur};
    }

    void test(int[] widths, String S, int expected1, int expected2) {
        int[] res = numberOfLines(widths, S);
        assertEquals(expected1, res[0]);
        assertEquals(expected2, res[1]);
    }

    @Test
    public void test() {
        test(new int[] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
                        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
             "abcdefghijklmnopqrstuvwxyz", 3, 60);
        test(new int[] {4, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
                        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
             "bbbcccdddaaa", 2, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
