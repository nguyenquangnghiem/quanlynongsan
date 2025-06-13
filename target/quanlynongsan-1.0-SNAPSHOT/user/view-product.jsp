<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<%@page import="com.mycompany.quanlynongsan.model.Category"%>
<jsp:include page="/header.jsp" />

<%
    Product product = (Product) request.getAttribute("product");
    List<Category> allCategories = (List<Category>) request.getAttribute("allCategories"); // full danh mục
    List<Integer> selectedCategoryIds = (List<Integer>) request.getAttribute("selectedCategoryIds"); // danh mục đã chọn
%>

<div class="container py-5" style="max-width: 900px;">
    <div class="card shadow-sm">
        <div class="card-body">
            <h3 class="mb-4 text-success">Chi tiết sản phẩm</h3>

            <div class="mb-3">
                <label class="form-label">Tên sản phẩm</label>
                <input type="text" class="form-control" value="<%= product.getName() %>" readonly>
            </div>

            <div class="mb-3">
                <label class="form-label">Mô tả sản phẩm</label>
                <textarea class="form-control" rows="3" readonly><%= product.getDescription() %></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">Nơi sản xuất</label>
                <input type="text" class="form-control" value="<%= product.getPlaceOfManufacture() %>" readonly>
            </div>

            <div class="mb-3">
                <label class="form-label">Danh mục sản phẩm</label>
                <select class="form-select" multiple disabled>
                    <% for (Category cat : allCategories) { %>
                        <option value="<%= cat.getCategoryId() %>" <%= selectedCategoryIds.contains(cat.getCategoryId()) ? "selected" : "" %>>
                            <%= cat.getName() %>
                        </option>
                    <% } %>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">Giá tiền (VNĐ)</label>
                <input type="text" class="form-control" value="<%= String.format("%,.0f", product.getPrice().doubleValue()) %> đ" readonly>
            </div>

            <div class="mb-3">
                <label class="form-label">Trạng thái</label>
                <input type="text" class="form-control" value="<%= product.getStatus() %>" readonly>
            </div>

            <div class="mb-3">
                <label class="form-label">Số lượng</label>
                <input type="number" class="form-control" value="<%= product.getQuantity() %>" readonly>
            </div>

            <div class="mb-3">
                <label class="form-label">Hình ảnh sản phẩm</label><br/>
                <% 
                    List<String> imageUrls = (List<String>)request.getAttribute("imageUrls");
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        for (String url : imageUrls) {
                %>
                    <img src="<%= url %>" alt="Ảnh sản phẩm" style="max-width: 150px; margin: 5px; border-radius: 8px;">
                <% 
                        }
                    } else {
                %>
                    <p class="text-muted">Không có ảnh</p>
                <% } %>
            </div>

            <div class="d-grid mt-4">
                <a href="${pageContext.request.contextPath}/secured/user/my-products" class="btn btn-secondary">Quay lại danh sách</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp" />
