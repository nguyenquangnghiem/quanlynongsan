<%@page import="com.mycompany.quanlynongsan.repository.*"%>
<%@page import="com.mycompany.quanlynongsan.model.*"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<%
    Integer orderId = Integer.valueOf(request.getParameter("orderId"));
    Integer sellerId = ((User) session.getAttribute("user")).getUserId();

    OrderRepository orderRepository = new OrderRepository();
    OrderProductRepository orderProductRepository = new OrderProductRepository();
    ProductRepository productRepository = new ProductRepository();
    ImageProductRepository imageProductRepository = new ImageProductRepository();
    UserRepository userRepository = new UserRepository();
    DeliveryRepository deliveryRepository = new DeliveryRepository();

    Order order = orderRepository.getById(orderId);
    if (order == null) {
%>
    <div class="container my-5 text-center">
        <h3 class="text-danger fw-bold">Không tìm thấy đơn hàng</h3>
        <a href="<%= request.getContextPath() %>/secured/user/pending-orders?status=pending" class="btn btn-primary mt-3">Quay lại danh sách đơn hàng</a>
    </div>
<%
        return;
    }

    User buyer = userRepository.findById(order.getUserId());
    List<OrderProduct> allOrderProducts = orderProductRepository.getProductsByOrderId(orderId);
    List<OrderProduct> orderProducts = allOrderProducts.stream()
        .filter(op -> productRepository.findById(op.getProductId()).getHolderId() == sellerId)
        .toList();

    double totalPrice = orderProducts.stream()
        .mapToDouble(op -> productRepository.findById(op.getProductId()).getPrice().doubleValue() * op.getQuantity())
        .sum();
%>

<div class="container my-5">
    <h2 class="fw-bold mb-4 text-center">Chi tiết đơn hàng</h2>

    <div class="row g-4 mb-4">
        <div class="col-md-6">
            <h5 class="fw-bold mb-2">Thông tin người mua</h5>
            <div class="border rounded p-3 shadow-sm">
                <p class="mb-1 fw-semibold"><%= buyer.getFullName() %></p>
                <p class="mb-1"><%= buyer.getAddress() %></p>
                <p class="mb-1"><%= buyer.getPhoneNumber() %></p>
                <p class="mb-0 text-muted small"><%= buyer.getEmail() %></p>
            </div>
        </div>

        <div class="col-md-6">
            <h5 class="fw-bold mb-2">Thông tin đơn hàng</h5>
            <div class="border rounded p-3 shadow-sm">
                <p class="mb-1">Ngày đặt: <%= order.getCreatedDate() %></p>
                <p class="mb-1">Phương thức thanh toán: <%= order.getPaymentMethod() %></p>
                <!-- Trạng thái đơn hàng -->
    <%
        String status = order.getStatus();
        String displayStatus = "";
        int progress = 0;
        String progressColor = "bg-secondary";

        switch (status) {
            case "PENDING": displayStatus = "Chờ Xác Nhận"; progress = 10; break;
            case "CONFIRMED": displayStatus = "Đã Xác Nhận"; progress = 30; progressColor = "bg-primary"; break;
            case "PAID": displayStatus = "Đã Thanh Toán"; progress = 50; progressColor = "bg-info"; break;
            case "SUCCESSFUL": displayStatus = "Giao Thành Công"; progress = 100; progressColor = "bg-success"; break;
            case "REVIEWED": displayStatus = "Đã Đánh Giá"; progress = 100; progressColor = "bg-success"; break;
            case "CANCELED": displayStatus = "Đã Hủy"; progress = 100; progressColor = "bg-danger"; break;
            case "RETURNED": displayStatus = "Đã Trả Hàng"; progress = 100; progressColor = "bg-warning"; break;
        }
    %>

    <h5 class="fw-bold mb-2">Trạng thái đơn hàng</h5>
    <div class="progress mb-2" style="height: 8px;">
        <div class="progress-bar <%= progressColor %>" role="progressbar" style="width: <%= progress %>%;" aria-valuenow="<%= progress %>" aria-valuemin="0" aria-valuemax="100"></div>
    </div>
    <p class="fw-semibold"><%= displayStatus %></p>
                <h5 class="fw-bold text-success mt-2">Tổng tiền bạn nhận: <%= String.format("%.0f", totalPrice) %> đ</h5>
            </div>
        </div>
    </div>

    <h5 class="fw-bold mb-2">Sản phẩm thuộc đơn hàng của bạn</h5>
    <div class="table-responsive mb-4">
        <table class="table align-middle">
            <thead class="table-light small text-uppercase text-muted">
                <tr>
                    <th>Sản phẩm</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Thành tiền</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (OrderProduct item : orderProducts) {
                        Product product = productRepository.findById(item.getProductId());
                        List<ImageProduct> images = imageProductRepository.findByProductId(product.getProductId());
                %>
                <tr>
                    <td>
                        <img src="<%= images.size() > 0 ? images.get(0).getUrlImage() : "" %>" alt="<%= product.getName() %>" width="60" class="me-2 rounded shadow-sm" />
                        <%= product.getName() %>
                    </td>
                    <td><%= String.format("%.0f", product.getPrice()) %> đ</td>
                    <td>x<%= item.getQuantity() %></td>
                    <td class="fw-semibold"><%= String.format("%.0f", product.getPrice().doubleValue() * item.getQuantity()) %> đ</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <h5 class="fw-bold mb-2">Lịch sử vận chuyển</h5>
    <ul class="list-group mb-4">
        <%
            List<Delivery> deliveries = deliveryRepository.getDeliveriesByOrderId(orderId);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", new java.util.Locale("vi", "VN"));
            for (Delivery d : deliveries) {
        %>
        <li class="list-group-item d-flex justify-content-between">
            <span>Đã tới: <%= d.getAddress() %></span>
            <span class="text-muted small"><%= sdf.format(d.getCreatedDate()) %></span>
        </li>
        <%
            }
        %>
    </ul>

    <a href="<%= request.getContextPath() %>/secured/user/pending-orders?status=pending" class="btn btn-outline-primary">Quay lại danh sách đơn hàng</a>
</div>

<jsp:include page="/footer.jsp" />
