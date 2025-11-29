package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    public static void login() {
        // Initialize files
        FileHandler.initializeFiles();
        
        // Create fullscreen window
        JFrame f = new JFrame("Library Management System");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create main panel with image background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw background image
                try {
                    ImageIcon imageIcon = new ImageIcon("library.jpg");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    // If image not found, use solid color
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        // ✅ FIX: Create loginPanel (was missing this line!)
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false); // Transparent
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Login to Your Account", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        subtitleLabel.setForeground(Color.YELLOW);
        gbc.gridy = 1;
        loginPanel.add(subtitleLabel, gbc);
        
        // Spacer
        gbc.gridy = 2;
        loginPanel.add(Box.createVerticalStrut(80), gbc);
        
        // Username
        JLabel userLabel = new JLabel("USERNAME:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 28));
        userLabel.setForeground(Color.YELLOW);
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(userLabel, gbc);
        
        JTextField userField = new JTextField();
        userField.setFont(new Font("Arial", Font.PLAIN, 24));
        userField.setPreferredSize(new Dimension(400, 60));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(userField, gbc);
        
        // Password
        JLabel passLabel = new JLabel("PASSWORD:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 28));
        passLabel.setForeground(Color.YELLOW);
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(passLabel, gbc);
        
        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Arial", Font.PLAIN, 24));
        passField.setPreferredSize(new Dimension(400, 60));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(passField, gbc);
        
        // Login button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 28));
        loginButton.setBackground(new Color(139, 69, 19));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 70));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);
        
        // Default credentials hint
        JLabel hintLabel = new JLabel("Default: admin / admin", JLabel.CENTER);
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        hintLabel.setForeground(Color.YELLOW);
        gbc.gridy = 6;
        loginPanel.add(hintLabel, gbc);
        
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Please enter username");
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Please enter password");
                } else {
                    if (FileHandler.validateUser(username, password)) {
                        String[] userInfo = FileHandler.getUserInfo(username, password);
                        f.dispose();
                        if (userInfo[3].equals("1")) {
                            Admin_Menu.admin_menu();
                        } else {
                            User_Menu.user_menu(userInfo[0]);
                        }
                    } else {
                        JOptionPane.showMessageDialog(f, "Wrong Username/Password!");
                    }
                }
            }
        });
        
        // Add login panel to main panel
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        f.add(mainPanel);
        f.setVisible(true);
        
        // Focus on username field
        userField.requestFocus();
    }
}