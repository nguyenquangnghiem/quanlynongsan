<%@page import="com.mycompany.quanlynongsan.dto.ProblemDetailDTO"%>
<%@page import="com.mycompany.quanlynongsan.model.Problem"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<%
    ProblemDetailDTO detail = (ProblemDetailDTO) request.getAttribute("detail");
    Problem problem = detail.getProblem();
    Product product = detail.getProduct(); // đã đổi từ Order sang Product
    User user = detail.getUser();
%>

<div class="container my-5">
    <h4 class="fw-bold mb-4">Chi tiết vấn đề</h4>

    <h5>Thông tin vấn đề</h5>
    <ul>
        <li><strong>Tiêu đề:</strong> <%= problem.getName() %></li>
        <li><strong>Lý do:</strong> <%= problem.getReason() %></li>
        <li><strong>Thời gian tạo:</strong> <%= problem.getCreatedDate() %></li>
        <li><strong>Đã xử lý:</strong> <%= problem.getIsResolved() ? "Đã xử lý" : "Chưa xử lý" %></li>
    </ul>

    <h5 class="mt-4">Thông tin sản phẩm</h5>
    <ul>
        <li><strong>Mã sản phẩm:</strong> <%= product.getProductId() %></li>
        <li><strong>Tên sản phẩm:</strong> <%= product.getName() %></li>
        <li><strong>Giá:</strong> <%= product.getPrice() %> đ</li>
        <li><strong>Số lượng còn:</strong> <%= product.getQuantity() %></li>
        <li><strong>Trạng thái:</strong> <%= product.getStatus() %></li>
        <li><strong>Nơi sản xuất:</strong> <%= product.getPlaceOfManufacture() %></li>
    </ul>

    <h5 class="mt-4">Thông tin người tạo sản phẩm</h5>
    <ul>
        <li><strong>Họ tên:</strong> <%= user.getFullName() %></li>
        <li><strong>Email:</strong> <%= user.getEmail() %></li>
        <li><strong>Số điện thoại:</strong> <%= user.getPhoneNumber() %></li>
        <li><strong>Địa chỉ:</strong> <%= user.getAddress() != null ? user.getAddress() : "Chưa có" %></li>
    </ul>

    <a href="<%= request.getContextPath() %>/admin/problems.jsp" class="btn btn-secondary mt-3">Quay lại danh sách</a>
</div>

<jsp:include page="/footer.jsp" />
