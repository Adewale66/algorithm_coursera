package weektwo;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> items = new RandomizedQueue<>();
        while (true) {
            try {
                items.enqueue(StdIn.readString());
            } catch (NoSuchElementException ignored) {
                break;
            }
        }
        Iterator<String> t = items.iterator();
        for (int i =0 ; i < k; i++) {
            StdOut.println("T: " + t.next());
        }
    }
}