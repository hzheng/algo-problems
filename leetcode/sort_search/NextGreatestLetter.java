import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC744: https://leetcode.com/problems/find-smallest-letter-greater-than-target/
//
// Given a list of sorted characters letters containing only lowercase letters,
// and given a target letter target, find the smallest element in the list that
// is larger than the given target. Letters also wrap around.
// Note:
// letters has a length in range [2, 10000].
// letters consists of lowercase letters, and contains at least 2 unique letters.
// target is a lowercase letter.
public class NextGreatestLetter {
    // Linear Search
    // time complexity: O(N), space complexity: O(1)
    // beats %(14 ms for 165 tests)
    public char nextGreatestLetter(char[] letters, char target) {
        for (char c : letters) {
            if (c > target) return c;
        }
        return letters[0];
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // beats %(14 ms for 165 tests)
    public char nextGreatestLetter2(char[] letters, char target) {
        int index = Arrays.binarySearch(letters, target);
        if (index < 0) {
            index = -index - 1;
            return (index >= letters.length) ? letters[0] : letters[index];
        }
        for (int i = index + 1; i < letters.length; i++) {
            if (letters[i] > target) return letters[i];
        }
        return letters[0];
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // beats %(13 ms for 165 tests)
    public char nextGreatestLetter3(char[] letters, char target) {
        int low = 0;
        for (int high = letters.length; low < high; ) {
            int mid = (low + high) >>> 1;
            if (letters[mid] <= target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return letters[low % letters.length];
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(15 ms for 165 tests)
    public char nextGreatestLetter4(char[] letters, char target) {
        boolean[] set = new boolean[26];
        for (char c : letters) {
            set[c - 'a'] = true;
        }
        for (char c = (char)(target + 1); ; c++) {
            if (c > 'z') {
                c = 'a';
            }
            if (set[c - 'a']) return c;
        }
    }
    void test(char[] letters, char target, char expected) {
        assertEquals(expected, nextGreatestLetter(letters, target));
        assertEquals(expected, nextGreatestLetter2(letters, target));
        assertEquals(expected, nextGreatestLetter3(letters, target));
        assertEquals(expected, nextGreatestLetter4(letters, target));
    }

    @Test
    public void test() {
        test(new char[] {'c', 'f', 'j'}, 'a', 'c');
        test(new char[] {'c', 'f', 'j'}, 'c', 'f');
        test(new char[] {'c', 'f', 'j'}, 'd', 'f');
        test(new char[] {'c', 'f', 'j'}, 'g', 'j');
        test(new char[] {'c', 'f', 'j'}, 'j', 'c');
        test(new char[] {'c', 'f', 'j'}, 'k', 'c');
        test(new char[] {'e', 'e', 'e', 'e', 'e', 'e', 'n', 'n', 'n', 'n'},
             'e', 'n');
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
