package weekthree;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;
    private int segments = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points.length == 0)
            throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("Argument is null");
            }
        }
        int n = points.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        Arrays.sort(points);
        this.points = points.clone();
    }
    public int numberOfSegments() { return  segments; }

    public LineSegment[] segments() {
        LineSegment[] t = new LineSegment[segments];
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) &&
                                points[i].slopeTo(points[j]) == points[i].slopeTo(points[l])) {
                            t = resize(t, segments + 1);
                            t[segments++] = new LineSegment(points[i], points[l]);
                        }
                    }
                }
            }
        }
        return t;
    }

    private LineSegment[] resize(LineSegment[] seg, int newSize) {
        LineSegment[] temp = new LineSegment[newSize];
        for (int i = 0; i < seg.length; i++)
            temp[i] = seg[i];
        return temp;

    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
