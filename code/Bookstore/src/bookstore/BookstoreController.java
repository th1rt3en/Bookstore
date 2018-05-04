/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore;

import Model.*;
import DAO.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Negarr
 */
public class BookstoreController implements Initializable {

    @FXML
    private Pane bookpane;
    @FXML
    private TableView<Book> bookcase;
    @FXML
    private TableColumn<Book, Integer> id;
    @FXML
    private TableColumn<Book, String> title;
    @FXML
    private TableColumn<Book, String> author;
    @FXML
    private TableColumn<Book, String> isbn;
    @FXML
    private TableColumn<Book, Double> price;
    @FXML
    private TableColumn<Book, Integer> stock;
    @FXML
    private TableColumn<Book, String> category;
    @FXML
    private TableColumn<Book, Boolean> action;
    @FXML
    private TextField titleSearch;
    @FXML
    private TextField authorSearch;
    @FXML
    private TextField categorySearch;
    @FXML
    private Button view;
    @FXML
    private Button addbook;

    @FXML
    private Pane cartpane;
    @FXML
    private TableView<Item> cart;
    @FXML
    private TableColumn<Item, String> cartTitle;
    @FXML
    private TableColumn<Item, String> cartAuthor;
    @FXML
    private TableColumn<Item, Double> cartPrice;
    @FXML
    private TableColumn<Item, Integer> cartAmount;
    @FXML
    private TableColumn<Item, Double> cartSubtotal;
    @FXML
    private TableColumn<Item, Boolean> cartAction;
    @FXML
    private Button checkout;
    @FXML
    private Button clear;
    @FXML
    private Button back;

    private ArrayList<Item> items;

    private void editBook(Book book) throws SQLException, ClassNotFoundException {
        BookDAO bDAO = new BookDAO();
        bDAO.editBook(book);
        bookcase.setItems(getAllBooks());
    }

    private void removeBook(Book book) throws SQLException, ClassNotFoundException {
        BookDAO bDAO = new BookDAO();
        bDAO.removeBook(book);
        bookcase.setItems(getAllBooks());
    }

    private void addBook(Book book) throws SQLException, ClassNotFoundException {
        BookDAO bDAO = new BookDAO();
        bDAO.addBook(book);
        bookcase.setItems(getAllBooks());
    }

    private void orderItem(ArrayList<Item> items, Customer customer) throws SQLException, ClassNotFoundException {
        CustomerDAO cDAO = new CustomerDAO();
        if (!cDAO.customerExists(customer)) {
            customer.setId(cDAO.addCustomer(customer));
        } else {
            customer = cDAO.getCustomerByEmail(customer.getEmail());
        }

        BookDAO bDAO = new BookDAO();
        OrderDAO oDAO = new OrderDAO();
        
        Order order = new Order();
        order.setCustomerId(customer.getId());
        order.setDiscount(0);
        order.setStatus("Processing");
        order.setId(oDAO.placeOrder(order));
        
        for (Item item : items) {
            oDAO.placeOrderItem(order, item.getId(), item.getAmount());
            bDAO.orderBook(item.getId(), item.getAmount());
        }
    }

    private boolean bookExists(String isbn) throws SQLException, ClassNotFoundException {
        BookDAO bDAO = new BookDAO();
        return bDAO.bookExists(isbn);
    }

    private ObservableList<Item> getCartItems() {
        ObservableList itemList = FXCollections.observableArrayList();
        for (Item item : items) {
            itemList.add(item);
        }
        return itemList;
    }

    private ObservableList<Book> getAllBooks() throws SQLException, ClassNotFoundException {
        ObservableList books = FXCollections.observableArrayList();
        BookDAO bDAO = new BookDAO();
        for (Book book : bDAO.getAllBooks()) {
            books.add(book);
        }

        return books;
    }

