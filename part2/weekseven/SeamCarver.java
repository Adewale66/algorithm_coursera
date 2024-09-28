/* *****************************************************************************
 *  Name: Adewale Kujore
 *  Date: 27th September 2024
 *  Description: Seam carver Coursera week 7 solution
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private boolean transposed = false;
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("constructor argument is null");
        this.picture = new Picture(picture);
    }

    private double xGradient(int x, int y) {
        int left = picture.getRGB(x - 1, y);
        int right = picture.getRGB(x + 1, y);

        double red = ((right >> 16) & 0xFF) - ((left >> 16) & 0xFF);
        double green = ((right >> 8) & 0xFF) - ((left >> 8) & 0xFF);
        double blue = (right & 0xFF) - (left & 0xFF);

        return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
    }

    private double yGradient(int x, int y) {
        int up = picture.getRGB(x, y - 1);
        int down = picture.getRGB(x, y + 1);

        double red = ((up >> 16) & 0xFF) - ((down >> 16) & 0xFF);
        double green = ((up >> 8) & 0xFF) - ((down >> 8) & 0xFF);
        double blue = (up & 0xFF) - (down & 0xFF);

        return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
    }

    // current picture
    public Picture picture() {
        if (transposed) {
            revert();
        }
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        if (x < 0 || x >= width())
            throw new IllegalArgumentException(
                    "column index must be between 0 and " + (width() - 1) + ": " + x);
        if (y < 0 || y >= height())
            throw new IllegalArgumentException(
                    "row index must be between 0 and " + (height() - 1) + ": " + y);

        double BORDER_ENERGY = 1000.00;
        if (x == 0 || x == width() - 1)
            return BORDER_ENERGY;


        if (y == 0 || y == height() - 1)
            return BORDER_ENERGY;


        return Math.sqrt(xGradient(x, y) + yGradient(x, y));
    }

    private void transpose() {
        transposed = true;
        Picture transposedPicture = new Picture(height(), width());
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                transposedPicture.setRGB(row, col, this.picture.getRGB(col, row));
            }
        }
        this.picture = transposedPicture;
    }

    private void revert() {
        transposed = false;
        Picture transposedPicture = new Picture(height(), width());
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                transposedPicture.setRGB(row, col, this.picture.getRGB(col, row));
            }
        }
        this.picture = transposedPicture;
    }

    private int[] findSeam() {
        double[][] energy = new double[height()][width()];
        int[] seem = new int[height()];
        double[][] tempEnergy = new double[height()][width()];

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energy[i][j] = energy(j, i);
            }
        }

        System.arraycopy(energy[height() - 1], 0, tempEnergy[height() - 1], 0, width());

        double minEnergy;
        for (int i = height() - 2; i >= 0; i--) {
            for (int j = 0; j < width(); j++) {
                minEnergy = tempEnergy[i + 1][j];

                if (j > 0 && tempEnergy[i + 1][j - 1] < minEnergy) {
                    minEnergy = tempEnergy[i + 1][j - 1];
                }
                if (j < width() - 1 && tempEnergy[i + 1][j + 1] < minEnergy) {
                    minEnergy = tempEnergy[i + 1][j + 1];
                }
                tempEnergy[i][j] = minEnergy + energy[i][j];
            }
        }
        double min = tempEnergy[0][0];
        int index = 0;
        for (int i = 1; i < width(); i++) {
            if (tempEnergy[0][i] < min) {
                min = tempEnergy[0][i];
                index = i;
            }
        }
        int pos = 0;
        seem[pos++] = index;

        for (int i = 0; i < height() - 1; i++) {
            if (i == height() - 2) {
                if (index > 0)
                    seem[pos] = index - 1;
                else
                    seem[pos] = index;
            }
            else {
                minEnergy = tempEnergy[i + 1][index];
                int y = index;

                if (index > 0 && tempEnergy[i + 1][index - 1] < minEnergy) {
                    minEnergy = tempEnergy[i + 1][index - 1];
                    y = index - 1;
                }

                if (index < width() - 1 && tempEnergy[i + 1][index + 1] < minEnergy) {
                    y = index + 1;
                }

                index = y;
                seem[pos++] = y;
            }
        }
        return seem;

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!transposed) transpose();
        return findSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (transposed) revert();
        return findSeam();
    }

    private void removeSeam(int[] seam) {
        int[][] rgbValues = new int[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                rgbValues[i][j] = picture.getRGB(j, i);
            }
        }

        for (int i = 0; i < height(); i++) {
            System.arraycopy(rgbValues[i], seam[i] + 1, rgbValues[i], seam[i],
                             (width() - seam[i]) - 1);
        }
        Picture newPicture = new Picture(width() - 1, height());
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width() - 1; j++) {
                newPicture.setRGB(j, i, rgbValues[i][j]);
            }
        }

        this.picture = newPicture;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();

        if (transposed) {
            if (width() <= 1) throw new IllegalArgumentException();
            if (seam.length != height()) throw new IllegalArgumentException();
        }
        else {
            if (height() <= 1) throw new IllegalArgumentException();
            if (seam.length != width()) throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height()) throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
        }
        if (!transposed) transpose();
        removeSeam(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();

        if (transposed) revert();

        if (width() <= 1) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width()) throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
        }
        removeSeam(seam);
    }

}

