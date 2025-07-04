package ui;

import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import db.UserDAO;
import model.User;
import utils.PasswordValidator;
import utils.SessionManager;
import utils.UIConstants;

public class AuthDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private UserDAO userDAO;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AuthDialog(Frame owner) {
        super(owner, "Login", true);
        userDAO = new UserDAO();
        initComponents();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setSize(400, 300);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        // Login Panel
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "LOGIN");

        // Signup Panel
        JPanel signupPanel = createSignupPanel();
        mainPanel.add(signupPanel, "SIGNUP");

        add(mainPanel);

        // Show login panel by default
        cardLayout.show(mainPanel, "LOGIN");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(UIConstants.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(UIConstants.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(e -> handleLogin());

        signupButton = new JButton("Sign Up");
        styleButton(signupButton);
        signupButton.addActionListener(e -> cardLayout.show(mainPanel, "SIGNUP"));

        buttonsPanel.add(loginButton);
        buttonsPanel.add(signupButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonsPanel, gbc);

        return panel;
    }

    private JPanel createSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(UIConstants.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);

        JTextField signupUsernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(signupUsernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(UIConstants.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        JPasswordField signupPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(signupPasswordField, gbc);

        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(UIConstants.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        JButton createAccountButton = new JButton("Create Account");
        styleButton(createAccountButton);
        createAccountButton.addActionListener(e -> {
            String username = signupUsernameField.getText();
            String password = new String(signupPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("All fields are required");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match");
                return;
            }

            if (!PasswordValidator.isValid(password)) {
                showError("Password must be at least 8 characters long and contain at least one number");
                return;
            }

            User newUser = userDAO.createUser(username, password);
            if (newUser != null) {
                SessionManager.getInstance().login(newUser.getId(), newUser.getUsername());
                dispose();
            } else {
                showError("Username already exists");
            }
        });

        JButton backToLoginButton = new JButton("Back to Login");
        styleButton(backToLoginButton);
        backToLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        buttonsPanel.add(createAccountButton);
        buttonsPanel.add(backToLoginButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonsPanel, gbc);

        return panel;
    }
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required");
            return;
        }

        User user = userDAO.authenticateUser(username, password);
        if (user != null) {
            SessionManager.getInstance().login(user.getId(), user.getUsername());
            dispose();
        } else {
            showError("Invalid username or password");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
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
}