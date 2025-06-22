<%@page import="com.mycompany.quanlynongsan.dto.ProductDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />

<%
    ProductDTO product = (ProductDTO) request.getAttribute("product");
%>
<style>
    .container-product{
        margin-left: 300px;
        margin-right: 300px;
        margin-top: 32px;
    }
    .discount-badge {
        background-color: #ff6b6b;
        color: white;
        padding: 3px 8px;
        border-radius: 4px;
        font-size: 0.9rem;
        font-weight: bold;
    }
    .price-section {
        font-size: 1.5rem;
        font-weight: bold;
    }
    .original-price {
        text-decoration: line-through;
        color: #888;
        font-size: 1rem;
    }
    .divider {
        border-top: 1px solid #eee;
        margin: 20px 0;
    }
    .tag-badge {
        background-color: #f0f0f0;
        color: #555;
        padding: 3px 8px;
        border-radius: 4px;
        font-size: 0.8rem;
        margin-right: 5px;
        display: inline-block;
        margin-bottom: 5px;
    }
    .info-section {
        margin-bottom: 20px;
    }
    .info-title {
        font-weight: bold;
        margin-bottom: 5px;
    }
    .organic-badge {
        background-color: #4caf50;
        color: white;
        padding: 5px 10px;
        border-radius: 4px;
        font-size: 0.9rem;
        display: inline-block;
        margin-top: 10px;
    }
</style>
<div class="container-product">

    <div class="row">
        <div class="col-md-6 text-center">
            <img src="${product.imageUrls[0]}" alt="" class="img-fluid mb-2" style="max-height: 300px;" id="productImageMain" />
            <div id="thumbnailContainer" class="thumbnail-container">
                <c:forEach var="url" items="${product.imageUrls}">
                    <img src="${url}" class="thumbnail-img m-1" style="width: 60px; cursor: pointer; border-radius: 5px;">
                </c:forEach>
            </div>
        </div>

        <div class="col-md-6">
            <h1 class="mb-2">${product.name}</h1>
            
            <!-- ⭐ Hiển thị số sao trung bình và số lượt đánh giá -->
            <div class="mb-2">
                <c:choose>
                    <c:when test="${product.rates > 0}">
                        <c:forEach begin="1" end="5" var="i">
                            <i class="fa-star <c:if test='${i <= product.rates}'>fas text-warning</c:if><c:if test='${i > product.rates}'>far text-warning</c:if>'"></i>
                        </c:forEach>
                        <span> (${product.reviewerQuantity} đánh giá)</span>
                    </c:when>
                    <c:otherwise>
                        <span>Chưa có đánh giá</span>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="mb-3">
                <span class="price-section">${product.price}đ</span>
            </div>
            
            <div class="divider"></div>
            
            <div class="info-section">
                <div class="info-title">Danh mục: 
                    <c:forEach var="category" items="${product.categories}" varStatus="status">
                        ${category}<c:if test="${!status.last}">, </c:if>
                    </c:forEach>
                </div>
            </div>
            
            <div class="info-section">
                <div class="info-title">Chi tiết:</div>
                <p>${product.description}</p>
            </div>
            
            <div class="divider"></div>
            
            <div class="quantity-selector mb-3">
                <label for="quantity" class="form-label"><strong>Số lượng:</strong></label>
                <div class="input-group" style="width: 150px;">
                    <button class="btn btn-outline-secondary quantity-minus" type="button">-</button>
                    <input type="number" class="form-control-lg text-center quantity-input" value="1" min="1" max="99">
                    <button class="btn btn-outline-secondary quantity-plus" type="button">+</button>
                </div>
            </div>   

            <div class="d-flex gap-2 mt-3">
                <button class="btn btn-success flex-grow-1 btn-add-to-cart">Thêm vào giỏ hàng</button>
                <button class="btn btn-outline-danger btn-favorite" title="Add to favorites">
                    <i class="far fa-heart"></i>
                </button>
            </div>
            
            <!-- Thêm nút liên hệ ở đây -->
            <div class="mt-3">
                <a href="${pageContext.request.contextPath}/contact-form.jsp?productId=${product.productId}" class="btn btn-primary w-100">
                    <i class="fas fa-envelope"></i> Liên hệ người bán
                </a>
            </div>
            
            <div class="divider"></div>
        </div>
            <div class="container mt-5">
    <h2>Đánh giá sản phẩm</h2>
    
    <c:choose>
        <c:when test="${empty requestScope.reviews}">
            <p>Chưa có đánh giá nào cho sản phẩm này.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="review" items="${requestScope.reviews}">
                <div class="review-item mb-3 p-3 border rounded">
                    <strong>${review.username}</strong>
                    <div>
                        <c:forEach begin="1" end="5" var="i">
                            <i class="fa-star <c:if test='${i <= review.rating}'>fas text-warning</c:if><c:if test='${i > review.rating}'>far text-muted</c:if>'"></i>
                        </c:forEach>
                    </div>
                    <p class="mt-2">${review.comment}</p>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
    </div>
            
            

