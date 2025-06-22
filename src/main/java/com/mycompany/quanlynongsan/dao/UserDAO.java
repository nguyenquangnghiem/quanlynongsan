/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quanlynongsan.dao;

import org.mindrot.jbcrypt.BCrypt;

import com.mycompany.quanlynongsan.model.Behavior;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.BehaviorRepository;
import com.mycompany.quanlynongsan.repository.UserRepository;
import com.mycompany.quanlynongsan.request.RegisterRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author nghiem
 */
public class UserDAO {
    final private UserRepository userRepository = new UserRepository();

    private BehaviorRepository behaviorRepository = new BehaviorRepository();

    public boolean register(RegisterRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            user.setRoleId(request.getRoleId());
            return userRepository.save(user);
        }
        return false;
    }

    public boolean login(String email, String password, HttpServletRequest req) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (BCrypt.checkpw(password, user.getPassword())) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(30 * 60); // Session hết hạn sau 30 phút (tính bằng giây)
                Behavior behavior = behaviorRepository.findByCode("LOGIN");
                behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
                return true;
            }
            Behavior behavior = behaviorRepository.findByCode("LOGIN_FAILED");
            behaviorRepository.insertLog(user.getUserId(), behavior.getBehaviorId());
        }
        return false;
    }

    public User findById(Integer userId) {
        return userRepository.findById(userId);
    }
}
