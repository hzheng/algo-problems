package common;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class UndirectedGraphNode {
    public int label;
    public List<UndirectedGraphNode> neighbors;

    public UndirectedGraphNode(int x) {
        label = x;
        neighbors = new ArrayList<UndirectedGraphNode>();
    }

    public void add(UndirectedGraphNode node) {
        neighbors.add(node);
    }

    public static UndirectedGraphNode of(String s) {
        Map<Integer, UndirectedGraphNode> map = new HashMap<>();
        UndirectedGraphNode root = null;
        for (String nodeStr : s.split("#")) {
            String[] neighbors = nodeStr.split(",");
            UndirectedGraphNode first = null;
            for (int i = 0; i < neighbors.length; i++) {
                int neighbor = Integer.parseInt(neighbors[i]);
                if (!map.containsKey(neighbor)) {
                    map.put(neighbor, new UndirectedGraphNode(neighbor));
                }
                UndirectedGraphNode node = map.get(neighbor);
                if (i == 0) {
                    first = node;
                    if (root == null) {
                        root = node;
                    }
                } else {
                    first.add(node);
                }
            }
        }
        return root;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof UndirectedGraphNode)) return false;

        UndirectedGraphNode o = (UndirectedGraphNode)other;
        return toString().equals(o.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toString(sb);
        for (UndirectedGraphNode neighbor : neighbors) {
            sb.append("#");
            neighbor.toString(sb);
        }
        sb.append("}");
        return sb.toString();
    }

    private void toString(StringBuilder sb) {
        sb.append(label);
        for (UndirectedGraphNode neighbor : neighbors) {
            sb.append(",").append(neighbor.label);
        }
    }
};
