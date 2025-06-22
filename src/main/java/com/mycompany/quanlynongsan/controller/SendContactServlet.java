package com.mycompany.quanlynongsan.servlet;

import com.mycompany.quanlynongsan.dao.EmailService;
import com.mycompany.quanlynongsan.model.Contact;
import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.repository.ContactRepository;
import com.mycompany.quanlynongsan.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

@WebServlet(urlPatterns = "/user/contact")
public class SendContactServlet extends HttpServlet {

    private final ContactRepository contactRepository = new ContactRepository();
    private final UserRepository userRepository = new UserRepository();
    
    private final EmailService emailService = new EmailService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String description = request.getParameter("description");
        String receiverIdParam = request.getParameter("receiverId");
        String productId = request.getParameter("productId");

        try {
            if (fullName == null || phoneNumber == null || description == null || receiverIdParam == null) {
                throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin.");
            }

            Integer receiverId = Integer.valueOf(receiverIdParam);

            // Lưu vào DB
            Contact contact = new Contact(null, fullName, phoneNumber, description, receiverId, new Date());
            contactRepository.save(contact);

            // Gửi email
            User receiver = userRepository.findById(receiverId); // cần có UserRepository
            if (receiver != null && receiver.getEmail() != null) {
                String subject = "Bạn có liên hệ mới từ " + fullName;
                String content = "Họ tên: " + fullName + "\n"
                               + "SĐT: " + phoneNumber + "\n"
                               + "Nội dung: " + description;

                emailService.sendEmail(receiver.getEmail(), subject, content);
            }

            response.sendRedirect(request.getContextPath() + "/product-detail?productId=" + productId + "&success=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/product-detail?productId=" + productId + "&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }
}
