package hw2;

import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    private int[] openNumArray;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0) throw new IllegalArgumentException(N + "should be larger than 0");
        if (T <= 0) throw new IllegalArgumentException(T + "should be larger than 0");
        openNumArray = new int[T];
        for (int t = 0; t < T; t += 1) {
            Percolation p = pf.make(N);
            while (!p.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                }
            }
            openNumArray[t] = p.numberOfOpenSites();
        }
    }
    // sample mean of percolation threshold
    public double mean() {
        double sum = 0.0;
        for (int i : openNumArray) {
            sum += i;
        }
        return sum / openNumArray.length;
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        double sum = 0.0;
        double mu = mean();
        for (int i : openNumArray) {
            sum += (i - mu) * (i - mu);
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
