package breach;

import settings.Colors;

import javax.swing.*;
import java.awt.*;

public class StateLayer extends JPanel {

    private final JLabel label = new JLabel();
    public StateLayer() {
        label.setText(" ");
        setBackground(Colors.MAIN_BACKGROUND);
        add(label, BorderLayout.CENTER);
    }

    public void update(BreachWindow breach) {
        if(breach.state == null) return;
        Thread animation = new Thread(() -> {
            String text = breach.state == BreachWindow.BreachState.SUCCESS ? "S  U  C  C  E  S  S" : "F  A  I  L  E  D";
            label.setVerticalAlignment(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setText(text);
            label.setFont(new Font("Agency", Font.BOLD, 30));
            label.setForeground(breach.state == BreachWindow.BreachState.SUCCESS ? Colors.SUCCESS_FOREGROUND : Colors.FAILED_FOREGROUND);
            setBackground(breach.state == BreachWindow.BreachState.SUCCESS ? Colors.SUCCESS_BACKGROUND : Colors.FAILED_BACKGROUND);

            try {
                Thread.sleep(500);
                label.setText("");
                Thread.sleep(500);
                label.setText(text);
                Thread.sleep(250);
                label.setText("");
                Thread.sleep(250);
                label.setText(text);
                Thread.sleep(250);
                label.setText("");
                Thread.sleep(500);
                label.setText(text);
                Thread.sleep(250);
                label.setText("");
                label.setText(text);
                breach.animateClosing();
                breach.dispose();
                Thread.currentThread().interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        animation.start();
    }
}
