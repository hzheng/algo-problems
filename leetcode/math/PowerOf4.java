import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/power-of-four/
//
// Given an integer, write a function to determine if it is a power of four.
// Could you do it without using any loop / recursion?
public class PowerOf4 {
    // beats 22.67%(2 ms)
    public boolean isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n - 1) % 3 == 0;
    }

    // beats 9.74%(3 ms)
    public boolean isPowerOfFour2(int n) {
        if (n < 1) return false;

        double log = Math.log10(n) / Math.log10(4);
        return Math.abs(log - (int)(log + 0.5)) < 1E-10;
    }

    // beats 22.67%(2 ms)
    public boolean isPowerOfFour3(int n) {
        return n > 0 && n == Math.pow(4, Math.round(Math.log(n) / Math.log(4)));
    }

    // beats 22.67%(2 ms)
    public boolean isPowerOfFour4(int n) {
        return Math.log10(n) / Math.log10(4) % 1 == 0;
    }

    void test(int n, boolean expected) {
        assertEquals(expected, isPowerOfFour(n));
        assertEquals(expected, isPowerOfFour2(n));
        assertEquals(expected, isPowerOfFour3(n));
        assertEquals(expected, isPowerOfFour4(n));
    }

    void test(int n) {
        test(n, true);
        test(n + 1, false);
        test(n - 1, false);
    }

    @Test
    public void test1() {
        for (int i = 0; i < 5000; i++) {
            test(i, i == 1 || i == 4 || i == 16 || i == 64 || i == 256
                 || i == 1024 || i == 4096);
        }
    }

    @Test
    public void test2() {
        test(65536);
        test(262144);
        test(4194304);
        test(1073741824);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PowerOf4");
    }
}
