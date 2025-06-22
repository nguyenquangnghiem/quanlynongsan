<%@page import="com.mycompany.quanlynongsan.response.BehaviorLog"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>

<jsp:include page="/header.jsp" />

<%
    List<BehaviorLog> logs = (List<BehaviorLog>) request.getAttribute("logs");
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
%>

<div class="container my-5">
    <h2 class="mb-4 fw-bold">Giám sát hành vi người dùng</h2>

    <table class="table table-bordered table-hover table-striped">
        <thead class="table-light">
            <tr>
                <th>Người dùng</th>
                <th>Email</th>
                <th>Hành vi</th>
                <th>Mô tả</th>
                <th>Thời gian</th>
            </tr>
        </thead>
        <tbody>
            <% if (logs != null && !logs.isEmpty()) {
                for (BehaviorLog log : logs) {
            %>
            <tr>
                <td><%= log.getFullName() != null ? log.getFullName() : "-" %></td>
                <td><%= log.getEmail() != null ? log.getEmail() : "-" %></td>
                <td><%= log.getCode() != null ? log.getCode() : "-" %></td>
                <td><%= log.getDescription() != null ? log.getDescription() : "-" %></td>
                <td><%= java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(log.getTime()) %></td>
            </tr>
            <% }} else { %>
            <tr><td colspan="5" class="text-center">Không có dữ liệu hành vi.</td></tr>
            <% } %>
        </tbody>
    </table>

    <!-- Phân trang -->
    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <% for (int i = 1; i <= totalPages; i++) { %>
                <li class="page-item <%= (i == currentPage) ? "active" : "" %>">
                    <a class="page-link" href="?page=<%= i %>"><%= i %></a>
                </li>
            <% } %>
        </ul>
    </nav>
</div>

<jsp:include page="/footer.jsp" />
