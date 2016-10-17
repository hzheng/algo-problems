import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC406: https://leetcode.com/problems/queue-reconstruction-by-height/
//
// Suppose you have a random list of people standing in a queue. Each person is
// described by a pair of integers (h, k), where h is the height of the person
// and k is the number of people in front of this person who have a height
// greater than or equal to h. Write an algorithm to reconstruct the queue.
public class QueueReconstruction {
    // Heap
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 97.23%(16 ms for 37 tests)
    public int[][] reconstructQueue(int[][] people) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return (a[0] == b[0]) ? (a[1] - b[1]) : (b[0] - a[0]);
            }
        });
        for (int[] person : people) {
            pq.offer(person);
        }
        List<int[]> list = new ArrayList<>();
        while (!pq.isEmpty()) {
            int[] person = pq.poll();
            list.add(person[1], person);
        }
        return list.toArray(new int[people.length][2]);
    }

    // Sort
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 87.21%(18 ms for 37 tests)
    public int[][] reconstructQueue2(int[][] people) {
        Arrays.sort(people, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return (a[0] == b[0]) ? (a[1] - b[1]) : (b[0] - a[0]);
            }
        });
        List<int[]> list = new ArrayList<>();
        for (int[] person : people) {
            list.add(person[1], person);
        }
        return list.toArray(new int[people.length][2]);
    }

    void test(int[][] people, int[][] expected) {
        assertArrayEquals(expected, reconstructQueue(people));
        assertArrayEquals(expected, reconstructQueue2(Utils.clone(people)));
    }

    @Test
    public void test1() {
        test(new int[][]{{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}},
             new int[][]{{5, 0}, {7, 0}, {5, 2}, {6, 1}, {4, 4}, {7, 1}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("QueueReconstruction");
    }
}
