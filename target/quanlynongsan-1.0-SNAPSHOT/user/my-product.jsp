<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<jsp:include page="/header.jsp" />

<div class="container py-5" style="max-width: 1000px;">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h3 class="text-success">Sản phẩm của bạn</h3>
        <a href="${pageContext.request.contextPath}/secured/user/sell-product" class="btn btn-success">+ Thêm sản phẩm mới</a>
    </div>

    <% 
        List<Product> products = (List<Product>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
    %>

    <div class="table-responsive">
        <table class="table table-bordered table-hover align-middle">
            <thead class="table-success text-center">
                <tr>
                    <th>#</th>
                    <th>Tên sản phẩm</th>
                    <th>Giá (VNĐ)</th>
                    <th>Số lượng</th>
                    <th>Trạng thái</th>
                    <th>Đăng bán</th> <!-- ✅ Cột mới -->
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <% int index = 1;
                   for (Product p : products) { %>
                    <tr>
                        <td class="text-center"><%= index++ %></td>
                        <td><%= p.getName() %></td>
                        <td><%= String.format("%,.0f", p.getPrice().doubleValue()) %></td>
                        <td class="text-center"><%= p.getQuantity() %></td>
                        <td class="text-center"><%= 
               "BINH_THUONG".equals(p.getStatus()) ? "Bình thường" :
               "SAP_HET_HAN".equals(p.getStatus()) ? "Sắp hết hạn" :
               "HET_HAN".equals(p.getStatus()) ? "Hết hạn" :
               p.getStatus()
           %></td>
                        <td class="text-center"><%= p.getIsSell()? "Có" : "Không" %></td> <!-- ✅ Hiển thị Có/Không -->
                        <td class="text-center">
                            <a href="${pageContext.request.contextPath}/secured/user/view-product?id=<%= p.getProductId() %>" class="btn btn-sm btn-primary">Xem chi tiết</a>
                            <a href="${pageContext.request.contextPath}/secured/user/edit-product?id=<%= p.getProductId() %>" class="btn btn-sm btn-warning">Chỉnh sửa</a>
                            <a href="${pageContext.request.contextPath}/secured/user/delete-product?id=<%= p.getProductId() %>" class="btn btn-sm btn-danger" onclick="return confirm('Bạn chắc chắn muốn xóa sản phẩm này?');">Xóa</a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <% } else { %>
        <div class="alert alert-info">Bạn chưa có sản phẩm nào được đăng bán.</div>
    <% } %>
</div>

<jsp:include page="/footer.jsp" />
