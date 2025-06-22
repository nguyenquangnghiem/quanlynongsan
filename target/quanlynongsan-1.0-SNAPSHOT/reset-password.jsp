<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<style>
    .reset-container {
        border-radius: 8px;
        border: 1px solid #F2F2F2;
        background: #FFF;
        box-shadow: 0 0 56px rgba(0, 38, 3, 0.08);
        padding: 24px;
        width: 520px;
        margin: 40px auto;
        display: flex;
        flex-direction: column;
        gap: 20px;
        align-items: center;
    }

    .reset-container h1 {
        color: #1A1A1A;
        font-family: "Be Vietnam Pro", sans-serif;
        font-size: 32px;
        font-weight: 600;
        line-height: 120%;
        margin-bottom: 8px;
    }

    .inp {
        width: 100%;
        border-radius: 6px;
        border: 1px solid #E6E6E6;
        background: #FFF;
        padding: 14px 16px;
        margin-bottom: 16px;
    }

    .form-control {
        border: none;
        outline: none;
        width: 100%;
        background: transparent;
        font-size: 16px;
        font-family: Poppins, sans-serif;
        color: #333;
    }

    .form-control::placeholder {
        color: #999;
    }

    .btn-success {
        padding: 12px;
        font-size: 16px;
        font-weight: 500;
        font-family: "Be Vietnam Pro", sans-serif;
    }

    .back-to-login {
        margin-top: 8px;
        font-size: 14px;
        color: #666;
        text-decoration: none;
        font-family: "Be Vietnam Pro", sans-serif;
        transition: 0.2s;
    }

    .back-to-login:hover {
        color: #000;
        text-decoration: underline;
    }
</style>

<div class="reset-container">
    <h1>Đặt lại mật khẩu</h1>
    <form action="reset-password" method="POST">
        <input type="hidden" name="email" value="<%=request.getParameter("email")%>">

        <div class="inp">
            <input type="password" name="newPassword" class="form-control" required placeholder="Mật khẩu mới">
        </div>

        <button type="submit" class="btn btn-success w-100">Đặt lại</button>

        <a href="login" class="back-to-login">← Quay về đăng nhập</a>
    </form>
</div>

<jsp:include page="/footer.jsp" />

<script>
    document.addEventListener("DOMContentLoaded", function () {
        var breadcrumb = document.querySelector(".breadcrumb");
        if (breadcrumb) {
            breadcrumb.innerHTML = '<div><span class="material-symbols-outlined">home</span> &bull; Đặt lại mật khẩu</div>';
        }

        var params = new URLSearchParams(window.location.search);
        var success = params.get("success");
        var error = params.get("error");

        if (success) alert(success);
        if (error) alert(error);
    });
</script>
