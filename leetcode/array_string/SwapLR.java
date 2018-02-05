import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC777:
//
// In a string composed of 'L', 'R', and 'X' characters, like "RXXLRXRXL", a
// move consists of either replacing one occurrence of "XL" with "LX", or
// replacing one occurrence of "RX" with "XR". Given the starting string start i
// and the ending string end, return True if and only if there exists a sequencei
// of moves to transform one string to the other.
// Note:
// 1 <= len(start) = len(end) <= 10000.
// Both start and end will only consist of characters in {'L', 'R', 'X'}.
public class SwapLR {
    // beats %(43 ms for 78 tests)
    public boolean canTransform(String start, String end) {
        int n = start.length();
        if (n != end.length()) return false;

        if (!start.replace("X", "").equals(end.replace("X", ""))) return false;

        for (int i = 0, lCount = 0, rCount = 0; i < n; i++) {
            switch (start.charAt(i)) {
            case 'L':
                lCount++;
                break;
            case 'R':
                rCount++;
                break;
            }
            switch (end.charAt(i)) {
            case 'L':
                lCount--;
                break;
            case 'R':
                rCount--;
                break;
            }
            if (lCount > 0 || rCount < 0) return false;
        }
        return true;
    }

    // beats %(43 ms for 78 tests)
    public boolean canTransform_2(String start, String end) {
        if (!start.replace("X", "").equals(end.replace("X", ""))) return false;

        int n = start.length();
        for (int i = 0, lCount = 0, rCount = 0; i < n; i++) {
            char s = start.charAt(i);
            char e = end.charAt(i);
            if (s == e) continue;

            lCount += (s == 'L') ? 1 : ((e == 'L') ? -1 : 0);
            rCount += (s == 'R') ? 1 : ((e == 'R') ? -1 : 0);
            if (lCount > 0 || rCount < 0) return false;
        }
        return true;
    }

    // beats %(46 ms for 78 tests)
    public boolean canTransform2(String start, String end) {
        if (!start.replace("X", "").equals(end.replace("X", ""))) return false;

        int n = start.length();
        for (int i = 0, j = 0; i < n; i++) {
            if (start.charAt(i) == 'L') {
                for (; end.charAt(j) != 'L'; j++) {}
                if (i < j++) return false;
            }
        }
        for (int i = 0, j = 0; i < n; i++) {
            if (start.charAt(i) == 'R') {
                for (; end.charAt(j) != 'R'; j++) {}
                if (i > j++) return false;
            }
        }
        return true;
    }

    // beats %(39 ms for 78 tests)
    public boolean canTransform3(String start, String end) {
        if (!start.replace("X", "").equals(end.replace("X", ""))) return false;

        int n = start.length();
        for (int i = 0, j = 0; i < n; i++) {
            if (start.charAt(i) == 'X') continue;

            for (; j < n && end.charAt(j) == 'X'; j++) {}
            if (start.charAt(i) == 'R') {
                if (i > j) return false;
            } else if (i < j) return false;
            j++;
        }
        return true;
    }

    void test(String start, String end, boolean expected) {
        assertEquals(expected, canTransform(start, end));
        assertEquals(expected, canTransform_2(start, end));
        assertEquals(expected, canTransform2(start, end));
        assertEquals(expected, canTransform3(start, end));
    }

    @Test
    public void test() {
        // test("LX", "LXX", false); // no such test?
        test("RL", "LR", false);
        test("X", "L", false);
        test("LX", "XL", false);
        test("XR", "RX", false);
        test("RXXLRXRXL", "XRLXXRRLX", true);
        test("XLXRRXXRXX", "LXXXXXXRRR", true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
