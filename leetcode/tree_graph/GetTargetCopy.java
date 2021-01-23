import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1379: https://leetcode.com/problems/find-a-corresponding-node-of-a-binary-tree-in-a-clone-of-that-tree/
//
// Given two binary trees original and cloned and given a reference to a node target in the original
// tree. The cloned tree is a copy of the original tree.
// Return a reference to the same node in the cloned tree.
// Note that you are not allowed to change any of the two trees or the target node and the answer
// must be a reference to a node in the cloned tree.
// Follow up: Solve the problem if repeated values on the tree are allowed.
//
// Constraints:
// The number of nodes in the tree is in the range [1, 10^4].
// The values of the nodes of the tree are unique.
// target node is a node from the original tree and is not null
public class GetTargetCopy {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(96.56%), 46.9 MB(31.71%) for 56 tests
    public final TreeNode getTargetCopy(final TreeNode original, final TreeNode cloned,
                                        final TreeNode target) {
        if (original == null || original == target) { return cloned; }

        TreeNode left = getTargetCopy(original.left, cloned.left, target);
        return (left != null) ? left : getTargetCopy(original.right, cloned.right, target);
    }

    @FunctionalInterface interface Function<A, B, C, D> {
        D apply(A a, B b, C c);
    }

    void test(Function<TreeNode, TreeNode, TreeNode, TreeNode> getTargetCopy, String original,
              int target) {
        TreeNode originalNode = TreeNode.of(original);
        TreeNode targetNode = find(originalNode, target);
        TreeNode cloned = TreeNode.of(original);
        TreeNode res = getTargetCopy.apply(originalNode, cloned, targetNode);
        assertEquals(target, find(res, target).val);
    }

    private TreeNode find(TreeNode root, int target) {
        if (root == null) { return null; }
        if (root.val == target) { return root; }
        TreeNode left = find(root.left, target);
        return (left != null) ? left : find(root.right, target);
    }

    private void test(String original, int target) {
        GetTargetCopy g = new GetTargetCopy();
        test(g::getTargetCopy, original, target);
    }

    @Test public void test() {
        test("7,4,3,#,#,6,19", 3);
        test("7", 7);
        test("8,#,6,#,5,#,4,#,3,#,2,#,1", 4);
        test("1,2,3,4,5,6,7,8,9,10", 5);
        test("1,2,#,3", 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
