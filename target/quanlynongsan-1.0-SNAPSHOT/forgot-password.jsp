<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<style>
    .forgot-password-container {
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

    .back-to-login {
        margin-top: 16px;
        font-size: 14px;
        color: #666;
        text-decoration: none;
        font-family: "Be Vietnam Pro", sans-serif;
        display: inline-block;
        transition: 0.2s;
    }

    .back-to-login:hover {
        text-decoration: underline;
        color: #000;
    }
</style>

<div class="forgot-password-container">
    <h1>Quên mật khẩu</h1>
    <form action="forgot-password" method="POST">

        <div class="form-outline inp">
            <label class="form-label" for="emailInput">Nhập email đã đăng ký</label>
            <input type="email" name="email" id="emailInput" class="form-control" required />
        </div>

        <button type="submit" class="btn btn-success w-100">Gửi mã xác nhận</button>

        <a href="login" class="back-to-login">← Quay về đăng nhập</a>
    </form>
</div>

<jsp:include page="/footer.jsp" />

<script>
    document.addEventListener("DOMContentLoaded", function () {
        var breadcrumb = document.querySelector(".breadcrumb");
        if (breadcrumb) {
            breadcrumb.innerHTML = '<div><span class="material-symbols-outlined">home</span> &bull; Quên mật khẩu</div>';
        }

        var params = new URLSearchParams(window.location.search);
        var success = params.get("success");
        var error = params.get("error");

        if (success) {
            alert(success);
        }

        if (error) {
            alert(error);
        }
    });
</script>
