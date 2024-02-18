package weektwo;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head = null;
    private Node tail = null;
    private int size = 0;

    private class Node {
        Item t;
        Node next;
        Node prev;
    }
    private class DequeIterator implements Iterator<Item> {
        Node current = head;
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item t = current.t;
            current = current.next;
            return t;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    // construct an empty deque;
    public Deque() { }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node temp = new Node();
        temp.t = item;
        if (isEmpty()) {
            tail = head = temp;
        } else {
            head.prev = temp;
            temp.next = head;
            head = temp;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node temp = new Node();
        temp.t = item;
        if (isEmpty()) {
            head = tail = temp;
        } else {
            tail.next = temp;
            temp.prev = tail;
            tail = temp;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item t = head.t;
        head = head.next;
        if (head != null)
            head.prev = null;
        if (tail.t == t)
            tail = head;
        size--;
        return t;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item t = tail.t;
        tail = tail.prev;
        if (tail != null)
            tail.next = null;
        if (head.t == t)
            head = tail;
        size--;
        return t;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> t = new Deque<>();
        StdOut.println(t.size());
        StdOut.println(t.isEmpty());
        t.addFirst(1);
        t.addLast(2);
        StdOut.println(t.size());
        StdOut.println(t.removeFirst());
        StdOut.println(t.removeLast());
        t.addLast(1);
        t.addLast(2);
        t.addFirst(3);
        for (Integer integer : t) StdOut.println(integer);
    }
}
