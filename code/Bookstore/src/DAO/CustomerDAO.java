/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.*;
import DBConnection.DatabaseConnection;
import java.sql.*;

/**
 *
 * @author Negarr
 */
public class CustomerDAO {
    
    private final Connection conn;

    public CustomerDAO() throws SQLException, ClassNotFoundException {
        conn = DatabaseConnection.getConnection();
    }
    
    public boolean customerExists(Customer customer) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer WHERE Email = ?");
        ps.setString(1, customer.getEmail());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
    
    public Customer getCustomerByEmail(String email) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer WHERE Email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            customer.setAddress(rs.getString("Address"));
            customer.setEmail(rs.getString("Email"));
            customer.setFirstName(rs.getString("First_Name"));
            customer.setId(rs.getInt("ID"));
            customer.setLastName(rs.getString("Last_Name"));
            customer.setPhoneNumber(rs.getString("Phone_Number"));
        }
        return customer;
    }
    
    public int addCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO customer (First_Name, Last_Name, Email, Phone_Number, Address) VALUES (?,?,?,?,?)");
        ps.setString(1, customer.getFirstName());
        ps.setString(2, customer.getLastName());
        ps.setString(3, customer.getEmail());
        ps.setString(4, customer.getPhoneNumber());
        ps.setString(5, customer.getAddress());
        ps.executeUpdate();
        return getCustomerByEmail(customer.getEmail()).getId();
    }
    
}
