import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/00000000000000cb/dashboard/00000000000079cb
// Qualification Round 2018: Problem B - Trouble Sort
//
// The basic operation of the standard bubble sort algorithm is to examine a 
// pair of adjacent numbers, and reverse that pair if the left number is larger 
// than the right number. But our algorithm examines a group of three adjacent 
// numbers, and if the leftmost number is larger than the rightmost number, it
// reverses that entire group. 
// Given a list of N integers, determine whether Trouble Sort will successfully 
// sort the list into non-decreasing order. If it will not, find the index  of 
// the first sorting error after the algorithm has finished.
public class TroubleSort {
    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    public static int solve(int[] A) {
        @SuppressWarnings("unchecked")
        PriorityQueue<Integer>[] pq =
            new PriorityQueue[] {new PriorityQueue<>(), new PriorityQueue<>()};
        for (int i = 0; i < A.length; i++) {
            pq[i & 1].offer(A[i]);
        }
        for (int i = 0, prev = Integer.MIN_VALUE;; i++) {
            Integer cur = pq[i & 1].poll();
            if (cur == null) return -1;
            if (cur < prev) return i - 1;

            prev = cur;
        }
    }

    public static void main(String[] args) {
        Scanner in =
            new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int n = in.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = in.nextInt();
        }
        int res = solve(A);
        out.println(res >= 0 ? String.valueOf(res) : "OK");
    }
}
