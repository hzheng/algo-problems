import java.util.*;

public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> complements = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            if (complements.containsKey(cur)) {
                return new int[] { i, complements.get(cur) };
            }
            complements.put(target - cur, i);
        }
        return null;
    }

    // from the answer
    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
