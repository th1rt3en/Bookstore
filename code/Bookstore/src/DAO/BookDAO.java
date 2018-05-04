/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.*;
import java.sql.*;
import java.util.ArrayList;
import DBConnection.DatabaseConnection;

/**
 *
 * @author Negarr
 */
public class BookDAO {

    private final Connection conn;

    public BookDAO() throws SQLException, ClassNotFoundException {
        conn = DatabaseConnection.getConnection();
    }

    public boolean bookExists(String isbn) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM book WHERE ISBN = ?");
        ps.setString(1, isbn);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public void editBook(Book book) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("UPDATE book SET Title = ?, Author = ?, ISBN = ?, Price = ?, In_Stock = ?, Category = ? WHERE ID = ?");
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setString(3, book.getISBN());
        ps.setDouble(4, book.getPrice());
        ps.setInt(5, book.getStock());
        ps.setString(6, book.getCategory());
        ps.setInt(7, book.getId());
        ps.executeUpdate();
    }
    
    public void removeBook(Book book) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM book WHERE ID = ?");
        ps.setInt(1, book.getId());
        ps.executeUpdate();
    }
    
    public Book getBookByISBN(String isbn) throws SQLException, ClassNotFoundException {
        Book book = new Book();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM book WHERE ISBN = ?");
        ps.setString(1, isbn);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            book.setAuthor(rs.getString("Author"));
            book.setCategory(rs.getString("Category"));
            book.setISBN(rs.getString("ISBN"));
            book.setId(rs.getInt("ID"));
            book.setPrice(rs.getDouble("Price"));
            book.setStock(rs.getInt("In_Stock"));
            book.setTitle(rs.getString("Title"));
        }
        return book;
    }

    public int addBook(Book book) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO book (Title, Author, ISBN, Price, In_Stock, Category) VALUES (?,?,?,?,?,?)");
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setString(3, book.getISBN());
        ps.setDouble(4, book.getPrice());
        ps.setInt(5, book.getStock());
        ps.setString(6, book.getCategory());
        ps.executeUpdate();
        return getBookByISBN(book.getISBN()).getId();
    }

    public ArrayList<Book> getBooksByAttributes(String title, String author, String category) throws SQLException, ClassNotFoundException {
        ArrayList<Book> books = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM book WHERE Title LIKE ? AND Author LIKE ? AND Category LIKE ?");
        ps.setString(1, "%" + title + "%");
        ps.setString(2, "%" + author + "%");
        ps.setString(3, "%" + category + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Book book = new Book();
            book.setAuthor(rs.getString("Author"));
            book.setCategory(rs.getString("Category"));
            book.setISBN(rs.getString("ISBN"));
            book.setId(rs.getInt("ID"));
            book.setPrice(rs.getDouble("Price"));
            book.setStock(rs.getInt("In_Stock"));
            book.setTitle(rs.getString("Title"));
            books.add(book);
        }
        return books;
    }

    public ArrayList<Book> getAllBooks() throws SQLException, ClassNotFoundException {
        ArrayList<Book> books = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM book");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Book book = new Book();
            book.setAuthor(rs.getString("Author"));
            book.setCategory(rs.getString("Category"));
            book.setISBN(rs.getString("ISBN"));
            book.setId(rs.getInt("ID"));
            book.setPrice(rs.getDouble("Price"));
            book.setStock(rs.getInt("In_Stock"));
            book.setTitle(rs.getString("Title"));
            books.add(book);
        }
        return books;
    }

    public void orderBook(int id, int amount) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("UPDATE book SET In_Stock = In_Stock - ? WHERE ID = ?");
        ps.setInt(1, amount);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

}
