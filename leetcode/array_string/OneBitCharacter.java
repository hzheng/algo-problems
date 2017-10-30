import org.junit.Test;
import static org.junit.Assert.*;

// LC717: https://leetcode.com/problems/1-bit-and-2-bit-characters/
//
// We have two special characters. The first character can be represented by one
// bit 0. The second character can be represented by two bits (10 or 11).
// Now given a string represented by several bits. Return whether the last
// character must be a one-bit character or not. The given string will always
// end with a zero.
public class OneBitCharacter {
    // time complexity: O(N), space complexity: O(1)
    // beats %(8 ms for 93 tests)
    public boolean isOneBitCharacter(int[] bits) {
        for (int i = 0, n = bits.length; i < n; i += 2) {
            for (; i < n && bits[i] == 0; i++) {}
            if (i == n) return true;
        }
        return false;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(8 ms for 93 tests)
    public boolean isOneBitCharacter2(int[] bits) {
        int i = 0;
        for (; i < bits.length - 1; i += bits[i] + 1) {}
        return i == bits.length - 1;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(7 ms for 93 tests)
    public boolean isOneBitCharacter3(int[] bits) {
        int i = bits.length - 2;
        for (; i >= 0 && bits[i] > 0; i--) {} // 0 must be an ending digit
        return (bits.length - i) % 2 == 0;
    }

    // Regex
    // time complexity: O(N), space complexity: O(N)
    // beats %(20 ms for 93 tests)
    public boolean isOneBitCharacter4(int[] bits) {
        StringBuilder sb = new StringBuilder();
        for (int bit : bits) {
            sb.append(bit);
        }
        return sb.toString().matches("^(10|11|0)*0$");
    }

    void test(int[] bits, boolean expected) {
        assertEquals(expected, isOneBitCharacter(bits));
        assertEquals(expected, isOneBitCharacter2(bits));
        assertEquals(expected, isOneBitCharacter3(bits));
        assertEquals(expected, isOneBitCharacter4(bits));
    }

    @Test
    public void test() {
        test(new int[] {1, 1, 1, 0}, false);
        test(new int[] {1, 0, 0}, true);
        test(new int[] {0, 0, 1, 0, 0}, true);
        test(new int[] {1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0}, true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
