import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class TwoDimArrayPractice extends JFrame {
    // GUI components
    private JButton fillValues, printArray, setValues, findMinimum, countFrequency;
    private ButtonHandler bh;

    private static int[][] intArray;
    private final int ROWS = 4;
    private final int COLUMNS = 20;
    private static int current1 = -1, current2 = -1, key, rowSelected = -1, columnSelected = -1;
    private static int counter = 0;
    private BarChart bc;
    private Image offscreen;
    private boolean firstTime = true;

    public TwoDimArrayPractice() {
        super("Choose your activity");
        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        // Initialize buttons
        fillValues = new JButton("Fill Values");
        printArray = new JButton("Print Array");
        setValues = new JButton("Set Values");
        findMinimum = new JButton("Find Minimum");
        countFrequency = new JButton("Count Frequency");

        // Add buttons to the container
        c.add(fillValues);
        c.add(printArray);
        c.add(setValues);
        c.add(findMinimum);
        c.add(countFrequency);

        // Setup ButtonHandler
        bh = new ButtonHandler();
        fillValues.addActionListener(bh);
        printArray.addActionListener(bh);
        setValues.addActionListener(bh);
        findMinimum.addActionListener(bh);
        countFrequency.addActionListener(bh);

        setSize(500, 550);

        intArray = new int[ROWS][COLUMNS];
        Random rand = new Random();

        // Fill the array with random numbers between 50 and 80
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[0].length; j++) {
                intArray[i][j] = rand.nextInt(31) + 50;
            }
        }

        bc = new BarChart(intArray);
        System.out.println("Row\tValue");
        for (int i = 0; i < intArray.length; i++) {
            System.out.print(i + "\t");
            for (int j = 0; j < intArray[i].length; j++) {
                System.out.print(intArray[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        setVisible(true);
        offscreen = this.createImage(getSize().width, getSize().height);
    }

    public void fillValues() {
        Random rand = new Random();
        for (int row = 0; row < intArray.length; row++) {
            System.out.print(row + "\t");
            for (int column = 0; column < intArray[row].length; column++) {
                intArray[row][column] = rand.nextInt(31) + 50;
                animate(row, column);
            }
            System.out.println();
        }
    }

    public void printArray() {
        for (int row = 0; row < intArray.length; row++) {
            System.out.print(row + "\t");
            for (int column = 0; column < intArray[row].length; column++) {
                animate(row, column);
                System.out.print(intArray[row][column] + " ");
            }
            System.out.println();
        }
    }

    public void setValues(int value, int row) {
        for (int i = 0; i < intArray[row].length; i++) {
            intArray[row][i] = value;
            animate(row, i);
        }
    }

    public int findMinimum(int column) {
        int min = Integer.MAX_VALUE, rowFound = 0;
        for (int row = 0; row < intArray.length; row++) {
            if (intArray[row][column] < min) {
                min = intArray[row][column];
                rowFound = row;
            }
        }
        animate(rowFound, column, min);
        return min;
    }

    public int countFound(int value) {
        int found = 0;
        for (int row = 0; row < intArray.length; row++) {
            for (int column = 0; column < intArray[row].length; column++) {
                if (intArray[row][column] == value) {
                    found++;
                    animate(row, column, intArray[row][column]);
                }
            }
        }
        return found;
    }

    public void startActivity(int act) {
        bc.setActivity(act);
        boolean goodInput = false;
        String answer;
        switch (act) {
            case 0: fillValues(); JOptionPane.showMessageDialog(null, "Array filled with new values"); break;
            case 1: printArray(); JOptionPane.showMessageDialog(null, "Array printed"); break;
            case 2:
                while (!goodInput || key < 50 || key > 80) {
                    try {
                        answer = JOptionPane.showInputDialog(null, "Enter a value between 50 and 80");
                        if (answer != null) {
                            key = Integer.parseInt(answer);
                            goodInput = true;
                        }
                    } catch (Exception e) {}
                }
                while (!goodInput || rowSelected < 0 || rowSelected > 3) {
                    try {
                        answer = JOptionPane.showInputDialog(null, "Enter a row number between 0 and 3");
                        if (answer != null) {
                            rowSelected = Integer.parseInt(answer);
                            goodInput = true;
                        }
                    } catch (Exception e) {}
                }
                bc.setKey(key);
                setValues(key, rowSelected);
                JOptionPane.showMessageDialog(null, "Values in row " + rowSelected + " set to " + key);
                break;
            case 3:
                while (!goodInput || columnSelected < 0 || columnSelected > 19) {
                    try {
                        answer = JOptionPane.showInputDialog(null, "Enter a column number between 0 and 19");
                        if (answer != null) {
                            columnSelected = Integer.parseInt(answer);
                            goodInput = true;
                        }
                    } catch (Exception e) {}
                }
                int min = findMinimum(columnSelected);
                JOptionPane.showMessageDialog(null, "Minimum value in column " + columnSelected + " is " + min);
                break;
            case 4:
                while (!goodInput || key < 50 || key > 80) {
                    try {
                        answer = JOptionPane.showInputDialog(null, "Enter a value between 50 and 80");
                        if (answer != null) {
                            key = Integer.parseInt(answer);
                            goodInput = true;
                        }
                    } catch (Exception e) {}
                }
                int frequency = countFound(key);
                JOptionPane.showMessageDialog(null, "You found " + key + " " + frequency + " time" + (frequency > 1 ? "s" : ""));
                break;
        }
        enableButtons();
    }

    private void animate(int row, int column) {
        if (bc.getActivity() >= 0 && bc.getActivity() <= 2) {
            try {
                current1 = row;
                current2 = column;
                bc.setArray(intArray);
                Graphics g = offscreen.getGraphics();
                paint(g);
                g = this.getGraphics();
                g.drawImage(offscreen, 0, 0, this);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Wrong number of arguments to animate method");
            System.exit(1);
        }
    }

    private void animate(int row, int column, int result) {
        if (bc.getActivity() == 3 || bc.getActivity() == 4) {
            try {
                current1 = row;
                current2 = column;
                bc.setStudentResult(result);
                bc.setArray(intArray);
                Graphics g = offscreen.getGraphics();
                paint(g);
                g = this.getGraphics();
                g.drawImage(offscreen, 0, 0, this);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Wrong number of arguments to animate method");
            System.exit(1);
        }
    }

    public void paint(Graphics g) {
        if ((current1 != -1 && current2 != -1) || firstTime) {
            super.paint(g);
            bc.draw(g);
            bc.updateBarChart(key, current1, current2, g);
            firstTime = false;
        }
    }

    public static void main(String[] args) {
        TwoDimArrayPractice app = new TwoDimArrayPractice();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void disableButtons() {
        fillValues.setEnabled(false);
        printArray.setEnabled(false);
        setValues.setEnabled(false);
        countFrequency.setEnabled(false);
        findMinimum.setEnabled(false);
    }

    public void enableButtons() {
        fillValues.setEnabled(true);
        printArray.setEnabled(true);
        setValues.setEnabled(true);
        countFrequency.setEnabled(true);
        findMinimum.setEnabled(true);
    }

    class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            disableButtons();
            String buttonText = e.getActionCommand();
            switch (buttonText) {
                case "Fill Values": startActivity(0); break;
                case "Print Array": startActivity(1); break;
                case "Set Values": startActivity(2); break;
                case "Find Minimum": startActivity(3); break;
                case "Count Frequency": startActivity(4); break;
            }
        }
    }
}