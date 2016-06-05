import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given an array where elements are sorted in ascending order, convert it to a
// height balanced BST.
public class ArrayToBst {
    // beats 6.91%
    public TreeNode sortedArrayToBST(int[] nums) {
        return sortedArrayToBST(nums, 0, nums.length);
    }

    private TreeNode sortedArrayToBST(int[] nums, int start, int end) {
        if (start >= end) return null;

        int mid = (start + end) / 2;
        TreeNode parent = new TreeNode(nums[mid]);
        parent.left  = sortedArrayToBST(nums, start, mid);
        parent.right  = sortedArrayToBST(nums, mid + 1, end);
        return parent;
    }

    // TODO: recursion solution

    void test(Function<int[], TreeNode> convert, int[] nums, String expected) {
        TreeNode expectedTree = TreeNode.of(expected);
        TreeNode res = convert.apply(nums);
        System.out.println(res);
        assertArrayEquals(expectedTree.toArray(), res.toArray());
    }

    void test(int[] nums, String expected) {
        ArrayToBst a = new ArrayToBst();
        test(a::sortedArrayToBST, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3}, "2,1,3");
        test(new int[] {1, 2, 3, 4}, "3,2,4,1");
        test(new int[] {1, 2, 3, 4, 5}, "3,2,5,1,#,4");
        test(new int[] {1, 2, 3, 4, 5, 6}, "4,2,6,1,3,5");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ArrayToBst");
    }
}
