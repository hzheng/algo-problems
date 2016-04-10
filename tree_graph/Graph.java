package tree_graph;

import java.util.List;

public class Graph
{
    public enum State {
        UNVISITED, VISITING, VISITED
    }
    public class Node {
        public int data;
        public State state;
        public List<Node> adjancency;
    }

    public List<Node> vertices;

    public static void main(String[] args) {
    }
}
