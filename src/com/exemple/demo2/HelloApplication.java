package com.exemple.demo2;

import com.exemple.demo2.model.Book;
import com.exemple.demo2.model.Library;
import com.exemple.demo2.model.Reader;
import com.exemple.demo2.model.User;
import com.exemple.demo2.pattern.*;
import com.exemple.demo2.storage.BookStorage;
import com.exemple.demo2.storage.UserStorage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private TreeView<String> bookTreeView;
    private TextArea infoArea;
    private Library library;
    private User currentUser;

    private TextField searchField;
    private ComboBox<String> searchTypeCombo;

    private ListView<String> borrowedBooksList;
    private Label badgeLabel;

    @Override
    public void start(Stage primaryStage) {
        library = Library.getInstance();
        Library.getInstance().clearBooks();
        Library.getInstance().clearUsers();
        BookStorage.loadBooks().forEach(library::addBook);
        UserStorage.loadUsers().forEach(library::addUser);

        showLoginWindow(primaryStage);
    }

    private void showLoginWindow(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");

        VBox loginBox = new VBox(10);
        loginBox.setStyle("-fx-padding: 20;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username...");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password...");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            currentUser = library.getUsers().stream()
                    .filter(u -> u.getUsername().equals(enteredUsername))
                    .findFirst()
                    .orElse(null);

            if (currentUser != null && currentUser.getPassword().equals(enteredPassword)) {
                loginStage.close();
                showMainWindow(primaryStage);
            } else {
                showAlert("Incorrect username or password!");
            }
        });

        loginBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);

        Scene loginScene = new Scene(loginBox, 300, 220);
        loginStage.setScene(loginScene);
        loginStage.show();
    }

    private void showMainWindow(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(1000, 700);

        // Top (Logout + Search)
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            BookStorage.saveBooks(library.getBooks());
            UserStorage.saveUsers(library.getUsers());
            primaryStage.close();
            start(new Stage());
        });
        HBox logoutBar = new HBox(10, logoutButton);
        logoutBar.setStyle("-fx-padding: 10;");

        HBox searchBar = setupSearchBar();
        VBox topArea = new VBox(10, logoutBar, searchBar);
        root.setTop(topArea);

        // Left (Books TreeView)
        bookTreeView = new TreeView<>();
        loadBookCategories();
        root.setLeft(bookTreeView);

        // Center (Book Details)
        infoArea = new TextArea();
        infoArea.setEditable(false);
        root.setCenter(infoArea);

        // Bottom (Borrow/Return Buttons)
        HBox buttonsBox = setupBorrowReturnButtons();
        root.setBottom(buttonsBox);

        // Right (Admin Panel + Reader Dashboard)
        VBox rightPanel = new VBox(20);
        rightPanel.setStyle("-fx-padding: 10;");

        if (currentUser.getRole().equalsIgnoreCase("Admin")) {
            VBox adminBox = setupAdminPanel();
            rightPanel.getChildren().add(adminBox);
        }

        if (currentUser instanceof Reader) {
            VBox readerDashboard = new VBox(10);
            readerDashboard.setStyle("-fx-padding: 10; -fx-background-color: #F5F5F5;");
            setupReaderDashboard(readerDashboard);
            rightPanel.getChildren().add(readerDashboard);
        }

        root.setRight(rightPanel);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            BookStorage.saveBooks(library.getBooks());
            UserStorage.saveUsers(library.getUsers());
        });
    }

    private HBox setupSearchBar() {
        searchField = new TextField();
        searchField.setPromptText("Enter search keyword...");
        searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("Title", "Author", "Category", "Publish Date");
        searchTypeCombo.getSelectionModel().selectFirst();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchBooks());

        HBox searchBar = new HBox(10, searchField, searchTypeCombo, searchButton);
        searchBar.setStyle("-fx-padding: 10; -fx-background-color: #E0E0E0;");
        return searchBar;
    }

    private HBox setupBorrowReturnButtons() {
        Button borrowButton = new Button("Borrow Book");
        Button returnButton = new Button("Return Book");
        borrowButton.setOnAction(e -> borrowSelectedBook());
        returnButton.setOnAction(e -> returnSelectedBook());

        HBox buttonsBox = new HBox(10, borrowButton, returnButton);
        buttonsBox.setStyle("-fx-padding: 10; -fx-alignment: center;");
        return buttonsBox;
    }

    private VBox setupAdminPanel() {
        VBox adminBox = new VBox(10);

        Button addBookButton = new Button("Add Book");
        Button removeBookButton = new Button("Remove Book");
        Button addUserButton = new Button("Add User");
        Button removeUserButton = new Button("Remove User");

        addBookButton.setOnAction(e -> addBook());
        removeBookButton.setOnAction(e -> removeBook());
        addUserButton.setOnAction(e -> addUser());
        removeUserButton.setOnAction(e -> removeUser());

        adminBox.getChildren().addAll(
                new Label("Admin Panel"),
                addBookButton,
                removeBookButton,
                addUserButton,
                removeUserButton
        );

        return adminBox;
    }

    private void setupReaderDashboard(VBox readerDashboard) {
        borrowedBooksList = new ListView<>();
        badgeLabel = new Label("Badge: None");
        updateReaderDashboard();

        readerDashboard.getChildren().addAll(
                new Label("Reader Dashboard"),
                new Label("Borrowed Books:"),
                borrowedBooksList,
                badgeLabel
        );
    }

    private void loadBookCategories() {
        TreeItem<String> rootItem = new TreeItem<>("Books");

        library.getBooks().stream()
                .map(Book::getCategory)
                .distinct()
                .forEach(category -> {
                    TreeItem<String> categoryItem = new TreeItem<>(category);
                    library.getBooks().stream()
                            .filter(book -> book.getCategory().equals(category))
                            .forEach(book -> categoryItem.getChildren().add(new TreeItem<>(book.getTitle())));
                    rootItem.getChildren().add(categoryItem);
                });

        bookTreeView.setRoot(rootItem);
        bookTreeView.setShowRoot(true);

        bookTreeView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                TreeItem<String> selected = bookTreeView.getSelectionModel().getSelectedItem();
                if (selected != null && selected.isLeaf()) {
                    displayBookInfo(selected.getValue());
                }
            }
        });
    }

    private void displayBookInfo(String title) {
        Book book = library.getBooks().stream()
                .filter(b -> b.getTitle().equals(title))
                .findFirst()
                .orElse(null);

        if (book != null) {
            infoArea.setText(
                    "Title: " + book.getTitle() + "\n" +
                            "Author: " + book.getAuthor() + "\n" +
                            "Category: " + book.getCategory() + "\n" +
                            "Published: " + book.getPublishDate() + "\n" +
                            "State: " + (book.getState().getClass().getSimpleName())
            );
        }
    }

    private void borrowSelectedBook() {
        if (currentUser instanceof Reader) {
            TreeItem<String> selected = bookTreeView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.isLeaf()) {
                String title = selected.getValue();
                Book book = library.getBooks().stream()
                        .filter(b -> b.getTitle().equals(title))
                        .findFirst()
                        .orElse(null);
                if (book != null) {
                    String message = ((Reader) currentUser).borrowBook(book);
                    if (!message.equals("ok")){
                        showAlert(message);
                    }
                    displayBookInfo(title);
                    updateReaderDashboard();
                }
            }
        } else {
            showAlert("Only Readers can borrow books!");
        }
    }

    private void returnSelectedBook() {
        if (currentUser instanceof Reader) {
            TreeItem<String> selected = bookTreeView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.isLeaf()) {
                String title = selected.getValue();
                Book book = library.getBooks().stream()
                        .filter(b -> b.getTitle().equals(title))
                        .findFirst()
                        .orElse(null);
                if (book != null) {
                    String message = ((Reader) currentUser).returnBook(book);
                    if (!message.equals("ok")){
                        showAlert(message);
                    }
                    displayBookInfo(title);
                    updateReaderDashboard();
                }
            }
        } else {
            showAlert("Only Readers can return books!");
        }
    }

    private void updateReaderDashboard() {
        if (currentUser instanceof Reader) {
            Reader reader = (Reader) currentUser;
            borrowedBooksList.getItems().clear();
            reader.getBorrowedBooks().forEach(book -> borrowedBooksList.getItems().add(book.getTitle()));
            badgeLabel.setText("Badge: " + reader.getBadge()+" Books Score: " + reader.getReaderScore());
        }
    }

    private void searchBooks() {
        String keyword = searchField.getText().trim();
        String type = searchTypeCombo.getValue();

        if (keyword.isEmpty()) {
            showAlert("Please enter a search keyword.");
            return;
        }

        SearchStrategy strategy;
        switch (type) {
            case "Author":
                strategy = new SearchByAuthor();
                break;
            case "Category":
                strategy = new SearchByCategory();
                break;
            case "Publish Date":
                strategy = new SearchByDate();
                break;
            default:
                strategy = new SearchByTitle();
        }

        var results = strategy.search(library.getBooks(), keyword);

        if (results.isEmpty()) {
            showAlert("No books found!");
        } else {
            TreeItem<String> rootItem = new TreeItem<>("Search Results");
            for (Book book : results) {
                rootItem.getChildren().add(new TreeItem<>(book.getTitle()));
            }
            bookTreeView.setRoot(rootItem);
            bookTreeView.setShowRoot(true);
        }
    }

    private void addBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Add New Book - Enter details separated by |");
        dialog.setContentText("Format: title|author|category|publishDate");

        dialog.showAndWait().ifPresent(input -> {
            String[] parts = input.split("\\|");
            if (parts.length == 4) {
                Book newBook = new Book(parts[0], parts[1], parts[2], parts[3]);
                library.addBook(newBook);
                loadBookCategories();
                showAlert("Book added successfully!");
            } else {
                showAlert("Invalid input format.");
            }
        });
    }

    private void removeBook() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Remove Book - Enter the title");
        dialog.setContentText("Title:");

        dialog.showAndWait().ifPresent(title -> {
            Book book = library.getBooks().stream()
                    .filter(b -> b.getTitle().equalsIgnoreCase(title))
                    .findFirst()
                    .orElse(null);
            if (book != null) {
                library.removeBook(book);
                loadBookCategories();
                showAlert("Book removed successfully!");
            } else {
                showAlert("Book not found!");
            }
        });
    }

    private void addUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Add New User - Enter details separated by |");
        dialog.setContentText("Format: type(Admin/Reader)|username|password");

        dialog.showAndWait().ifPresent(input -> {
            String[] parts = input.split("\\|");
            if (parts.length == 3) {
                try {
                    User newUser = UserFactory.createUser(parts[0], parts[1], parts[2]);
                    library.addUser(newUser);
                    showAlert("User added successfully!");
                } catch (IllegalArgumentException e) {
                    showAlert(e.getMessage());
                }
            } else {
                showAlert("Invalid input format.");
            }
        });
    }

    private void removeUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Remove User - Enter the username");
        dialog.setContentText("Username:");

        dialog.showAndWait().ifPresent(username -> {
            User user = library.getUsers().stream()
                    .filter(u -> u.getUsername().equalsIgnoreCase(username))
                    .findFirst()
                    .orElse(null);
            if (user != null) {
                library.removeUser(user);
                showAlert("User removed successfully!");
            } else {
                showAlert("User not found!");
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}