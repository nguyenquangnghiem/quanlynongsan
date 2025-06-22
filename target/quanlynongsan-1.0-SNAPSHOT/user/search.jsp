
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/header.jsp" />

<style>
    .card-header {
        background-color: #28a745 !important; /* Màu xanh lục */
        color: #fff;
    }

    .btn-primary {
        background-color: #28a745 !important;
        border-color: #28a745 !important;
    }

    .btn-primary:hover {
        background-color: #218838 !important;
        border-color: #1e7e34 !important;
    }

    .btn-outline-primary {
        color: #28a745;
        border-color: #28a745;
    }

    .btn-outline-primary:hover {
        background-color: #28a745;
        color: #fff;
    }

    .form-check-input:checked {
        background-color: #28a745;
        border-color: #28a745;
    }

    .rating {
        color: #f5b301;
    }

    .product-card {
        transition: transform 0.2s ease, box-shadow 0.2s ease;
    }

    .product-card:hover {
        transform: translateY(-4px);
        box-shadow: 0 4px 20px rgba(0,0,0,0.1);
    }
</style>

<div class="container mt-4">
    <div class="row">
        <div class="col-md-3">
            <form id="searchForm" action="<%=request.getContextPath()%>/user/search" method="POST">
                <div class="card mb-4">
                    <div class="card-header">
                        <h5>Tên sản phẩm</h5>
                    </div>
                    <div class="card-body">
                        <input type="text" class="form-control" name="name" placeholder="Nhập tên sản phẩm...">
                    </div>
                </div>

                <div class="card mb-4">
                    <div class="card-header">
                        <h5>Danh mục</h5>
                    </div>
                    <div class="card-body">
                        <c:forEach var="tab" items="${requestScope.categories}">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="category" value="${tab.categoryId}" id="cat${tab.categoryId}" ${tab.categoryId == 0 ? 'checked' : ''}>
                                <label class="form-check-label" for="cat${tab.categoryId}">
                                    ${tab.name}
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div class="card mb-4">
                    <div class="card-header">
                        <h5>Mức giá</h5>
                    </div>
                    <div class="card-body">
                        <p>Giá: <span id="minPriceDisplay">5000</span> – <span id="maxPriceDisplay">1500000</span></p>

                        <input type="hidden" name="minPrice" id="minPriceInput">
                        <input type="hidden" name="maxPrice" id="maxPriceInput">

                        <div class="d-flex flex-column align-items-center">
                            <input type="range" id="minPriceRange" min="5000" max="1500000" step="50" value="5000" class="form-range mb-2">
                            <input type="range" id="maxPriceRange" min="5000" max="1500000" step="50" value="1500000" class="form-range">
                        </div>
                    </div>
                </div>

                <div class="card mb-4">
                    <div class="card-header">
                        <h5>Nơi sản xuất</h5>
                    </div>
                    <div class="card-body">
                        <div class="form-check"><input class="form-check-input" type="checkbox" name="origin" value="Việt Nam" id="originVN"><label class="form-check-label" for="originVN">Việt Nam</label></div>
                        <div class="form-check"><input class="form-check-input" type="checkbox" name="origin" value="Thái Lan" id="originTH"><label class="form-check-label" for="originTH">Thái Lan</label></div>
                        <div class="form-check"><input class="form-check-input" type="checkbox" name="origin" value="Trung Quốc" id="originCN"><label class="form-check-label" for="originCN">Trung Quốc</label></div>
                        <div class="form-check"><input class="form-check-input" type="checkbox" name="origin" value="Mỹ" id="originUS"><label class="form-check-label" for="originUS">Mỹ</label></div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100">Lọc</button>
            </form>
        </div>

        <div class="col-md-9">
            <div class="row" id="product-list">
                <c:set var="products" value="${requestScope.products}" />
            </div>
        </div>
    </div>
</div>

    <!-- Modal hiển thị chi tiết sản phẩm -->
    <div class="modal fade" id="productDetailModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="productName"></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body row">
                    <div class="col-md-6 text-center">
                        <img id="productImageMain" src="" alt="" class="img-fluid mb-2" style="max-height: 300px;" />
                        <div class="thumbnail-container" id="thumbnailContainer"></div>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Giá:</strong> <span id="productPrice"></span></p>
                        <p><strong>Mô tả:</strong> <span id="productDescription"></span></p>
                        <p><strong>Đánh giá:</strong> <span id="productRating"></span> ⭐ (<span id="productReviewCount"></span> đánh giá)</p>
                        <p><strong>Danh mục:</strong> <span id="productCategories"></span></p>

                        <!-- Phần tăng giảm số lượng -->
                        <div class="quantity-selector mb-3">
                            <label for="quantity" class="form-label"><strong>Số lượng:</strong></label>
                            <div class="input-group" style="width: 150px;">
                                <button class="btn btn-outline-secondary quantity-minus" type="button">-</button>
                                <input type="number" class="form-control-lg full- text-center quantity-input" value="1" min="1" max="99">
                                <button class="btn btn-outline-secondary quantity-plus" type="button">+</button>
                            </div>
                        </div>                      

                        <div class="d-flex gap-2 mt-3">
                            <!-- Nút thêm vào giỏ hàng -->
                            <button class="btn btn-success flex-grow-1 btn-add-to-cart">Thêm vào giỏ hàng</button>

                            <!-- Nút thêm vào yêu thích -->
                            <button class="btn btn-outline-danger btn-favorite" title="Thêm vào yêu thích">
                                <i class="far fa-heart"></i>
                            </button>

                            <!-- ✅ Nút Xem chi tiết -->
                            <a id="productDetailPageLink" class="btn btn-primary" href="${pageContext.request.contextPath}/product-detail">Xem chi tiết</a>
                            
                            <!-- ✅ Nút Báo cáo sản phẩm -->
                            <a id="reportProductLink" class="btn btn-warning" href="${pageContext.request.contextPath}/user/report-product-page.jsp?productId=${productId}">Báo cáo</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../footer.jsp" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
  const minSlider = document.getElementById("minPriceRange");
  const maxSlider = document.getElementById("maxPriceRange");
  const minDisplay = document.getElementById("minPriceDisplay");
  const maxDisplay = document.getElementById("maxPriceDisplay");
  const minHidden = document.getElementById("minPriceInput");
  const maxHidden = document.getElementById("maxPriceInput");

  function updatePriceDisplay() {
    let minVal = parseInt(minSlider.value);
    let maxVal = parseInt(maxSlider.value);

    if (minVal > maxVal) {
      [minVal, maxVal] = [maxVal, minVal]; // Đổi chỗ nếu min > max
    }

    minDisplay.textContent = minVal.toLocaleString();
    maxDisplay.textContent = maxVal.toLocaleString();
    minHidden.value = minVal;
    maxHidden.value = maxVal;
  }

  minSlider.addEventListener("input", updatePriceDisplay);
  maxSlider.addEventListener("input", updatePriceDisplay);

  // Gọi ban đầu để hiển thị đúng giá trị
  updatePriceDisplay();
