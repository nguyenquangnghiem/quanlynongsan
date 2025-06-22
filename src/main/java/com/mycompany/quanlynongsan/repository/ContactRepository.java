package com.mycompany.quanlynongsan.repository;

import com.mycompany.quanlynongsan.config.DatabaseConnection;
import com.mycompany.quanlynongsan.model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactRepository {

    private static final String INSERT_CONTACT = ""
            + "INSERT INTO CONTACT (full_name, phone_number, description, receiver_id, created_date) \n" +
"            VALUES (?, ?, ?, ?, GETDATE())";

    public void save(Contact contact) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CONTACT)) {

            stmt.setString(1, contact.getFullName());
            stmt.setString(2, contact.getPhoneNumber());
            stmt.setString(3, contact.getDescription());
            stmt.setInt(4, contact.getReceiverId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
