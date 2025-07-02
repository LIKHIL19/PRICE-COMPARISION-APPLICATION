import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.AuthDialog;
import ui.HomePanel;
import ui.SearchPanel;
import ui.UserProfilePanel;
import ui.WishlistPanel;
import utils.SessionManager;
import utils.UIConstants;

public class AppLauncher {
    public static void main(String[] args) {
        try {
            // Set dark theme look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set dark theme colors using UIConstants
            UIManager.put("Panel.background", UIConstants.BACKGROUND_COLOR);
            UIManager.put("TextField.background", UIConstants.PANEL_BACKGROUND);
            UIManager.put("TextField.foreground", UIConstants.TEXT_COLOR);
            UIManager.put("TextField.caretForeground", UIConstants.ACCENT_COLOR);
            UIManager.put("Button.background", UIConstants.ACCENT_COLOR);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Label.foreground", UIConstants.TEXT_COLOR);
            UIManager.put("ScrollPane.background", UIConstants.BACKGROUND_COLOR);
            UIManager.put("ScrollPane.foreground", UIConstants.TEXT_COLOR);

            // Dialog and JOptionPane colors
            UIManager.put("OptionPane.background", UIConstants.PANEL_BACKGROUND);
            UIManager.put("OptionPane.messageForeground", UIConstants.TEXT_COLOR);
            UIManager.put("OptionPane.foreground", UIConstants.TEXT_COLOR);
            UIManager.put("OptionPane.buttonFont", UIConstants.BUTTON_FONT);
            UIManager.put("OptionPane.messageFont", UIConstants.NORMAL_FONT);
            UIManager.put("OptionPane.buttonBackground", UIConstants.ACCENT_COLOR);
            UIManager.put("OptionPane.buttonForeground", Color.BLACK);
            UIManager.put("Button.select", UIConstants.ACCENT_ORANGE);
            UIManager.put("Button.focus", UIConstants.ACCENT_COLOR);
            UIManager.put("Button.border", UIConstants.BORDER_COLOR);
            UIManager.put("nimbusFocus", UIConstants.ACCENT_COLOR);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error setting up UI: " + e.getMessage(),
                "UI Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Price Comparison Platform");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.setBackground(UIConstants.BACKGROUND_COLOR);

            // Show login dialog first
            AuthDialog authDialog = new AuthDialog(frame);
            authDialog.setVisible(true);

            // Check if user is logged in
            if (!SessionManager.getInstance().isLoggedIn()) {
                System.exit(0);
                return;
            }

            // Main layout
            JPanel rootPanel = new JPanel(new BorderLayout());
            rootPanel.setBackground(UIConstants.BACKGROUND_COLOR);
            // Sidebar
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(UIConstants.SIDEBAR_BACKGROUND);
            sidebar.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, UIConstants.SIDEBAR_HEIGHT));
            sidebar.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            // Sidebar buttons
            JButton userBtn = createSidebarButton("USER");
            JButton homeBtn = createSidebarButton("HOME");
            JButton searchBtn = createSidebarButton("SEARCH");
            JButton wishlistBtn = createSidebarButton("WISHLIST");
            sidebar.add(userBtn);
            sidebar.add(Box.createVerticalStrut(20));
            sidebar.add(homeBtn);
            sidebar.add(Box.createVerticalStrut(20));
            sidebar.add(searchBtn);
            sidebar.add(Box.createVerticalStrut(20));
            sidebar.add(wishlistBtn);
            // Main content with CardLayout
            JPanel mainContent = new JPanel(new CardLayout());
            mainContent.setBackground(UIConstants.BACKGROUND_COLOR);
            UserProfilePanel userProfilePanel = new UserProfilePanel();
            HomePanel homePanel = new HomePanel();
            SearchPanel searchPanel = new SearchPanel();
            WishlistPanel wishlistPanel = new WishlistPanel();
            mainContent.add(userProfilePanel, "USER");
            mainContent.add(homePanel, "HOME");
            mainContent.add(searchPanel, "SEARCH");
            mainContent.add(wishlistPanel, "WISHLIST");
            // Add to root panel
            rootPanel.add(sidebar, BorderLayout.WEST);
            rootPanel.add(mainContent, BorderLayout.CENTER);
            frame.setContentPane(rootPanel);
            // Show Home panel by default
            ((CardLayout) mainContent.getLayout()).show(mainContent, "HOME");
            // Button actions
            userBtn.addActionListener(e -> switchPanel(mainContent, "USER"));
            homeBtn.addActionListener(e -> switchPanel(mainContent, "HOME"));
            searchBtn.addActionListener(e -> switchPanel(mainContent, "SEARCH"));
            wishlistBtn.addActionListener(e -> switchPanel(mainContent, "WISHLIST"));

            frame.setVisible(true);
        });
    }
    private static JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setForeground(UIConstants.ACCENT_COLOR);
        button.setBackground(UIConstants.SIDEBAR_BACKGROUND);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(UIConstants.BUTTON_WIDTH, UIConstants.BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_COLOR, 2, true));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(UIConstants.ACCENT_ORANGE);
                button.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_ORANGE, 2, true));
                button.setBackground(UIConstants.HOVER_COLOR);
                button.setOpaque(true);
                button.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent evt) {
                button.setForeground(UIConstants.ACCENT_COLOR);
                button.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_COLOR, 2, true));
                button.setBackground(UIConstants.SIDEBAR_BACKGROUND);
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }
        });
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.ACCENT_COLOR, 2, true),
            BorderFactory.createEmptyBorder(8, 24, 8, 24)
        ));
        return button;
    }
    private static void switchPanel(JPanel mainContent, String panelName) {
        CardLayout cl = (CardLayout) (mainContent.getLayout());
        cl.show(mainContent, panelName);
    }
    private static ImageIcon loadUserIcon() {
        try {
            URL iconUrl = AppLauncher.class.getResource("/user_icon.png");
            if (iconUrl != null) {
                return new ImageIcon(iconUrl);
            }
        } catch (Exception e) {
            System.err.println("Error loading user icon: " + e.getMessage());
        }
        return null;
    }
}