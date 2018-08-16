import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC652: https://leetcode.com/problems/find-duplicate-subtrees/
//
// Given a binary tree, return all duplicate subtrees. For each kind of 
// duplicate subtrees, you only need to return the root node of any one of them.
public class FindDuplicateSubtrees {
    // Recursion
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 33.33%(33 ms for 168 tests)
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        List<TreeNode> res = new ArrayList<>();
        postorder(root, new HashMap<>(), res);
        return res;
    }

    private String postorder(TreeNode root, Map<String, Integer> map, List<TreeNode> res) {
        if (root == null) return "#";

        String left = postorder(root.left, map, res);
        String right = postorder(root.right, map, res);
        String self = root.val + "," + left + "," + right;
        map.put(self, map.getOrDefault(self, 0) + 1);
        if (map.get(self) == 2) {
            res.add(root);
        }
        return self;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 97.31%(18 ms for 168 tests)
    private int num;
    public List<TreeNode> findDuplicateSubtrees2(TreeNode root) {
        num = 1;
        List<TreeNode> res = new ArrayList<>();
        lookup(root, new HashMap<>(), new HashMap<>(), res);
        return res;
    }

    private int lookup(TreeNode node, Map<Integer, Integer> count,
                       Map<String, Integer> trees, List<TreeNode> res) {
        if (node == null) return 0;

        int left = lookup(node.left, count, trees, res);
        int right = lookup(node.right, count, trees, res);
        String serial = node.val + "," + left  + "," + right;
        // int uid = trees.computeIfAbsent(serial, x -> t++);
        int uid = trees.getOrDefault(serial, 0);
        if (uid == 0) {
            trees.put(serial, uid = num++);
        }
        count.put(uid, count.getOrDefault(uid, 0) + 1);
        if (count.get(uid) == 2) {
            res.add(node);
        }
        return uid;
    }

    void test(String tree, String... expected) {
        FindDuplicateSubtrees f = new FindDuplicateSubtrees();
        test(f::findDuplicateSubtrees, tree, expected);
        test(f::findDuplicateSubtrees2, tree, expected);
    }

    void test(Function<TreeNode, List<TreeNode>> find, String tree, String... expected) {
        Set<TreeNode> exp = new HashSet<>();
        for (String e : expected) {
            exp.add(TreeNode.of(e));
        }
        Set<TreeNode> actual = new HashSet<>();
        actual.addAll(find.apply(TreeNode.of(tree)));
        assertEquals(exp, actual);
    }

    @Test
    public void test() {
        test("1,2,3,4,#,2,4,#,#,4", "4", "2,4");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
