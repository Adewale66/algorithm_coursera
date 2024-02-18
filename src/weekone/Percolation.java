package weekone;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private final boolean[][] grid;
    private final int  n;
    private int openSites = 0;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF perc;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        grid = new boolean[n][n];
        this.n = n;
        uf = new WeightedQuickUnionUF(n*n + 2);
        perc = new WeightedQuickUnionUF(n*n + 1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }
        for (int i = 1; i < n + 1; i++) {
            uf.union(0, encode(1, i));
            uf.union(n*n+1, encode(n, i));
            perc.union(0, encode(1, i));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n  || col < 1 || col > n) {
            throw new IllegalArgumentException(row + " " + col);
        }
        if (grid[row-1][col-1])
            return;

        grid[row-1][col-1] = true;
        openSites++;
        connect(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n  || col < 1 || col > n) {
            throw new IllegalArgumentException(row + " " + col);
        }
        return grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n  || col < 1 || col > n) {
            throw new IllegalArgumentException(row + " " + col);
        }
        if (!isOpen(row, col))
            return false;
        return perc.find(encode(row, col)) == perc.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(n*n + 1);
    }

    private void connect(int row, int col) {
        // North
        if (row - 2 >= 0) {
            if (isOpen(row-1, col)) {
                uf.union(encode(row-1, col), encode(row, col));
                perc.union(encode(row-1, col), encode(row, col));
            }
        }

        // South
        if (row < n) {
            if (isOpen(row+1, col)) {
                uf.union(encode(row+1, col), encode(row, col));
                perc.union(encode(row+1, col), encode(row, col));
            }
        }

        // East
        if (col < n) {
            if (isOpen(row, col+1)) {
                uf.union(encode(row, col+1), encode(row, col));
                perc.union(encode(row, col+1), encode(row, col));
            }
        }

        // West
        if (col - 2 >= 0) {
            if (isOpen(row, col - 1)) {
                uf.union(encode(row, col - 1), encode(row, col));
                perc.union(encode(row, col - 1), encode(row, col));
            }
        }
    }

    private int encode(int row, int col) {
        return (row-1) * n + (col-1) + 1;
    }

    public static void main(String[] args) {
        System.out.println("test");
    }
}
