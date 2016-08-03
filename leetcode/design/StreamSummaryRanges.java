import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// https://leetcode.com/problems/data-stream-as-disjoint-intervals/
//
// Given a data stream input of non-negative integers a1, a2, ..., an, ...,
// summarize the numbers seen so far as a list of disjoint intervals.
public class StreamSummaryRanges {
    static interface SummaryRanges {
        public void addNum(int val);

        public List<Interval> getIntervals();
    }

    static class Node {
        Node left;
        Node right;
        Interval val;

        public Node(int v) {
            val = new Interval(v, v);
        }

        public void insert(int v) {
            Node node = this;
            while (true) {
                int start = node.val.start;
                int end = node.val.end;
                if (v + 1 < start) {
                    if (node.left != null) {
                        node = node.left;
                        continue;
                    }
                    node.left = new Node(v);
                } else if (v - 1 > end) {
                    if (node.right != null) {
                        node = node.right;
                        continue;
                    }
                    node.right = new Node(v);
                } else if (v + 1 == start) {
                    node.val.start--;
                    node.mergeLeft();
                } else if (v - 1 == end) {
                    node.val.end++;
                    node.mergeRight();
                }
                return;
            }
        }

        private void mergeLeft() {
            if (left == null) return;

            Node parent = this;
            Node maxNode = left;
            while (maxNode.right != null) {
                parent = maxNode;
                maxNode = maxNode.right;
            }
            if (val.start - 1 == maxNode.val.end) {
                val.start = maxNode.val.start;
                if (parent == this) {
                    parent.left = maxNode.left;
                } else {
                    parent.right = maxNode.left;
                }
            }
        }

        private void mergeRight() {
            if (right == null) return;

            Node parent = this;
            Node minNode = right;
            while (minNode.left != null) {
                parent = minNode;
                minNode = minNode.left;
            }
            if (val.end + 1 == minNode.val.start) {
                val.end = minNode.val.end;
                if (parent == this) {
                    parent.right = minNode.right;
                } else {
                    parent.left = minNode.right;
                }
            }
        }
    }

    // beats 83.13%(206 ms)
    static class SummaryRanges1 implements SummaryRanges {
        private Node root = new Node(-2);

        public SummaryRanges1() {
        }

        public void addNum(int val) {
            root.insert(val);
        }

        public List<Interval> getIntervals() {
            List<Interval> res = new ArrayList<>();
            getIntervals(root.right, res);
            return res;
        }

        private void getIntervals(Node node, List<Interval> res) {
            if (node == null) return;

            getIntervals(node.left, res);
            res.add(node.val);
            getIntervals(node.right, res);
        }
    }

    static class Node2 {
        Node2[] children = new Node2[2];
        int[] val;

        public Node2(int v) {
            val = new int[] {v, v};
        }

        public void insert(int v) {
            Node2 node = this;
            while (true) {
                int[] val = node.val;
                if (v + 1 < val[0]) {
                    if (node.children[0] != null) {
                        node = node.children[0];
                        continue;
                    }
                    node.children[0] = new Node2(v);
                } else if (v - 1 > val[1]) {
                    if (node.children[1] != null) {
                        node = node.children[1];
                        continue;
                    }
                    node.children[1] = new Node2(v);
                } else if (v + 1 == val[0]) {
                    node.val[0]--;
                    node.merge(0);
                } else if (v - 1 == val[1]) {
                    node.val[1]++;
                    node.merge(1);
                }
                return;
            }
        }

        private void merge(int index) {
            Node2 node = children[index];
            if (node == null) return;

            Node2 parent = this;
            while (node.children[1 - index] != null) {
                parent = node;
                node = node.children[1 - index];
            }
            if (val[index] + index * 2 - 1 == node.val[1 - index]) {
                val[index] = node.val[index];
                parent.children[(parent == this) ? index : 1 - index]
                    = node.children[index];
            }
        }
    }

    // beats 41.12%(230 ms)
    static class SummaryRanges2 implements SummaryRanges {
        private Node2 root = new Node2(-2);

        public SummaryRanges2() {
        }

        public void addNum(int val) {
            root.insert(val);
        }

        public List<Interval> getIntervals() {
            List<Interval> res = new ArrayList<>();
            getIntervals(root.children[1], res);
            return res;
        }

        private void getIntervals(Node2 node, List<Interval> res) {
            if (node == null) return;

            getIntervals(node.children[0], res);
            res.add(new Interval(node.val[0], node.val[1]));
            getIntervals(node.children[1], res);
        }
    }

    void test1(SummaryRanges summary, int[] nums, int[][] ... expected) {
        int i = 0;
        for (int num : nums) {
            summary.addNum(num);
            List<Interval> res = summary.getIntervals();
            int[][] expectedArray = expected[i++];
            assertEquals(expectedArray.length, res.size());
            int j = 0;
            for (Interval interval : res) {
                assertTrue("interval " + interval + " at index=" + j,
                           interval.equals(expectedArray[j++]));
            }
        }
    }

    void test1(int[] nums, int[][] ... expected) {
        test1(new SummaryRanges1(), nums, expected);
        test1(new SummaryRanges2(), nums, expected);
    }

    @Test
    public void test1() {
        test1(new int[] {1, 3, 7, 2, 6},
              new int[][] {{1, 1}}, new int[][] {{1, 1}, {3, 3}},
              new int[][] {{1, 1}, {3, 3}, {7, 7}},
              new int[][] {{1, 3}, {7, 7}}, new int[][] {{1, 3}, {6, 7}});
        test1(new int[] {9, 4, 7, 2, 8},
              new int[][] {{9, 9}}, new int[][] {{4, 4}, {9, 9}},
              new int[][] {{4, 4}, {7, 7}, {9, 9}},
              new int[][] {{2, 2}, {4, 4}, {7, 7}, {9, 9}},
              new int[][] {{2, 2}, {4, 4}, {7, 9}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StreamSummaryRanges");
    }
}
