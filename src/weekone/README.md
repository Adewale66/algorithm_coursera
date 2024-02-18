# Programming Assignment 1: Percolation

Reference: http://coursera.cs.princeton.edu/algs4/assignments/percolation.html

**The model**
A percolation system using an n-by-n grid of sites is modeled. Each site is either open or blocked. A full site is an open site that can be connected to an open site in the top row via a chain of neighboring (left, right, up, down) open sites. We say the system percolates if there is a full site in the bottom row. In other words, a system percolates if we fill all open sites connected to the top row and that process fills some open site on the bottom row.

**Percolation data type**. To model a percolation system, a data type _Percolation_ with the following AIP is created:
```
public class Percolation {
   public Percolation(int n)                // create n-by-n grid, with all sites blocked
   public void open(int row, int col)       // open site (row, col) if it is not open already
   public boolean isOpen(int row, int col)  // is site (row, col) open?
   public boolean isFull(int row, int col)  // is site (row, col) full?
   public boolean percolates()              // does the system percolate?

   public static void main(String[] args)   // test client (optional)
}
```

To perform a series of computational experiments, a data type _PercolationStats_ with the following API is created.
```
public class PercolationStats {
   public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
   public double mean()                          // sample mean of percolation threshold
   public double stddev()                        // sample standard deviation of percolation threshold
   public double confidenceLo()                  // low  endpoint of 95% confidence interval
   public double confidenceHi()                  // high endpoint of 95% confidence interval

   public static void main(String[] args)    // test client (described below)
}
```