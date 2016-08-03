package common;

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

    public String toString() {
        return "[" + start + "," + end + "]";
    }

    public boolean equals(int[] nums) {
        return nums.length == 2 && start == nums[0] && end == nums[1];
    }
}
