<%@page import="com.mycompany.quanlynongsan.repository.ProductRepository"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />

<div class="container my-5" style="max-width: 600px;">
    <h2 class="mb-4 text-center">Liên hệ với người bán</h2>

    <!-- Hiển thị thông báo thành công -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>

    <!-- Hiển thị thông báo lỗi -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
        <%
            ProductRepository productRepository = new ProductRepository();
            Integer productId = Integer.valueOf(request.getParameter("productId"));
            Product product = productRepository.findById(productId);
            request.setAttribute("product", product);
        %>

    <form action="${pageContext.request.contextPath}/user/contact" method="post">
        <!-- Người nhận -->
        <input type="hidden" name="receiverId" value="${product.holderId}" />
        <input type="hidden" name="productId" value="${product.productId}" />

        <div class="mb-3">
            <label for="fullName" class="form-label">Họ tên</label>
            <input type="text" class="form-control" id="fullName" name="fullName" required value="${user.fullName}">
        </div>

        <div class="mb-3">
            <label for="phoneNumber" class="form-label">Số điện thoại</label>
            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" required value="${user.phoneNumber}">
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Nội dung liên hệ</label>
            <textarea class="form-control" id="description" name="description" rows="4" required placeholder="Nhập nội dung liên hệ..."></textarea>
        </div>

        <div class="d-grid">
            <button type="submit" class="btn btn-primary btn-lg">Gửi liên hệ</button>
        </div>
    </form>
</div>

<jsp:include page="footer.jsp" />
