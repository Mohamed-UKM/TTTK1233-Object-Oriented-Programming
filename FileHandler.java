package lib;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class FileHandler {
    // File paths
    private static final String USERS_FILE = "users.txt";
    private static final String BOOKS_FILE = "books.txt";
    private static final String ISSUED_FILE = "issued.txt";
    
    // Initialize files with sample data
    public static void initializeFiles() {
        try {
            // Create users file with admin if doesn't exist
            File usersFile = new File(USERS_FILE);
            if (!usersFile.exists()) {
                PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE));
                writer.println("1,admin,admin,1"); // UID,Username,Password,Admin(1=true,0=false)
                writer.println("2,user1,password,0"); // Sample user
                writer.println("3,user2,password,0"); // Sample user
                writer.close();
            }
            
            // Create books file with sample data if doesn't exist
            File booksFile = new File(BOOKS_FILE);
            if (!booksFile.exists()) {
                PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE));
                writer.println("1,War and Peace,Mystery,200");
                writer.println("2,The Guest Book,Fiction,300");
                writer.println("3,The Perfect Murder,Mystery,150");
                writer.println("4,Accidental Presidents,Biography,250");
                writer.println("5,The Wicked King,Fiction,350");
                writer.close();
            }
            
            // Create empty issued file if doesn't exist
            File issuedFile = new File(ISSUED_FILE);
            if (!issuedFile.exists()) {
                issuedFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // User methods
    public static boolean validateUser(String username, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(username) && parts[2].equals(password)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static String[] getUserInfo(String username, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(username) && parts[2].equals(password)) {
                    reader.close();
                    return parts; // [UID, Username, Password, Admin]
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void addUser(String username, String password, boolean isAdmin) {
        try {
            // Find max UID
            int maxUID = 0;
            BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int uid = Integer.parseInt(parts[0]);
                if (uid > maxUID) maxUID = uid;
            }
            reader.close();
            
            // Add new user
            PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE, true));
            writer.println((maxUID + 1) + "," + username + "," + password + "," + (isAdmin ? "1" : "0"));
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Book methods
    public static List<String[]> getAllBooks() {
        List<String[]> books = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                books.add(line.split(",")); // [BID, BName, Genre, Price]
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    public static void addBook(String name, String genre, int price) {
        try {
            // Find max BID
            int maxBID = 0;
            BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int bid = Integer.parseInt(parts[0]);
                if (bid > maxBID) maxBID = bid;
            }
            reader.close();
            
            // Add new book
            PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE, true));
            writer.println((maxBID + 1) + "," + name + "," + genre + "," + price);
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Book methods - Data Retrieval for JTable
    /**
     * Retrieves all book data formatted for use in a JTable.
     * Columns: ID, Name, Genre, Price.
     * @return Object[][] array containing book details.
     */
    public static Object[][] getBookData() {
        List<Object[]> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Use -1 limit to ensure empty fields are kept
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4) {
                        // Assuming BID, Name, Genre, Price
                        dataList.add(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][]);
    }
    
    // Book availability check
    public static boolean isBookAvailable(String bookId) {
        try {
            // First check if book exists
            boolean bookExists = false;
            BufferedReader bookReader = new BufferedReader(new FileReader(BOOKS_FILE));
            String line;
            while ((line = bookReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(bookId)) {
                    bookExists = true;
                    break;
                }
            }
            bookReader.close();
            
            if (!bookExists) return false;
            
            // Check if book is currently issued and not returned
            BufferedReader issuedReader = new BufferedReader(new FileReader(ISSUED_FILE));
            while ((line = issuedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    // IID, UID, BID, IssueDate, ReturnDate, , Period, Fine
                    // parts[4] is ReturnDate. If empty, book is currently issued.
                    if (parts[2].equals(bookId) && (parts.length < 5 || parts[4].isEmpty())) {
                        issuedReader.close();
                        return false; // Book is currently borrowed
                    }
                }
            }
            issuedReader.close();
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Issue methods
    public static void issueBook(int uid, int bid, String issueDate, int period) {
        try {
            // Find max IID
            int maxIID = 0;
            BufferedReader reader = new BufferedReader(new FileReader(ISSUED_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    int iid = Integer.parseInt(parts[0]);
                    if (iid > maxIID) maxIID = iid;
                }
            }
            reader.close();
            
            // Add new issue
            PrintWriter writer = new PrintWriter(new FileWriter(ISSUED_FILE, true));
            // IID, UID, BID, IssueDate, ReturnDate, , Period, Fine
            writer.println((maxIID + 1) + "," + uid + "," + bid + "," + issueDate + ",,," + period + ",0");
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // New method for issuing book with current date
    public static boolean issueBook(String uid, String bookId, int period) {
        try {
            // Check if book exists and is available
            if (!isBookAvailable(bookId)) {
                return false;
            }
            
            // Get current date
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            
            // Find max IID
            int maxIID = 0;
            BufferedReader reader = new BufferedReader(new FileReader(ISSUED_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    int iid = Integer.parseInt(parts[0]);
                    if (iid > maxIID) maxIID = iid;
                }
            }
            reader.close();
            
            // Add new issue
            PrintWriter writer = new PrintWriter(new FileWriter(ISSUED_FILE, true));
            // IID, UID, BID, IssueDate, ReturnDate, , Period, Fine
            writer.println((maxIID + 1) + "," + uid + "," + bookId + "," + currentDate + ",,," + period + ",0");
            writer.close();
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<String[]> getUserIssuedBooks(int uid) {
        List<String[]> issuedBooks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ISSUED_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (Integer.parseInt(parts[1]) == uid) {
                        issuedBooks.add(parts);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return issuedBooks;
    }
    
    // Overloaded method for String UID
    public static List<String[]> getUserIssuedBooks(String uid) {
        return getUserIssuedBooks(Integer.parseInt(uid));
    }
    
    public static List<String[]> getAllIssuedBooks() {
        List<String[]> issuedBooks = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ISSUED_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    issuedBooks.add(line.split(","));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return issuedBooks;
    }
    
    public static void returnBook(int iid, String returnDate, int fine) {
        try {
            // Read all lines
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(ISSUED_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
            reader.close();
            
            // Update the specific line
            PrintWriter writer = new PrintWriter(new FileWriter(ISSUED_FILE));
            for (String l : lines) {
                String[] parts = l.split(",");
                if (Integer.parseInt(parts[0]) == iid) {
                    // IID, UID, BID, IssueDate, ReturnDate, , Period, Fine
                    writer.println(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + returnDate + "," + parts[5] + "," + parts[6] + "," + fine);
                } else {
                    writer.println(l);
                }
            }
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // New method for returning book with current date
    public static boolean returnBook(String uid, String issueId) {
        try {
            // Get current date
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            
            // Read all lines
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(ISSUED_FILE));
            String line;
            boolean found = false;
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    // Check if this is the issue record and it belongs to the user and is not already returned
                    // Check parts[4] (ReturnDate) for emptiness
                    if (parts[0].equals(issueId) && parts[1].equals(uid) && (parts.length < 5 || parts[4].isEmpty())) {
                        // Calculate fine if any (simple implementation - you can enhance this)
                        int fine = calculateFine(parts[3], parts[6], currentDate);
                        // IID, UID, BID, IssueDate, ReturnDate, , Period, Fine
                        lines.add(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + currentDate + "," + parts[5] + "," + parts[6] + "," + fine);
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
            reader.close();
            
            if (found) {
                // Write all lines back
                PrintWriter writer = new PrintWriter(new FileWriter(ISSUED_FILE));
                for (String l : lines) {
                    writer.println(l);
                }
                writer.close();
                return true;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Helper method to calculate fine
    private static int calculateFine(String issueDate, String period, String returnDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date issue = sdf.parse(issueDate);
            Date returnD = sdf.parse(returnDate);
            
            long diff = returnD.getTime() - issue.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            int borrowPeriod = Integer.parseInt(period);
            
            // Fine calculation: 10 QAR per day after due date
            if (days > borrowPeriod) {
                return (int)((days - borrowPeriod) * 10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Check if user exists
    public static boolean userExists(String uid) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(uid)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Check if issue exists and is valid for return
    public static boolean canReturnBook(String uid, String issueId) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ISSUED_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    // IID, UID, BID, IssueDate, ReturnDate, , Period, Fine
                    // Check parts[4] (ReturnDate) for emptiness
                    if (parts[0].equals(issueId) && parts[1].equals(uid) && (parts.length < 5 || parts[4].isEmpty())) {
                        reader.close();
                        return true;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void resetDatabase() {
        try {
            // Delete existing files
            new File(USERS_FILE).delete();
            new File(BOOKS_FILE).delete();
            new File(ISSUED_FILE).delete();
            
            // Reinitialize with default data
            initializeFiles();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Updates a specific field (Name, Genre, or Price) for a book in the books file.
     * @param bookId The ID of the book to update.
     * @param col The column index (1 for Name, 2 for Genre, 3 for Price).
     * @param newValue The new value for the field.
     */
    public static void updateBookDetails(String bookId, int col, String newValue) {
        // Column indices: 0: ID, 1: Name, 2: Genre, 3: Price
        if (col < 1 || col > 3) return; // Only allow editing Name, Genre, Price
        
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                // Use -1 limit to ensure empty fields (if allowed) are kept
                String[] parts = line.split(",", -1); 
                if (parts.length >= 4 && parts[0].equals(bookId)) {
                    // Found the book to update
                    parts[col] = newValue;
                    line = String.join(",", parts);
                }
                lines.add(line);
            }
            reader.close();

            // Overwrite the file with updated content
            PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE));
            for (String updatedLine : lines) {
                writer.println(updatedLine);
            }
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
 // Inside FileHandler.java

    /**
     * Checks if the given username already exists in the users.txt file.
     * @param username The username to check.
     * @return true if the username is unique, false otherwise.
     */
    public static boolean isUsernameUnique(String username) {
        // Use try-with-resources for automatic resource closing (cleaner code)
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by comma. The username is parts[1] (0:UID, 1:Username, 2:Password, 3:Admin)
                String[] parts = line.split(",", -1);
                
                // Check for valid line structure and if the username matches (case-insensitive)
                if (parts.length >= 4 && parts[1].equalsIgnoreCase(username)) {
                    return false; // Username found, so it is NOT unique
                }
            }
        } catch (IOException e) {
            // In case of a file error, print the stack trace and assume unique to prevent data loss
            e.printStackTrace();
        }
        return true; // Username is unique
    }
    
    /**
     * Deletes a book from the books file by Book ID
     * @param bookId The ID of the book to delete
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteBook(String bookId) {
        List<String> lines = new ArrayList<>();
        boolean bookFound = false;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE));
            String line;
            
            // Read all books and exclude the one to delete
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4 && parts[0].equals(bookId)) {
                        bookFound = true; // Mark that we found the book
                        continue; // Skip this line (don't add to list)
                    }
                    lines.add(line);
                }
            }
            reader.close();
            
            if (bookFound) {
                // Write all lines back except the deleted one
                PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE));
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
                writer.close();
            }
            
            return bookFound;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a user from the users file by User ID
     * @param userId The ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteUser(String userId) {
        List<String> lines = new ArrayList<>();
        boolean userFound = false;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE));
            String line;
            
            // Read all users and exclude the one to delete
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4 && parts[0].equals(userId)) {
                        userFound = true; // Mark that we found the user
                        continue; // Skip this line (don't add to list)
                    }
                    lines.add(line);
                }
            }
            reader.close();
            
            if (userFound) {
                // Write all lines back except the deleted one
                PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE));
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
                writer.close();
            }
            
            return userFound;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}