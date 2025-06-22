<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page import="com.mycompany.quanlynongsan.repository.CategoryRepository"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<%@page import="com.mycompany.quanlynongsan.model.Category"%>
<jsp:include page="/header.jsp" />

<%
    Product product = (Product) request.getAttribute("product");
    CategoryRepository categoryRepository = new CategoryRepository();
    List<Category> allCategories = (List<Category>) request.getAttribute("allCategories"); // full danh mục
    List<Category> selectedCategoryIds = (List<Category>) request.getAttribute("selectedCategoryIds"); // danh mục đã chọn
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
                    <% for (Category cat : selectedCategoryIds) { %>
                        <option value="<%= cat.getCategoryId() %>">
                            <%= cat.getName() %>
                        </option>
                    <% } %>
                </select>
            </div>
                
            <div class="mb-3">
                <label class="form-label">Đăng bán</label>
                <input type="text" class="form-control" value="<%= product.getIsSell()? "Có" : "Không" %>" readonly>
            </div>


            <div class="mb-3">
                <label class="form-label">Giá tiền (VNĐ)</label>
                <input type="text" class="form-control" value="<%= String.format("%,.0f", product.getPrice().doubleValue()) %> đ" readonly>
            </div>

            <div class="mb-3">
                <label class="form-label">Trạng thái</label>
                <input type="text" class="form-control" 
                       value="<%= 
                           "BINH_THUONG".equals(product.getStatus()) ? "Bình thường" :
                           "SAP_HET_HAN".equals(product.getStatus()) ? "Sắp hết hạn" :
                           "HET_HAN".equals(product.getStatus()) ? "Hết hạn" :
                           product.getStatus()
                       %>" readonly>
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

            <%
    User user = (User) session.getAttribute("user");
    String backUrl = request.getContextPath() + "/secured/user/my-products"; // Mặc định quay về my-products
    if (user != null) {
         if (user.getRoleId() == 2) {
            backUrl = request.getContextPath() + "/secured/user/my-stock"; // Nếu là user (role_id = 2)
        } else if (user.getRoleId() == 4) {
            backUrl = request.getContextPath() + "/secured/admin/product-pending"; // Nếu là user (role_id = 2)
                }
    }
%>

<div class="d-grid mt-4">
    <a href="<%= backUrl %>" class="btn btn-secondary">Quay lại danh sách</a>
</div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp" />
