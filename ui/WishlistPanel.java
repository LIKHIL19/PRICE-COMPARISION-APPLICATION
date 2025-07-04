package ui;

import db.WishlistDAO;
import model.Product;
import utils.BrowserLauncher;
import utils.SessionManager;
import utils.UIConstants;
import utils.WishlistEventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WishlistPanel extends JPanel implements WishlistEventBus.WishlistListener {
    private JPanel wishlistPanel;
    private WishlistDAO wishlistDAO;
    private JProgressBar progressBar;
    private ExecutorService executorService;
    private NumberFormat currencyFormat;

    public WishlistPanel() {
        wishlistDAO = new WishlistDAO();
        executorService = Executors.newSingleThreadExecutor();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        initComponents();
        WishlistEventBus.addListener(this);
        loadWishlist();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BACKGROUND_COLOR);

        // Title
        JLabel titleLabel = new JLabel("My Wishlist");
        titleLabel.setFont(UIConstants.SUBTITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(400, 20));

        // Wishlist panel
        wishlistPanel = new JPanel();
        wishlistPanel.setLayout(new BoxLayout(wishlistPanel, BoxLayout.Y_AXIS));
        wishlistPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(wishlistPanel);
        scrollPane.setBackground(UIConstants.BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        add(progressBar, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadWishlist() {
        if (!SessionManager.getInstance().isLoggedIn()) {
            showLoginMessage();
            return;
        }

        showLoading(true);
        executorService.submit(() -> {
            try {
                int userId = SessionManager.getInstance().getUserId();
                List<Product> wishlist = wishlistDAO.getWishlist(userId);
                SwingUtilities.invokeLater(() -> {
                    displayWishlist(wishlist);
                    showLoading(false);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    showError("Error loading wishlist: " + e.getMessage());
                    showLoading(false);
                });
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisible(show);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Wishlist Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private void showLoginMessage() {
        wishlistPanel.removeAll();
        JLabel messageLabel = new JLabel("Please login to view your wishlist");
        messageLabel.setFont(UIConstants.NORMAL_FONT);
        messageLabel.setForeground(UIConstants.TEXT_COLOR);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wishlistPanel.add(messageLabel);
        wishlistPanel.revalidate();
        wishlistPanel.repaint();
    }

    private void displayWishlist(List<Product> products) {
        wishlistPanel.removeAll();
        
        if (products.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your wishlist is empty");
            emptyLabel.setFont(UIConstants.NORMAL_FONT);
            emptyLabel.setForeground(UIConstants.TEXT_COLOR);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            wishlistPanel.add(emptyLabel);
        } else {
            for (Product product : products) {
                JPanel productPanel = createProductPanel(product);
                wishlistPanel.add(productPanel);
                wishlistPanel.add(Box.createVerticalStrut(20));
            }
        }
        
        wishlistPanel.revalidate();
        wishlistPanel.repaint();
    }

    private JPanel createProductPanel(Product product) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panel.setMaximumSize(new Dimension(800, 400));

        // Product Name
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(UIConstants.SUBTITLE_FONT);
        nameLabel.setForeground(UIConstants.TEXT_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel(product.getDescription());
        descLabel.setFont(UIConstants.NORMAL_FONT);
        descLabel.setForeground(UIConstants.TEXT_COLOR);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Category
        JLabel categoryLabel = new JLabel("Category: " + product.getCategory());
        categoryLabel.setFont(UIConstants.NORMAL_FONT);
        categoryLabel.setForeground(UIConstants.TEXT_COLOR);
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Prices Panel
        JPanel pricesPanel = new JPanel();
        pricesPanel.setLayout(new BoxLayout(pricesPanel, BoxLayout.Y_AXIS));
        pricesPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        pricesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add all platform prices
        for (Product.ProductPrice price : product.getPrices()) {
            JPanel priceRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            priceRow.setBackground(UIConstants.PANEL_BACKGROUND);
            
            JLabel platformLabel = new JLabel(price.getPlatform() + ": ");
            platformLabel.setFont(UIConstants.NORMAL_FONT);
            platformLabel.setForeground(UIConstants.ACCENT_ORANGE);
            
            JLabel priceLabel = new JLabel(currencyFormat.format(price.getPrice()));
            priceLabel.setFont(UIConstants.NORMAL_FONT);
            priceLabel.setForeground(UIConstants.ACCENT_GREEN);
            
            JButton buyButton = new JButton("Buy");
            styleButton(buyButton);
            buyButton.setBackground(UIConstants.ACCENT_COLOR);
            buyButton.setForeground(Color.BLACK);
            buyButton.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_COLOR, 2, true));
            buyButton.addActionListener(e -> BrowserLauncher.openWebpage(price.getUrl()));
            
            priceRow.add(platformLabel);
            priceRow.add(priceLabel);
            priceRow.add(buyButton);
            pricesPanel.add(priceRow);
        }

        // Best Price Panel
        JPanel bestPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bestPricePanel.setBackground(UIConstants.PANEL_BACKGROUND);
        bestPricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bestPriceLabel = new JLabel("Best Price: " + currencyFormat.format(product.getLowestPrice()) + 
                                         " at " + product.getBestPlatform());
        bestPriceLabel.setFont(UIConstants.BOLD_FONT);
        bestPriceLabel.setForeground(UIConstants.ACCENT_PURPLE);
        bestPricePanel.add(bestPriceLabel);

        // Remove Button
        JButton removeButton = new JButton("Remove from Wishlist");
        styleButton(removeButton);
        removeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        removeButton.setBackground(UIConstants.ACCENT_ORANGE);
        removeButton.setForeground(Color.BLACK);
        removeButton.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_ORANGE, 2, true));
        removeButton.addActionListener(e -> removeFromWishlist(product));

        // Add components to panel
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(categoryLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(pricesPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(bestPricePanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(removeButton);

        return panel;
    }

    private void styleButton(JButton button) {
        button.setFont(UIConstants.BUTTON_FONT);
        button.setForeground(UIConstants.TEXT_COLOR);
        button.setBackground(UIConstants.PANEL_BACKGROUND);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(UIConstants.HOVER_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIConstants.PANEL_BACKGROUND);
            }
        });
    }

    private void removeFromWishlist(Product product) {
        showLoading(true);
        executorService.submit(() -> {
            try {
                int userId = SessionManager.getInstance().getUserId();
                if (wishlistDAO.removeFromWishlist(userId, product.getId())) {
                    SwingUtilities.invokeLater(() -> {
                        loadWishlist(); // Refresh the wishlist
                        showLoading(false);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        showError("Failed to remove item from wishlist");
                        showLoading(false);
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    showError("Error removing item: " + e.getMessage());
                    showLoading(false);
                });
            }
        });
    }

    @Override
    public void onWishlistChanged() {
        loadWishlist();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        executorService.shutdown();
    }
}
