import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/bulls-and-cows/
//
// You are playing the following Bulls and Cows game with your friend: You write
// down a number and ask your friend to guess what the number is. Each time your
// friend makes a guess, you provide a hint that indicates how many digits in
// said guess match your secret number exactly in both digit and position
// (called "bulls") and how many digits match the secret number but locate in
// the wrong position (called "cows"). Your friend will use successive guesses
// and hints to eventually derive the secret number.
// Write a function to return a hint according to the secret number and friend's
// guess, use A to indicate the bulls and B to indicate the cows.
public class BullsAndCows {
    final static int totalDigits = 10;

    // beats 83.16%(3 ms)
    public String getHint(String secret, String guess) {
        int bulls = 0;
        int[] secretDigits = new int[totalDigits];
        int[] guessDigits = new int[totalDigits];
        for (int i = secret.length() - 1; i >= 0; i--) {
            char c1 = secret.charAt(i);
            char c2 = guess.charAt(i);
            if (c1 == c2) {
                bulls++;
            } else {
                secretDigits[c1 - '0']++;
                guessDigits[c2 - '0']++;
            }
        }
        int cows = 0;
        for (int i = totalDigits - 1; i >= 0; i--) {
            cows += Math.min(secretDigits[i], guessDigits[i]);
        }
        return bulls + "A" + cows + "B";
        // return String.format("%dA%dB", bulls, cows); // beats 19.84%(19 ms)
    }

    // beats 64.03%(4 ms)
    public String getHint2(String secret, String guess) {
        int bulls = 0;
        int cows = 0;
        int[] count = new int[totalDigits];
        for (int i = secret.length() - 1; i >= 0; i--) {
            int secretDigit = secret.charAt(i) - '0';
            int guessDigit = guess.charAt(i) - '0';
            if (secretDigit == guessDigit) {
                bulls++;
            } else {
                if (count[secretDigit] < 0) {
                    cows++;
                }
                count[secretDigit]++;

                if (count[guessDigit] > 0) {
                    cows++;
                }
                count[guessDigit]--;
            }
        }
        return bulls + "A" + cows + "B";
    }

    void test(String secret, String guess, String expected) {
        assertEquals(expected, getHint(secret, guess));
        assertEquals(expected, getHint2(secret, guess));
    }

    @Test
    public void test1() {
        test("1807", "7810", "1A3B");
        test("1123", "0111", "1A1B");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BullsAndCows");
    }
}