</div>
            
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script>
                async function isProductFavorited(id) {
        const contextPath = "/" + window.location.pathname.split('/')[1];
        try {
            const url = contextPath + `/secured/user/has-like-product?productId=` + id;
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            });

            // ✅ Xử lý nếu chưa đăng nhập
             if (response.status === 401) {
                return false; // Dừng luôn
            }

            if (!response.ok) {
                throw new Error('Failed to fetch');
            }

            const data = await response.json();
            return data; // true hoặc false
        } catch (error) {
            console.error(error);
            return false;
        }
    }
   (async function() {
        const productId = <%= product.getProductId()%>; 

        const isFavorited = await isProductFavorited(productId);
        const $heartIcon = $('.btn-favorite i');

        if (isFavorited) {
            $heartIcon.removeClass('far').addClass('fas text-danger');
        } else {
            $heartIcon.removeClass('fas text-danger').addClass('far');
        }

        window.currentProductId = productId;

        const contextPath = window.location.origin + "${pageContext.request.contextPath}";
        $('#productDetailPageLink').attr('href', contextPath + '/product-detail?productId=' + productId);

    })();
</script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const breadcrumb = document.querySelector(".breadcrumb").innerHTML = `<div><span class="material-symbols-outlined">home</span> &bull; Trang chủ &bull; Tìm kiếm &bull; Chi tiết sản phẩm</div>`;
    });
</script>
<script>
    
   

    $(document).ready(function() {
    // Xử lý giảm số lượng
        $('.quantity-minus').click(function() {
            let input = $(this).siblings('.quantity-input');
            let value = parseInt(input.val());
            let min = parseInt(input.attr('min')) || 1;
            if (value > min) {
                input.val(value - 1);
            }
        });
                // Xử lý tăng số lượng
        $('.quantity-plus').click(function() {
            let input = $(this).siblings('.quantity-input');
            let value = parseInt(input.val());
            let max = parseInt(input.attr('max')) || 99;
            if (value < max) {
                input.val(value + 1);
                }
        });
            // Đảm bảo số lượng không vượt quá giới hạn khi nhập tay
        $('.quantity-input').on('input', function() {
            let min = parseInt($(this).attr('min')) || 1;
            let max = parseInt($(this).attr('max')) || 99;
            let value = parseInt($(this).val()) || min;
            if (value < min) value = min;
            if (value > max) value = max;
            $(this).val(value);
        });
            // Xử lý thêm vào yêu thích
        $('.btn-favorite').click(async function () {
            const $icon = $(this).find('i');
            const isFavorited = $icon.hasClass('fas');
            const productId = window.currentProductId;
            console.log(isFavorited + "," + productId);
            const contextPath = "/" + window.location.pathname.split('/')[1];
            if (isFavorited) {
            // Gọi API bỏ yêu thích
                const response = await fetch(contextPath + `/secured/user/has-like-product?productId=` + productId + `&action=delete`, { 
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                });
                // ✅ Xử lý nếu chưa đăng nhập
                if (response.status === 401) {
                    if(window.confirm("Vui lòng đăng nhập trước!")){
                        window.location.href = contextPath + '/login';
                    }
                    return;
                }
            } else {
                // Gọi API thêm vào yêu thích
                const response = await fetch(contextPath + `/secured/user/has-like-product?productId=` + productId + `&action=add`, { 
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                });
                // ✅ Xử lý nếu chưa đăng nhập
                if (response.status === 401) {
                    if(window.confirm("Vui lòng đăng nhập trước!")){
                        window.location.href = contextPath + '/login';
                    }
                    return;
                }
            }

            // Toggle giao diện sau khi API xong
            $icon.toggleClass('far fas').toggleClass('text-danger');
        });
        
        $('.btn-add-to-cart').click(async function () {
            const productId = window.currentProductId;
            const quantity = parseInt($('.quantity-input').val()) || 1;

            const contextPath = "/" + window.location.pathname.split('/')[1];

            try {
                const response = await fetch(contextPath + `/secured/user/has-cart`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    body: new URLSearchParams({
                        productId: productId,
                        quantity: quantity
                    }),
                    credentials: 'include' // <-- Quan trọng khi dùng session
                });
                
                 if (response.status === 401) {
                    if(window.confirm("Vui lòng đăng nhập trước!")){
                        window.location.href = contextPath + '/login';
                    }
                    return;
                }

                const result = await response.json();

                if (result === true) {
                    alert("Đã thêm vào giỏ hàng!");

                    // Sau khi thêm thành công -> cập nhật giao diện giỏ hàng
                    await updateCartSummary(contextPath);
                } else {
                    alert("Thêm vào giỏ hàng thất bại!");
                }
            } catch (error) {
                console.error(error);
                alert("Lỗi kết nối máy chủ!");
            }
        });

        // Hàm cập nhật lại tổng tiền và số lượng sản phẩm trong giỏ hàng
        async function updateCartSummary(contextPath) {
            try {
                const response = await fetch(contextPath + '/secured/user/has-cart', {
                    credentials: 'include', // <-- BẮT BUỘC khi dùng session
                    method: 'GET'
                });

                if (!response.ok) {
                    document.querySelector('.mid__information--cart').style.display = 'none';
                    return;
                }

                const data = await response.json();
                const total = data.totalPrice || 0;
                const count = data.totalQuantity || 0;

                document.getElementById("cart-total-price").textContent = formatMoney(total) + "đ";
                document.getElementById("cart-count").textContent = count;

                if (count === 0) {
                    document.getElementById("cart-count").style.display = 'none';
                } else {
                    document.getElementById("cart-count").style.display = 'inline-block';
                    document.querySelector('.mid__information--cart').style.display = 'flex';
                }
            } catch (err) {
                console.error(err);
                document.querySelector('.mid__information--cart').style.display = 'none';
            }
        }

        function formatMoney(amount) {
            return amount.toLocaleString('vi-VN');
        }
    });
</script>

<jsp:include page="footer.jsp" />