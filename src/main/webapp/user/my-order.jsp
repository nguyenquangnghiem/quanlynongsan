<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.repository.OrderProductRepository.OrderSummary"%>
<jsp:include page="/header.jsp" />
<%
    String success = request.getParameter("success");
    String error = request.getParameter("error");
    if ("cancel".equals(success)) {
%>
    <div class="alert alert-success">Đơn hàng đã được hủy thành công.</div>
<% } else if ("invalid_status".equals(error)) { %>
    <div class="alert alert-warning">Chỉ có thể hủy đơn hàng khi đang ở trạng thái chờ xác nhận.</div>
<% } else if ("exception".equals(error)) { %>
    <div class="alert alert-danger">Đã xảy ra lỗi khi hủy đơn hàng.</div>
<% } %>
<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="fw-bold mb-0">Đơn hàng của tôi</h5>
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
            <%
                User user = (User) session.getAttribute("user");  // ✅ Lấy role hiện tại từ session
                int roleId = user.getRoleId();
            %>

            <tbody>
                <%
                    List<OrderSummary> orders = (List<OrderSummary>) request.getAttribute("orders");
                    if (orders != null && !orders.isEmpty()) {
                        for (OrderSummary order : orders) {
                %>
                <tr>
                    <td>#<%= order.getOrderId()%></td>
                    <td><%= order.getCreatedDate()%></td>
                    <td><%= String.format("%.2f", order.getTotalPrice())%> đ (<%= order.getTotalProducts()%> sản phẩm)</td>
                    <td>
                        <%
                            String status = order.getStatus();
                            String displayStatus = "";
                            switch (status) {
                                case "PENDING":
                                    displayStatus = "Chờ Xác Nhận";
                                    break;
                                case "CONFIRMED":
                                    displayStatus = "Đã Xác Nhận";
                                    break;
                                case "PAID":
                                    displayStatus = "Đã Thanh Toán";
                                    break;
                                case "CANCELED":
                                    displayStatus = "Đã Hủy";
                                    break;
                                case "RETURNED":
                                    displayStatus = "Đã Trả Hàng";
                                    break;
                                case "SUCCESSFUL":
                                    displayStatus = "Giao Thành Công";
                                    break;
                                case "REVIEWED":
                                    displayStatus = "Đã Đánh Giá";
                                    break;
                                default:
                                    displayStatus = status;
                                    break;
                            }
                        %>

                        <%= displayStatus%>
                    </td>
                    <td class="d-flex justify-content-center gap-4">
                        <a href="${pageContext.request.contextPath}/user/detail-order.jsp?orderId=<%= order.getOrderId()%>" class="text-success fw-semibold">Xem chi tiết</a>

                        <%-- ✅ Hiện nút "Cập nhật đơn hàng" nếu role là 2 và trạng thái là CONFIRMED --%>
                        <%
                            if (roleId == 2 && ("CONFIRMED".equals(status) || "PAID".equals(status))) {
                        %>
                        <a href="${pageContext.request.contextPath}/secured/user/update-order-status?orderId=<%= order.getOrderId()%>" class="text-warning fw-semibold">Cập nhật đơn hàng</a>
                        <%
                            }
                        %>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="5" class="text-center text-muted">Không có đơn hàng nào.</td>
                </tr>
                <%
                    }
                %>
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