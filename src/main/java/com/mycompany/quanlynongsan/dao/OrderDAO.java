package com.mycompany.quanlynongsan.dao;

import com.mycompany.quanlynongsan.model.HasCart;
import com.mycompany.quanlynongsan.model.Order;
import com.mycompany.quanlynongsan.model.Product;
import com.mycompany.quanlynongsan.repository.HasCartRepository;
import com.mycompany.quanlynongsan.repository.OrderRepository;
import com.mycompany.quanlynongsan.repository.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderDAO {

    private OrderRepository orderRepository = new OrderRepository();

    private HasCartRepository hasCartRepository = new HasCartRepository();

    private ProductRepository productRepository = new ProductRepository();

    // ✅ Lấy danh sách đơn hàng theo productId
    public List<Order> getOrderByProductId(Integer productId) {
        return orderRepository.findDistinctOrdersByProductId(productId);
    }

    // ✅ Lưu đơn hàng mới
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    // ✅ Chuyển giỏ hàng sang bảng ORDER_PRODUCT
    public void transferCartToOrder(int userId, Order order) {
//        orderRepository.transferCartToOrder(userId, orderId);
        List<HasCart> hasCarts = hasCartRepository.getHasCartByUserId(userId);
        Map<Integer, Map<Product, Integer>> productsByHolder = hasCarts.stream()
                .map(hasCart -> Map.entry(productRepository.findById(hasCart.getProductId()), hasCart.getQuantity()))
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().getHolderId(),
                        Collectors.toMap(
                                entry -> entry.getKey(), // key: Product
                                entry -> entry.getValue(),// value: quantity
                                (quantity1, quantity2) -> quantity1 + quantity2 // gộp nếu trùng Product (tùy logic)
                        )
                ));
        for (Map.Entry<Integer, Map<Product, Integer>> entry : productsByHolder.entrySet()) {
            Integer holderId = entry.getKey();
            Map<Product, Integer> products = entry.getValue();
            Order newOrder = new Order(null, order.getEstimatedTime(), order.getComment(), order.getStatus(), order.getRate(), order.getPaymentMethod(), userId, order.getCreatedDate(), order.getIsImported());
            orderRepository.save(newOrder);
            orderRepository.transferCartToOrder(userId, newOrder.getOrderId(), products);
        }
    }

    public void confirmOrderById(int orderId) {
        orderRepository.confirmOrder(orderId);
    }
    
    public Order findById(int orderId) {
        return orderRepository.getById(orderId);
    }

}
