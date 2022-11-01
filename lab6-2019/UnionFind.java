public class UnionFind {
    private int[] parent;
    private int[] size;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i += 1) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public void validate(int v1) {
        if (v1 < 0 || v1 >= parent.length) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
    }

    public int sizeOf(int v1) {
        validate(v1);
        return size[find(v1)];
    }

    /** Return parent or -size(when root)*/
    public int parent(int v1) {
        validate(v1);
        if (v1 == parent[v1]) {
            return -size[v1];
        } else {
            return parent[v1];
        }
    }

    public boolean connected(int v1, int v2) {
        validate(v1);
        validate(v2);
        return find(v1) == find(v2);
    }

    public void union(int v1, int v2) {
        validate(v1);
        validate(v2);
        if (sizeOf(v1) <= sizeOf(v2)) {
            parent[find(v1)] = find(v2);
            size[find(v2)] += size[find(v1)];
        } else {
            parent[find(v2)] = find(v1);
            size[find(v1)] += size[find(v2)];
        }
    }

    /** Return root */
    public int find(int v1) {
        validate(v1);
        while (v1 != parent[v1]) {
            v1 = parent[v1];
        }
        return v1;
    }

}