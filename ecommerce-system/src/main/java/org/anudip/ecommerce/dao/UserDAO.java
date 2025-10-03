package org.anudip.ecommerce.dao;

import org.anudip.ecommerce.model.User;
import org.anudip.ecommerce.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public int addUser(User user) {
        String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    user.setUserId(id);
                    return id;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("addUser error: " + e.getMessage());
        }
        return -1;
    }

    public User getUserById(int userId) {
        String sql = "SELECT user_id, name, email FROM users WHERE user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"));
                }
            }
        } catch (Exception e) {
            LOGGER.severe("getUserById error: " + e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, name, email FROM users";
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(rs.getInt("user_id"), rs.getString("name"), rs.getString("email")));
            }
        } catch (Exception e) {
            LOGGER.severe("getAllUsers error: " + e.getMessage());
        }
        return list;
    }
}
