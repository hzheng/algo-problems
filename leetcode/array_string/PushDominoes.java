import org.junit.Test;
import static org.junit.Assert.*;

// LC838: https://leetcode.com/problems/push-dominoes/
//
// There are N dominoes in a line, and we place each domino vertically upright.
// In the beginning, we simultaneously push some of the dominoes either to the
// left or to the right. After each second, each domino that is falling to the 
// left pushes the adjacent domino on the left. Similarly, the dominoes falling
// to the right push their adjacent dominoes standing on the right. When a 
// vertical domino has dominoes falling on it from both sides, it stays still.
// We will consider that a falling domino expends no additional force to a 
// falling or already fallen domino. Given a string "S" representing the initial
// state. S[i] = 'L', if the i-th domino has been pushed to the left; S[i] = 'R'
// if the i-th domino has been pushed to the right; S[i] = '.', if the i-th 
// domino has not been pushed. Return a string representing the final state. 
public class PushDominoes {
    // time complexity: O(N), space complexity: O(N)
    // beats %(19 ms for 36 tests)
    public String pushDominoes(String dominoes) {
        int n = dominoes.length();
        int[] leftFall = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            switch (dominoes.charAt(i)) {
            case 'L':
                leftFall[i] = i;
                break;
            case '.':
                leftFall[i] = leftFall[i + 1];
            }
        }
        char[] res = new char[n];
        for (int i = 0, right = -1; i < n; i++) {
            if ((res[i] = dominoes.charAt(i)) != '.') {
                right = (res[i] == 'R') ? i : -1;
            } else {
                int left = leftFall[i + 1];
                if (right >= 0 && (left == 0 || (i - right < left - i))) {
                    res[i] = 'R';
                } else if (left > 0 && (right < 0 || (i - right > left - i))) {
                    res[i] = 'L';
                }
            }
        }
        return String.valueOf(res);
    }

    // time complexity: O(N), space complexity: O(N)
    // beats %(24 ms for 36 tests)
    public String pushDominoes2(String dominoes) {
        int n = dominoes.length();
        int[] indices = new int[n + 2];
        char[] symbols = new char[n + 2];
        int len = 1;
        indices[0] = -1;
        symbols[0] = 'L';
        for (int i = 0; i < n; i++) {
            if (dominoes.charAt(i) != '.') {
                indices[len] = i;
                symbols[len++] = dominoes.charAt(i);
            }
        }
        indices[len] = n;
        symbols[len] = 'R';
        char[] res = dominoes.toCharArray();
        for (int index = 0; index < len; index++) {
            int i = indices[index];
            int j = indices[index + 1];
            char x = symbols[index];
            char y = symbols[index + 1];
            if (x == y) { // L...L or R...R
                for (int k = i + 1; k < j; k++) {
                    res[k] = x;
                }
            } else if (x > y) { // R...L
                for (int k = i + 1; k < j; k++) {
                    res[k] = (k - i == j - k) ? '.' : k - i < j - k ? 'R' : 'L';
                }
            }
        }
        return String.valueOf(res);
    }

    // time complexity: O(N), space complexity: O(N)
    // beats %(30 ms for 36 tests)
    public String pushDominoes3(String dominoes) {
        int n = dominoes.length();
        int[] forces = new int[n];
        for (int i = 0, force = 0; i < n; forces[i++] += force) {
            switch (dominoes.charAt(i)) {
            case 'R':
                force = n;
                break;
            case 'L':
                force = 0;
                break;
            default:
                force = Math.max(force - 1, 0);
            }
        }
        for (int i = n - 1, force = 0; i >= 0; forces[i--] -= force) {
            switch (dominoes.charAt(i)) {
            case 'L':
                force = n;
                break;
            case 'R':
                force = 0;
                break;
            default:
                force = Math.max(force - 1, 0);
            }
        }
        StringBuilder res = new StringBuilder();
        for (int f : forces) {
            res.append(f > 0 ? 'R' : f < 0 ? 'L' : '.');
        }
        return res.toString();
    }

    void test(String dominoes, String expected) {
        assertEquals(expected, pushDominoes(dominoes));
        assertEquals(expected, pushDominoes2(dominoes));
        assertEquals(expected, pushDominoes3(dominoes));
    }

    @Test
    public void test() {
        test("..R..", "..RRR");
        test(".L.R.", "LL.RR");
        test("RR.L", "RR.L");
        test(".......L.L", "LLLLLLLLLL");
        test(".L.R...LR..L..", "LL.RR.LLRRLL..");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
