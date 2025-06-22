<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/header.jsp" />

<div class="container my-5">
    <h2 class="mb-4">📊 Báo cáo doanh thu hệ thống</h2>

    <!-- Hiển thị thông báo lỗi nếu có -->
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger">${errorMsg}</div>
    </c:if>

    <!-- Form chọn lọc -->
    <form method="get" action="${pageContext.request.contextPath}/secured/admin/system-revenue-report" class="row g-3 mb-4 align-items-end">
        <div class="col-auto">
            <label for="type" class="form-label">Loại báo cáo</label>
            <select name="type" id="type" class="form-select" required>
                <option value="">-- Chọn --</option>
                <option value="day" ${type == 'day' ? 'selected' : ''}>Theo ngày</option>
                <option value="month" ${type == 'month' ? 'selected' : ''}>Theo tháng</option>
                <option value="year" ${type == 'year' ? 'selected' : ''}>Theo năm</option>
            </select>
        </div>
        <div class="col-auto">
            <label for="fromDate" class="form-label">Từ ngày</label>
            <input type="date" id="fromDate" name="fromDate" class="form-control"
                   value="${fromDate != null ? fromDate : ''}" required>
        </div>
        <div class="col-auto">
            <label for="toDate" class="form-label">Đến ngày</label>
            <input type="date" id="toDate" name="toDate" class="form-control"
                   value="${toDate != null ? toDate : ''}" required>
        </div>
        <div class="col-auto">
            <button type="submit" class="btn btn-primary">Xem báo cáo</button>
        </div>
    </form>

    <!-- Nếu có báo cáo thì hiển thị tổng quan và chi tiết -->
    <c:if test="${not empty reportList}">
        <!-- Tổng quan -->
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card border-success">
                    <div class="card-body">
                        <h5 class="card-title">Tổng doanh thu nông dân</h5>
                        <p class="card-text text-danger fw-bold">
                            <fmt:formatNumber value="${totalFarmerRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card border-warning">
                    <div class="card-body">
                        <h5 class="card-title">Tổng chi phí nhập của nhà phân phối</h5>
                        <p class="card-text text-danger fw-bold">
                            <fmt:formatNumber value="${totalImportCost}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card border-info">
                    <div class="card-body">
                        <h5 class="card-title">Tổng doanh thu nhà phân phối</h5>
                        <p class="card-text text-success fw-bold">
                            <fmt:formatNumber value="${totalDistributorRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bảng chi tiết theo nhóm time_group -->
        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <thead class="table-light">
                    <tr>
                        <th>Thời gian</th>
                        <th>Doanh thu nông dân</th>
                        <th>Chi phí nhập (distributor)</th>
                        <th>Doanh thu distributor</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${reportList}">
                        <tr>
                            <td>${r.timeGroup}</td>
                            <td class="text-end text-danger">
                                <fmt:formatNumber value="${r.farmerRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </td>
                            <td class="text-end text-warning">
                                <fmt:formatNumber value="${r.distributorImportCost}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </td>
                            <td class="text-end text-success">
                                <fmt:formatNumber value="${r.distributorRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>

<jsp:include page="/footer.jsp" />
