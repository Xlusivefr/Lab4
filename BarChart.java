import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.image.ImageObserver;

public class BarChart implements ImageObserver {
    public static final int XMAX = 400;
    public static final int XSTART = 60;
    public static final int[] YMAX = {220, 320, 420, 520};
    private static final Color[] COLORS = {Color.BLUE, new Color(150, 150, 0), Color.DARK_GRAY, Color.MAGENTA};

    private int[][] data;
    private int barSize, xStart, activity = 5, studentResult = -1, exactFrequencyCount = -1, exactMinimum = -1, key = -1;
    private boolean checkNewValues = true;

    public BarChart() {}

    public BarChart(int[][] dArray) {
        data = deepCopy(dArray);
        barSize = (XMAX - 20) / data[0].length;
    }

    public void setArray(int[][] dArray) {
        data = deepCopy(dArray);
    }

    public void setStudentResult(int newStudentResult) {
        studentResult = newStudentResult;
    }

    public void setKey(int newKey) {
        key = newKey;
    }

    public void setActivity(int a) {
        activity = a;
    }

    public int getActivity() {
        return activity;
    }

    public void updateBarChart(int key, int index1, int index2, Graphics g) {
        draw(g);
        switch (activity) {
            case 1: drawArray(index1, index2, g); break;
            case 2: drawNewValue(index1, index2, g); break;
            case 3: drawMinimum(index1, index2, g); break;
            case 4: drawFrequency(index1, index2, key, g); break;
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < data.length; i++) {
            xStart = XSTART;
            g.setColor(COLORS[i]);
            g.drawString("Row " + i, xStart - 50, YMAX[i]);
            for (int j = 0; j < data[i].length; j++) {
                g.fillRect(xStart, YMAX[i] - data[i][j], barSize - 5, data[i][j]);
                g.drawString("" + data[i][j], xStart, YMAX[i] + 15);
                xStart += barSize;
            }
        }
    }

    public void drawArray(int index1, int index2, Graphics g) {
        g.setColor(Color.red);
        for (int i = 0; i < index1; i++) {
            drawRow(i, g);
        }
        for (int j = 0; j <= index2; j++) {
            drawBar(index1, j, g);
        }
    }

    public void drawNewValue(int index1, int index2, Graphics g) {
        g.setColor(Color.blue);
        checkCurrentNewValues(index1, index2);
        String s = checkNewValues ? "correctly" : "incorrectly";
        g.drawString("Setting new array elements of row " + index1 + " to " + key, 25, 110);
        g.drawString("Up to index " + index2 + ", the new values are set " + s, 25, 130);
        for (int j = 0; j <= index2; j++) {
            drawBar(index1, j, g);
        }
    }

    public void drawMinimum(int index1, int index2, Graphics g) {
        int b = findSubMinimum(index1, index2);
        if (index2 != -1) {
            g.setColor(Color.BLUE);
            g.drawString("Your current minimum in column " + index2 + ": " + studentResult, 25, 110);
            exactMinimum = b;
            g.drawString("Correct current minimum in column " + index2 + ": " + exactMinimum, 25, 130);
            drawBar(index1, index2, g);
            for (int i = 0; i <= index1; i++) {
                if (data[i][index2] == b) {
                    g.setColor(Color.RED);
                    drawBar(i, index2, g);
                    break;
                }
            }
        }
    }

    public void drawFrequency(int index1, int index2, int value, Graphics g) {
        g.setColor(Color.BLUE);
        g.drawString("Your current frequency count: " + studentResult, 25, 110);
        exactFrequencyCount = findExactFrequencyCount(index1, index2, value);
        g.drawString("Correct current frequency count: " + exactFrequencyCount, 25, 130);
        for (int i = 0; i < index1; i++) {
            drawRow(i, value, g);
        }
        drawBar(index1, index2, g);
        for (int j = 0; j <= index2; j++) {
            if (data[index1][j] == value) {
                drawBar(index1, j, g);
            }
        }
    }

    private int[][] deepCopy(int[][] array) {
        int[][] copy = new int[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(array[i], 0, copy[i], 0, array[i].length);
        }
        return copy;
    }

    private void drawRow(int row, Graphics g) {
        xStart = XSTART;
        for (int j = 0; j < data[row].length; j++) {
            g.fillRect(xStart, YMAX[row] - data[row][j], barSize - 5, data[row][j]);
            g.drawString("" + data[row][j], xStart, YMAX[row] + 15);
            xStart += barSize;
        }
    }

    private void drawRow(int row, int value, Graphics g) {
        xStart = XSTART;
        for (int j = 0; j < data[row].length; j++) {
            if (data[row][j] == value) {
                g.fillRect(xStart, YMAX[row] - data[row][j], barSize - 5, data[row][j]);
            }
            xStart += barSize;
        }
    }

    private void drawBar(int row, int col, Graphics g) {
        xStart = XSTART + col * barSize;
        g.drawRect(xStart, YMAX[row] - data[row][col], barSize - 5, data[row][col]);
        g.drawString("" + data[row][col], xStart, YMAX[row] + 15);
    }

    private int findSubMinimum(int index1, int index2) {
        int minimum = data[0][index2];
        for (int i = 1; i <= index1; i++) {
            if (minimum > data[i][index2]) minimum = data[i][index2];
        }
        return minimum;
    }

    private int findExactFrequencyCount(int rowIndex, int colIndex, int searchValue) {
        int count = 0;
        for (int i = 0; i < rowIndex; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == searchValue) count++;
            }
        }
        for (int j = 0; j <= colIndex; j++) {
            if (data[rowIndex][j] == searchValue) count++;
        }
        return count;
    }

    private void checkCurrentNewValues(int index1, int index2) {
        checkNewValues = true;
        for (int i = 0; i <= index2; i++) {
            if (key != data[index1][i]) {
                checkNewValues = false;
                break;
            }
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }
}
