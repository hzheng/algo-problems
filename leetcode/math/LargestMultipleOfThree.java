import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1363: https://leetcode.com/problems/largest-multiple-of-three/
//
// Given an integer array of digits, return the largest multiple of three that can be formed by concatenating some of
// the given digits in any order.
// Since the answer may not fit in an integer data type, return the answer as a string.
// If there is no answer return an empty string.
// Constraints:
//
// 1 <= digits.length <= 10^4
// 0 <= digits[i] <= 9
// The returning answer must not contain unnecessary leading zeros.
public class LargestMultipleOfThree {
    // Heap + Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 15 ms(31.02%), 40.1 MB(100%) for 25 tests
    public String largestMultipleOfThree(int[] digits) {
        List<Integer> res = new ArrayList<>();
        PriorityQueue<Integer> pq1 = new PriorityQueue<>();
        PriorityQueue<Integer> pq2 = new PriorityQueue<>();
        int sum = 0;
        for (int d : digits) {
            sum += d;
            sum %= 3;
            res.add(d);
            int mod = d % 3;
            if (mod == 1) {
                pq1.offer(d);
            } else if (mod == 2) {
                pq2.offer(d);
            }
        }
        if (sum == 1) {
            if (!pq1.isEmpty()) {
                Integer d = pq1.poll();
                res.remove(d);
            } else {
                res.remove(pq2.poll());
                res.remove(pq2.poll());
            }
        } else if (sum == 2) {
            if (!pq2.isEmpty()) {
                res.remove(pq2.poll());
            } else {
                res.remove(pq1.poll());
                res.remove(pq1.poll());
            }
        }
        res.sort(Collections.reverseOrder());
        StringBuilder sb = new StringBuilder();
        for (int i : res) {
            sb.append(i);
        }
        return (sb.length() > 0 && sb.charAt(0) == '0') ? "0" : sb.toString();
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // 6 ms(57.58%), 40.5 MB(100%) for 25 tests
    public String largestMultipleOfThree2(int[] digits) {
        Arrays.sort(digits);
        List<Integer> indices1 = new ArrayList<>(2);
        List<Integer> indices2 = new ArrayList<>(2);
        int sum = 0;
        int i = 0;
        for (int d : digits) {
            if (d % 3 == 1 && indices1.size() < 2) {
                indices1.add(i);
            } else if (d % 3 == 2 && indices2.size() < 2) {
                indices2.add(i);
            }
            sum = (sum + d) % 3;
            i++;
        }
        if (sum == 1) {
            return indices1.isEmpty() ? toString(digits, indices2.get(0), indices2.get(1))
                    : toString(digits, indices1.get(0), -1);
        }
        if (sum == 2) {
            return indices2.isEmpty() ? toString(digits, indices1.get(0), indices1.get(1))
                    : toString(digits, indices2.get(0), -1);
        }
        return toString(digits, -1, -1);
    }

    private String toString(int[] digits, int ignoredIndex1, int ignoredIndex2) {
        StringBuilder sb = new StringBuilder();
        for (int i = digits.length - 1; i >= 0; i--) {
            if (i != ignoredIndex1 && i != ignoredIndex2) {
                sb.append(digits[i]);
            }
        }
        return (sb.length() > 0 && sb.charAt(0) == '0') ? "0" : sb.toString();
    }

    // Count Sort
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(99.42%), 40.5 MB(100%) for 25 tests
    public String largestMultipleOfThree3(int[] digits) {
        int[] cnt = new int[10];
        for (int d : digits) {
            cnt[d]++;
        }
        int cnt1 = cnt[1] + cnt[4] + cnt[7];
        int cnt2 = cnt[2] + cnt[5] + cnt[8];
        int sum = (cnt1 + 2 * cnt2) % 3;
        if (sum == 1) {
            if (cnt1 > 0) {
                cnt1--;
            } else {
                cnt2 -= 2;
            }
        } else if (sum == 2) {
            if (cnt2 > 0) {
                cnt2--;
            } else {
                cnt1 -= 2;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int d = 9; d >= 0; d--) {
            if (d % 3 == 0) {
                while (cnt[d]-- > 0) {
                    sb.append(d);
                }
            } else if (d % 3 == 1) {
                while (cnt[d]-- > 0 && cnt1-- > 0) {
                    sb.append(d);
                }
            } else {
                while (cnt[d]-- > 0 && cnt2-- > 0) {
                    sb.append(d);
                }
            }
        }
        return (sb.length() > 0 && sb.charAt(0) == '0') ? "0" : sb.toString();
    }

    // Sort
    // time complexity: O(N), space complexity: O(1)
    // 5 ms(63.45%), 39.8 MB(100%) for 25 tests
    public String largestMultipleOfThree4(int[] digits) {
        Arrays.sort(digits);
        int sum = 0;
        int[] count = new int[10];
        for (int digit : digits) {
            sum += digit;
            count[digit]++;
        }
        return (sum == 0) ? "0" : largestMultipleOfThree(count, sum);
    }

    private String largestMultipleOfThree(int[] count, int sum) {
        int mod = sum % 3;
        if (mod == 0) return stringify(count);

        // when mod is 1 and we have one of 1,4,7, remove the first one
        // otherwise remove two digits of 2,5,8
        // similar when mod is 2
        for (int m = mod; sum % 3 != 0; ) {
            if (count[m]-- > 0) {
                sum -= m;
            }
            if (m % 3 == mod || count[m] <= 0) {
                m += 3;
            }
            if (m > 9) {
                m = m * 2 % 3;
            }
        }
        return stringify(count);
    }

    private String stringify(int[] count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 9; i >= 0; i--) {
            while (count[i]-- > 0) {
                sb.append(i);
            }
        }
        return sb.toString();
    }

    void test(int[] digits, String expected) {
        assertEquals(expected, largestMultipleOfThree(digits));
        assertEquals(expected, largestMultipleOfThree2(digits));
        assertEquals(expected, largestMultipleOfThree3(digits));
        assertEquals(expected, largestMultipleOfThree4(digits));
    }

    @Test
    public void test() {
        test(new int[]{8, 1, 9}, "981");
        test(new int[]{8, 6, 7, 1, 0}, "8760");
        test(new int[]{1}, "");
        test(new int[]{0, 0, 0, 0, 0, 0}, "0");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
