import java.util.*;
import java.io.*;
import java.math.BigInteger;

// https://codingcompetitions.withgoogle.com/codejam/round/000000000043585d/00000000007549e5
// Round 1A 2021: Problem A - Append Sort
//
// We have a list of integers X1,X2,…,XN. We would like them to be in strictly increasing order, but
// unfortunately, we cannot reorder them. This means that usual sorting algorithms will not work.
// Our only option is to change them by appending digits 0 through 9 to their right (in base 10)
// For example, if one of the integers is 10, you can turn it into 100 or 109 with a single append
// operation, or into 1034 with two operations (as seen in the image below).
// Given the current list, what is the minimum number of single digit append operations that are
// necessary for the list to be in strictly increasing order?
//
// Input
//The first line of the input gives the number of test cases, T test cases follow. Each test case is
// described in two lines. The first line of a test case contains a single integer N, the number of
// integers in the list. The second line contains N integers X1,X2,…,XN, the members of the list.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// (starting from 1) and y is the minimum number of single digit append operations needed for the
// list to be in strictly increasing order.
// Limits
// Time limit: 10 seconds.
// Memory limit: 1 GB.
// 1≤T≤100
//
// Test Set 1 (Visible Verdict)
// 2≤N≤3
// 1≤Xi≤100, for all i
//
// Test Set 2 (Visible Verdict)
// 2≤N≤100.
// 1≤Xi≤10^9 , for all i.
public class AppendSort {
    private static final BigInteger NINE = new BigInteger("9");

    public static int solve(String[] arr) {
        int res = 0;
        BigInteger prev = new BigInteger(arr[0]); // long is not enough for big set!
        for (int i = 1, n = arr.length; i < n; i++) {
            BigInteger cur = new BigInteger(arr[i]);
            BigInteger v0 = cur;
            BigInteger v9 = cur;
            for (; v9.compareTo(prev) <= 0; res++) {
                v0 = v0.multiply(BigInteger.TEN);
                v9 = v9.multiply(BigInteger.TEN).add(NINE);
            }
            if (v0.compareTo(prev) > 0) {
                prev = v0;
            } else {
                prev = prev.add(BigInteger.ONE);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            int n = in.nextInt();
            String[] arr = new String[n];
            for (int k = 0; k < n; k++) {
                arr[k] = in.next();
            }
            out.format("Case #%d: ", i);
            int res = solve(arr);
            out.println(res);
        }
    }
}
