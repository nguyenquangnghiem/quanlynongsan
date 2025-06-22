<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/header.jsp" />

<div class="container my-5">
    <div class="card shadow rounded-4">
        <div class="card-header bg-secondary text-white text-center rounded-top-4 py-3">
            <h3 class="mb-0">📦 Đơn hàng đã hoàn tất</h3>
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
                        <c:forEach var="order" items="${requestScope.successfulOrders}">
                            <tr>
                                <td>#${order.id}</td>
                                <td>${order.createdDate}</td>
                                <td>${order.buyerName}</td>
                                <td>${order.phone}</td>
                                <td>${order.address}</td>
                                <td><strong class="text-danger">${order.totalPrice}₫</strong></td>
                                <td class="text-center">
                                    <form action="${pageContext.request.contextPath}/secured/user/pending-orders?action=detail" method="post" class="d-inline">
                                        <input type="hidden" name="orderId" value="${order.id}" />
                                        <button class="btn btn-primary btn-sm" type="submit">Chi tiết</button>
                                    </form>
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
