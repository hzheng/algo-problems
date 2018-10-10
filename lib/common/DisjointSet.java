package common;

import java.util.Arrays;

public class DisjointSet {
    // positive id refers to parent
    // negative id means root, and its absolute value is the component size
    private int[] id;
    private int count;

    public DisjointSet(int n) {
        id = new int[n];
        count = n;
        Arrays.fill(id, -1);
    }

    public int[] getParent() {
        return id;
    }

    public int root(int x) {
        // return parent[x] < 0 ? x : (parent[x] = root(parent[x]));
        for (; id[x] >= 0; x = id[x]) {}
        return x;
    }

    public boolean union(int x, int y) {
        x = root(x);
        y = root(y);
        if (x == y) return false;

        if (id[y] < id[x]) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        id[x] += id[y];
        id[y] = x;
        count--;
        return true;
    }

    public boolean equals(int x, int y) {
        return root(x) == root(y);
    }

    public int count() {
        // int res = 0;
        // for (int p : parent) {
        //     if (p < 0) {
        //         res++;
        //     }
        // }
        // return res;
        return count;
    }
}