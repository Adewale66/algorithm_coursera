package weektwo;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {

    private Node head = null;
    private Node tail = null;
    private int size = 0;

    private class Node {
        Item value;
        Node next;
    }
    private class RandomizedQueueiterator implements Iterator<Item> {
        RandomizedQueue<Item> items = new RandomizedQueue<>();
        public RandomizedQueueiterator() {
             Node t = head;
             while (t != null) {
                 items.enqueue(t.value);
                 t = t.next;
             }
        }

        @Override
        public boolean hasNext() {
            return items.size() != 0;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return items.dequeue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    // construct an empty randomized queue
    public RandomizedQueue() { }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (head == null) {
            head = new Node();
            head.value = item;
            tail = head;
        } else {
            Node t = new Node();
            t.value = item;
            tail.next = t;
            tail = t;
        }
        size++;

    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        int randomIdx = StdRandom.uniformInt(size);
        int index = 0;
        Node temp = head;
        while (index != randomIdx - 1 && randomIdx != 0) {
            temp = temp.next;
            index++;
        }
        Item removed;
        if (randomIdx == 0) {
            removed = temp.value;
            head = head.next;
        } else {
            removed = temp.next.value;
            temp.next = temp.next.next;
        }
        size--;
        return removed;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        int randomIdx = StdRandom.uniformInt(size);
        int index = 0;
        Node temp = head;
        while (index != randomIdx) {
            temp = temp.next;
            index++;
        }
        return temp.value;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueiterator();
    }


    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> r = new RandomizedQueue<>();
        StdOut.println(r.size());
        r.enqueue(1);
        r.enqueue(2);
        r.enqueue(3);
        StdOut.println(r.sample());
        StdOut.println(r.dequeue());
        StdOut.println();
    }

}