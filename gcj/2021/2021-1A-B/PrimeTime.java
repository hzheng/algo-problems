import java.util.*;
import java.io.*;

// https://codingcompetitions.withgoogle.com/codejam/round/000000000043585d/00000000007543d8
// Round 1A 2021: Problem B - Prime Time
//
// You are playing a new solitaire game called Prime Time. You are given a deck of cards, and each
// card has a prime number written on it. Multiple cards may have the same number. Your goal is to
// divide the cards into two groups in such a way that the sum of the numbers in the first group is
// equal to the product of the numbers in the second group. Each card must belong to exactly one of
// the two groups, and each group must contain at least one card. The sum or product of a group that
// consists of a single card is simply the number on that card.
// Your score is the sum of the numbers in the first group (which is equal to the product of the
// numbers in the second group), or 0 if you cannot split the cards this way at all. What is the
// maximum score you can achieve?
//
// Input
// The first line of the input gives the number of test cases, T test cases follow. The first line
// of each test case contains a single integer M, representing the number of distinct prime numbers
// in your deck. Each of the next M lines contains two values: Pi and Ni, representing that you have
// exactly Ni cards with the prime Pi written on them.
// Note that the total number of cards in your deck is the sum of all Nis.
//
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// (starting from 1) and y is the maximum score you can achieve.
// Limits
// Time limit: 45 seconds.
// Memory limit: 1 GB.
// 1≤T≤100.
// 1≤M≤95. (Note that there are exactly 95 distinct primes between 2 and 499)
// 2≤Pi≤499, for all i.
// Each Pi is prime.
// Pi<Pi+1, for all i. (The primes are given in strictly increasing order)
// 1≤Ni, for all i.
// Test Set 1 (Visible Verdict)
// 2≤N1+N2+⋯+NM≤10.
// Test Set 2 (Visible Verdict)
// 2≤N1+N2+⋯+NM≤100.
// Test Set 3 (Hidden Verdict)
// 2≤N1+N2+⋯+NM≤10^15.
public class PrimeTime {
    public static long solve0(int[][] A) { // only works for test set 1 and 2
        int n = A.length;
        long sum = 0;
        for (int[] a : A) {
            sum += a[0] * a[1];
        }
        return dfs(A, sum, 0, 1L);
    }

    private static long dfs(int[][] A, long total, int i, long product) {
        int n = A.length;
        if (i >= n || total < product) return 0;

        if (total == product) return product;

        int[] cur = A[i] ;
        int p = cur[0];
        int cnt = cur[1];
        long res = 0;
        if (cnt > 0) {
            if (product * p < total) {
                cur[1]--;
                res = dfs(A, total - p, i, product * p);
                cur[1]++;
            }
        }
        res = Math.max(res, dfs(A, total, i + 1, product));
        return res;
    }

    public static long solve(long[][] A) {
        int n = A.length;
        long total = 0;
        for (long[] a : A) {
            total += a[0] * a[1];
        }
        int maxPrime = 499;
        double maxCount = 1E15;
        int maxCard = (int)(Math.log(maxPrime*maxCount) / Math.log(2));
        int maxSum = maxCard * maxPrime;
        for (long i = total - 1, min = Math.max(0, total - maxSum); i > min; i--) {
            long product = i; // find subset {a_i} s.t total = sum({a_i})+product({a_i})
            long sum = 0;
            outer: for (long[] a : A) {
                long p = a[0];
                long k = 0;
                for (; product % p == 0; product /= p) {
                    if (++k > a[1]) { continue outer; }
                }
                sum += k * p;
            }
            if (product == 1 && (sum + i) == total) { return i; }
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            int n = in.nextInt();
            long[][] arr = new long[n][2];
            for (int k = 0; k < n; k++) {
                arr[k][0] = in.nextLong();
                arr[k][1] = in.nextLong();
            }
            out.format("Case #%d: ", i);
            long res = solve(arr);
            out.println(res);
        }
    }
}
