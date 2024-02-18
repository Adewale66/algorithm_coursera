
package weekfour;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int[][] board;
    private final int[][] goal;

    private final int[][] twin;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        board = copy(tiles);
        goal = solve(tiles);
        twin = createTwin();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        int n = board.length;
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int notCorrect = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0)
                    continue;
                if (board[i][j] != goal[i][j])
                    notCorrect++;
            }
        }
        return notCorrect;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int total = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != goal[i][j] && board[i][j] != 0) {
                    int row = (board[i][j] - 1) / board.length;
                    int col = (board[i][j] - 1) % board.length;
                    total += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        return total;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != goal[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[][] solve(int[][] initial) {
        int [] values  = new int[initial.length * initial.length];
        int index = 0;
        for (int i = 0; i < initial.length; i++) {
            for (int j = 0; j < initial.length; j++) {
                values[index++] = initial[i][j];
            }
        }
        sort(values);
        for (int i = 1; i < values.length; i++) {
            int temp = values[i-1];
            values[i-1] = values[i];
            values[i] = temp;
        }

        index = 0;
        int[][] goalBoard = new int[initial.length][initial.length];
        for (int i = 0; i < initial.length; i++) {
            for (int j = 0; j < initial.length; j++) {
                goalBoard[i][j] = values[index++];
            }
        }
        return goalBoard;

    }

    private static void sort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }
            int temp = array[i];
            array[i] = array[min];
            array[min] = temp;
        }
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (that.board.length != this.board.length)
            return false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (that.board[i][j] != this.board[i][j])
                    return false;
            }
        }
        return true;
    }


    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        int row = -1;
        int col = -1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++)
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            if (row != -1 && col != -1)
                break;
        }
        if (row - 1 >= 0) {
            int[][] cp = copy(board);
            int temp = cp[row-1][col];
            cp[row-1][col] = 0;
            cp[row][col] = temp;
            neighbors.push(new Board(cp));
        }
        if (row + 1 < board.length) {
            int[][] cp = copy(board);
            int temp = cp[row+1][col];
            cp[row+1][col] = 0;
            cp[row][col] = temp;
            neighbors.push(new Board(cp));
        }

        if (col - 1 >= 0) {
            int[][] cp = copy(board);
            int temp = cp[row][col-1];
            cp[row][col-1] = 0;
            cp[row][col] = temp;
            neighbors.push(new Board(cp));
        }

        if (col + 1 < board.length) {
            int[][] cp = copy(board);
            int temp = cp[row][col+1];
            cp[row][col+1] = 0;
            cp[row][col] = temp;
            neighbors.push(new Board(cp));
        }
        return neighbors;

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return new Board(twin);
    }

    private int[][] createTwin() {
        int[][] copy = copy(board);
        int row1 = StdRandom.uniformInt(board.length);
        int col1 = StdRandom.uniformInt(board.length);
        int row2 = StdRandom.uniformInt(board.length);
        int col2 = StdRandom.uniformInt(board.length);

        // Ensure the selected tiles are distinct and non-zero
        while (copy[row1][col1] == 0 || copy[row2][col2] == 0 || (row1 == row2 && col1 == col2)) {
            row1 = StdRandom.uniformInt(board.length);
            col1 = StdRandom.uniformInt(board.length);
            row2 = StdRandom.uniformInt(board.length);
            col2 = StdRandom.uniformInt(board.length);
        }

        // Swap the selected tiles
        int temp = copy[row1][col1];
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = temp;

        return copy;
    }
    private static int[][] copy(int[][] arr) {
        int[][] copy = new int[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, copy[i], 0, arr.length);
        }
        return copy;
    }

    public static void main(String[] args) {
        int[][] b = {
                {1, 6, 4},
                {2, 0 ,8},
                {7, 3, 5}
        };
        Board board = new Board(b);
        System.out.println(board.twin());
        System.out.println(board.twin());
        System.out.println(board.twin());
    }
}
