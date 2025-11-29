package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class User_Menu {
    public static void user_menu(String UID) {
        // Create fullscreen window
        JFrame f = new JFrame("User Dashboard - Library Management System");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel with background image
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon imageIcon = new ImageIcon("library.png");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        // --- NEW: Header and Control Panel ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        // Logout Button (Top Right)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        controlPanel.setOpaque(false);
        
        JButton logout_but = new JButton("Logout");
        styleControlButton(logout_but, new Color(220, 20, 60)); // Crimson
        logout_but.addActionListener(e -> {
            f.dispose();
            Login.login(); // Go back to login screen
        });
        controlPanel.add(logout_but);
        topPanel.add(controlPanel, BorderLayout.EAST);
        
        // Header (Moved to center of topPanel)
        JLabel headerLabel = new JLabel("USER DASHBOARD", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 48));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        topPanel.add(headerLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        // -------------------------------------
        
        // Center panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 30, 30));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));
        
        // View Books Button
        JButton view_but = new JButton("VIEW ALL BOOKS");
        styleControlButton(view_but, new Color(101, 84, 192));
        view_but.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showBooksWindow();
            }
        });
        
        // My Books Button
        JButton my_book = new JButton("MY BORROWED BOOKS");
        styleControlButton(my_book, new Color(101, 84, 192));
        my_book.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMyBooksWindow(UID);
            }
        });
        
        // --- FUNCTION 1: Issue Book Button ---
        JButton issue_but = new JButton("BORROW A BOOK");
        styleControlButton(issue_but, new Color(101, 84, 192));
        issue_but.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showIssueBookWindow(UID);
            }
        });
        
        // --- FUNCTION 2: Return Book Button ---
        JButton return_but = new JButton("RETURN A BOOK");
        styleControlButton(return_but, new Color(101, 84, 192));
        return_but.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showReturnBookWindow(UID);
            }
        });
        
        buttonPanel.add(view_but);
        buttonPanel.add(my_book);
        buttonPanel.add(issue_but);
        buttonPanel.add(return_but);
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("User ID: " + UID + " | © 2025 Library Management System", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        
        f.add(mainPanel);
        f.setVisible(true);
    }
    
    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 28)); 
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 80)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    // NEW: Style for the Logout/Back control buttons
    private static void styleControlButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(80, 35)); 
    }
    
    // --- Helper function for View All Books ---
    private static void showBooksWindow() {
        JFrame booksFrame = new JFrame("Available Books");
        booksFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon imageIcon = new ImageIcon("library.png");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        // --- NEW: Top Panel for Back Button and Title ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        // Back Button (Top Left)
        JButton back_but = new JButton("Back");
        styleControlButton(back_but, new Color(105, 105, 105)); // Dark Grey
        back_but.addActionListener(e -> booksFrame.dispose());
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        backPanel.setOpaque(false);
        backPanel.add(back_but);
        topPanel.add(backPanel, BorderLayout.WEST);

        // Title Label
        JLabel titleLabel = new JLabel("ALL AVAILABLE BOOKS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        // --------------------------------------------------
        
        List<String[]> books = FileHandler.getAllBooks();
        String[] columnNames = {"Book ID", "Book Name", "Genre", "Price (QAR)"};
        Object[][] data = new Object[books.size()][4];
        
        for (int i = 0; i < books.size(); i++) {
            String[] book = books.get(i);
            data[i][0] = book[0];
            data[i][1] = book[1];
            data[i][2] = book[2];
            data[i][3] = "QAR " + book[3];
        }
        
        JTable table = createNonEditableTable(data, columnNames);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        booksFrame.add(mainPanel);
        booksFrame.setVisible(true);
    }
    
    // --- Helper function for View My Books ---
    private static void showMyBooksWindow(String UID) {
        JFrame myBooksFrame = new JFrame("My Borrowed Books");
        myBooksFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon imageIcon = new ImageIcon("library.png");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        // --- NEW: Top Panel for Back Button and Title ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        // Back Button (Top Left)
        JButton back_but = new JButton("Back");
        styleControlButton(back_but, new Color(105, 105, 105)); // Dark Grey
        back_but.addActionListener(e -> myBooksFrame.dispose());
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        backPanel.setOpaque(false);
        backPanel.add(back_but);
        topPanel.add(backPanel, BorderLayout.WEST);
        
        // Title Label
        JLabel titleLabel = new JLabel("MY BORROWED BOOKS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        // --------------------------------------------------
        
        List<String[]> issuedBooks = FileHandler.getUserIssuedBooks(UID);
        // Note: issue[4] and issue[5] are Return Date and Reserved for future use (empty in your FileHandler)
        // We only want currently issued books for return, but display all for a full history.
        String[] columnNames = {"Issue ID", "Book ID", "Issue Date", "Period (Days)", "Return Date", "Fine (QAR)"};
        Object[][] data = new Object[issuedBooks.size()][6];
        
        for (int i = 0; i < issuedBooks.size(); i++) {
            String[] issue = issuedBooks.get(i);
            data[i][0] = issue[0]; // Issue ID
            data[i][1] = issue[2]; // Book ID
            data[i][2] = issue[3]; // Issue Date
            data[i][3] = issue[6]; // Period
            data[i][4] = issue[4].isEmpty() ? "NOT RETURNED" : issue[4]; // Return Date
            data[i][5] = "QAR " + issue[7]; // Fine
        }
        
        JTable table = createNonEditableTable(data, columnNames);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        myBooksFrame.add(mainPanel);
        myBooksFrame.setVisible(true);
    }

    // --- FUNCTION 1 IMPLEMENTATION: Borrow/Issue Book ---
    private static void showIssueBookWindow(String UID) {
        // Create new JFrame for issuing a book
        JFrame issueFrame = new JFrame("Borrow a Book");
        issueFrame.setSize(500, 300);
        issueFrame.setLayout(new GridBagLayout());
        issueFrame.setLocationRelativeTo(null);
        
        // Setup components
        JLabel title = new JLabel("BORROW BOOK (User ID: " + UID + ")");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        
        JLabel bookIdLabel = new JLabel("Book ID:");
        JTextField bookIdField = new JTextField(15);
        
        JLabel periodLabel = new JLabel("Period (Days):");
        String[] periods = {"7", "14", "21"}; // Typical loan periods
        JComboBox<String> periodComboBox = new JComboBox<>(periods);
        
        JButton issueButton = new JButton("Borrow");
        issueButton.setBackground(new Color(34, 139, 34)); // Forest Green
        issueButton.setForeground(Color.WHITE);
        issueButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Layout using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        issueFrame.add(title, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        issueFrame.add(bookIdLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        issueFrame.add(bookIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        issueFrame.add(periodLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        issueFrame.add(periodComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        issueFrame.add(issueButton, gbc);
        
        // Add action listener
        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookId = bookIdField.getText().trim();
                String periodStr = (String) periodComboBox.getSelectedItem();
                
                if (bookId.isEmpty()) {
                    JOptionPane.showMessageDialog(issueFrame, "Please enter a Book ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    int period = Integer.parseInt(periodStr);
                    
                    // Call the FileHandler method to issue the book
                    boolean success = FileHandler.issueBook(UID, bookId, period);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(issueFrame, 
                            "Book ID " + bookId + " successfully borrowed for " + period + " days!", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                        issueFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(issueFrame, 
                            "Failed to borrow book. Possible reasons:\n1. Book ID does not exist.\n2. Book is currently not available.", 
                            "Issue Failed", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(issueFrame, "Invalid Period selection.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        issueFrame.setVisible(true);
    }

    // --- FUNCTION 2 IMPLEMENTATION: Return Book ---
    private static void showReturnBookWindow(String UID) {
        // Create new JFrame for returning a book
        JFrame returnFrame = new JFrame("Return a Book");
        returnFrame.setSize(500, 250);
        returnFrame.setLayout(new GridBagLayout());
        returnFrame.setLocationRelativeTo(null);

        // Setup components
        JLabel title = new JLabel("RETURN BOOK (User ID: " + UID + ")");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel issueIdLabel = new JLabel("Issue ID to Return:");
        JTextField issueIdField = new JTextField(15);

        JButton returnButton = new JButton("Return Book");
        returnButton.setBackground(new Color(255, 69, 0)); // Orange Red
        returnButton.setForeground(Color.WHITE);
        returnButton.setFont(new Font("Arial", Font.BOLD, 16));

        // Layout using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        returnFrame.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        returnFrame.add(issueIdLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        returnFrame.add(issueIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        returnFrame.add(returnButton, gbc);

        // Add action listener
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String issueId = issueIdField.getText().trim();

                if (issueId.isEmpty()) {
                    JOptionPane.showMessageDialog(returnFrame, "Please enter the Issue ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check if return is valid before attempting
                if (!FileHandler.canReturnBook(UID, issueId)) {
                     JOptionPane.showMessageDialog(returnFrame, 
                        "Cannot return book: Invalid Issue ID, or book has already been returned, or it doesn't belong to this user.", 
                        "Return Failed", 
                        JOptionPane.ERROR_MESSAGE);
                     return;
                }

                // Call the FileHandler method to return the book
                boolean success = FileHandler.returnBook(UID, issueId);

                if (success) {
                    // Re-check fine calculation to display to the user
                    int fine = 0;
                    List<String[]> issuedBooks = FileHandler.getUserIssuedBooks(UID);
                    for (String[] issue : issuedBooks) {
                        if (issue[0].equals(issueId)) {
                            // issue[7] holds the fine (String format, should be parseable to int)
                            try {
                                fine = Integer.parseInt(issue[7]);
                            } catch (NumberFormatException ignored) {} // Should not happen if FileHandler is correct
                            break;
                        }
                    }

                    String message = "Book with Issue ID " + issueId + " successfully returned!";
                    if (fine > 0) {
                        message += "\n\nOVERDUE FINE: QAR " + fine;
                    }

                    JOptionPane.showMessageDialog(returnFrame, message, "Return Successful", JOptionPane.INFORMATION_MESSAGE);
                    returnFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(returnFrame, "An error occurred during the return process.", "Return Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        returnFrame.setVisible(true);
    }

    // --- NEW HELPER METHOD TO ENSURE TABLES ARE NON-EDITABLE ---
    private static JTable createNonEditableTable(Object[][] data, String[] columns) {
        // Use DefaultTableModel and override isCellEditable to return false
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make all cells non-editable
            }
        };

        JTable table = new JTable(model);
        // Apply styling to the new table
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
        // Add a slight background for visibility over the image background
        table.setBackground(new Color(255, 255, 255, 220)); 
        return table;
    }
}