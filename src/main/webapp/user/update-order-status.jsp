<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.model.Order"%>
<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page import="com.mycompany.quanlynongsan.model.Delivery"%>
<%@page import="com.mycompany.quanlynongsan.repository.OrderRepository"%>
<%@page import="com.mycompany.quanlynongsan.repository.UserRepository"%>
<%@page import="com.mycompany.quanlynongsan.repository.DeliveryRepository"%>
<jsp:include page="/header.jsp" />

<%
    Integer orderId = Integer.valueOf(request.getParameter("orderId"));
    OrderRepository orderRepo = new OrderRepository();
    UserRepository userRepo = new UserRepository();
    DeliveryRepository deliveryRepo = new DeliveryRepository();

    Order order = orderRepo.getById(orderId);
    User user = userRepo.findById(order.getUserId());
    List<Delivery> deliveries = deliveryRepo.getDeliveriesByOrderId(orderId);
%>

<div class="container py-4" style="max-width: 800px;">
    <h3 class="mb-4">Cập nhật đơn hàng</h3>

    <form action="<%=request.getContextPath()%>/secured/user/update-order-status" method="post">
        <input type="hidden" name="orderId" value="<%=orderId%>" />

        <div class="mb-3">
            <label class="form-label fw-semibold">Trạng thái đơn hàng</label>
            <select class="form-select" name="status" required>
                <option value="<%= "PAID".equals(order.getStatus())? "PAID" : "CONFIRMED"%>" <%= "CONFIRMED".equals(order.getStatus()) || "PAID".equals(order.getStatus()) ? "selected" : "" %>>Đang giao hàng</option>
                <option value="SUCCESSFUL" <%= "SUCCESSFUL".equals(order.getStatus()) ? "selected" : "" %>>Giao thành công</option>
                <option value="CANCELED" <%= "CANCELED".equals(order.getStatus()) ? "selected" : "" %>>Hủy đơn hàng</option>
                <option value="RETURNED" <%= "RETURNED".equals(order.getStatus()) ? "selected" : "" %>>Trả đơn hàng</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Thêm lịch sử vận chuyển mới</label>
            <input type="text" class="form-control" name="newHistory" placeholder="Ví dụ: Kho phân loại...">
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">Lịch sử vận chuyển</label>
            <ul class="list-group">
                <% for (Delivery d : deliveries) { %>
                    <li class="list-group-item d-flex justify-content-between">
                        <span><%= d.getAddress() %></span>
                        <span class="text-muted">
                            <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(d.getCreatedDate()) %>
                        </span>
                    </li>
                <% } %>
            </ul>
        </div>

        <div class="d-flex justify-content-end gap-2">
            <a href="<%=request.getContextPath()%>/secured/user/my-order" class="btn btn-secondary">Quay lại</a>
            <button type="submit" class="btn btn-primary">Cập nhật đơn hàng</button>
        </div>
    </form>
</div>

<jsp:include page="/footer.jsp" />