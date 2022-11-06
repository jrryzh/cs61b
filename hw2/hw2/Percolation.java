package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grids;
    private int numOpenSites;
    private WeightedQuickUnionUF gridsSet;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException(N + "should be larger than 0");
        grids = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grids[i][j] = 0;
            }
        }
        numOpenSites = 0;
        // 额外添加两个site到最后，分别是 virtual top site 和 virtual bottom site
        gridsSet = new WeightedQuickUnionUF(N * N + 2);
        for (int c = 0; c < N; c += 1) {
            gridsSet.union(c, N * N);
            gridsSet.union(N * (N - 1) + c, N * N + 1);
        }
    }

    // 1次union操作
    private void openHelper(int row, int col, int i, int j, WeightedQuickUnionUF gridsSet) {
        if (checkBounds(row + i) && checkBounds(col + j) && isOpen(row + i, col + j))
            gridsSet.union(calc(row + i, col + j), calc(row, col));
    }

    // open the site (row, col) if it is not open already
    // 4次union操作
    public void open(int row, int col) {
        validate(row, col);
        // 检查如果没有open
        if (grids[row][col] == 0) {
            // 设置状态为open
            grids[row][col] = 1;
            // 与上下左右连通的site设置为joint set
            openHelper(row, col, -1, 0, gridsSet);
            openHelper(row, col, 1, 0, gridsSet);
            openHelper(row, col, 0, -1, gridsSet);
            openHelper(row, col, 0, 1, gridsSet);
            // 数量增加1
            numOpenSites += 1;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grids[row][col] == 1;
    }

    // is the site (row, col) full?
    // 如果逐个检查第一行全部元素，则复杂度为O(n)
    // 因此利用 virtual top site 进行简化 检查时只需要检查与virtual top site是否相连即可
    public boolean isFull(int row, int col) {
        validate(row, col);
//        for (int c = 0; c < grids[0].length; c += 1) {
//            if (isOpen(0, c) && gridsSet.connected(calc(row, col), calc(0, c))) return true;
//        }
        int n = grids.length;
        if (isOpen(row, col) && gridsSet.connected(calc(row, col), n * n)) {
            return true;
        } else {
            return false;
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
//        for (int c = 0; c < grids[0].length; c += 1) {
//            if (isFull(grids.length - 1, c)) return true;
//        }
        int n = grids.length;
        if (gridsSet.connected(n * n, n * n + 1)) {
            return true;
        } else {
            return false;
        }
    }

    private int calc(int row, int col) {
        return row * grids[0].length + col;
    }

    private boolean checkBounds(int m) {
        int n = grids[0].length;
        return m >= 0 && m < n;
    }

    private void validate(int row, int col) {
        int n = grids[0].length;
        if (!checkBounds(row)) {
            throw new IndexOutOfBoundsException("index " + row + " is not between 0 and " + (n - 1));
        }
        if (!checkBounds(col)) {
            throw new IndexOutOfBoundsException("index " + col + " is not between 0 and " + (n - 1));
        }
    }

    public static void main(String[] args) {

    }
}
