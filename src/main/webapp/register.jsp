<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<style>
    .login {
        border-radius: 8px;
        border: 1px solid #F2F2F2;
        background: #FFF;
        box-shadow: 0 0 56px rgba(0, 38, 3, 0.08);
        display: flex;
        padding: 24px;
        flex-direction: column;
        align-items: center;
        gap: 20px;
        width: 520px;
        margin: 40px auto;
    }

    h1 {
        color: #1A1A1A;
        font-family: "Be Vietnam Pro", sans-serif;
        font-size: 32px;
        font-weight: 600;
        line-height: 120%;
        margin-bottom: 8px;
    }

    .form-outline input.form-control {
        width: 100%;
        color: #999;
        font-family: Poppins, sans-serif;
        font-size: 16px;
        font-weight: 400;
        line-height: 130%;
        background: transparent;
        border: none;
        outline: none;
    }

    .inp {
        border-radius: 6px;
        border: 1px solid #E6E6E6;
        background: #FFF;
        padding: 14px 16px;
        margin-bottom: 16px;
    }

    .form-label {
        font-size: 14px;
        color: #666;
        margin-bottom: 4px;
        display: block;
        font-family: "Be Vietnam Pro", sans-serif;
    }

    .form-check-label {
        font-family: "Be Vietnam Pro", sans-serif;
        color: #1A1A1A;
        margin-right: 8px;
        font-size: 14px;
    }

    .register {
        display: flex;
        justify-content: center;
        margin-top: 24px;
        color: #666;
        font-family: "Be Vietnam Pro", sans-serif;
        font-size: 14px;
    }

    .register a {
        color: #1A1A1A;
        font-weight: 500;
        margin-left: 5px;
        text-decoration: none;
        transition: 0.2s;
    }

    .register a:hover {
        text-decoration: underline;
        color: #000;
    }
</style>

<div class="login">
    <h1>Tạo tài khoản mới</h1>
    <form action="register" method="POST">

        <div class="form-outline email inp">
            <label class="form-label" for="emailInput">Email</label>
            <input type="text" name="email" id="emailInput" class="form-control" required />
        </div>

        <div class="form-outline inp">
            <label class="form-label" for="passwordInput">Mật khẩu</label>
            <input type="password" name="password" id="passwordInput" class="form-control" required />
        </div>

        <div class="form-outline inp">
            <label class="form-label" for="confirmPasswordInput">Xác nhận mật khẩu</label>
            <input type="password" name="confirm-password" id="confirmPasswordInput" class="form-control" required />
        </div>

        <div class="form-outline inp">
            <label class="form-label">Loại tài khoản</label>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="role" id="farmer" value="FARMER" required />
                <label class="form-check-label" for="farmer">Nông dân</label>
            </div>
            <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="role" id="customer" value="CUSTOMER" required />
                <label class="form-check-label" for="customer">Khách hàng</label>
            </div>
        </div>

        <button type="submit" class="btn btn-success w-100" data-mdb-ripple-init>Đăng ký</button>

        <p class="register">Đã có tài khoản? <a href="login">Đăng nhập</a></p>

    </form>
</div>

<jsp:include page="/footer.jsp" />

<script>
    document.addEventListener("DOMContentLoaded", function () {
        var breadcrumb = document.querySelector(".breadcrumb");
        if (breadcrumb) {
            breadcrumb.innerHTML = '<div><span class="material-symbols-outlined">home</span> &bull; Đăng ký</div>';
        }

        var params = new URLSearchParams(window.location.search);
        var error = params.get("error");

        if (error === "500") {
            alert("Tên tài khoản đã tồn tại hoặc đã bị vô hiệu hóa!");
        }
    });
</script>
