package org.anudip.ecommerce.service;

import org.anudip.ecommerce.dao.UserDAO;
import org.anudip.ecommerce.model.User;

import java.util.List;

public class UserService {
    private final UserDAO dao = new UserDAO();

    public int register(User u) { return dao.addUser(u); }
    public User getById(int id) { return dao.getUserById(id); }
    public List<User> listAll() { return dao.getAllUsers(); }
}
