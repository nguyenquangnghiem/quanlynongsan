<%@page import="com.mycompany.quanlynongsan.model.HasLikeProduct"%>
<%@page import="com.mycompany.quanlynongsan.repository.HasLikeProductRepository"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.model.Product"%>
<%@page import="com.mycompany.quanlynongsan.model.ImageProduct"%>
<%@page import="com.mycompany.quanlynongsan.repository.ProductRepository"%>
<%@page import="com.mycompany.quanlynongsan.repository.ImageProductRepository"%>
<%@page import="com.mycompany.quanlynongsan.model.User"%>
<jsp:include page="/header.jsp" />

<%
    User currentUser = (User) session.getAttribute("user");
    HasLikeProductRepository favoriteRepo = new HasLikeProductRepository();
    ProductRepository productRepo = new ProductRepository();
    ImageProductRepository imageRepo = new ImageProductRepository();

    List<Integer> productIds = favoriteRepo.findProductIdsByUserId(currentUser.getUserId());
%>

<div class="container my-5">
    <h2 class="fw-bold text-center mb-4">Sản phẩm yêu thích</h2>
    <div class="table-responsive">
        <table class="table table-borderless align-middle">
            <thead class="bg-light">
                <tr class="text-uppercase small text-muted">
                    <th>Sản phẩm</th>
                    <th>Giá</th>
                    <th>Trạng thái</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Integer fav : productIds) {
                        Product product = productRepo.findById(fav);
                        List<ImageProduct> images = imageRepo.findByProductId(fav);
                        String imageUrl = (images != null && !images.isEmpty()) ? images.get(0).getUrlImage() : "default.png";
                %>
                <tr>
                    <td>
                        <div class="d-flex align-items-center">
                            <img src="<%= imageUrl %>" alt="<%= product.getName() %>" width="70" class="me-3 rounded">
                            <div><%= product.getName() %></div>
                        </div>
                    </td>
                    <td>
                            <span class="fw-bold"><%= product.getPrice() %>đ</span>
                    </td>
                    <td>
                        <span class="badge 
                            <%= product.getStatus().equals("BINH_THUONG") ? "bg-success-subtle text-success" : "" %>
                            <%= product.getStatus().equals("HET_HAN") ? "bg-danger-subtle text-danger" : "" %>
                            <%= product.getStatus().equals("SAP_HET_HAN") ? "bg-warning-subtle text-warning" : "" %>">
                            <%= 
                                product.getStatus().equals("BINH_THUONG") ? "Còn Hàng" :
                                product.getStatus().equals("HET_HAN") ? "Hết Hạn" :
                                product.getStatus().equals("SAP_HET_HAN") ? "Sắp Hết Hạn" : "" 
                            %>
                        </span>
                    </td>
                    <td class="text-end">
                        <form action="<%= request.getContextPath() %>/secured/user/favorite-products?productId=<%=product.getProductId()%>&action=addToCart" method="post" class="d-inline">
                            <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                            <button type="submit" class="btn btn-success btn-sm" <%= product.getQuantity() > 0 ? "" : "disabled" %>>Thêm Giỏ Hàng</button>
                        </form>
                        <form action="<%= request.getContextPath() %>/secured/user/favorite-products?productId=<%=product.getProductId()%>&action=delete" method="post" class="d-inline ms-2">
                            <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                            <button type="submit" class="btn btn-outline-danger btn-sm">&times;</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../footer.jsp" />

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const breadcrumb = document.querySelector(".breadcrumb").innerHTML = `<div><span class="material-symbols-outlined">home</span> &bull; Sản phẩm yêu thích</div>`;
    });
</script>
