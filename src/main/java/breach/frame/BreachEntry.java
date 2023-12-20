package breach.frame;

import lombok.Getter;
import lombok.Setter;
import settings.Colors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BreachEntry extends JButton {

    private final BreachEntry entry = this;
    private final BreachWindow breach;

    @Getter
    private final String hexCode;
    @Getter
    private final int row;
    @Getter
    private final int col;
    @Getter
    @Setter
    private boolean triggered = false;
    @Getter
    @Setter
    private boolean canHover = true;

    public BreachEntry(BreachWindow breach, String hexCode, int row, int col) {
        this.breach = breach;
        this.hexCode = hexCode;
        this.row = row;
        this.col = col;
        initialize();
    }

    private void initialize() {
        setBorder(null);
        setText(hexCode);
        setFont(new Font("Agency", Font.BOLD, 18));
        setForeground(Colors.MAIN_FONT);
        setBackground(Colors.BREACH_STANDARD);
        setBorder(new LineBorder(Colors.BREACH_FONT, 1));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!triggered)
                    breach.updateCurrentClick(row, col);
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(!canHover || triggered) return;
                Border outerBorder = new LineBorder(Color.WHITE, 3);
                Border innerBorder = new LineBorder(Color.WHITE, 2);
                Border spaceBorder = new EmptyBorder(3, 3, 3, 3);

                // Combine the outer, space, and inner borders
                Border compoundBorder = new CompoundBorder(outerBorder, spaceBorder);
                compoundBorder = new CompoundBorder(compoundBorder, innerBorder);
                entry.setBorder(compoundBorder);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!canHover || triggered) return;
                setBorder(new LineBorder(Colors.BREACH_FONT, 1));
                super.mouseExited(e);
            }
        });
        setFocusPainted(false);
    }
}
