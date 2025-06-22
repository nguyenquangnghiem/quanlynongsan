<!-- /WEB-INF/views/includes/footer.jsp -->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/chatbox.jsp" />

<style>
    footer {
        background: var(--Gray-Scale-Gray-800, #333);
        color: #f2f2f2;
        padding: 24px 0;
        font-family: "Be Vietnam Pro", sans-serif;
        font-size: 14px;
        margin-top: 24px;
    }

    .footer-container {
        max-width: 1200px;
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        padding: 0 24px;
    }

    .footer-column {
        flex: 1 1 200px;
        margin-bottom: 16px;
    }

    .footer-column h4 {
        font-size: 16px;
        margin-bottom: 12px;
        font-weight: 600;
        color: #fff;
    }

    .footer-column a {
        color: #ccc;
        text-decoration: none;
        display: block;
        margin-bottom: 6px;
        transition: color 0.2s;
    }

    .footer-column a:hover {
        color: #fff;
    }

    .footer-bottom {
        text-align: center;
        margin-top: 24px;
        color: #aaa;
        font-size: 13px;
    }

    @media (max-width: 768px) {
        .footer-container {
            flex-direction: column;
            align-items: center;
        }
    }
</style>

<footer>
    <div class="footer-container">
        <div class="footer-column">
            <h4>Về chúng tôi</h4>
            <a href="#">Giới thiệu</a>
            <a href="#">Liên hệ</a>
            <a href="#">Hỏi đáp</a>
        </div>
        <div class="footer-column">
            <h4>Hỗ trợ</h4>
            <a href="#">Chính sách bảo mật</a>
            <a href="#">Điều khoản sử dụng</a>
            <a href="#">Hướng dẫn đặt hàng</a>
        </div>
        <div class="footer-column">
            <h4>Kết nối</h4>
            <a href="#">Facebook</a>
            <a href="#">Zalo</a>
            <a href="#">Email</a>
        </div>
    </div>

    <div class="footer-bottom">
        &copy; 2025 Hệ thống phân phối nông sản. All rights reserved.
    </div>
</footer>
</body>
</html>
