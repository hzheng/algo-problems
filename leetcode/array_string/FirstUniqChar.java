import org.junit.Test;
import static org.junit.Assert.*;

// LC387: Given a string, find the first non-repeating character in it and
// return it's index. If it doesn't exist, return -1.
// Note: You may assume the string contain only lowercase letters.
public class FirstUniqChar {
    // one pass, worse case: N + 26
    // beats 93.78%(18 ms for 104 tests)
    public int firstUniqChar(String s) {
        int[] indices = new int[26];
        int i = 0;
        for (char c : s.toCharArray()) {
            int index = indices[c - 'a'];
            if (index == 0) {
                indices[c - 'a'] = i + 1;
            } else if (index > 0) {
                indices[c - 'a'] = -1;
            }
            i++;
        }
        int min = Integer.MAX_VALUE;
        for (i = 0; i < 26; i++) {
            int index = indices[i] - 1;
            if (index >= 0 && index < min) {
                min = index;
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    // Solution of Choice
    // two passes, worse case: 2N
    // beats 96.42%(16 ms for 104 tests)
    public int firstUniqChar2(String s) {
        int[] counts = new int[26];
        char[] chars = s.toCharArray();
        for (char c : chars) {
            counts[c - 'a']++;
        }
        int i = -1;
        for (char c : chars) {
            i++;
            if (counts[c - 'a'] == 1) return i;
        }
        return -1;
    }

    // Fast/Slow Pointers
    // beats 88.40%(21 ms for 104 tests)
    public int firstUniqChar3(String s) {
        char[] cs = s.toCharArray();
        int[] count = new int[26];
        for (int slow = 0, fast = 0, len = s.length(); slow < len; ) {
            if (count[cs[slow] - 'a'] == 0) {
                count[cs[slow] - 'a']++;
                fast = slow;
            }
            if (++fast >= len) return slow;
            count[cs[fast] - 'a']++;
            for ( ; slow < len && count[cs[slow] - 'a'] > 1; slow++) {}
        }
        return -1;
    }

    void test(String s, int expected) {
        assertEquals(expected, firstUniqChar(s));
        assertEquals(expected, firstUniqChar2(s));
        assertEquals(expected, firstUniqChar3(s));
    }

    @Test
    public void test1() {
        test("", -1);
        test("a", 0);
        test("lleettccoodde", -1);
        test("lleettccoode", 10);
        test("lleettcode", 6);
        test("lleetcode", 4);
        test("leetcode", 0);
        test("loveleetcode", 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FirstUniqChar");
    }
}
