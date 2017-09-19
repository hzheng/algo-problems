
import org.junit.Test;
import static org.junit.Assert.*;

// LC537: https://leetcode.com/problems/complex-number-multiplication/
//
// Given two strings representing two complex numbers.
// You need to return a string representing their multiplication
public class ComplexNumberMultiplication {
    // beats N/A(11 ms for 55 tests)
    public String complexNumberMultiply(String a, String b) {
        int[] n1 = convert(a);
        int[] n2 = convert(b);
        int real = n1[0] * n2[0] - n1[1] * n2[1];
        int image = n1[1] * n2[0] + n1[0] * n2[1];
        return real + "+" + image + "i";
    }

    private int[] convert(String s) {
        String[] n = s.split("\\+|i");
        return new int[]{Integer.parseInt(n[0]), Integer.parseInt(n[1])};
    }

    void test(String a, String b, String expected) {
        assertEquals(expected, complexNumberMultiply(a, b));
    }

    @Test
    public void test() {
        test("1+1i", "1+1i", "0+2i");
        test("1+-1i", "1+-1i", "0+-2i");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ComplexNumberMultiplication");
    }
}
