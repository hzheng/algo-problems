import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 5.5:
 * Determine the number of bits required to convert integer A to integer B.
 */
public class ConvertBits {
    public static int diffBits(int n, int m) {
        int count = 0;
        for (int xor = n ^ m; xor > 0; xor >>= 1) {
            count += (xor & 1);
            /*
            if ((xor & 1) > 0) {
                count++;
            }*/
        }
        return count;
    }

    void test(String nStr, String mStr, int expected) {
        int n = Integer.parseInt(nStr, 2);
        int m = Integer.parseInt(mStr, 2);
        assertEquals(expected, diffBits(n, m));
    }

    @Test
    public void test1() {
        test("0", "0", 0);
        test("1", "1", 0);
        test("1010", "1010", 0);
        test("10", "1", 2);
        test("1101010", "1001010", 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConvertBits");
    }
}
