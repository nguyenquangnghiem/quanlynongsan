<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/header.jsp" />

<div class="container my-5">

    <div class="text-center mb-5">
        <h2 class="fw-bold text-primary">📊 Báo cáo doanh thu</h2>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/secured/user/revenue-report" class="row justify-content-center mb-5">
        <div class="col-auto">
            <label for="type" class="form-label fw-semibold me-2">Chọn loại báo cáo:</label>
        </div>
        <div class="col-auto">
            <select name="type" id="type" class="form-select" onchange="this.form.submit()">
                <option value="">-- Chọn --</option>
                <option value="day" ${param.type == 'day' ? 'selected' : ''}>Theo ngày (tháng này)</option>
                <option value="month" ${param.type == 'month' ? 'selected' : ''}>Theo tháng (năm này)</option>
                <option value="year" ${param.type == 'year' ? 'selected' : ''}>Theo năm</option>
            </select>
        </div>
    </form>

    <c:if test="${not empty revenueReport}">
        <div class="mb-5 p-4 rounded bg-light shadow-sm border">
            <h4 class="fw-bold mb-3">Tổng quan</h4>
            <ul class="list-unstyled fs-5 mb-0">
                <li class="mb-2"><strong>Tổng số sản phẩm đã bán:</strong> ${revenueReport.totalProductsSold}</li>
                <li class="mb-2"><strong>Tổng số đơn hàng:</strong> ${revenueReport.totalDistinctOrders}</li>
                <li><strong>Tổng doanh thu:</strong> <span class="text-danger fw-bold fs-4">${revenueReport.totalRevenue}₫</span></li>
            </ul>
        </div>

        <c:set var="groupedProducts" value="${revenueReport.products}" />
        <c:if test="${not empty groupedProducts}">
            <c:set var="prevGroupName" value="" />

            <c:forEach var="group" items="${groupedProducts}" varStatus="loop">

                <c:choose>
                    <c:when test="${param.type == 'day'}"><c:set var="groupName" value="${group.dateDisplay}" /></c:when>
                    <c:when test="${param.type == 'month'}"><c:set var="groupName" value="${group.monthDisplay}" /></c:when>
                    <c:when test="${param.type == 'year'}"><c:set var="groupName" value="${group.year}" /></c:when>
                </c:choose>

                <!-- Nếu sang nhóm mới, đóng bảng cũ (nếu có) -->
                <c:if test="${not loop.first and groupName != prevGroupName}">
                            </tbody>
                        </table>
                    </div> <!-- table-responsive -->
                </div> <!-- card -->
                </c:if>

                <!-- Nếu là nhóm mới hoặc lần đầu -->
                <c:if test="${loop.first or groupName != prevGroupName}">
                    <div class="card shadow-sm rounded-4 mb-4 border-primary">
                        <div class="card-header bg-primary text-white rounded-top-4 py-2">
                            <h5 class="mb-0">
                                <c:choose>
                                    <c:when test="${param.type == 'day'}">📅 Ngày: ${groupName}</c:when>
                                    <c:when test="${param.type == 'month'}">🗓️ Tháng: ${groupName}</c:when>
                                    <c:when test="${param.type == 'year'}">📆 Năm: ${groupName}</c:when>
                                </c:choose>
                            </h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover align-middle mb-0">
                                    <thead class="table-light small text-uppercase text-muted">
                                        <tr>
                                            <th>Mã SP</th>
                                            <th>Tên sản phẩm</th>
                                            <th>Giá</th>
                                            <th>Đã bán</th>
                                            <th>Đơn hàng</th>
                                            <th>Doanh thu</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                </c:if>

                <!-- Dữ liệu sản phẩm -->
                <tr>
                    <td>#${group.productId}</td>
                    <td>${group.productName}</td>
                    <td>${group.productPrice}₫</td>
                    <td>${group.totalQuantitySold}</td>
                    <td>${group.totalDistinctOrders}</td>
                    <td class="text-danger fw-bold">${group.totalRevenue}₫</td>
                </tr>

                <c:set var="prevGroupName" value="${groupName}" />

                <!-- Nếu là vòng lặp cuối cùng, đóng bảng -->
                <c:if test="${loop.last}">
                            </tbody>
                        </table>
                    </div> <!-- table-responsive -->
                </div> <!-- card -->
                </c:if>

            </c:forEach>
        </c:if>
    </c:if>

</div>

