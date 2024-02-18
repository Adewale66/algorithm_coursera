package weekone;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int t;
    private final double[] p;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        t = trials;
        p = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation temp = new Percolation(n);
            while (!temp.percolates()) {
                int r = StdRandom.uniformInt(1, n + 1);
                int c = StdRandom.uniformInt(1, n + 1);
                if (temp.isOpen(r, c)) {
                    continue;
                }
                temp.open(r, c);
            }
            p[i] = (double) temp.numberOfOpenSites() / n*n;
        }
    }

    // sample mean of percolation threshold
    public double mean() { return StdStats.mean(p); }

    // sample standard deviation of percolation threshold
    public double stddev() { return StdStats.stddev(p); }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return  mean() - ((CONFIDENCE_95* stddev()) / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return  mean() + ((CONFIDENCE_95* stddev()) / Math.sqrt(t));
    }


    public static void main(String[] args) {
        PercolationStats t = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + t.mean());
        System.out.println("stddev                    = " + t.stddev());
        System.out.println("95% confidence interval                    = " + "[" + t.confidenceLo() + ", " + t.confidenceHi()+ "]");
    }
}
