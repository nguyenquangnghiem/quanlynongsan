<%@page import="com.mycompany.quanlynongsan.response.StockReport"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, java.text.SimpleDateFormat"%>
<jsp:include page="/header.jsp" />

<div class="container py-5" style="max-width: 1000px;">
    <h3 class="mb-4 text-success">Báo cáo nhập xuất hàng</h3>

    <!-- Form chọn thời gian -->
    <form action="<%= request.getContextPath() %>/secured/user/report-stock" method="get" class="row g-3 align-items-end mb-4">
        <div class="col-md-4">
            <label class="form-label">Từ ngày</label>
            <input type="date" name="startDate" class="form-control" value="<%= request.getParameter("startDate") != null ? request.getParameter("startDate") : "" %>">
        </div>
        <div class="col-md-4">
            <label class="form-label">Đến ngày</label>
            <input type="date" name="endDate" class="form-control" value="<%= request.getParameter("endDate") != null ? request.getParameter("endDate") : "" %>">
        </div>
        <div class="col-md-4 d-grid">
            <button type="submit" class="btn btn-success">Lọc báo cáo</button>
        </div>
    </form>

    <%
        List<StockReport> reports = (List<StockReport>) request.getAttribute("reports");
        if (reports != null && !reports.isEmpty()) {
    %>

    <!-- Bảng báo cáo -->
    <div class="table-responsive">
        <table class="table table-bordered align-middle text-center">
            <thead class="table-success">
                <tr>
                    <th>#</th>
                    <th>Ngày</th>
                    <th>Nhập hàng</th>
                    <th>Xuất hàng</th>
                    <th>Tồn cuối</th>
                </tr>
            </thead>
            <tbody>
                <%
                    int index = 1;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    for (StockReport report : reports) {
                %>
                <tr>
                    <td><%= index++ %></td>
                    <td><%= sdf.format(report.getDate()) %></td>
                    <td><%= report.getImportedQuantity() %></td>
                    <td><%= report.getExportedQuantity() %></td>
                    <td><%= report.getStockRemaining() %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <% } else if (request.getParameter("startDate") != null && request.getParameter("endDate") != null) { %>
        <div class="alert alert-warning">Không có dữ liệu trong khoảng thời gian đã chọn.</div>
    <% } %>

    <div class="mt-3">
        <a href="<%= request.getContextPath() %>/secured/user/my-stock" class="btn btn-secondary">Quay lại kho của tôi</a>
    </div>
</div>

<jsp:include page="/footer.jsp" />
