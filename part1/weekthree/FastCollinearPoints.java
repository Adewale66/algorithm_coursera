package weekthree;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private final Point[] points;
    private int segments = 0;
    public FastCollinearPoints(Point[] points) {
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
    public int numberOfSegments() {
        return segments;
    }
    public LineSegment[] segments() {
        LineSegment[] t = new LineSegment[segments];
        for (int i = 0; i < points.length; i++) {
            Point[] temp = new Point[points.length - i - 1];
            for (int j = i + 1; j < points.length; j++) {
                temp[j - i - 1] = points[j];
            }
            Arrays.sort(temp, points[i].slopeOrder());
            int count = 1;
            for (int j = 1; j < temp.length; j++) {
                if (points[i].slopeTo(temp[j]) == points[i].slopeTo(temp[j - 1])) {
                    count++;
                } else {
                    if (count >= 3) {
                        if (points[i].compareTo(temp[j - count]) < 0) {
                            t = resize(t, segments + 1);
                            t[segments++] = new LineSegment(points[i], temp[j - 1]);
                        }
                    }
                    count = 1;
                }
            }
            if (count >= 3) {
                if (points[i].compareTo(temp[temp.length - count]) < 0) {
                    t = resize(t, segments + 1);
                    t[segments++] = new LineSegment(points[i], temp[temp.length - 1]);
                }
            }
        }
        return t;
    }

    private LineSegment[] resize(LineSegment[] t, int i) {
        LineSegment[] temp = new LineSegment[i];
        for (int j = 0; j < t.length; j++)
            temp[j] = t[j];
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
