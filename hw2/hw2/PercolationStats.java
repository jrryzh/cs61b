package hw2;

import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    private int n;
    private int t;
    private int[] openNumArray;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0) {
            throw new IllegalArgumentException(N + "should be larger than 0");
        }
        if (T <= 0) {
            throw new IllegalArgumentException(T + "should be larger than 0");
        }
        n = N;
        t = T;
        openNumArray = new int[T];
        for (int i = 0; i < T; i += 1) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                }
            }
            openNumArray[i] = p.numberOfOpenSites();
        }
    }
    // sample mean of percolation threshold
    public double mean() {
        double sum = 0.0;
        for (double i : openNumArray) {
            sum += i / (n * n);
        }
        return sum / openNumArray.length;
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        double sum = 0.0;
        double mu = mean();
        for (double i : openNumArray) {
            sum += (i / (n * n) - mu) * (i / (n * n) - mu);
        }
        return Math.sqrt(sum / (openNumArray.length - 1));
    }
    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(openNumArray.length);
    }
    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(openNumArray.length);
    }
}
