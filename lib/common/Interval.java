package common;

import java.util.Objects;

public class Interval {
    public int start;
    public int end;

    public Interval() {
        start = 0;
        end = 0;
    }

    public Interval(int s, int e) {
        start = s;
        end = e;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Interval)) return false;

        Interval o = (Interval)other;
        return start == o.start && end == o.end;
    }

    public static Interval[] array(int[][] array) {
        Interval[] intervals = new Interval[array.length];
        int i = 0;
        for (int[] a : array) {
            intervals[i++] = new Interval(a[0], a[1]);
        }
        return intervals;
    }

    public String toString() {
        return "[" + start + "," + end + "]";
    }

    public boolean equals(int[] nums) {
        return nums.length == 2 && start == nums[0] && end == nums[1];
    }
}
