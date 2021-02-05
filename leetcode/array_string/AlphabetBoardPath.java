import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

// LC1138: https://leetcode.com/problems/alphabet-board-path/
//
// On an alphabet board, we start at position (0, 0), corresponding to character board[0][0].
// Here, board = ["abcde", "fghij", "klmno", "pqrst", "uvwxy", "z"], as shown in the diagram below.
// We may make the following moves:
// 'U' moves our position up one row, if the position exists on the board;
// 'D' moves our position down one row, if the position exists on the board;
// 'L' moves our position left one column, if the position exists on the board;
// 'R' moves our position right one column, if the position exists on the board;
// '!' adds the character board[r][c] at our current position (r, c) to the answer.
// (Here, the only positions that exist on the board are positions with letters on them.)
// Return a sequence of moves that makes our answer equal to target in the minimum number of moves.
// You may return any path that does so.
//
// Constraints:
// 1 <= target.length <= 100
// target consists only of English lowercase letters.
public class AlphabetBoardPath {
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.9 MB(89.82%) for 61 tests
    public String alphabetBoardPath(String target) {
        StringBuilder sb = new StringBuilder();
        char cur = 'a';
        for (char t : target.toCharArray()) {
            walk(cur, t, sb);
            cur = t;
        }
        return sb.toString();
    }

    // time complexity: O(N), space complexity: O(N)
    // 9 ms(12.43%), 39.6 MB(6.92%) for 61 tests
    private void walk(char src, char target, StringBuilder sb) {
        int srcCol = (src - 'a') % 5;
        int tgtCol = (target - 'a') % 5;
        for (; srcCol > tgtCol; srcCol--) {
            sb.append("L");
        }
        int srcRow = (src - 'a') / 5;
        int tgtRow = (target - 'a') / 5;
        for (; srcRow > tgtRow; srcRow--) {
            sb.append("U");
        }
        for (; srcCol < tgtCol; srcCol++) {
            sb.append("R");
        }
        for (; srcRow < tgtRow; srcRow++) {
            sb.append("D");
        }
        sb.append("!");
    }

    public String alphabetBoardPath2(String target) {
        int x = 0;
        int y = 0;
        StringBuilder sb = new StringBuilder();
        for (char c : target.toCharArray()) {
            int x1 = (c - 'a') % 5;
            int y1 = (c - 'a') / 5;
            sb.append(String.join("", Collections.nCopies(Math.max(0, x - x1), "L")))
              .append(String.join("", Collections.nCopies(Math.max(0, y - y1), "U")))
              .append(String.join("", Collections.nCopies(Math.max(0, x1 - x), "R")))
              .append(String.join("", Collections.nCopies(Math.max(0, y1 - y), "D"))).append("!");
            x = x1;
            y = y1;
        }
        return sb.toString();
    }

    private void test(String target, String... expected) {
        assertThat(alphabetBoardPath(target), in(expected));
        assertThat(alphabetBoardPath2(target), in(expected));
    }

    @Test public void test() {
        test("leet", "DDR!UURRR!!DDD!", "RDD!UURRR!!DDD!");
        test("code", "RR!DDRR!UUL!R!", "RR!RRDD!LUU!R!");
        test("zdz", "DDDDD!UUUUURRR!DDDDLLLD!", "DDDDD!UUUUURRR!LLLDDDDD!");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
