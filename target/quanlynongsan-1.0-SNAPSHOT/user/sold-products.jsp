<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../header.jsp" />

<div class="container mt-5">
    <h2 class="mb-4">Thống kê sản phẩm đã bán</h2>

    <table class="table table-bordered table-hover">
        <thead>
            <tr>
                <th>#</th>
                <th>Tên sản phẩm</th>
                <th>Giá</th>
                <th>Đang bán</th>
                <th>Số lượng đã bán</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="product" items="${soldProducts}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${product.name}</td>
                    <td>${product.price} đ</td>
                    <td>
                        <c:choose>
                            <c:when test="${product.isSell}">✔️</c:when>
                            <c:otherwise>❌</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${product.soldQuantity}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <a href="${pageContext.request.contextPath}/secured/user/my-products" class="btn btn-secondary mt-3">← Quay lại Sản phẩm của bạn</a>
</div>

<jsp:include page="../footer.jsp" />
