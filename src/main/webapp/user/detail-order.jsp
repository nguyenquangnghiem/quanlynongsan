<%@page import="com.mycompany.quanlynongsan.repository.*"%>
<%@page import="com.mycompany.quanlynongsan.model.*"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />
<%
    OrderRepository orderRepository = new OrderRepository();
    OrderProductRepository orderProductRepository = new OrderProductRepository();
    UserRepository userRepository = new UserRepository();
    DeliveryRepository deliveryRepository = new DeliveryRepository();
    ProductRepository productRepository = new ProductRepository();
    ImageProductRepository imageProductRepository = new ImageProductRepository();

    Integer orderId = Integer.valueOf(request.getParameter("orderId"));
    Order order = orderRepository.getById(orderId);
    User user = userRepository.findById(order.getUserId());
    List<OrderProduct> orderProducts = orderProductRepository.getProductsByOrderId(orderId);
    OrderProductRepository.OrderSummary summary = orderProductRepository.getOrderSummaryByOrderId(orderId);
%>

<style>
    #rating-stars {
        unicode-bidi: bidi-override;
        direction: rtl;
    }
    #rating-stars input { display: none; }
    #rating-stars label {
        font-size: 2rem;
        color: gray;
        cursor: pointer;
    }
    #rating-stars label:hover,
    #rating-stars label:hover ~ label {
        color: gold;
    }
    #rating-stars input:checked ~ label {
        color: gray;
    }
    #rating-stars input:checked + label,
    #rating-stars input:checked + label ~ label {
        color: gold;
    }
</style>

<div class="container my-5">
    <h2 class="fw-bold mb-4 text-center">Chi tiết đơn hàng</h2>

    <!-- Thông tin địa chỉ + thanh toán -->
    <div class="row g-4 mb-4">
        <div class="col-md-6">
            <h5 class="fw-bold mb-2">Địa chỉ nhận hàng</h5>
            <div class="border p-3 rounded shadow-sm">
                <p class="mb-1 fw-semibold"><%= user.getFullName() %></p>
                <p class="mb-1"><%= user.getAddress() %></p>
                <p class="mb-1"><%= user.getPhoneNumber() %></p>
                <p class="mb-0 text-muted small"><%= user.getEmail() %></p>
            </div>
        </div>

        <div class="col-md-6">
            <h5 class="fw-bold mb-2">Thông tin thanh toán</h5>
            <div class="border p-3 rounded shadow-sm">
                <p class="mb-1">Phương thức: <%= order.getPaymentMethod() %></p>
                <p class="mb-1">Tạm tính: <%= summary.getTotalPrice() %> đ</p>
                <p class="mb-1">Phí vận chuyển: <strong>Miễn phí</strong></p>
                <h5 class="fw-bold text-success mb-0">Tổng tiền: <%= summary.getTotalPrice() %> đ</h5>
            </div>
        </div>
    </div>

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

    <!-- Lịch sử vận chuyển -->
    <h5 class="fw-bold mb-2">Lịch sử vận chuyển</h5>
    <ul class="list-group mb-4">
        <%
            List<Delivery> historyList = deliveryRepository.getDeliveriesByOrderId(orderId);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", new java.util.Locale("vi", "VN"));
            if (historyList != null) {
                for (Delivery h : historyList) {
        %>
        <li class="list-group-item d-flex justify-content-between align-items-center">
            <span>Đơn hàng tới <%= h.getAddress() %></span>
            <span class="text-muted small"><%= sdf.format(h.getCreatedDate()) %></span>
        </li>
        <%
                }
            }
        %>
    </ul>

    <!-- Danh sách sản phẩm -->
    <h5 class="fw-bold mb-2">Sản phẩm trong đơn</h5>
    <div class="table-responsive mb-4">
        <table class="table align-middle">
            <thead class="table-light small text-uppercase text-muted">
                <tr>
                    <th>Sản phẩm</th>
                    <th>Người bán</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Tổng</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (orderProducts != null) {
                        for (OrderProduct item : orderProducts) {
                            Product product = productRepository.findById(item.getProductId());
                            List<ImageProduct> imageProducts = imageProductRepository.findByProductId(product.getProductId());
                            User holder = userRepository.findById(product.getHolderId());
                %>
                <tr>
                    <td>
                        <img src="<%= imageProducts.size() > 0 ? imageProducts.get(0).getUrlImage() : "" %>" alt="<%= product.getName() %>" width="60" class="me-2 rounded" />
                        <%= product.getName() %>
                    </td>
                    <td><%= holder.getFullName() %></td>
                    <td><%= String.format("%.0f", product.getPrice()) %> đ</td>
                    <td>x<%= item.getQuantity() %></td>
                    <td><%= String.format("%.0f", product.getPrice().doubleValue() * item.getQuantity()) %> đ</td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>

    <!-- Các nút thao tác -->
    <div class="d-flex gap-2 flex-wrap mb-4">
        <a href="<%= request.getContextPath() %>/secured/user/my-order" class="btn btn-outline-primary">Quay lại đơn hàng</a>

        <% if ("SUCCESSFUL".equals(summary.getStatus())) { %>
            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#reviewModal">Đánh giá đơn hàng</button>
        <% } else if ("REVIEWED".equals(summary.getStatus())) { %>
            <button type="button" class="btn btn-secondary" disabled>Đã đánh giá</button>
        <% } %>

        <% if ("PENDING".equals(order.getStatus())) { %>
            <form action="<%= request.getContextPath() %>/secured/user/cancel-order" method="post" class="d-inline">
                <input type="hidden" name="orderId" value="<%= orderId %>">
                <button type="submit" class="btn btn-danger">Hủy đơn hàng</button>
            </form>
        <% } %>

        <% if ("SUCCESSFUL".equals(summary.getStatus()) || "REVIEWED".equals(summary.getStatus())) { %>
            <a href="<%= request.getContextPath() %>/user/return-request.jsp?orderId=<%= orderId %>" class="btn btn-warning">Yêu cầu trả hàng</a>
        <% } %>
    </div>
</div>

<!-- Modal đánh giá -->
<div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="<%= request.getContextPath() %>/secured/review" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="reviewModalLabel">Đánh giá đơn hàng</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                    <div class="mb-3">
                        <label class="form-label">Số sao</label>
                        <div id="rating-stars" class="mb-2">
                            <% for (int i = 5; i >= 1; i--) { %>
                                <input type="radio" name="rate" id="star<%= i %>" value="<%= i %>" required hidden>
                                <label for="star<%= i %>">&#9733;</label>
                            <% } %>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="comment" class="form-label">Nhận xét</label>
                        <textarea class="form-control" id="comment" name="comment" rows="3" required></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Gửi đánh giá</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp" />
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const breadcrumb = document.querySelector(".breadcrumb");
        if (breadcrumb) breadcrumb.innerHTML = '<div><span class="material-symbols-outlined">home</span> &bull; Đơn hàng của tôi &bull; Chi tiết đơn hàng</div>';
    });
</script>
