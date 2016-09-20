import java.awt.geom.Arc2D;
import java.util.*;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// LC384: https://leetcode.com/problems/shuffle-an-array/
//
// Shuffle a set of numbers without duplicates.
public class Shuffler {
    static interface IShuffler {
        /** Resets the array to its original configuration and return it. */
        public int[] reset();

        /** Returns a random shuffling of the array. */
        public int[] shuffle();
    }

    // beat 81.87%(242 ms)
    static class Shuffler1 implements IShuffler {
        private Random rand = new Random();

        private int[] original;
        private int[] nums;

        public Shuffler1(int[] nums) {
            this.original = nums.clone();
            this.nums = nums;
        }

        public int[] reset() {
            return nums = original.clone();
        }

        public int[] shuffle() {
            for (int i = nums.length - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                swap(i, j);
            }
            return nums;
        }

        private void swap(int i, int j) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }
    }

    @Test
    public void test1() {
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Shuffler");
    }
}