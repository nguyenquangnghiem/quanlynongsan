<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page import="java.util.stream.Collectors"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<%@page import="com.mycompany.quanlynongsan.model.Category"%>
<jsp:include page="/header.jsp" />

<%
    Product product = (Product) request.getAttribute("product");
    List<Category> allCategories = (List<Category>) request.getAttribute("allCategories");
    List<Category> selectedCategoryIds = (List<Category>) request.getAttribute("selectedCategoryIds");
%>

<div class="container py-5" style="max-width: 900px;">
    <div class="card shadow-sm">
        <div class="card-body">
            <h3 class="mb-4 text-success">Chỉnh sửa sản phẩm</h3>

            <form action="${pageContext.request.contextPath}/secured/user/edit-product" method="POST" enctype="multipart/form-data">
                <input hidden name="action" value="update"/>
                <input hidden name="product_id" value="<%= product.getProductId() %>"/>

                <div class="mb-3">
                    <label class="form-label">Tên sản phẩm</label>
                    <input type="text" class="form-control" name="name" value="<%= product.getName() %>" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Mô tả sản phẩm</label>
                    <textarea class="form-control" name="description" rows="3"><%= product.getDescription() %></textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Nơi sản xuất</label>
                    <input type="text" class="form-control" name="place_of_manufacture" value="<%= product.getPlaceOfManufacture() %>" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Danh mục sản phẩm</label>
                    <select class="form-select" name="category_ids" multiple required>
                        <% for (Category cat : allCategories) { %>
                            <option value="<%= cat.getCategoryId() %>" 
                                    <%= selectedCategoryIds.stream().map(it -> it.getCategoryId()).collect(Collectors.toList()).contains(cat.getCategoryId()) ? "selected" : "" %>>
                                <%= cat.getName() %>
                            </option>
                        <% } %>
                    </select>
                    <div class="form-text">Giữ Ctrl (hoặc Cmd trên Mac) để chọn nhiều danh mục</div>
                </div>
                    
                <div class="mb-3">
                    <label class="form-label">Đăng bán</label>
                    <select class="form-select" name="is_sell" required>
                        <option value="true" <%= product.getIsSell()? "selected" : "" %>>Có</option>
                        <option value="false" <%= !product.getIsSell()? "selected" : "" %>>Không</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Giá tiền (VNĐ)</label>
                    <input type="number" class="form-control" name="price" value="<%= product.getPrice().doubleValue() %>" required min="0">
                </div>

                <div class="mb-3">
                    <label class="form-label">Trạng thái</label>
                    <select class="form-select" name="status" required>
                        <option value="">-- Chọn trạng thái --</option>
                        <option value="BINH_THUONG" <%= "BINH_THUONG".equals(product.getStatus()) ? "selected" : "" %>>Bình thường</option>
                        <option value="SAP_HET_HAN" <%= "SAP_HET_HAN".equals(product.getStatus()) ? "selected" : "" %>>Sắp hết hạn</option>
                        <option value="HET_HAN" <%= "HET_HAN".equals(product.getStatus()) ? "selected" : "" %>>Hết hạn</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Số lượng</label>
                    <input type="number" class="form-control" name="quantity" value="<%= product.getQuantity() %>" required min="1">
                </div>

                <div class="mb-3">
                    <label class="form-label">Hình ảnh mới (nếu muốn thay đổi)</label>
                    <input type="file" class="form-control" name="images" accept="image/*" multiple>
                    <div class="form-text">Bạn có thể chọn nhiều ảnh mới (jpg, png...). Bỏ qua nếu không muốn thay đổi ảnh.</div>
                </div>

                <% 
                    List<String> imageUrls = (List<String>) request.getAttribute("imageUrls");
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                %>
                    <div class="mb-3">
                        <label class="form-label">Hình ảnh hiện tại:</label><br/>
                        <% for (String url : imageUrls) { %>
                            <img src="<%= url %>" alt="Ảnh sản phẩm" style="max-width: 150px; margin: 5px; border-radius: 8px;">
                        <% } %>
                    </div>
                <% } %>

                <!-- Hidden fields mặc định -->
                <input type="hidden" name="is_active" value="<%= product.getIsActive() %>">
                <input type="hidden" name="is_browse" value="<%= product.getIsBrowse() %>">

                <div class="d-grid mt-4">
                    <button type="submit" class="btn btn-warning">Cập nhật sản phẩm</button>
                                <%
    User user = (User) session.getAttribute("user");
    String backUrl = request.getContextPath() + "/secured/user/my-products"; // Mặc định quay về my-products
    if (user != null) {
         if (user.getRoleId() == 2) {
            backUrl = request.getContextPath() + "/secured/user/my-stock"; // Nếu là user (role_id = 2)
        }
    }
%>

<div class="d-grid mt-4">
    <a href="<%= backUrl %>" class="btn btn-secondary">Quay lại danh sách</a>
</div>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp" />
