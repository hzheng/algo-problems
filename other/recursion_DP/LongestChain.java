import java.util.*;

import static org.junit.Assert.*;

// Given an input of a list of lists, each list contains a list of node_ids. The order of the list
// [A,B] represents a one way connection from node A to node B, and node_ids that are the same
// between different lists are the same node. We want to find the number of nodes in the longest
// chain across all the input lists.
// Input:
// [["A", "C", "D"],
// ["A", "B", "E"],
// ["D", "F", "G", "H"],
// ["Z", "X", "C", "Q", "R"]]
// z, x, c, d, f, g, h
// Output: 7

public class LongestChain {
    // Recursion + Dynamic Programming(Top-Down)
    public static int getLongestChain(List<List<String>> inputLists) {
        Map<String, Set<String>> graph = new HashMap<>();
        for (List<String> inputList : inputLists) {
            int n = inputList.size();
            for (int i = 0; i < n - 1; i++) {
                graph.computeIfAbsent(inputList.get(i), x -> new HashSet<>())
                     .add(inputList.get(i + 1));
            }
        }
        Map<String, Integer> memo = new HashMap<>();
        int res = 0;
        for (String cur : graph.keySet()) {
            res = Math.max(res, dfs(graph, memo, cur));
        }
        return res;
    }

    private static int dfs(Map<String, Set<String>> graph, Map<String, Integer> memo, String cur) {
        if (memo.containsKey(cur)) {return memo.get(cur);}

        int max = 0;
        for (String nei : graph.getOrDefault(cur, Collections.emptySet())) {
            max = Math.max(max, dfs(graph, memo, nei));
        }
        memo.put(cur, max + 1);
        return max + 1;
    }

    public static void main(String[] args) {
        List<List<String>> inputLists1 = new ArrayList<>();
        inputLists1.add(Arrays.asList("A", "C", "D"));
        inputLists1.add(Arrays.asList("A", "B", "E"));
        inputLists1.add(Arrays.asList("D", "F", "G", "H"));
        inputLists1.add(Arrays.asList("Z", "X", "C", "Q", "R"));
        assertEquals(7, getLongestChain(inputLists1));
    }
}
