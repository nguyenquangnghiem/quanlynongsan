<%@page import="com.mycompany.quanlynongsan.repository.UserRepository"%>
<%@page import="com.mycompany.quanlynongsan.model.ImageProduct"%>
<%@page import="com.mycompany.quanlynongsan.repository.ImageProductRepository"%>
<%@page import="com.mycompany.quanlynongsan.repository.ProductRepository"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<%@page import="com.mycompany.quanlynongsan.repository.DeliveryRepository"%>
<%@page import="com.mycompany.quanlynongsan.model.Delivery"%>
<%@page import="com.mycompany.quanlynongsan.repository.OrderProductRepository"%>
<%@page import="com.mycompany.quanlynongsan.repository.OrderRepository"%>
<%@page import="com.mycompany.quanlynongsan.model.Order"%>
<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.model.OrderProduct"%>
<jsp:include page="/header.jsp" />
<%
    OrderRepository orderRepository = new OrderRepository();
    OrderProductRepository orderProductRepository = new OrderProductRepository();
  User user = (User)session.getAttribute("user");
  Integer orderId = Integer.valueOf(request.getParameter("orderId"));
  Order order = orderRepository.getById(orderId);
  List<OrderProduct> orderProducts = orderProductRepository.getProductsByOrderId(orderId);
  OrderProductRepository.OrderSummary summary = orderProductRepository.getOrderSummaryByOrderId(orderId);
%>

<style>
  #rating-stars {
    unicode-bidi: bidi-override;
    direction: rtl;
  }
  #rating-stars input {
    display: none;
  }
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
    <h4 class="mb-4 fw-bold">Chi tiết đơn hàng</h4>

    <!-- Thông tin địa chỉ nhận hàng -->
    <div class="row mb-4">
        <div class="col-md-6">
            <h6 class="fw-bold">Địa chỉ nhận hàng</h6>
            <p><%= user.getFullName() %></p>
            <p><%= user.getAddress() %></p>
            <p><%= user.getPhoneNumber() %></p>
            <p><%= user.getEmail() %></p>
        </div>

        <!-- Thông tin thanh toán -->
        <div class="col-md-6">
            <h6 class="fw-bold">Thông tin thanh toán</h6>
            <p>Phương thức: <%= order.getPaymentMethod() %></p>
            <p>Tạm tính: <%= summary.getTotalPrice() %> đ</p>
            <p>Phí vận chuyển: <%= "Miễn phí" %></p>
            <h5 class="fw-bold text-success">Tổng tiền: $<%= summary.getTotalPrice() %></h5>
        </div>
    </div>

    <!-- Trạng thái đơn hàng -->
    <div class="mb-4">
        <h6 class="fw-bold">Trạng thái đơn hàng</h6>
        <%
    String status = order.getStatus();
    String displayStatus = "";
    int progress = 0;
    String progressColor = "bg-secondary"; // màu mặc định

    switch (status) {
        case "NOT_CONFIRMED":
            displayStatus = "Chờ Xác Nhận";
            progress = 10;
            progressColor = "bg-secondary";
            break;
        case "CONFIRMED":
            displayStatus = "Đã Xác Nhận";
            progress = 30;
            progressColor = "bg-primary";
            break;
        case "PAID":
            displayStatus = "Đã Thanh Toán";
            progress = 50;
            progressColor = "bg-info";
            break;
        case "SUCCESSFUL":
            displayStatus = "Giao Thành Công";
            progress = 100;
            progressColor = "bg-success";
            break;
        case "REVIEWED":
            displayStatus = "Đã Đánh Giá";
            progress = 100;
            progressColor = "bg-success";
            break;
        case "CANCELED":
            displayStatus = "Đã Hủy";
            progress = 100;
            progressColor = "bg-danger";
            break;
        case "RETURNED":
            displayStatus = "Đã Trả Hàng";
            progress = 100;
            progressColor = "bg-warning";
            break;
    }
%>

<div class="progress" style="height: 8px;">
    <div class="progress-bar <%= progressColor %>" role="progressbar"
         style="width: <%= progress %>%;" aria-valuenow="<%= progress %>"
         aria-valuemin="0" aria-valuemax="100">
    </div>
</div>
<p class="mt-2 mb-0"><%= displayStatus %></p>
    </div>

    <!-- Lịch sử vận chuyển -->
    <div class="mb-4">
        <h6 class="fw-bold">Lịch sử vận chuyển</h6>
        <ul class="list-group">
            <%
                DeliveryRepository deliveryRepository = new DeliveryRepository();
                List<Delivery> historyList = (List<Delivery>) deliveryRepository.getDeliveriesByOrderId(orderId);
                if (historyList != null) {
                    for (Delivery h : historyList) {
            %>
            <li class="list-group-item d-flex justify-content-between">
                <span>Đơn hàng đã tới <%= h.getAddress() %></span>
                <%
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm:ss", new java.util.Locale("vi", "VN"));
                    String formattedDate = sdf.format(h.getCreatedDate());
                %>
                <span class="text-muted"><%= formattedDate %></span>
            </li>
            <%
                    }
                }
            %>
        </ul>
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="table-responsive">
        <table class="table table-borderless align-middle">
            <thead class="bg-light">
                <tr class="text-uppercase small text-muted">
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
                    ProductRepository productRepository = new ProductRepository();
                    ImageProductRepository imageProductRepository = new ImageProductRepository();
                    UserRepository userRepository = new UserRepository();
                    for (OrderProduct item : orderProducts) {
                        Product product = productRepository.findById(item.getProductId());
                        List<ImageProduct> imageProducts = imageProductRepository.findAllByProductId(product.getProductId());
                        User holder = userRepository.findById(product.getHolderId());
                %>
                <tr>
                    <td>
                        <img src="<%= imageProducts.get(0).getUrlImage() %>" alt="<%= product.getName() %>" width="50" class="me-2" />
                        <%= product.getName() %>
                    </td>
                    <td><%= holder.getFullName() %></td>
                    <td><%= String.format("%.2f", product.getPrice()) %>đ</td>
                    <td>x<%= item.getQuantity() %></td>
                    <td><%= String.format("%.2f", product.getPrice().doubleValue() * item.getQuantity()) %>đ</td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
            <% if ("SUCCESSFUL".equals(summary.getStatus())) { %>
    <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#reviewModal">
        Đánh giá đơn hàng
    </button>
<% } else if ("REVIEWED".equals(summary.getStatus())) { %>
    <button type="button" class="btn btn-secondary" disabled>Đã đánh giá</button>
<% } %>
    <a href="<%= request.getContextPath() %>/secured/user/my-order" class="btn btn-outline-primary">Quay lại danh sách đơn hàng</a>
</div>

<div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form action="<%= request.getContextPath() %>/secured/review" method="post">
        <div class="modal-header">
          <h5 class="modal-title" id="reviewModalLabel">Đánh giá đơn hàng</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <!-- Truyền orderId ẩn -->
          <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">

          <!-- Số sao đánh giá -->
          <div class="mb-3">
  <label class="form-label">Đánh giá</label>
  <div id="rating-stars" class="mb-2" style="direction: rtl;">
    <% for (int i = 5; i >= 1; i--) { %>
      <input type="radio" name="rate" id="star<%= i %>" value="<%= i %>" required hidden>
      <label for="star<%= i %>">&#9733;</label>
    <% } %>
  </div>
</div>
          <!-- Comment -->
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

<jsp:include page="../footer.jsp" />
