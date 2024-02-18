package weekfour;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;


public class Solver {

    private SearchNode curr;
    private final boolean solved;
    private static class SearchNode {
        public final int priority;
        private final Board node;
        private final int moves;
        private final SearchNode prev;

         SearchNode(Board node, int moves, SearchNode prev) {
            this.node = node;
            this.moves = moves;
            this.prev = prev;
            this.priority = moves + node.manhattan();
        }
    }
    private static class ComparatorSolver implements Comparator<SearchNode> {

        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            return o1.priority - o2.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        solved = solvedGame(initial);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solved;
    }
    private boolean solvedGame(Board initial) {
        MinPQ<SearchNode> heap = new MinPQ<>(new ComparatorSolver());
        MinPQ<SearchNode> twinHeap = new MinPQ<>(new ComparatorSolver());

        heap.insert(new SearchNode(initial, 0, null));
        twinHeap.insert(new SearchNode(initial.twin(), 0, null));

        while (!heap.isEmpty() && !twinHeap.isEmpty()) {
            curr = heap.delMin();
            if (curr.node.isGoal()) {
                return true;
            } else {
                insert(curr, heap);
            }
            curr = twinHeap.delMin();
            if (curr.node.isGoal()) {
                return false;
            } else {
                insert(curr, twinHeap);
            }
        }
        return false;
    }
    private void insert(SearchNode c, MinPQ<SearchNode> h) {
        for (Board t : c.node.neighbors()) {
            if (c.prev != null) {
                if (!c.prev.node.equals(t))
                    h.insert(new SearchNode(t, c.moves + 1, c));
            } else
                h.insert(new SearchNode(t, c.moves + 1, c));
        }
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;
        return curr.moves;
    }



    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Stack<Board> boardPath = new Stack<>();
        if (!isSolvable())
            return null;
        SearchNode temp = curr;

        while (temp != null) {
            boardPath.push(temp.node);
            temp = temp.prev;
        }
        return boardPath;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

