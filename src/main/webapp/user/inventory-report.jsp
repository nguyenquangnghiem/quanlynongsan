<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/header.jsp" />

<div class="container my-5">

    <div class="text-center mb-4">
        <h2>📊 Báo cáo nhập xuất hàng</h2>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/secured/user/inventory-report" class="text-center mb-4">
        <label for="type" class="me-2">Chọn loại báo cáo:</label>
        <select name="type" id="type" class="form-select d-inline w-auto" onchange="this.form.submit()">
            <option value="">-- Chọn --</option>
            <option value="day" ${param.type == 'day' ? 'selected' : ''}>Theo ngày (tháng này)</option>
            <option value="month" ${param.type == 'month' ? 'selected' : ''}>Theo tháng (năm này)</option>
            <option value="year" ${param.type == 'year' ? 'selected' : ''}>Theo năm</option>
        </select>
    </form>

    <c:if test="${not empty report}">
        <!-- Tổng quan nhập hàng -->
        <div class="mb-4">
            <h4>📥 Tổng quan nhập hàng</h4>
            <ul>
                <li><strong>Tổng số sản phẩm đã nhập:</strong> ${report.totalImportedProducts}</li>
                <li><strong>Tổng số lần nhập kho:</strong> ${report.totalImportEvents}</li>
                <li><strong>Tổng chi phí nhập hàng:</strong> <span class="text-danger">${report.totalImportCost}₫</span></li>
            </ul>
        </div>

        <!-- Tổng quan xuất hàng -->
        <div class="mb-4">
            <h4>📤 Tổng quan bán hàng</h4>
            <ul>
                <li><strong>Tổng số sản phẩm đã bán:</strong> ${report.totalSoldProducts}</li>
                <li><strong>Tổng số đơn hàng:</strong> ${report.totalSoldOrders}</li>
                <li><strong>Tổng doanh thu:</strong> <span class="text-success">${report.totalSalesRevenue}₫</span></li>
            </ul>
        </div>

        <c:if test="${not empty report.details}">
            <c:set var="grouped" value="${report.details}" />

            <c:forEach var="group" items="${grouped}" varStatus="loop">

                <c:choose>
                    <c:when test="${param.type == 'day'}">
                        <c:set var="groupName" value="${group.dateDisplay}" />
                    </c:when>
                    <c:when test="${param.type == 'month'}">
                        <c:set var="groupName" value="${group.monthDisplay}" />
                    </c:when>
                    <c:when test="${param.type == 'year'}">
                        <c:set var="groupName" value="${group.year}" />
                    </c:when>
                </c:choose>

                <c:if test="${loop.first or groupName != prevGroupName}">
                    <c:if test="${not loop.first}">
                        </tbody></table></div></div>
                    </c:if>
                    <div class="card shadow rounded-4 mb-4">
                        <div class="card-header bg-secondary text-white rounded-top-4 py-2">
                            <h5 class="mb-0">
                                <c:choose>
                                    <c:when test="${param.type == 'day'}">Ngày: ${groupName}</c:when>
                                    <c:when test="${param.type == 'month'}">Tháng: ${groupName}</c:when>
                                    <c:when test="${param.type == 'year'}">Năm: ${groupName}</c:when>
                                </c:choose>
                            </h5>
                        </div>
                        <div class="card-body p-0">
                            <table class="table table-bordered table-striped align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th>Giá nhập</th>
                                        <th>Số lượng nhập</th>
                                        <th>Chi phí nhập</th>
                                        <th>Giá bán</th>
                                        <th>Số lượng bán</th>
                                        <th>Doanh thu bán</th>
                                    </tr>
                                </thead>
                                <tbody>
                </c:if>

                <tr>
                    <td>${group.productName}</td>
                    <td>${group.importPrice}₫</td>
                    <td>${group.totalImported}</td>
                    <td class="text-danger">${group.totalImportCost}₫</td>
                    <td>${group.sellPrice}₫</td>
                    <td>${group.totalSold}</td>
                    <td class="text-success">${group.totalRevenue}₫</td>
                </tr>

                <c:set var="prevGroupName" value="${groupName}" />

                <c:if test="${loop.last}">
                        </tbody></table></div></div>
                </c:if>

            </c:forEach>
        </c:if>
    </c:if>

</div>

<jsp:include page="/footer.jsp" />
