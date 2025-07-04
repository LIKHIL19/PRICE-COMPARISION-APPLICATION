package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utils.SessionManager;
import utils.UIConstants;

public class UserProfilePanel extends JPanel {
    private JLabel usernameLabel;
    private JButton logoutButton;

    public UserProfilePanel() {
        initComponents();
        updateUserInfo();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BACKGROUND_COLOR);

        // Gradient header with user icon and username
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.ACCENT_COLOR, w, h, UIConstants.ACCENT_PURPLE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 160));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 40, 30));
        JLabel userIcon = new JLabel("\uD83D\uDC64"); // Unicode user icon
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        userIcon.setForeground(Color.WHITE);
        usernameLabel = new JLabel();
        usernameLabel.setFont(UIConstants.TITLE_FONT);
        usernameLabel.setForeground(Color.WHITE);
        headerPanel.add(userIcon);
        headerPanel.add(usernameLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Profile summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        JLabel statusLabel = new JLabel();
        statusLabel.setFont(UIConstants.SUBTITLE_FONT);
        statusLabel.setForeground(UIConstants.ACCENT_COLOR);
        summaryPanel.add(statusLabel);
        summaryPanel.add(Box.createVerticalStrut(20));
        JLabel funLabel = new JLabel("Welcome to your profile! Manage your account and enjoy smart shopping.");
        funLabel.setFont(UIConstants.NORMAL_FONT);
        funLabel.setForeground(UIConstants.TEXT_COLOR);
        summaryPanel.add(funLabel);
        add(summaryPanel, BorderLayout.CENTER);

        // Logout button
        logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.setFont(UIConstants.BUTTON_FONT);
        logoutButton.setBackground(UIConstants.ACCENT_ORANGE);
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_ORANGE, 2, true));
        logoutButton.setPreferredSize(new Dimension(180, 48));
        logoutButton.addActionListener(e -> handleLogout());
        JPanel logoutPanel = new JPanel();
        logoutPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        logoutPanel.add(logoutButton);
        add(logoutPanel, BorderLayout.SOUTH);

        // Update status label on login/logout
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateUserInfo();
                if (SessionManager.getInstance().isLoggedIn() && SessionManager.getInstance().getCurrentUser() != null) {
                    statusLabel.setText("Logged in as: " + SessionManager.getInstance().getCurrentUser().getUsername());
                } else {
                    statusLabel.setText("Not logged in");
                }
            }
        });
    }
    private void styleButton(JButton button) {
        button.setFont(UIConstants.BUTTON_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(UIConstants.ACCENT_ORANGE);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(UIConstants.ACCENT_COLOR);
                button.setForeground(Color.BLACK);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIConstants.ACCENT_ORANGE);
                button.setForeground(Color.BLACK);
            }
        });
    }
    private void updateUserInfo() {
        SessionManager sessionManager = SessionManager.getInstance();
        if (sessionManager.isLoggedIn() && sessionManager.getCurrentUser() != null) {
            usernameLabel.setText(sessionManager.getCurrentUser().getUsername());
            logoutButton.setEnabled(true);
        } else {
            usernameLabel.setText("Not logged in");
            logoutButton.setEnabled(false);
        }
    }
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                SessionManager.getInstance().logout();
                updateUserInfo();
                // Notify parent to update UI
                Container parent = getParent();
                if (parent != null) {
                    parent.revalidate();
                    parent.repaint();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error during logout: " + e.getMessage(),
                    "Logout Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}