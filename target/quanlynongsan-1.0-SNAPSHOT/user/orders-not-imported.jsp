<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.repository.OrderProductRepository.OrderSummary"%>
<jsp:include page="/header.jsp" />

<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="fw-bold mb-0">Đơn hàng chưa nhập kho</h5>
    </div>

    <div class="table-responsive">
        <table class="table table-borderless align-middle">
            <thead class="bg-light">
                <tr class="text-uppercase small text-muted">
                    <th>Mã đơn hàng</th>
                    <th>Ngày đặt hàng</th>
                    <th>Tổng</th>
                    <th>Trạng thái</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<OrderSummary> orders = (List<OrderSummary>) request.getAttribute("orders");
                    if (orders != null && !orders.isEmpty()) {
                        for (OrderSummary order : orders) {
                        String id = String.valueOf(order.getOrderId());
                        request.setAttribute("orderId", id);
                %>
                <tr>
                    <td>#<%= order.getOrderId() %></td>
                    <td><%= order.getCreatedDate() %></td>
                    <td><%= String.format("%.2f", order.getTotalPrice()) %> đ (<%= order.getTotalProducts() %> sản phẩm)</td>
                    <td>
                        <%
                            String status = order.getStatus();
                            String displayStatus = "";
                            switch (status) {
                                case "PENDING": displayStatus = "Chờ Xác Nhận"; break;
                                case "CONFIRMED":     displayStatus = "Đã Xác Nhận"; break;
                                case "PAID":          displayStatus = "Đã Thanh Toán"; break;
                                case "CANCELED":      displayStatus = "Đã Hủy"; break;
                                case "RETURNED":      displayStatus = "Đã Trả Hàng"; break;
                                case "SUCCESSFUL":    displayStatus = "Giao Thành Công"; break;
                                case "REVIEWED":      displayStatus = "Đã Đánh Giá"; break;
                            }
                        %>
                        <%= displayStatus %>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/user/detail-order.jsp?orderId=${requestScope.orderId}" class="text-success fw-semibold">Xem chi tiết</a>
                        |
                        <a href="${pageContext.request.contextPath}/confirm-stock?orderId=${requestScope.orderId}" class="text-primary fw-semibold">Nhập kho</a>
                    </td>

                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="5" class="text-center text-muted">Không có đơn hàng nào.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../footer.jsp" />

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const breadcrumb = document.querySelector(".breadcrumb").innerHTML = `<div><span class="material-symbols-outlined">home</span> &bull; Đơn hàng của tôi</div>`;
    });
</script>