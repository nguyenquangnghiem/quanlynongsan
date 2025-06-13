<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/header.jsp" />

<div class="container my-5">
    <div class="card shadow rounded-4">
        <div class="card-header bg-success text-white text-center rounded-top-4 py-3">
            <h3 class="mb-0">📦 Đơn hàng chưa xác nhận</h3>
        </div>

        <div class="card-body p-4">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th scope="col">Mã đơn</th>
                            <th scope="col">Ngày đặt</th>
                            <th scope="col">Khách hàng</th>
                            <th scope="col">Điện thoại</th>
                            <th scope="col">Địa chỉ</th>
                            <th scope="col">Tổng tiền</th>
                            <th scope="col" class="text-center">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${requestScope.pendingOrders}">
                            <tr>
                                <td>#${order.id}</td>
                                <td>${order.createdDate}</td> <!-- Có thể định dạng lại nếu muốn -->
                                <td>${order.buyerName}</td>
                                <td>${order.phone}</td>
                                <td>${order.address}</td>
                                <td><strong class="text-danger">${order.totalPrice}₫</strong></td>
                                <td class="text-center">
                                    <form action="${pageContext.request.contextPath}/secured/user/confirm-order" method="post" class="d-inline">
                                        <input type="hidden" name="orderId" value="${order.id}" />
                                        <button type="submit" name="action" value="confirm" class="btn btn-success btn-sm me-1">
                                            ✅ Xác nhận
                                        </button>
                                        <button type="submit" name="action" value="reject" class="btn btn-outline-danger btn-sm me-1">
                                            ❌ Từ chối
                                        </button>
                                    </form>
                                    <a href="${pageContext.request.contextPath}/secured/user/order-detail?orderId=${order.id}" class="btn btn-primary btn-sm">
                                        🔎 Chi tiết
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp" />
