<%@page import="com.mycompany.quanlynongsan.dto.ProductDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="/header.jsp" />

<div class="container my-5">
    <h2 class="text-center mb-4">Giỏ hàng của tôi</h2>
    <c:set var="subtotal" value="0" />
<c:forEach var="item" items="${requestScope.summary.itemCartResponses}">
    <c:set var="subtotal" value="${subtotal + (item.price * item.quantity)}" />
</c:forEach>
    <form id="checkoutForm" action="${pageContext.request.contextPath}/secured/checkout?totalPrice=${subtotal}" method="post">
        <table class="table table-bordered text-center align-middle">
            <thead class="table-light">
                <tr>
                    <th>Sản phẩm</th>
                    <th>Giá</th>
                    <th>Số lượng</th>
                    <th>Tổng tiền</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${requestScope.summary.itemCartResponses}">
                    <tr>
                        <td class="d-flex align-items-center justify-content-center gap-3">
                            <img src="${item.image}" alt="${item.productName}" width="60" height="60">
                            ${item.productName}
                        </td>
                        <td>${item.price}đ</td>
                        <td>${item.quantity}</td>
                        <td>${item.price * item.quantity}đ</td>
                        <td>
                            <button type="button" data-product-id="${item.productId}" class="btn btn-danger btn-sm btn-delete-product">×</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="d-flex justify-content-between mb-3">
            <a href="${pageContext.request.contextPath}/user/home" class="btn btn-outline-primary">Quay lại cửa hàng</a>
        </div>

        <div class="row">
            <div class="col-md-6">
                <h5>Chọn phương thức thanh toán</h5>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="COD" checked>
                    <label class="form-check-label" for="cod">
                        Thanh toán khi nhận hàng (COD)
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="paymentMethod" id="vnpay" value="VNPAY">
                    <label class="form-check-label" for="vnpay">
                        Thanh toán qua VNPay
                    </label>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card p-3">
                    <h5>Tổng giỏ hàng</h5>
                    <ul class="list-group list-group-flush mb-3">
                        <li class="list-group-item d-flex justify-content-between">
                            <span>Tiền đơn hàng:</span>
                            <span>${subtotal}đ</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span>Tiền ship:</span>
                            <span>Miễn phí</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between fw-bold">
                            <span>Tổng:</span>
                            <span>${subtotal}đ</span>
                        </li>
                    </ul>
                    <button type="submit" class="btn btn-success w-100">Tiến hành thanh toán</button>
                </div>
            </div>
        </div>
    </form>
</div>
           

<jsp:include page="../footer.jsp" />
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const deleteButtons = document.querySelectorAll(".btn-delete-product");

        deleteButtons.forEach(function(button) {
            button.addEventListener("click", function(e) {
                e.preventDefault();
                
                const productId = this.getAttribute("data-product-id");

                if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng không?")) {

                    fetch(window.location.href + "?action=delete&productId=" + productId, {
                        method: "POST",
                    })
                    .then(response => {
                        if (response.ok) {
                            window.location.reload(); // Reload lại trang giỏ hàng
                        } else {
                            alert("Xóa sản phẩm thất bại.");
                        }
                    })
                    .catch(err => {
                        console.error(err);
                        alert("Có lỗi xảy ra.");
                    });
                }
            });
        });
        
        const form = document.getElementById("checkoutForm");
        const codRadio = document.getElementById("cod");
        const vnpayRadio = document.getElementById("vnpay");

        form.addEventListener("submit", function(event) {
            if (codRadio.checked) {
                form.action = "${pageContext.request.contextPath}/secured/cod";
            } else if (vnpayRadio.checked) {
                form.action = "${pageContext.request.contextPath}/secured/checkout?totalPrice=${subtotal}";
            }
            // Không cần preventDefault vì vẫn muốn form submit sau khi set action
        });
    });
</script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const breadcrumb = document.querySelector(".breadcrumb").innerHTML = `<div><span class="material-symbols-outlined">home</span> &bull; Chi tiết giỏ hàng</div>`;
    });
</script>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get("incompleteProfile") === "true") {
            alert("Vui lòng cập nhật đầy đủ họ tên, số điện thoại và địa chỉ trước khi thanh toán.");
        }
    });
</script>