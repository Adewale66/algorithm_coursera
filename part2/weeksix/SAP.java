package weeksix;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


import java.util.Deque;
import java.util.ArrayDeque;



public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    private void checkRange(int v, int w) {
        if (v >= G.V() || w >= G.V())
            throw new IllegalArgumentException();
        if (v < 0  || w < 0)
            throw new IllegalArgumentException();
    }
    private void checkIterable(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        int vCount = 0;
        int wCount = 0;
        for (Integer i : v) {
            vCount++;
            if (i == null)
                throw new IllegalArgumentException();
        }
        if (vCount == 0)
            throw new IllegalArgumentException();
        for (Integer i : w) {
            wCount++;
            if (i == null)
                throw new IllegalArgumentException();
        }
        if (wCount == 0)
            throw new IllegalArgumentException();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkRange(v, w);
        int ancestor = ancestor(v, w);
        if (ancestor == -1)
            return -1;
        BreadthFirstDirectedPaths paths = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths paths1 = new BreadthFirstDirectedPaths(G, w);

        return paths1.distTo(ancestor) + paths.distTo(ancestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkRange(v, w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        int shortesAncestor = -1;
        int shortesPath = Integer.MAX_VALUE;
        Deque<Integer> ancestors = new ArrayDeque<Integer>();

        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                ancestors.push(i);
            }
        }

        for (Integer integer : ancestors) {
            if ((bfsV.distTo(integer) + bfsW.distTo(integer)) < shortesPath) {
                shortesPath = (bfsV.distTo(integer) + bfsW.distTo(integer));
                shortesAncestor = integer;
            }
        }
        return shortesAncestor;

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v, w);
        int ancester = ancestor(v, w);
        if (ancester == -1)
            return -1;
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return bfsV.distTo(ancester) + bfsW.distTo(ancester);
    }

    // a common ancestor that participates in the shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v, w);
        int shortestPath = Integer.MAX_VALUE;
        Deque<Integer> ancestors = new ArrayDeque<Integer>();
        int ancestor = -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                ancestors.push(i);
            }
        }

        for (Integer integer : ancestors) {
            if ((bfsV.distTo(integer) + bfsW.distTo(integer)) < shortestPath) {
                shortestPath = (bfsV.distTo(integer) + bfsW.distTo(integer));
                ancestor = integer;
            }
        }
        return ancestor;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

