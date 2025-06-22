<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="/header.jsp" />

<%
    List<Product> products = (List<Product>) request.getAttribute("products");
%>

<div class="container my-5">
    <h2 class="mb-4 fw-bold">Sản phẩm chờ duyệt</h2>

    <table class="table table-striped table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Tên sản phẩm</th>
                <th>Giá</th>
                <th>Số lượng</th>
                <th>Ngày tạo</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <% if (products != null && !products.isEmpty()) {
                for (Product p : products) {
            %>
            <tr>
                <td><%= p.getProductId() %></td>
                <td><%= p.getName() %></td>
                <td><%= p.getPrice() %> đ</td>
                <td><%= p.getQuantity() %></td>
                <td><%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getCreatedDate()) %></td>
                <td class="d-flex gap-1">
                    <!-- 🔔 Nút Duyệt -->
                    <form action="<%= request.getContextPath() %>/secured/admin/product-approve" method="post" class="d-inline">
                        <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                        <input type="hidden" name="action" value="approve">
                        <button type="submit" class="btn btn-success btn-sm">Duyệt</button>
                    </form>

                    <!-- 🔔 Nút Từ chối -->
                    <form action="<%= request.getContextPath() %>/secured/admin/product-approve" method="post" class="d-inline">
                        <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                        <input type="hidden" name="action" value="reject">
                        <button type="submit" class="btn btn-danger btn-sm">Từ chối</button>
                    </form>

                    <!-- 🔔 Nút Chi tiết -->
                    <a href="<%= request.getContextPath() %>/secured/user/view-product?id=<%= p.getProductId() %>" class="btn btn-primary btn-sm">Chi tiết</a>
                </td>
            </tr>
            <% }} else { %>
            <tr><td colspan="6" class="text-center">Không có sản phẩm chờ duyệt.</td></tr>
            <% } %>
        </tbody>
    </table>
</div>

<jsp:include page="/footer.jsp" />
