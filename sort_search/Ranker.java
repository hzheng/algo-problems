import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 11.8:
 * You are reading in a stream of integers. Periodically, you wish to be able to
 * look up the rank of a number x. Implement the method track(int x), which is
 * called when each number is generated, and the method getRankOfNumber(int x),
 * which returns the number of values less than or equal to x.
 */
public class Ranker {
    static class CountNode {
        CountNode left;
        CountNode right;
        int count; // benefit: can potentially calculate reverse rank
        int data;

        CountNode(int data) {
            this.data = data;
        }

        void insert(int x) {
            if (x <= data) {
                if (left == null) {
                    left = new CountNode(x);
                } else {
                    left.insert(x);
                }
            } else if (right == null) {
                right = new CountNode(x);
            } else {
                right.insert(x);
            }
            count++;
        }

        int getRank(int x) {
            if (x == data) {
                return (left == null) ? 0 : (left.count + 1);
            }

            if (x < data) {
                return (left == null) ? -1 : left.getRank(x);
            }

            if (right == null) return -1;

            int rightRank = right.getRank(x);
            if (rightRank < 0) return -1;

            int leftCount = (left == null) ? 0 : (left.count + 1);
            return leftCount + rightRank + 1;
        }
    } // class CountNode

    // from the book
    static class RankNode {
        RankNode left;
        RankNode right;
        int leftSize; // benefit: a little bit easier than total count
        int data;

        RankNode(int data) {
            this.data = data;
        }

        void insert(int x) {
            if (x <= data) {
                if (left == null) {
                    left = new RankNode(x);
                } else {
                    left.insert(x);
                }
                leftSize++;
            } else if (right == null) {
                right = new RankNode(x);
            } else {
                right.insert(x);
            }
        }

        int getRank(int x) {
            if (x == data) {
                return leftSize;
            }

            if (x < data) {
                return (left == null) ? -1 : left.getRank(x);
            }

            if (right == null) return -1;

            int rightRank = right.getRank(x);
            if (rightRank < 0) return -1;

            return leftSize + rightRank + 1;
        }
    }

    private CountNode countTree;
    private RankNode rankTree;

    public void track(int x) {
        if (countTree == null) {
            countTree = new CountNode(x);
        } else {
            countTree.insert(x);
        }
    }

    public int getRank(int x) {
        return countTree.getRank(x);
    }

    public void track2(int x) {
        if (rankTree == null) {
            rankTree = new RankNode(x);
        } else {
            rankTree.insert(x);
        }
    }

    public int getRank2(int x) {
        return rankTree.getRank(x);
    }

    public void clear() {
        countTree = null;
        rankTree = null;
    }

    private void test(int[] data, int[] expected) {
        for (int x : data) {
            track(x);
            track2(x);
        }

        int i = 0;
        for (int x : data) {
            assertEquals(expected[i], getRank(x));
            assertEquals(expected[i], getRank2(x));
            i++;
        }
        clear();
    }

    @Test
    public void test1() {
        test(new int[] {5}, new int[] {0});
        test(new int[] {5, 5}, new int[] {1, 1});
        test(new int[] {5, 2, 9},
             new int[] {1, 0, 2});
        test(new int[] {5, 10, 9, 15, 67, 9, 2, 8},
             new int[] {1, 5, 4, 6, 7, 4, 0, 2});
    }

    private void test(int n, int max) {
        int[] data = IntStream.range(1, n).map(
            i -> ThreadLocalRandom.current().nextInt(0, max + 1)).toArray();
        for (int x : data) {
            track(x);
            track2(x);
        }

        for (int x : data) {
            assertEquals(getRank(x), getRank2(x));
        }
        clear();
    }

    @Test
    public void test2() {
        test(50, 100);
        test(100, 100);
        test(1000, 100);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Ranker");
    }
}
