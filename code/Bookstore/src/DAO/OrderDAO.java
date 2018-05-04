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

public class OrderDAO {
    
    private final Connection conn;

    public OrderDAO() throws SQLException, ClassNotFoundException {
        conn = DatabaseConnection.getConnection();
    }
    
    public Order getOrderByCustomerId(int id) throws SQLException, ClassNotFoundException {
        Order order = new Order();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM `order` WHERE Customer_ID = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            order.setCustomerId(rs.getInt("Customer_ID"));
            order.setDiscount(rs.getInt("Discount"));
            order.setId(rs.getInt("ID"));
            order.setStatus(rs.getString("Status"));
        }
        return order;
    }
    
    public int placeOrder(Order order) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO `order` (Customer_ID, Discount, Status) VALUES (?,?,?)");
        ps.setInt(1, order.getCustomerId());
        ps.setInt(2, order.getDiscount());
        ps.setString(3, order.getStatus());
        ps.executeUpdate();
        return getOrderByCustomerId(order.getCustomerId()).getId();
    }
    
    public void placeOrderItem(Order order, int bookId, int quantity) throws SQLException, ClassNotFoundException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO order_item VALUES (?,?,?,?)");
        ps.setInt(1, order.getId());
        ps.setInt(2, order.getCustomerId());
        ps.setInt(3, bookId);
        ps.setInt(4, quantity);
        ps.executeUpdate();
    }
}
