package breach;

import device.FileUT;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BreachWindow extends JFrame {

    private final BreachWindow instance;

    // File/Folder that'll be encrypted through breach
    public final File driver;
    private Thread cryptoThread;

    // Active assets for breaching
    public BreachState state = BreachState.RUNNING;
    public StateLayer stateLayer;

    public int currentRow = 0;
    public int currentCol = 0;
    public BreachEntry[][] breachEntries;
    public Integer[][] usedPositions = new Integer[6][6];

    // Solution and resolved-state of the breach
    public List<String> solution;
    public List<String> resolved = new ArrayList<>();

    public BreachWindow(File driver) {
        this.instance = this;
        this.driver = driver;
        if (driver.toString().equals("C:\\")) return;
        cryptoThread = runCrypto();
        setTitle("Breach Protocol");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(480, 540));
        setMinimumSize(new Dimension(480, 540));
        setResizable(false);
        getContentPane().setBackground(Colors.MAIN_BACKGROUND);

        stateLayer = new StateLayer();
        stateLayer.setPreferredSize(new Dimension(480, 60));

        JPanel breachMatrix = new JPanel(new GridLayout(6, 6));
        breachMatrix.setPreferredSize(new Dimension(480, 480));
        breachEntries = BreachUT.getHexMatrix(this);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                breachMatrix.add(breachEntries[i][j]);
            }
        }

        solution = generateSolution(breachEntries, 5);
        setTitle(getTitle() + ": " + solution);

        add(stateLayer, BorderLayout.NORTH);
        add(breachMatrix, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (state != BreachState.SUCCESS) {
                    state = BreachState.FAILED;
                    stateLayer.update(instance);
                }
                super.windowClosed(e);
            }
        });
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
    }

    private List<String> generateSolution(BreachEntry[][] entries, int size) {
        List<String> solution = new ArrayList<>();
        Integer[][] usedPositions = new Integer[6][6];
        int lastRow = 0;
        int lastCol = -1;

        boolean horizontal = true;

        Random rand = new Random();
        while (solution.size() < size) {
            if (lastCol == -1) {
                lastCol = rand.nextInt(0, 5);
                solution.add(entries[lastRow][lastCol].getHexCode());
                usedPositions[lastRow][lastCol] = 1;
            } else {
                if (horizontal)
                    lastCol = rand.nextInt(0, 5);
                else
                    lastRow = rand.nextInt(0, 5);

                if (usedPositions[lastRow][lastCol] != null)
                    continue;
                solution.add(entries[lastRow][lastCol].getHexCode());
                usedPositions[lastRow][lastCol] = 1;
            }
            horizontal = solution.size() % 2 == 0;
        }
        return solution;
    }

    public void updateCurrentClick(BreachEntry entry) {
        int row = entry.getRow();
        int col = entry.getCol();
        if (usedPositions[row][col] != null) return;
        if (!solution.get(resolved.size()).equals(entry.getHexCode())) {
            state = BreachState.FAILED;
            stateLayer.update(instance);
            return;
        }
        this.currentRow = row;
        this.currentCol = col;
        usedPositions[currentRow][currentCol] = 1;
        entry.setBackground(Colors.BREACH_STANDARD);
        entry.setBorder(new LineBorder(Colors.MAIN_FONT, 4));
        resolved.add(entry.getHexCode());
        entry.setTriggered(true);

        if (resolved.size() % 2 == 0) {
            changeHorizontalBackground(currentRow);
        } else {
            changeVerticalBackground(currentCol);
        }

        if (solution.size() == resolved.size()) {
            state = BreachState.SUCCESS;
            stateLayer.update(this);
            //dispose();
            cryptoThread.interrupt();
        }
    }

    public void changeHorizontalBackground(int row) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == row) {
                    breachEntries[i][j].setBackground(Colors.BREACH_HOVER);
                    breachEntries[i][j].setCanHover(true);
                } else {
                    breachEntries[i][j].setBackground(Colors.BREACH_STANDARD);
                    breachEntries[i][j].setCanHover(false);
                }
            }
        }
    }

    public void changeVerticalBackground(int col) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (j == col) {
                    breachEntries[i][j].setBackground(Colors.BREACH_HOVER);
                } else {
                    breachEntries[i][j].setBackground(Colors.BREACH_STANDARD);
                }
            }
        }
    }

    private Thread runCrypto() {
        FileUT.lock(driver);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    if (Thread.currentThread().isInterrupted() || state != BreachState.RUNNING) {
                        decryptDriver();
                        if (state == BreachState.SUCCESS) {
                            try {
                                Desktop.getDesktop().open(driver);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    } else {
                        decryptDriver();
                        encryptDriver();
                        if (!Thread.currentThread().isInterrupted())
                            Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    state = BreachState.FAILED;
                }
            }
        });
        thread.start();
        return thread;
    }

    private void encryptDriver() {
        if (driver.listFiles().length > 250) return;
        for (int i = 0; i < 250; i++) {
            FileUT.createEmptyZip(driver);
        }
    }

    private void decryptDriver() {
        FileUT.deleteEmptyZip(driver);
    }

    public enum BreachState {
        RUNNING,
        FAILED,
        SUCCESS;
    }
}
