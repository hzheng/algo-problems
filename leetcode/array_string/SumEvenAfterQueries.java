import org.junit.Test;
import static org.junit.Assert.*;

// LC985: https://leetcode.com/problems/sum-of-even-numbers-after-queries/
//
// We have an array A of integers, and an array queries of queries. For the i-th
// query val = queries[i][0], index = queries[i][1], we add val to A[index].  
// Then, the answer to the i-th query is the sum of the even values of A.
// Return the answer to all queries. Your answer array should have answer[i] as
// the answer to the i-th query.
public class SumEvenAfterQueries {
    // 7 ms(98.96%), 40.8 MB(%) for 58 tests
    public int[] sumEvenAfterQueries(int[] A, int[][] queries) {
        int[] res = new int[queries.length];
        int sum = 0;
        for (int a : A) {
            if (a % 2 == 0) {
                sum += a;
            }
        }
        int i = 0;
        for (int[] q : queries) {
            int a = A[q[1]];
            int b = A[q[1]] += q[0];
            if (a % 2 != 0) {
                if (b % 2 == 0) {
                    sum += b;
                }
            } else if (b % 2 == 0) {
                sum += (b - a);
            } else {
                sum -= a;
            }
            res[i++] = sum;
        }
        return res;
    }

    // 7 ms(98.96%), 40.8 MB(%) for 58 tests
    public int[] sumEvenAfterQueries2(int[] A, int[][] queries) {
        int[] res = new int[queries.length];
        int sum = 0;
        for (int a : A) {
            if (a % 2 == 0) {
                sum += a;
            }
        }
        int i = 0;
        for (int[] q : queries) {
            int val = q[0];
            int index = q[1];
            if (A[index] % 2 == 0) {
                sum -= A[index];
            }
            A[index] += val;
            if (A[index] % 2 == 0) {
                sum += A[index];
            }
            res[i++] = sum;
        }
        return res;
    }

    void test(int[] A, int[][] queries, int[] expected) {
        assertArrayEquals(expected, sumEvenAfterQueries(A, queries));
        assertArrayEquals(expected, sumEvenAfterQueries2(A, queries));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 4}, new int[][] {{1, 0}, {-3, 1}, {-4, 0}, {2, 3}},
             new int[] {8, 6, 2, 4});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
