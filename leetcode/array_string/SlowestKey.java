import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1629: https://leetcode.com/problems/slowest-key/
//
// A newly designed keypad was tested, where a tester pressed a sequence of n keys, one at a time.
// You are given a string keysPressed of length n, where keysPressed[i] was the ith key pressed in
// the testing sequence, and a sorted list releaseTimes, where releaseTimes[i] was the time the ith
// key was released. Both arrays are 0-indexed. The 0th key was pressed at the time 0, and every
// subsequent key was pressed at the exact time the previous key was released. The tester wants to
// know the key of the keypress that had the longest duration. The ith keypress had a duration of
// releaseTimes[i] - releaseTimes[i - 1], and the 0th keypress had a duration of releaseTimes[0].
// Note that the same key could have been pressed multiple times during the test, and these multiple
// presses of the same key may not have had the same duration.
// Return the key of the keypress that had the longest duration. If there are multiple such
// keypresses, return the lexicographically largest key of the keypresses.
public class SlowestKey {
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100%), 39.1 MB(25.00%) for 101 tests
    public char slowestKey(int[] releaseTimes, String keysPressed) {
        char res = keysPressed.charAt(0);
        int maxDuration = releaseTimes[0];
        for (int i = 1; i < releaseTimes.length; i++) {
            char c = keysPressed.charAt(i);
            int duration = releaseTimes[i] - releaseTimes[i - 1];
            if (duration > maxDuration || (duration == maxDuration && c > res)) {
                maxDuration = duration;
                res = c;
            }
        }
        return res;
    }

    private void test(int[] releaseTimes, String keyPressed, char expected) {
        assertEquals(expected, slowestKey(releaseTimes, keyPressed));
    }

    @Test public void test() {
        test(new int[] {9, 29, 49, 50}, "cbcd", 'c');
        test(new int[] {12, 23, 36, 46, 62}, "spuda", 'a');
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
