import org.junit.Test;
import static org.junit.Assert.*;

// LC434: https://leetcode.com/problems/number-of-segments-in-a-string/
//
// Count the number of segments in a string, where a segment is defined to be a
// contiguous sequence of non-space characters.
public class CountStringSegments {
    // beats 20.00%(4 ms for 26 tests)
    public int countSegments(String s) {
        s = s.trim();
        if (s.length() == 0) return 0;

        return s.split("\\s+").length;
    }

    // beats 100.00%(2 ms for 26 tests)
    public int countSegments2(String s) {
        int count = 0;
        boolean inWord = false;
        for (char c : s.toCharArray()) {
            if (c != ' ') {
                inWord = true;
            } else if (inWord) {
                count++;
                inWord = false;
            }
        }
        return inWord ? count + 1 : count;
    }

    // beats 80.00%(3 ms for 26 tests)
    public int countSegments3(String s) {
        int count = 0;
        boolean inWord = false;
        for (char c : s.toCharArray()) {
            if (c == ' ' && inWord) {
                count++;
            }
            inWord = (c != ' ');
        }
        return inWord ? count + 1 : count;
    }

    // beats 100.00%(2 ms for 26 tests)
    public int countSegments4(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ' && (i == 0 || s.charAt(i - 1) == ' ')) {
                count++;
            }
        }
        return count;
    }

    void test(String s, int expected) {
        assertEquals(expected, countSegments(s));
        assertEquals(expected, countSegments2(s));
        assertEquals(expected, countSegments3(s));
        assertEquals(expected, countSegments4(s));
    }

    @Test
    public void test() {
        test("", 0);
        test(" ", 0);
        test("Hello, my name is John", 5);
        test(" Hello,  my name is  John ", 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountStringSegments");
    }
}
