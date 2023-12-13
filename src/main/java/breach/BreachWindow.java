package breach;

import settings.Colors;
import settings.SecurityProperties;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BreachWindow extends JFrame {

    private final BreachTask task;

    private StateLayer stateLayer;
    private int currentRow = 0;
    private int currentCol = 0;
    private BreachEntry[][] breachEntries;
    private Integer[][] usedPositions = new Integer[6][6];

    // Solution and resolved-state of the breach
    private final List<String> solution = new ArrayList<>();
    private final List<String> resolved = new ArrayList<>();

    // TODO secure with encryption (rsa?)
    private List<Integer[]> master = new ArrayList<>();

    public BreachWindow(BreachTask task) {
        this.task = task;
        setTitle("Breach Protocol");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(480, 540));
        setMinimumSize(new Dimension(480, 540));
        setResizable(false);
        getContentPane().setBackground(Colors.MAIN_BACKGROUND);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                switch (task.state()) {
                    case SUCCESS -> {
                        task.getSuccessCallback().run();
                        task.event().onBreachSuccess(task);
                    }
                    case FAILED, RUNNING -> {
                        if (task.state() == BreachTask.BreachState.RUNNING)
                            task.setState(BreachTask.BreachState.FAILED);
                        task.getFailedCallback().run();
                        task.event().onBreachFailed(task);
                    }
                }
                super.windowClosed(e);
            }
        });
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
    }

    public StateLayer stateLayer() {
        return stateLayer;
    }

    public List<Integer[]> master() {
        return master;
    }

    public void continueBreach() {
        getContentPane().removeAll();
        breachEntries = BreachUT.getHexMatrix(this);
        usedPositions = new Integer[6][6];
        master = new ArrayList<>();

        stateLayer = new StateLayer();
        stateLayer.setPreferredSize(new Dimension(480, 60));

        JPanel breachMatrix = new JPanel(new GridLayout(6, 6));
        breachMatrix.setPreferredSize(new Dimension(480, 480));
        breachMatrix.setBackground(Colors.MAIN_BACKGROUND);
        breachEntries = BreachUT.getHexMatrix(this);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                breachMatrix.add(breachEntries[i][j]);
            }
        }

        solution.clear();
        solution.addAll(generateSolution(breachEntries, BreachUT.random.nextInt(SecurityProperties.minPuffer, SecurityProperties.maxPuffer)));
        resolved.clear();
        setTitle("Breach Protocol: " + solution);

        add(stateLayer, BorderLayout.NORTH);
        add(breachMatrix, BorderLayout.CENTER);
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
            master.add(new Integer[]{lastRow, lastCol});
        }
        return solution;
    }

    public void updateCurrentClick(int row, int col) {
        BreachEntry entry = breachEntries[row][col];
        if (usedPositions[row][col] != null) return;
        if (!solution.get(resolved.size()).equals(entry.getHexCode())) {
            task.setState(BreachTask.BreachState.FAILED);
            stateLayer.update(task);
            return;
        }
        triggerEntry(row, col);

        if (resolved.size() % 2 == 0) {
            changeHorizontalBackground(currentRow);
        } else {
            changeVerticalBackground(currentCol);
        }

        if (solution.size() == resolved.size()) {
            task.incrementBreach();
            if (task.solveAmount() == task.level()) {
                task.setState(BreachTask.BreachState.SUCCESS);
                stateLayer.update(task);
            } else {
                continueBreach();
            }
        }
    }

    public void triggerEntry(int row, int col) {
        this.currentRow = row;
        this.currentCol = col;
        usedPositions[row][col] = 1;
        breachEntries[row][col].setBackground(Colors.BREACH_STANDARD);
        breachEntries[row][col].setBorder(new LineBorder(Colors.MAIN_FONT, 4));
        resolved.add(breachEntries[row][col].getHexCode());
        breachEntries[row][col].setTriggered(true);
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

    public void animateClosing() {
        if (task.state() == BreachTask.BreachState.RUNNING) return;
        try {
            Color color = task.state() == BreachTask.BreachState.SUCCESS ? Colors.SUCCESS_BACKGROUND : Colors.FAILED_BACKGROUND;
            stateLayer.setBorder(null);
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    breachEntries[i][j].setBackground(color);
                    breachEntries[i][j].setText(" ");
                    breachEntries[i][j].setBorder(null);
                }
                Thread.sleep(25);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
