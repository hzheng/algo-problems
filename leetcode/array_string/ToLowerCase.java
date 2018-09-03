import org.junit.Test;
import static org.junit.Assert.*;

// LC709: https://leetcode.com/problems/to-lower-case/
//
// Implement function ToLowerCase() that has a string parameter str, and returns
// the same string in lowercase.
public class ToLowerCase {
    // beats 100.00%(0 ms for 8 tests)
    public String toLowerCase(String str) {
        char[] cs = str.toCharArray();
        for (int i = cs.length - 1; i >= 0; i--) {
            if (cs[i] >= 'A' && cs[i] <= 'Z') {
                cs[i] = (char) (cs[i] - 'A' + 'a');
            }
        }
        return String.valueOf(cs);
    }

    void test(String str, String expected) {
        assertEquals(expected, toLowerCase(str));
    }

    @Test
    public void test() {
        test("Hello", "hello");
        test("here", "here");
        test("LOVELY", "lovely");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
