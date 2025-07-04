package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.UIConstants;

public class HomePanel extends JPanel {
    public HomePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Price Comparison Platform");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Find the best deals across multiple platforms");
        subtitleLabel.setFont(UIConstants.SUBTITLE_FONT);
        subtitleLabel.setForeground(UIConstants.TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(subtitleLabel);

        // Features Panel
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new GridLayout(1, 3, 20, 0));
        featuresPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Feature 1
        JPanel feature1 = createFeaturePanel(
            "Compare Prices",
            "Compare prices across Amazon, Flipkart, and Croma",
            UIConstants.ACCENT_COLOR
        );

        // Feature 2
        JPanel feature2 = createFeaturePanel(
            "Track Prices",
            "Add products to your wishlist and track price changes",
            UIConstants.ACCENT_ORANGE
        );

        // Feature 3
        JPanel feature3 = createFeaturePanel(
            "Best Deals",
            "Find the best deals and save money on your purchases",
            UIConstants.ACCENT_PURPLE
        );

        featuresPanel.add(feature1);
        featuresPanel.add(feature2);
        featuresPanel.add(feature3);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel footerLabel = new JLabel("Make shopping effortless and save money by PriceComparing!");
        footerLabel.setFont(UIConstants.NORMAL_FONT);
        footerLabel.setForeground(UIConstants.ACCENT_GREEN);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        footerPanel.add(footerLabel);

        // Add all panels
        add(headerPanel, BorderLayout.NORTH);
        add(featuresPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createFeaturePanel(String title, String description, Color accentColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><body style='text-align: center'>" + description + "</body></html>");
        descLabel.setFont(UIConstants.NORMAL_FONT);
        descLabel.setForeground(UIConstants.TEXT_COLOR);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(descLabel);

        return panel;
    }
}
