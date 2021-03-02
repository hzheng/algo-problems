import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1776: https://leetcode.com/problems/car-fleet-ii/
//
// There are n cars traveling at different speeds in the same direction along a one-lane road. You
// are given an array cars of length n, where cars[i] = [positioni, speedi] represents:
// positioni is the distance between the ith car and the beginning of the road in meters. It is
// guaranteed that positioni < positioni+1.
// speedi is the initial speed of the ith car in meters per second.
// For simplicity, cars can be considered as points moving along the number line. Two cars collide
// when they occupy the same position. Once a car collides with another car, they unite and form a
// single car fleet. The cars in the formed fleet will have the same position and the same speed,
// which is the initial speed of the slowest car in the fleet.
// Return an array answer, where answer[i] is the time, in seconds, at which the ith car collides
// with the next car, or -1 if the car does not collide with the next car. Answers within 10-5 of the actual answers are accepted.
//
// Constraints:
// 1 <= cars.length <= 10^5
// 1 <= positioni, speedi <= 10^6
// positioni < positioni+1
public class CarFleetII {
    // SortedSet
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 369 ms(25.00%), 102.9 MB(12.50%) for 83 tests
    public double[] getCollisionTimes(int[][] cars) {
        int n = cars.length;
        double[] time = new double[n];
        time[n - 1] = Double.MAX_VALUE;
        int[] nextCars = new int[n];
        for (int i = 0; i < n - 1; i++) {
            nextCars[i] = i + 1;
            time[i] = getTime(i, nextCars, cars);
        }
        TreeSet<Integer> set = new TreeSet<>();
        TreeSet<Integer> timedCars = new TreeSet<>(
                (a, b) -> (time[a] == time[b]) ? a - b : Double.compare(time[a], time[b]));
        for (int i = 0; i < n - 1; i++) {
            timedCars.add(i);
            set.add(i);
        }
        double[] res = new double[n];
        Arrays.fill(res, -1);
        while (!timedCars.isEmpty()) {
            int index = timedCars.pollFirst();
            res[index] = (time[index] == Double.MAX_VALUE) ? -1 : time[index];
            Integer prev = set.lower(index);
            set.remove(index);
            if (prev != null) {
                timedCars.remove(prev);
                nextCars[prev] = nextCars[index];
                time[prev] = getTime(prev, nextCars, cars);
                timedCars.add(prev);
            }
        }
        return res;
    }

    private double getTime(int cur, int[] nextCars, int[][] cars) {
        int[] curCar = cars[cur];
        int[] nextCar = cars[nextCars[cur]];
        return (curCar[1] > nextCar[1]) ?
               ((double)nextCar[0] - curCar[0]) / (curCar[1] - nextCar[1]) : Double.MAX_VALUE;
    }

    // Monotonic Stack
    // time complexity: O(N), space complexity: O(N)
    // 22 ms(100.00%), 98.8 MB(12.50%) for 83 tests
    public double[] getCollisionTimes2(int[][] cars) {
        int n = cars.length;
        double[] res = new double[n];
        Stack<Integer> stack = new Stack<>();
        for (int i = n - 1; i >= 0; --i) {
            res[i] = -1.0;
            for (int pos = cars[i][0], speed = cars[i][1]; !stack.isEmpty(); stack.pop()) {
                int j = stack.peek();
                int pos2 = cars[j][0];
                int speed2 = cars[j][1];
                double time = (speed > speed2) ? ((double)pos2 - pos) / (speed - speed2) : 0;
                if (time > 0 && (res[j] < 0 || res[j] > time)) {
                    res[i] = time;
                    break;
                }
            }
            stack.push(i);
        }
        return res;
    }

    private void test(int[][] cars, double[] expected) {
        assertArrayEquals(expected, getCollisionTimes(cars), 1E-5);
        assertArrayEquals(expected, getCollisionTimes2(cars), 1E-5);
    }

    @Test public void test() {
        test(new int[][] {{3, 4}, {5, 4}, {6, 3}, {9, 1}},
             new double[] {2.00000, 1.00000, 1.50000, -1.00000});
        test(new int[][] {{1, 2}, {2, 1}, {4, 3}, {7, 2}},
             new double[] {1.00000, -1.00000, 3.00000, -1.00000});
        test(new int[][] {{3, 1}, {9, 4}, {19, 4}}, new double[] {-1.00000, -1.00000, -1.00000});
        test(new int[][] {{1, 4}, {4, 5}, {7, 1}, {13, 4}, {14, 3}, {15, 2}, {16, 5}, {19, 1}},
             new double[] {2.00000, 0.75000, -1.00000, 1.00000, 1.00000, 4.00000, 0.75000,
                           -1.00000});
        test(new int[][] {{1, 5}, {6, 5}, {7, 5}, {14, 5}, {15, 3}, {16, 4}, {17, 5}, {18, 1},
                          {19, 2}, {20, 2}},
             new double[] {4.25000, 3.00000, 2.75000, 0.50000, 1.50000, 0.66667, 0.25000, -1.00000,
                           -1.00000, -1.00000});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
