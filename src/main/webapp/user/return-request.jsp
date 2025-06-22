<%@page import="com.mycompany.quanlynongsan.model.Order"%>
<%@page import="com.mycompany.quanlynongsan.repository.OrderRepository"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<%
    Integer orderId = Integer.valueOf(request.getParameter("orderId"));
    OrderRepository orderRepository = new OrderRepository();
    Order order = orderRepository.getById(orderId);
%>

<div class="container my-5">
    <h4 class="fw-bold mb-4">Yêu cầu trả hàng - Đơn hàng #<%= orderId %></h4>

    <form action="<%= request.getContextPath() %>/secured/user/return-request" method="post">
        <input type="hidden" name="orderId" value="<%= orderId %>">

        <div class="mb-3">
            <label class="form-label fw-bold">Lý do trả hàng</label>
            <textarea class="form-control" name="reason" rows="4" required placeholder="Nhập lý do trả hàng..."></textarea>
        </div>

        <button type="submit" class="btn btn-warning">Gửi yêu cầu trả hàng</button>
        <a href="<%= request.getContextPath() %>/secured/user/my-order" class="btn btn-secondary">Quay lại đơn hàng</a>
    </form>
</div>

<jsp:include page="/footer.jsp" />