</script>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const breadcrumb = document.querySelector(".breadcrumb").innerHTML = `<div><span class="material-symbols-outlined">home</span> &bull; Trang chủ &bull; Tìm kiếm</div>`;
    });
</script>
   <script>
    document.addEventListener("DOMContentLoaded", function () {
        const products = [
            <c:forEach var="product" items="${products}" varStatus="loop">
                {
                    productId: ${product.productId},
                    name: "${product.name}",
                    price: ${product.price},
                    imageUrls: ["${product.imageUrls[0]}"],
                    rates: ${product.rates}
                }<c:if test="${!loop.last}">,</c:if>
            </c:forEach>
        ];

        renderProducts(products);

        function renderProducts(products) {
            let listProduct = "";
            products.forEach(product => {
                const rating = Number(product.rates).toFixed(1); // rating phải lấy từ API trả về JSON
                        const fullStars = Math.floor(rating);
                        const hasHalfStar = rating % 1 >= 0.25 && rating % 1 <= 0.75;
                        const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

                        let starsHtml = "";
                        for (let i = 0; i < fullStars; i++) starsHtml += "&#9733;";
                        if (hasHalfStar) starsHtml += "&#189;";
                        for (let i = 0; i < emptyStars; i++) starsHtml += "&#9734;";

                        listProduct += `
                            <div class="col-12 col-sm-6 col-md-4 mb-4">
                                <div class="card h-100 product-card shadow-sm" data-id="` + product.productId + `" style="cursor: pointer;">
                                    <img src="` + product.imageUrls[0] + `" class="card-img-top product-image" alt="` + product.name + `" />
                                    <div class="card-body text-center">
                                        <h6 class="card-title mb-2">` + product.name + `</h6>
                                        <p class="price mb-1 text-dark fw-bold">` + product.price + `đ</p>
                                        <div class="rating text-warning mb-2">
                                            ` + starsHtml + ` <span style="font-size:0.9em; color:#666;">(`+rating + `)</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        `;
            });
            const contextPath = "/" + window.location.pathname.split('/')[1];
            document.getElementById("product-list").innerHTML = listProduct;
            $('.product-card').click(function (e) {
                            e.preventDefault();
                            const productId = $(this).data('id');

                            $.ajax({
                                url: contextPath + '/user/product-detail', // servlet
                                type: 'GET',
                                dataType: 'json',
                                data: {productId},
                                success: function (product) {
                                    showProductDetail(product);
                                },
                                error: function () {
                                    alert('Lỗi khi tải thông tin sản phẩm!');
                                }
                            });
                        });
        }
    });
</script>
    
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

    async function showProductDetail(product) {
        $('#productName').text(product.name);
        $('#productPrice').text(product.price + 'đ');
        $('#productDescription').text(product.description);
        $('#productRating').text(product.rates.toFixed(1));
        $('#productReviewCount').text(product.reviewerQuantity);
        $('#productCategories').text(product.categories.join(', '));
        $('#productImageMain').attr('src', product.imageUrls[0]);
        $('#thumbnailContainer').empty();

        product.imageUrls.forEach((url, index) => {
            const img = $('<img>')
                    .attr('src', url)
                    .addClass('thumbnail-img m-1')
                    .css({width: '60px', cursor: 'pointer', borderRadius: '5px'})
                    .toggleClass('active', index === 0)
                    .click(function () {
                        $('#productImageMain').attr('src', url);
                        $('.thumbnail-img').removeClass('active');
                        $(this).addClass('active');
                    });
            $('#thumbnailContainer').append(img);
        });

        // ⚠️ Kiểm tra API để biết sản phẩm có đang được yêu thích hay không
        const isFavorited = await isProductFavorited(product.productId);
        const $heartIcon = $('.btn-favorite i');

        if (isFavorited) {
            $heartIcon.removeClass('far').addClass('fas text-danger');
        } else {
            $heartIcon.removeClass('fas text-danger').addClass('far');
        }

        // Lưu lại productId hiện tại để dùng khi click "thêm yêu thích"
        window.currentProductId = product.productId;
        
         // ⚙️ ===> GÁN href cho nút Xem chi tiết
        const contextPath = window.location.origin + "${pageContext.request.contextPath}";
        $('#productDetailPageLink').attr('href', contextPath + '/product-detail?productId=' + product.productId);
        $('#reportProductLink').attr('href', contextPath + '/user/report-product-page.jsp?productId=' + product.productId);
        $('#productDetailModal').modal('show');
    }



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