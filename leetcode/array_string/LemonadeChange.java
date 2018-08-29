import org.junit.Test;
import static org.junit.Assert.*;

// LC860: https://leetcode.com/problems/lemonade-change/
//
// At a lemonade stand, each lemonade costs $5. Customers are standing in a
// queue to buy from you, and order one at a time. Each customer will only buy
// one lemonade and pay with either a $5, $10, or $20 bill.  You must provide
// the correct change to each customer, so that the net transaction is that the
// customer pays $5. Note that you don't have any change in hand at first.
// Return true if and only if you can provide each customer with correct change.
public class LemonadeChange {
    // beats 20.19%(7 ms for 45 tests)
    public boolean lemonadeChange(int[] bills) {
        int nickels = 0;
        int dimes = 0;
        for (int cash : bills) {
            if (cash == 5) {
                nickels++;
            } else if (cash == 10) {
                dimes++;
                if (--nickels < 0) return false;
            } else {
                if (dimes > 0) {
                    dimes--;
                    nickels--;
                } else {
                    nickels -= 3;
                }
                if (nickels < 0) return false;
            }
        }
        return true;
    }

    void test(int[] bills, boolean expected) {
        assertEquals(expected, lemonadeChange(bills));
    }

    @Test
    public void test() {
        test(new int[] { 5, 5, 5, 10, 20 }, true);
        test(new int[] { 5, 5, 10 }, true);
        test(new int[] { 10, 10 }, false);
        test(new int[] { 5, 5, 10, 10, 20 }, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
