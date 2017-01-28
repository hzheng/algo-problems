import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC278: https://leetcode.com/problems/first-bad-version/
//
// You are a product manager and currently leading a team to develop a new
// product. Unfortunately, the latest version of your product fails the quality
// check. Since each version is developed based on the previous version, all the
// versions after a bad version are also bad. Suppose you have n versions
// [1, 2, ..., n] and you want to find out the first bad one, which causes all
// the following ones to be bad.
// You are given an API bool isBadVersion(version) which will return whether
// version is bad. Implement a function to find the first bad version. You
// should minimize the number of calls to the API.
class VersionControl {
    private Random random = new Random();

    boolean isBadVersion(int version) {
        return random.nextBoolean();
    }
}

public class FirstBadVersion extends VersionControl {
    // Solution of Choice
    // beats 67.38%(17 ms for 21 tests)
    public int firstBadVersion(int n) {
        int low = 1;
        int high = n;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (isBadVersion(mid)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    void test(int x, int expected) {
    }

    @Test
    public void test1() {
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FirstBadVersion");
    }
}
