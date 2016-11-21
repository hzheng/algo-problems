import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC465: https://leetcode.com/problems/optimal-account-balancing
//
// A group of friends went on holiday and sometimes lent each other money.
// We can model each transaction as a tuple (x, y, z) which means person x gave
// person y $z.
// Given a list of transactions between a group of people, return the minimum
// number of transactions required to settle the debt.
// Note:
// A transaction will be given as a tuple (x, y, z). Note that x ≠ y and z > 0.
// Person's IDs may not be linear
public class MinTransfers {
    // Hash Table + Recursion + DFS/Backtracking
    // beats N/A(7 ms for 27 tests)
    public int minTransfers(int[][] transactions) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int[] transaction : transactions) {
            int person1 = transaction[0];
            int person2 = transaction[1];
            int money = transaction[2];
            map.put(person1, map.getOrDefault(person1, 0) + money);
            map.put(person2, map.getOrDefault(person2, 0) - money);
        }
        List<Integer> credits = new ArrayList<>();
        List<Integer> debts = new ArrayList<>();
        for (int person : map.keySet()) {
            int balance = map.get(person);
            if (balance > 0) {
                credits.add(balance);
            } else if (balance < 0) {
                debts.add(-balance);
            }
        }
        int[] min = {Integer.MAX_VALUE};
        minTransfers(credits, debts, 0, min);
        return min[0];
    }

    private void minTransfers(List<Integer> credits, List<Integer> debts,
                              int cur, int[] min) {
        if (cur >= min[0] || credits.isEmpty()) {
            min[0] = Math.min(min[0], cur);
            return;
        }
        int credit = moveMinToLast(credits); // should be faster than sort
        int debt = moveMinToLast(debts);
        if (credit == debt) {
            credits.remove(credits.size() - 1);
            debts.remove(debts.size() - 1);
            minTransfers(credits, debts, cur + 1, min);
            credits.add(credit);
            debts.add(debt);
        } else if (credit < debt) {
            transfer(credits, debts, cur, min);
        } else {
            transfer(debts, credits, cur, min);
        }
    }

    private int moveMinToLast(List<Integer> list) {
        int size = list.size();
        int minVal = list.get(0);
        int minIndex = 0;
        for (int i = 1; i < size; i++) {
            int cur = list.get(i);
            if (cur < minVal) {
                minVal = cur;
                minIndex = i;
            }
        }
        int oldLast = list.get(size - 1);
        list.set(size - 1, minVal);
        return list.set(minIndex, oldLast);
    }

    private void transfer(List<Integer> balances1, List<Integer> balances2,
                          int cur, int[] min) {
        int removed = balances1.remove(balances1.size() - 1);
        for (int balance : new ArrayList<>(balances2)) {
            balances2.remove((Object)balance);
            balances2.add(balance - removed);
            minTransfers(balances1, balances2, cur + 1, min);
            balances2.remove((Object)(balance - removed));
            balances2.add(balance);
        }
        balances1.add(removed);
    }

    // Hash Table + Recursion + DFS/Backtracking
    // beats N/A(5 ms for 27 tests)
    public int minTransfers2(int[][] transactions) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int[] transaction : transactions) {
            int person1 = transaction[0];
            int person2 = transaction[1];
            int money = transaction[2];
            map.put(person1, map.getOrDefault(person1, 0) + money);
            map.put(person2, map.getOrDefault(person2, 0) - money);
        }
        List<Integer> credits = new ArrayList<>();
        List<Integer> debts = new ArrayList<>();
        for (int person : map.keySet()) {
            int balance = map.get(person);
            if (balance > 0) {
                credits.add(balance);
            } else if (balance < 0) {
                debts.add(-balance);
            }
        }
        int[] min = {Integer.MAX_VALUE};
        minTransfers(toArray(credits), credits.size(), toArray(debts),
                     debts.size(), 0, min);
        return min[0];
    }

    private int[] toArray(List<Integer> list) {
        int[] res = new int[list.size()];
        for (int i = list.size() - 1; i >= 0; i--) {
            res[i] = list.get(i);
        }
        return res;
    }

    private void minTransfers(int[] credits, int creditCount, int[] debts,
                              int debtCount, int cur, int[] min) {
        if (cur >= min[0] || creditCount == 0) {
            min[0] = Math.min(min[0], cur);
            return;
        }
        int credit = moveMinToLast(credits, creditCount); // should be faster than sort
        int debt = moveMinToLast(debts, debtCount);
        if (credit == debt) {
            minTransfers(credits, creditCount - 1, debts, debtCount - 1, cur + 1, min);
        } else if (credit < debt) {
            transfer(credits, creditCount, debts, debtCount, cur, min);
        } else {
            transfer(debts, debtCount, credits, creditCount, cur, min);
        }
    }

    private int moveMinToLast(int[] array, int end) {
        int minIndex = 0;
        for (int i = 1; i < end; i++) {
            if (array[i] < array[minIndex]) {
                minIndex = i;
            }
        }
        int min = array[minIndex];
        array[minIndex] = array[end - 1];
        return array[end - 1] = min;
    }

    private void transfer(int[] balances1, int count1, int[] balances2,
                          int count2, int cur, int[] min) {
        int last = balances1[count1 - 1];
        int[] copy = balances2.clone(); // can we avoid clone?
        for (int i = 0; i < count2; i++) {
            int old = copy[i];
            for (int j = 0; j < count2; j++) {
                if (balances2[j] == old) {
                    balances2[j] -= last;
                    break;
                }
            }
            minTransfers(balances1, count1 - 1, balances2, count2, cur + 1, min);
            for (int j = 0; j < count2; j++) {
                if (balances2[j] == old - last) {
                    balances2[j] = old;
                    break;
                }
            }
        }
    }

    void test(int[][] transactions, int expected) {
        assertEquals(expected, minTransfers(transactions));
        assertEquals(expected, minTransfers2(transactions));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 1, 10}, {2, 0, 5}}, 2);
        test(new int[][] {{0, 6, 7}, {0, 7, 7}, {1, 4, 5}, {1, 5, 4}, {2, 5, 2}, {3, 7, 1}}, 6);
        test(new int[][] {{1, 2, 3}, {1, 3, 3}, {6, 4, 1}, {5, 4, 4}}, 4);
        test(new int[][] {{0, 1, 5}, {2, 3, 1}, {2, 0, 1}, {4, 0, 2}}, 4);
        test(new int[][] {{1, 5, 8}, {8, 9, 8}, {2, 3, 9}, {4, 3, 1}}, 4);
        test(new int[][] {{10, 11, 6}, {12, 13, 7}, {14, 15, 2}, {14, 16, 2},
                          {14, 17, 2}, {14, 18, 2}}, 6);
        test(new int[][] {{1, 3, 1}, {1, 4, 2}, {2, 3, 3}, {2, 4, 4}}, 3);
        test(new int[][] {{0, 1, 10}, {1, 0, 1}, {1, 2, 5}, {2, 0, 5}}, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinTransfers");
    }
}
