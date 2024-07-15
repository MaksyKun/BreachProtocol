package examples.server;

import lombok.Getter;
import settings.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DaemonWindow extends JFrame {

    private List<String> ips = List.of("255.255.0.32", "255.253.0.22", "255.253.0.42", "255.253.0.57", "255.253.0.78");
    private final DaemonWindow instance = this;
    @Getter
    private String currentIp;

    public DaemonWindow() {
        setTitle("Daemon");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(480, 540));
        setMinimumSize(new Dimension(480, 540));
        setResizable(false);
        getContentPane().setBackground(Colors.MAIN_BACKGROUND);

        // Main panel with GridLayout to divide into three parts
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Colors.MAIN_BACKGROUND);
        mainPanel.setLayout(new GridLayout(4, 1));

        // Panel for the IP pool and whitelist
        JPanel ipPanel = new JPanel();
        ipPanel.setBackground(Colors.MAIN_BACKGROUND);
        ipPanel.setLayout(new GridLayout(1, 2, 10, 0));

        // IP pool list
        DefaultListModel<String> poolModel = new DefaultListModel<>();
        poolModel.addAll(ips);
        JList<String> poolList = new JList<>(poolModel);
        poolList.setBackground(Colors.BREACH_STANDARD);
        poolList.setForeground(Colors.MAIN_FONT);
        poolList.setSelectionBackground(Colors.BREACH_HOVER);
        poolList.setSelectionForeground(Colors.MAIN_FONT);
        poolList.setBorder(BorderFactory.createLineBorder(Colors.BREACH_FONT, 1));

        // Whitelist
        DefaultListModel<String> whitelistModel = new DefaultListModel<>();
        JList<String> whitelist = new JList<>(whitelistModel);
        whitelist.setBackground(Colors.BREACH_STANDARD);
        whitelist.setForeground(Colors.MAIN_FONT);
        whitelist.setSelectionBackground(Colors.BREACH_HOVER);
        whitelist.setSelectionForeground(Colors.MAIN_FONT);
        whitelist.setBorder(BorderFactory.createLineBorder(Colors.BREACH_FONT, 1));
        whitelist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selectedValue = whitelist.getSelectedValue();
                    if (selectedValue != null && !poolModel.contains(selectedValue)) {
                        poolModel.addElement(selectedValue);
                        whitelistModel.removeElement(selectedValue);
                        List<String> whitelist = new ArrayList<>();
                        List<String> blacklist = new ArrayList<>();
                        whitelistModel.elements().asIterator().forEachRemaining(whitelist::add);
                        poolModel.elements().asIterator().forEachRemaining(blacklist::add);

                        Daemon_Server.firewall.updateIpList(whitelist, blacklist);
                    }
                }
            }
        });

        poolList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selectedValue = poolList.getSelectedValue();
                    if (selectedValue != null && !whitelistModel.contains(selectedValue)) {
                        whitelistModel.addElement(selectedValue);
                        poolModel.removeElement(selectedValue);
                        List<String> whitelist = new ArrayList<>();
                        List<String> blacklist = new ArrayList<>();
                        whitelistModel.elements().asIterator().forEachRemaining(whitelist::add);
                        poolModel.elements().asIterator().forEachRemaining(blacklist::add);

                        Daemon_Server.firewall.updateIpList(whitelist, blacklist);
                    }
                }
            }
        });

        // Create panels with titled borders
        JPanel poolPanel = new JPanel(new BorderLayout());
        poolPanel.setBackground(Colors.MAIN_BACKGROUND);
        poolPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BREACH_FONT), "IP-Pool"));
        JScrollPane poolScrollPane = new JScrollPane(poolList);
        poolScrollPane.setPreferredSize(new Dimension(200, 100)); // Set preferred height
        poolPanel.add(poolScrollPane, BorderLayout.CENTER);

        JPanel whitelistPanel = new JPanel(new BorderLayout());
        whitelistPanel.setBackground(Colors.MAIN_BACKGROUND);
        whitelistPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BREACH_FONT), "IP-Whitelist"));
        JScrollPane whitelistScrollPane = new JScrollPane(whitelist);
        whitelistScrollPane.setPreferredSize(new Dimension(200, 100)); // Set preferred height
        whitelistPanel.add(whitelistScrollPane, BorderLayout.CENTER);

        // Add pool and whitelist panels to the ipPanel
        ipPanel.add(poolPanel);
        ipPanel.add(whitelistPanel);

        // Panel for combobox and button
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(Colors.MAIN_BACKGROUND);

        // Combobox for selecting IPs
        JComboBox<String> ipsComboBox = new JComboBox<>();
        ipsComboBox.setBackground(Colors.BREACH_STANDARD);
        ipsComboBox.setForeground(Colors.MAIN_FONT);
        ips.forEach(ipsComboBox::addItem);
        this.currentIp = (String) ipsComboBox.getSelectedItem();
        ipsComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                currentIp = (String) ipsComboBox.getSelectedItem();
                System.out.println("Selected IP: " + currentIp);
            }
        });

        // Access button
        JButton accessButton = new JButton("Access Server");
        accessButton.setBackground(Colors.BREACH_STANDARD);
        accessButton.setForeground(Colors.MAIN_FONT);
        accessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        accessButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, accessButton.getMinimumSize().height));

        accessButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selectedValue = (String) ipsComboBox.getSelectedItem();
                    if (selectedValue == null) return;
                    if (Daemon_Server.firewall.isDenied((selectedValue)) || Daemon_Server.firewall.isUnregistered(selectedValue)) {
                        accessButton.setText("Access Denied");
                        accessButton.setBackground(Colors.FAILED_BACKGROUND);
                        accessButton.setForeground(Colors.FAILED_FOREGROUND);
                        Daemon_Server.runDaemon(instance);
                    } else if(Daemon_Server.firewall.isForwarded(selectedValue)) {
                        accessButton.setText("Successfully Accessed");
                        accessButton.setBackground(Colors.SUCCESS_BACKGROUND);
                        accessButton.setForeground(Colors.SUCCESS_FOREGROUND);
                    }
                    new Thread(() -> {
                        try {
                            Thread.sleep(1500);
                            accessButton.setText("Access Server");
                            accessButton.setBackground(Colors.BREACH_STANDARD);
                            accessButton.setForeground(Colors.MAIN_FONT);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }
            }
        });

        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(ipsComboBox);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(accessButton);

        // Empty panel for the last third
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Colors.MAIN_BACKGROUND);

        // Add panels to the main panel
        mainPanel.add(ipPanel);      // 1/3 IP-Pool and IP-Whitelist
        mainPanel.add(controlPanel); // 1/3 Combobox and Access Button
        mainPanel.add(emptyPanel);   // 1/3 Fully empty

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
