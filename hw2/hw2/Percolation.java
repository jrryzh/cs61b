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
        gridsSet = new WeightedQuickUnionUF(N*N);
    }

    private void openHelper(int row, int col, int i, int j, WeightedQuickUnionUF gridsSet) {
        if (checkBounds(row + i) && checkBounds(col + j) && isOpen(row + i, col + j))
            gridsSet.union(calc(row + i, col + j), calc(row, col));
    }

    // open the site (row, col) if it is not open already
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
    public boolean isFull(int row, int col) {
        validate(row, col);
        for (int c = 0; c < grids[0].length; c += 1) {
            if (isOpen(0, c) && gridsSet.connected(calc(row, col), calc(0, c))) return true;
        }
        return false;
    }
    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }
    // does the system percolate?
    public boolean percolates() {
        for (int c = 0; c < grids[0].length; c += 1) {
            if (isFull(grids.length - 1, c)) return true;
        }
        return false;
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