    public void search() throws SQLException, ClassNotFoundException {
        ObservableList books = FXCollections.observableArrayList();
        BookDAO bDAO = new BookDAO();
        for (Book book : bDAO.getBooksByAttributes(titleSearch.getText(),
                authorSearch.getText(), categorySearch.getText())) {
            books.add(book);
        }
        bookcase.setItems(books);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleSearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                search();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        authorSearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                search();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        categorySearch.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                search();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));

        cartTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        cartAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        cartAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        cartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        items = new ArrayList<>();

        action.setCellValueFactory((TableColumn.CellDataFeatures<Book, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        action.setCellFactory((TableColumn<Book, Boolean> p) -> new AddButtonCell(bookcase));

        cartAction.setCellValueFactory((TableColumn.CellDataFeatures<Item, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
        cartAction.setCellFactory((TableColumn<Item, Boolean> p) -> new RemoveButtonCell(cart));

        bookpane.setVisible(true);
        cartpane.setVisible(false);

        checkout.setOnAction((ActionEvent e) -> {
            Dialog<ArrayList<String>> dialog = new Dialog<>();
            dialog.setTitle("Fill in your information");
            dialog.setHeaderText(null);
            dialog.setGraphic(null);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField firstName = new TextField();
            TextField lastName = new TextField();
            TextField email = new TextField();
            TextField phoneNumber = new TextField();
            TextField address = new TextField();

            grid.add(new Label("First Name: "), 0, 0);
            grid.add(firstName, 1, 0);
            grid.add(new Label("Last Name: "), 0, 1);
            grid.add(lastName, 1, 1);
            grid.add(new Label("Email: "), 0, 2);
            grid.add(email, 1, 2);
            grid.add(new Label("Phone Number: "), 0, 3);
            grid.add(phoneNumber, 1, 3);
            grid.add(new Label("Address: "), 0, 4);
            grid.add(address, 1, 4);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty()
                    .bind(Bindings.createBooleanBinding(
                            () -> firstName.getText().isEmpty()
                            || lastName.getText().isEmpty()
                            || email.getText().isEmpty()
                            || phoneNumber.getText().isEmpty()
                            || address.getText().isEmpty(),
                            firstName.textProperty(),
                            lastName.textProperty(),
                            email.textProperty(),
                            phoneNumber.textProperty(),
                            address.textProperty()
                    ));

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    ArrayList<String> result = new ArrayList<>();
                    result.add(firstName.getText());
                    result.add(lastName.getText());
                    result.add(email.getText());
                    result.add(phoneNumber.getText());
                    result.add(address.getText());
                    return result;
                }
                return null;
            });

            Optional<ArrayList<String>> result = dialog.showAndWait();
            if (result.isPresent()) {
                Customer customer = new Customer();
                customer.setFirstName(firstName.getText());
                customer.setLastName(lastName.getText());
                customer.setEmail(email.getText());
                customer.setPhoneNumber(phoneNumber.getText());
                customer.setAddress(address.getText());
                try {
                    orderItem(items, customer);
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
                }
                items = new ArrayList<>();
                cart.setItems(getCartItems());
                try {
                    bookcase.setItems(getAllBooks());
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        clear.setOnAction((ActionEvent e) -> {
            items = new ArrayList<>();
            cart.setItems(getCartItems());
        });

        back.setOnAction((ActionEvent e) -> {
            bookpane.setVisible(true);
            cartpane.setVisible(false);
            try {
                bookcase.setItems(getAllBooks());
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        view.setOnAction((ActionEvent e) -> {
            bookpane.setVisible(false);
            cartpane.setVisible(true);
            cart.setItems(getCartItems());
        });

        addbook.setOnAction((ActionEvent e) -> {
            Dialog<ArrayList<String>> dialog = new Dialog<>();
            dialog.setTitle("Add a book");
            dialog.setHeaderText(null);
            dialog.setGraphic(null);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField bookTitle = new TextField();
            TextField bookAuthor = new TextField();
            TextField bookISBN = new TextField();
            TextField bookPrice = new TextField();
            TextField bookStock = new TextField();
            TextField bookCategory = new TextField();

            grid.add(new Label("Title: "), 0, 0);
            grid.add(bookTitle, 1, 0);
            grid.add(new Label("Author: "), 0, 1);
            grid.add(bookAuthor, 1, 1);
            grid.add(new Label("ISBN: "), 0, 2);
            grid.add(bookISBN, 1, 2);
            grid.add(new Label("Price: "), 0, 3);
            grid.add(bookPrice, 1, 3);
            grid.add(new Label("Stock: "), 0, 4);
            grid.add(bookStock, 1, 4);
            grid.add(new Label("Category: "), 0, 5);
            grid.add(bookCategory, 1, 5);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty()
                    .bind(Bindings.createBooleanBinding(
                            () -> bookTitle.getText().isEmpty()
                            || bookAuthor.getText().isEmpty()
                            || bookISBN.getText().isEmpty()
                            || bookPrice.getText().isEmpty()
                            || bookStock.getText().isEmpty()
                            || bookCategory.getText().isEmpty(),
                            bookTitle.textProperty(),
                            bookAuthor.textProperty(),
                            bookISBN.textProperty(),
                            bookPrice.textProperty(),
                            bookStock.textProperty(),
                            bookCategory.textProperty()
                    ));

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    ArrayList<String> result = new ArrayList<>();
                    result.add(bookTitle.getText());
                    result.add(bookAuthor.getText());
                    result.add(bookISBN.getText());
                    result.add(bookPrice.getText());
                    result.add(bookStock.getText());
                    result.add(bookCategory.getText());
                    return result;
                }
                return null;
            });

            Optional<ArrayList<String>> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    if (bookExists(bookISBN.getText())) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setGraphic(null);
                        alert.setContentText("Book already exists");
                        alert.showAndWait();
                    } else {
                        Book book = new Book();
                        book.setAuthor(bookAuthor.getText());
                        book.setCategory(bookCategory.getText());
                        book.setISBN(bookISBN.getText());
                        book.setPrice(Double.parseDouble(bookPrice.getText()));
                        book.setStock(Integer.parseInt(bookStock.getText()));
                        book.setTitle(bookTitle.getText());
                        addBook(book);
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setGraphic(null);
                        alert.setContentText("Book added");
                        alert.showAndWait();
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        try {
            bookcase.setItems(getAllBooks());
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class RemoveButtonCell extends TableCell<Item, Boolean> {

        final Button cellButton = new Button("Remove from cart");

        public RemoveButtonCell(final TableView cart) {
            cellButton.setOnAction((ActionEvent e) -> {
                int index = getTableRow().getIndex();
                items.remove(index);
                cart.getItems().clear();
                cart.setItems(getCartItems());
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (t == null) {
                setGraphic(null);
                return;
            }
            setGraphic(cellButton);
        }
    }

    private class AddButtonCell extends TableCell<Book, Boolean> {

        GridPane grid = new GridPane();
        Button addButton = new Button("Add to cart");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        public AddButtonCell(final TableView bookcase) {
            editButton.setOnAction((ActionEvent e) -> {
                int index = getTableRow().getIndex();
                Book book = (Book) bookcase.getItems().get(index);

                Dialog<ArrayList<String>> dialog = new Dialog<>();
                dialog.setTitle("Edit '" + book.getTitle() + "'");
                dialog.setHeaderText(null);
                dialog.setGraphic(null);

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 150, 10, 10));

                TextField bookTitle = new TextField(book.getTitle());
                TextField bookAuthor = new TextField(book.getAuthor());
                TextField bookISBN = new TextField(book.getISBN());
                TextField bookPrice = new TextField(book.getPrice() + "");
                TextField bookStock = new TextField(book.getStock() + "");
                TextField bookCategory = new TextField(book.getCategory());

                gridPane.add(new Label("Title: "), 0, 0);
                gridPane.add(bookTitle, 1, 0);
                gridPane.add(new Label("Author: "), 0, 1);
                gridPane.add(bookAuthor, 1, 1);
                gridPane.add(new Label("ISBN: "), 0, 2);
                gridPane.add(bookISBN, 1, 2);
                gridPane.add(new Label("Price: "), 0, 3);
                gridPane.add(bookPrice, 1, 3);
                gridPane.add(new Label("Stock: "), 0, 4);
                gridPane.add(bookStock, 1, 4);
                gridPane.add(new Label("Category: "), 0, 5);
                gridPane.add(bookCategory, 1, 5);

                dialog.getDialogPane().setContent(gridPane);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty()
                        .bind(Bindings.createBooleanBinding(
                                () -> bookTitle.getText().isEmpty()
                                || bookAuthor.getText().isEmpty()
                                || bookISBN.getText().isEmpty()
                                || bookPrice.getText().isEmpty()
                                || bookStock.getText().isEmpty()
                                || bookCategory.getText().isEmpty(),
                                bookTitle.textProperty(),
                                bookAuthor.textProperty(),
                                bookISBN.textProperty(),
                                bookPrice.textProperty(),
                                bookStock.textProperty(),
                                bookCategory.textProperty()
                        ));

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        ArrayList<String> result = new ArrayList<>();
                        result.add(bookTitle.getText());
                        result.add(bookAuthor.getText());
                        result.add(bookISBN.getText());
                        result.add(bookPrice.getText());
                        result.add(bookStock.getText());
                        result.add(bookCategory.getText());
                        return result;
                    }
                    return null;
                });

                Optional<ArrayList<String>> result = dialog.showAndWait();
                if (result.isPresent()) {
                    book.setAuthor(bookAuthor.getText());
                    book.setCategory(bookCategory.getText());
                    book.setISBN(bookISBN.getText());
                    book.setPrice(Double.parseDouble(bookPrice.getText()));
                    book.setStock(Integer.parseInt(bookStock.getText()));
                    book.setTitle(bookTitle.getText());
                    try {
                        editBook(book);
                    } catch (SQLException | ClassNotFoundException ex) {
                        Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            deleteButton.setOnAction((ActionEvent e) -> {
                int index = getTableRow().getIndex();
                Book book = (Book) bookcase.getItems().get(index);
                try {
                    removeBook(book);
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(BookstoreController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            addButton.setOnAction((ActionEvent e) -> {
                int index = getTableRow().getIndex();
                Book book = (Book) bookcase.getItems().get(index);

                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add to cart");
                dialog.setHeaderText("You are adding '" + book.getTitle() + "' to your cart");
                dialog.setContentText("Please specify the amount:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    int amount = Integer.parseInt(result.get());
                    for (Item item : items) {
                        if (item.getId() == book.getId()) {
                            amount += item.getAmount();
                        }
                    }
                    if (amount > book.getStock()) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setGraphic(null);
                        alert.setContentText("There's not enough copies of the book");
                        alert.showAndWait();
                    } else {
                        items.removeIf(item -> item.getId() == book.getId());
                        items.add(new Item(book.getId(), book.getTitle(), book.getAuthor(),
                                book.getPrice(), amount));
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setGraphic(null);
                        alert.setContentText("Added " + result.get()
                                + " copies of '" + book.getTitle() + "' to your cart");
                        alert.showAndWait();
                    }
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (t == null) {
                setGraphic(null);
                return;
            }
            grid.setHgap(5);
            if (grid.getChildren().size() < 1) {
                grid.add(addButton, 0, 0);
                grid.add(editButton, 1, 0);
                grid.add(deleteButton, 2, 0);
            }
            setGraphic(grid);
        }
    }

}
