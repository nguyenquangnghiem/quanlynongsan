package com.mycompany.quanlynongsan.dao;

import com.mycompany.quanlynongsan.model.Order;
import com.mycompany.quanlynongsan.repository.OrderRepository;
import java.util.List;

public class OrderDAO {
    private OrderRepository orderRepository = new OrderRepository();

    // ✅ Lấy danh sách đơn hàng theo productId
    public List<Order> getOrderByProductId(Integer productId) {
        return orderRepository.findDistinctOrdersByProductId(productId);
    }

    // ✅ Lưu đơn hàng mới
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    // ✅ Chuyển giỏ hàng sang bảng ORDER_PRODUCT
    public void transferCartToOrder(int userId, int orderId) {
        orderRepository.transferCartToOrder(userId, orderId);
    }
}
