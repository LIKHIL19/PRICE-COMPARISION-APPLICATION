package ui;

import db.ProductDAO;
import db.WishlistDAO;
import model.Product;
import utils.SessionManager;
import utils.UIConstants;
import utils.BrowserLauncher;
import utils.WishlistEventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SearchPanel extends JPanel implements WishlistEventBus.WishlistListener {
    private JTextField searchField;
    private JPanel resultsPanel;
    private JProgressBar progressBar;
    private ProductDAO productDAO;
    private WishlistDAO wishlistDAO;
    private NumberFormat currencyFormat;

    public SearchPanel() {
        productDAO = new ProductDAO();
        wishlistDAO = new WishlistDAO();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        initComponents();
        WishlistEventBus.addListener(this);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConstants.BACKGROUND_COLOR);

        // Search Panel with gradient background
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = UIConstants.ACCENT_COLOR;
                Color color2 = UIConstants.ACCENT_PURPLE;
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        searchPanel.setOpaque(false);
        searchPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(UIConstants.SEARCH_FIELD_WIDTH, UIConstants.SEARCH_FIELD_HEIGHT));
        searchField.setFont(UIConstants.NORMAL_FONT);
        searchField.setBackground(UIConstants.PANEL_BACKGROUND);
        searchField.setForeground(UIConstants.ACCENT_COLOR);
        searchField.setCaretColor(UIConstants.ACCENT_COLOR);
        searchField.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_COLOR, 2, true));
        
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, UIConstants.SEARCH_FIELD_HEIGHT));
        styleButton(searchButton);
        searchButton.setBackground(UIConstants.ACCENT_COLOR);
        searchButton.setForeground(Color.BLACK);
        searchButton.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_COLOR, 2, true));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(400, 20));
        progressBar.setForeground(UIConstants.ACCENT_ORANGE);
        progressBar.setBackground(UIConstants.PANEL_BACKGROUND);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(progressBar);

        // Results Panel with ScrollPane
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBackground(UIConstants.BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        searchButton.addActionListener(e -> performSearch());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a search term",
                "Search Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        progressBar.setVisible(true);
        resultsPanel.removeAll();

        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() {
                return productDAO.searchProducts(query);
            }

            @Override
            protected void done() {
                try {
                    List<Product> products = get();
                    displayResults(products);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(SearchPanel.this,
                        "Error performing search: " + e.getMessage(),
                        "Search Error",
                        JOptionPane.ERROR_MESSAGE);
                } finally {
                    progressBar.setVisible(false);
                }
            }
        };
        worker.execute();
    }

    private void displayResults(List<Product> products) {
        resultsPanel.removeAll();
        if (products.isEmpty()) {
            JLabel noResults = new JLabel("No products found");
            noResults.setForeground(UIConstants.TEXT_COLOR);
            noResults.setFont(UIConstants.SUBTITLE_FONT);
            noResults.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(noResults);
        } else {
            for (Product product : products) {
                resultsPanel.add(createProductPanel(product));
                resultsPanel.add(Box.createVerticalStrut(20));
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
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

        // Wishlist Button
        JButton wishlistButton = new JButton(isInWishlist(product.getId()) ? "Remove from Wishlist" : "Add to Wishlist");
        styleButton(wishlistButton);
        wishlistButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        wishlistButton.setBackground(UIConstants.ACCENT_ORANGE);
        wishlistButton.setForeground(Color.BLACK);
        wishlistButton.setBorder(BorderFactory.createLineBorder(UIConstants.ACCENT_ORANGE, 2, true));
        wishlistButton.addActionListener(e -> toggleWishlist(product.getId(), wishlistButton));

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
        panel.add(wishlistButton);

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

    private boolean isInWishlist(int productId) {
        return wishlistDAO.isInWishlist(SessionManager.getInstance().getUserId(), productId);
    }

    private void toggleWishlist(int productId, JButton wishlistButton) {
        int userId = SessionManager.getInstance().getUserId();
        boolean isCurrentlyInWishlist = isInWishlist(productId);
        boolean success;
        if (isCurrentlyInWishlist) {
            success = wishlistDAO.removeFromWishlist(userId, productId);
            if (success) {
                wishlistButton.setText("Add to Wishlist");
                WishlistEventBus.notifyWishlistChanged();
            }
        } else {
            success = wishlistDAO.addToWishlist(userId, productId);
            if (success) {
                wishlistButton.setText("Remove from Wishlist");
                WishlistEventBus.notifyWishlistChanged();
            }
        }
        if (!success) {
            JOptionPane.showMessageDialog(this,
                "Failed to " + (isCurrentlyInWishlist ? "remove from" : "add to") + " wishlist",
                "Wishlist Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onWishlistChanged() {
        // Optionally refresh search results if you want to update wishlist button states
        if (!searchField.getText().trim().isEmpty()) {
            performSearch();
        }
    }

    public void setCategorySearch(String category) {
        searchField.setText(category);
        performSearch();
    }
}
