package common;

import java.util.Arrays;

public class DisjointSet {
    private int[] parent;

    public DisjointSet(int n) {
        parent = new int[n];
        Arrays.fill(parent, -1);
    }

    public int[] getParent() {
        return parent;
    }

    public int root(int x) {
        return parent[x] < 0 ? x : (parent[x] = root(parent[x]));
    }

    public boolean union(int x, int y) {
        x = root(x);
        y = root(y);
        if (x == y) return false;

        if (parent[y] < parent[x]) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        parent[x] += parent[y];
        parent[y] = x;
        return true;
    }

    public boolean equals(int x, int y) {
        return root(x) == root(y);
    }

    public int count() {
        int res = 0;
        for (int p : parent) {
            if (p < 0) {
                res++;
            }
        }
        return res;
    }
}