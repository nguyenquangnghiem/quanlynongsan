<%@page import="com.mycompany.quanlynongsan.model.User"%>
<!-- /WEB-INF/views/includes/header.jsp -->
<!DOCTYPE html>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý nông sản</title>
        <link rel="stylesheet" href="/quanlynongsan/resource/reset.css"> <!-- nếu có -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <link
            href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.css"
            rel="stylesheet"
            />

        <!-- Bootstrap 5 JS Bundle with Popper (optional, nếu dùng JS) -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
        <!-- Font Awesome 6 (Free CDN) -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=chevron_right" />
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=home" />

    </head>
    <style>
        header .top {
            display: flex;
            background: var(--Gray-Scale-Gray-800, #333);
            box-shadow: 0px 1px 0px 0px #E5E5E5;
            padding: 12px 300px;
            justify-content: space-between;
            align-items: center;
        }

        .header__address{
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .header__address--content{
            color: var(--Gray-Scale-Gray-600, #666);

            /* Body Tiny/Body Tiny, 400 */
            font-family: "Be Vietnam Pro";
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            line-height: 130%; /* 15.6px */
            width: 300px;
            margin: auto;
        }

        .header__tabs{
            display: flex;
            gap: 0 4px;
        }

        .header__tabs a {
            color: var(--Gray-Scale-Gray-600, #666);

            /* Body Tiny/Body Tiny, 400 */
            font-family: "Be Vietnam Pro";
            font-size: 12px;
            font-style: normal;
            font-weight: 400;
            text-decoration: none;
            line-height: 130%; /* 15.6px */
            margin: auto;
        }

        .header__tabs a:hover{
            text-decoration: underline;
        }

        .header__tabs span {
            color: var(--Gray-Scale-Gray-300, #B3B3B3);
        }

        header .mid {
            padding: 24px 300px;
            display: flex;
            justify-content: space-between;
            background: var(--Gray-Scale-White, #FFF);
            box-shadow: 0px -1px 0px 0px #E5E5E5 inset;
        }

        .mid__logo{
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .mid__search{
            margin-bottom: auto;
            margin-top: auto;
        }

        .mid__logo p{
            color: var(--Gren-Gray-Scale-900, #002603);
            font-family: Pacifico;
            font-size: 32px;
            font-style: normal;
            font-weight: 400;
            line-height: 38px; /* 118.75% */
            letter-spacing: -0.96px;
            margin: auto;
        }
        .tab{
            cursor: pointer;
        }
        .mid__information{
            display: flex;
            align-items: center;
            gap: 0 16px;
            justify-content: space-between;
        }

        .driver {
            position: relative;
        }

        .driver::after {
            position: absolute;
            content: "";
            height: 24px;
            width: 1px;
            background-color: #CCC;
            top: -12px;
            left: 0;
        }

        .mid__information--cart{
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 0 12px;
            position: relative;
            cursor: pointer;
        }

        .mid__information--cart div{
            display: flex;
            flex-direction: column;
            gap: 7px 0;
            align-items: stretch;
        }

        p {
            margin-top: auto;
            margin-bottom: auto;
        }

        .mid__information--cart div p:first-of-type{
            color: var(--Gray-Scale-Gray-700, #4D4D4D);
            font-family: Poppins;
            font-size: 11px;
            font-style: normal;
            font-weight: 400;
            line-height: 120%; /* 13.2px */
        }

        .mid__information--cart div p:last-of-type{
            color: var(--Gray-Scale-Gray-900, #1A1A1A);
            font-family: Poppins;
            font-size: 14px;
            font-style: normal;
            font-weight: 500;
            line-height: 100%; /* 14px */

        }

        .bottom {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0 32px;
            padding: 16px 300px;
        }

        .bottom .tab{
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0 4px;
        }

        .breadcrumb {
            background-image:
                linear-gradient(90deg, rgba(0, 0, 0, 0.70) 0.03%, rgba(0, 0, 0, 0.00) 91.31%),
                url("/quanlynongsan/resource/Breadcrumbs.png");

            background-color: lightgray;


            background-size: cover;

            background-repeat: no-repeat;
            height: 120px;
            width: 100%;

        }

        .breadcrumb div {
            color: var(--Gray-Scale-Gray-400, #999);
            /* Body Medium/Body Medium, 400 */
            font-family: "Be Vietnam Pro";
            font-size: 16px;
            font-style: normal;
            font-weight: 400;
            line-height: 150%; /* 24px */
            margin-top: auto;
            margin-bottom: auto;
            margin-left: 300px;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .cart-count {
            position: absolute;
            top: -5px;
            right: 60%;
            background-color: red;
            color: #fff;
            border-radius: 50%;
            padding: 2px 6px;
            font-size: 12px;
            font-weight: bold;
        }

        .cart-modal {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0,0,0,0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }

        .cart-modal.hidden {
            display: none;
        }

        .cart-modal-content {
            background-color: white;
            padding: 20px;
            width: 400px;
            border-radius: 8px;
            max-height: 80vh;
            overflow-y: auto;
        }

        .cart-items {
            margin-bottom: 15px;
        }

        .cart-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 8px;
        }

        .cart-summary {
            margin-top: 10px;
            font-size: 16px;
        }

        .checkout-btn {
            background-color: green;
            color: white;
            padding: 8px 16px;
            border: none;
            margin-top: 10px;
            cursor: pointer;
            border-radius: 4px;
        }

        .close-btn {
            background-color: gray;
            color: white;
            padding: 6px 12px;
            border: none;
            margin-left: 10px;
            cursor: pointer;
            border-radius: 4px;
        }

        .dropdown {
            position: relative;
            display: inline-block;
        }

        .dropdown-toggle {
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 5px;
            padding: 8px 12px;
            color: inherit;
            text-decoration: none;
        }

        .dropdown-menu {
            display: none;
            position: absolute;
            top: 100%;
            left: 0;
            min-width: 180px;
            background-color: #fff;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
            border-radius: 6px;
            padding: 6px 0;
            z-index: 1000;
        }

        .dropdown-menu a {
            display: block;
            padding: 8px 16px;
            color: #333;
            text-decoration: none;
            transition: background-color 0.2s;
        }

        .dropdown-menu a:hover {
            background-color: #f0f0f0;
        }

        .dropdown.open .dropdown-menu {
            display: block;
        }

    </style>
    <% String error = (String) request.getAttribute("error"); %>
<% String success = (String) request.getAttribute("success"); %>
<%
    if(success == null && error == null){
    success = request.getParameter("success");
    error = request.getParameter("error");

    if (success != null) {
        success = java.net.URLDecoder.decode(success, "UTF-8");
        }
    if (error != null) {
        error = java.net.URLDecoder.decode(error, "UTF-8");
    }
    }
%>
    <body>
        <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 9999">
            <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header">
                    <strong class="me-auto" id="toast-title">Thông báo</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body" id="toast-body">
                </div>
            </div>
        </div>

        <header>
            <div class="top">
                <div class="header__address">
                    <svg xmlns="http://www.w3.org/2000/svg" width="17" height="20" viewBox="0 0 17 20" fill="none">
                    <path d="M16 8.36364C16 14.0909 8.5 19 8.5 19C8.5 19 1 14.0909 1 8.36364C1 6.41068 1.79018 4.53771 3.1967 3.15676C4.60322 1.77581 6.51088 1 8.5 1C10.4891 1 12.3968 1.77581 13.8033 3.15676C15.2098 4.53771 16 6.41068 16 8.36364Z" stroke="#B3B3B3" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M8.5 10.8183C9.88071 10.8183 11 9.71933 11 8.36373C11 7.00812 9.88071 5.90918 8.5 5.90918C7.11929 5.90918 6 7.00812 6 8.36373C6 9.71933 7.11929 10.8183 8.5 10.8183Z" stroke="#B3B3B3" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <p class="header__address--content">Địa chỉ: ABC</p>
                </div>
                <div class="header__tabs">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <span>Xin chào, ${sessionScope.user.fullName}</span>
                            <span>/</span>
                            <a href="${pageContext.request.contextPath}/logout">Đăng Xuất</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login">Đăng Nhập</a>
                            <span>/</span>
                            <a href="${pageContext.request.contextPath}/register">Đăng Ký</a>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>
            <div class="mid">
                <div class="mid__logo d-flex align-items-center gap-2">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="33" viewBox="0 0 32 33" fill="none">
                    <path d="M31.2749 4.84283C27.4022 4.84283 21.9311 5.16687 19.1352 7.96129C17.9247 9.1718 17.3202 11.1451 17.4771 13.3787C17.4975 13.6737 17.6937 13.9251 17.9756 14.0181C18.256 14.1097 18.5641 14.0239 18.7559 13.7987C20.3108 11.9692 22.2435 10.5029 24.5046 9.44066C24.6964 9.34911 24.9231 9.34182 25.1295 9.4145C25.3111 9.47989 25.4535 9.60339 25.5276 9.76183C25.6816 10.0874 25.6322 10.516 25.1207 10.7572C25.0917 10.7717 25.0655 10.7907 25.0365 10.8037C25.0263 10.8081 25.0147 10.8067 25.006 10.811C19.4346 13.4296 16.6954 17.9984 15.4514 22.6964C14.5446 16.814 12.6294 13.3511 10.8202 11.2252C9.51095 9.52791 8.22341 8.62836 7.47943 8.1198C7.34136 8.02532 6.92435 7.74053 6.7514 7.56758C6.46803 7.28421 6.46803 6.82354 6.7514 6.54017C7.03477 6.25823 7.49687 6.25823 7.81218 6.57212C7.88923 6.64044 8.01272 6.72763 8.16966 6.83226L8.29902 6.91945C9.35108 7.64019 11.31 8.97855 13.0479 11.8602C13.2034 12.1174 13.5042 12.2554 13.7963 12.2002C14.0928 12.1479 14.3252 11.9198 14.3834 11.6248C14.7627 9.68771 14.5927 6.81332 12.8649 5.08553C10.069 2.29254 4.59789 1.96851 0.726671 1.96851C0.32553 1.96844 0 2.29397 0 2.69504C0 6.56769 0.324032 12.0388 3.11846 14.8348C4.28387 16.0002 6.08147 16.5233 7.86736 16.5233C9.32486 16.5233 10.7533 16.1585 11.8476 15.5264C13.3399 18.6187 14.5316 23.2383 14.5316 30.3051C14.5316 30.7061 14.8571 31.0317 15.2582 31.0317C15.6592 31.0317 15.9848 30.7061 15.9848 30.3051C15.9848 26.3627 16.6605 21.6311 19.173 17.7367C20.18 18.7205 21.9137 19.3439 23.8347 19.402C23.9306 19.4049 24.0251 19.4064 24.1195 19.4064C26.0711 19.4064 27.796 18.7946 28.8815 17.7076C31.6773 14.9117 31.9999 9.44059 31.9999 5.56787C32.0015 5.16687 31.6774 4.84283 31.2749 4.84283Z" fill="#00B307"/>
                    </svg>
                    <p>Nông Sản Sạch</p>
                </div>
                <div class="mid__search">

                    <div class="input-group">

                    </div>
                </div>
                <div class="mid__information">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="25" viewBox="0 0 24 25" fill="none">
                    <path d="M18 8.5C18 6.9087 17.3679 5.38258 16.2426 4.25736C15.1174 3.13214 13.5913 2.5 12 2.5C10.4087 2.5 8.88258 3.13214 7.75736 4.25736C6.63214 5.38258 6 6.9087 6 8.5C6 15.5 3 17.5 3 17.5H21C21 17.5 18 15.5 18 8.5Z" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M13.7295 21.5C13.5537 21.8031 13.3014 22.0547 12.9978 22.2295C12.6941 22.4044 12.3499 22.4965 11.9995 22.4965C11.6492 22.4965 11.3049 22.4044 11.0013 22.2295C10.6977 22.0547 10.4453 21.8031 10.2695 21.5" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <div class="driver mid__information--cart-driver"></div>
                    <div class="mid__information--cart">
                        <svg style="cursor: pointer;" xmlns="http://www.w3.org/2000/svg" width="34" height="35" viewBox="0 0 34 35" fill="none">
                        <path d="M11.3333 14.6667H7.08333L4.25 30.25H29.75L26.9167 14.6667H22.6667M11.3333 14.6667V10.4167C11.3333 7.28705 13.8704 4.75 17 4.75V4.75C20.1296 4.75 22.6667 7.28705 22.6667 10.4167V14.6667M11.3333 14.6667H22.6667M11.3333 14.6667V18.9167M22.6667 14.6667V18.9167" stroke="#1A1A1A" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                        <div>
                            <p>Giỏ hàng</p>
                            <p id="cart-total-price">0</p> <!-- Tổng tiền -->
                        </div>
                        <span id="cart-count" class="cart-count">0</span> <!-- Số sản phẩm -->
                    </div>
                    <div class="driver"></div>
                    <svg xmlns="http://www.w3.org/2000/svg" id="user-profile" style="cursor: pointer;" width="32" height="33" viewBox="0 0 32 33" fill="none">
                    <path d="M16.0003 15.1667C18.9458 15.1667 21.3337 12.7789 21.3337 9.83333C21.3337 6.88781 18.9458 4.5 16.0003 4.5C13.0548 4.5 10.667 6.88781 10.667 9.83333C10.667 12.7789 13.0548 15.1667 16.0003 15.1667Z" stroke="#1A1A1A" stroke-width="1.5"/>
                    <path d="M20.0003 19.1665H12.0003C8.31764 19.1665 5.02031 22.5665 7.44297 25.3385C9.09097 27.2238 11.8163 28.4998 16.0003 28.4998C20.1843 28.4998 22.9083 27.2238 24.5563 25.3385C26.9803 22.5652 23.6816 19.1665 20.0003 19.1665Z" stroke="#1A1A1A" stroke-width="1.5"/>
                    </svg>
                </div>
            </div>
            <%
                User currentUser = (User) session.getAttribute("user");
                Integer roleId = null;
                if (currentUser != null) {
                    roleId = currentUser.getRoleId();
                }
            %>

            <div class="bottom">
                <!-- Trang chủ: luôn hiển thị -->
                <% if (currentUser == null || (currentUser != null && roleId != 4)) { %>
                <a href="${pageContext.request.contextPath}/user/home" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Trang chủ</p>
                    </div>
                </a>
                <% } %>

                <% if (currentUser != null && roleId != null) { %>
                <% if (roleId == 1) { %>
                <!-- Nếu roleId = 1 → hiển thị "Sản phẩm của bạn", "Sản phẩm đã bán", "Đơn hàng của bạn" -->
                <a href="${pageContext.request.contextPath}/secured/user/my-products" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Sản phẩm của bạn</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/secured/user/sold-products" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Sản phẩm đã bán</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/user/return-requests.jsp" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Yêu cầu trả hàng</p>
                    </div>
                </a>


                <div class="dropdown">
                    <div class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <p class="mb-0">Đơn hàng</p>
                    </div>
                    <div class="dropdown-menu">
                        <a href="${pageContext.request.contextPath}/secured/user/pending-orders?status=pending">Đơn hàng chưa xác nhận</a>
                        <a href="${pageContext.request.contextPath}/secured/user/pending-orders?status=confirmed">Đơn hàng đã xác nhận</a>
                        <a href="${pageContext.request.contextPath}/secured/user/pending-orders?status=successful">Đơn hàng giao thành công</a>
                        <a href="${pageContext.request.contextPath}/secured/user/pending-orders?status=canceled">Đơn hàng đã hủy/trả</a>
                    </div>
                </div>

                <a href="${pageContext.request.contextPath}/secured/user/revenue-report" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Báo cáo doanh thu</p>
                    </div>
                </a>

                <% } else if (roleId == 3) { %>
                <!-- Nếu roleId = 3 → hiển thị "Yêu thích" và "Đơn hàng của tôi" -->
                <a href="${pageContext.request.contextPath}/secured/user/favorite-products" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Yêu thích</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/secured/user/my-order" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Đơn hàng của tôi</p>
                    </div>
                </a>
                <a href="${pageContext.request.contextPath}/user/my-return-requests.jsp" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Yêu cầu trả hàng của tôi</p>
                    </div>
                </a>
                <% } else if (roleId == 2) { %>
                <!-- Nếu roleId = 2 → hiển thị "Yêu thích" và "Đơn hàng của tôi" -->
                <a href="${pageContext.request.contextPath}/secured/user/favorite-products" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Yêu thích</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/secured/user/my-order" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Đơn hàng của tôi</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/secured/user/orders-not-imported" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Đơn hàng chưa nhập kho</p>
                    </div>
                </a>
                <a href="${pageContext.request.contextPath}/user/return-requests.jsp" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Yêu cầu trả hàng</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/user/my-return-requests.jsp" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Yêu cầu trả hàng của tôi</p>
                    </div>
                </a>

                <div class="dropdown">
                    <div class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <p class="mb-0">Đơn hàng</p>
                    </div>
                    <div class="dropdown-menu">
                        <a href="${pageContext.request.contextPath}/secured/user/pending-orders?status=pending">Đơn hàng chưa xác nhận</a>
                        <a href="${pageContext.request.contextPath}/secured/user/pending-orders?status=confirmed">Đơn hàng đã xác nhận</a>
                        <a href="${pageContext.request.contextPath}/secured/user/pending-orders?status=successful">Đơn hàng giao thành công</a>
                        <a href="${pageContext.request.contextPath}/secured/user/pending-orders?status=canceled">Đơn hàng đã hủy/trả</a>
                    </div>
                </div>
                <div class="dropdown">
                    <div class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <p class="mb-0">Báo cáo</p>
                    </div>
                    <div class="dropdown-menu">
                        <a href="${pageContext.request.contextPath}/secured/user/report-stock">Báo cáo xuất/nhập</a>
                        <a href="${pageContext.request.contextPath}/secured/user/inventory-report">Báo cáo doanh thu</a>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/secured/user/my-stock" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Kho của tôi</p>
                    </div>
                </a>
                <% } else if (roleId == 4) { %>
                <a href="${pageContext.request.contextPath}/secured/admin/account" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Danh sách tài khoản</p>
                    </div>
                </a>
                </a>
                <a href="${pageContext.request.contextPath}/secured/admin/product-pending" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Danh sách sản phẩm chờ duyệt</p>
                    </div>
                </a>

                </a>
                <a href="${pageContext.request.contextPath}/secured/admin/system-revenue-report" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Báo cáo doanh thu</p>
                    </div>
                </a>
                </a>
                <a href="${pageContext.request.contextPath}/secured/admin/behavior-logs" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Giám sát hành vi</p>
                    </div>
                </a>
                <a href="${pageContext.request.contextPath}/secured/admin/solve-problem" style="text-decoration: none; color: inherit;">
                    <div class="tab">
                        <p>Vấn đề chưa giải quyết</p>
                    </div>
                </a>
                <% } %>
                <% }%>
            </div>
            <div class="breadcrumb"></div>
            <!-- Modal giỏ hàng -->
            <div id="cart-modal" class="cart-modal hidden">
                <div class="cart-modal-content">
                    <h2>Giỏ hàng của bạn</h2>
                    <div id="cart-items" class="cart-items">
                        <!-- Các sản phẩm trong giỏ hàng sẽ render ở đây -->
                    </div>
                    <div class="cart-summary">
                        <span>Tổng tiền:</span> 
                        <strong id="cart-modal-total">0đ</strong>
                    </div>
                    <button id="checkout-btn" class="checkout-btn">Chi tiết giỏ hàng</button>
                    <button id="close-cart-modal" class="close-btn">Đóng</button>
                </div>
            </div>

        </header>

        <script
            type="text/javascript"
            src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/6.4.2/mdb.min.js"
        ></script>
        <script>
                        document.addEventListener("DOMContentLoaded", function () {
                            const userProfileIcon = document.getElementById("user-profile");
                            if (userProfileIcon) {
                                userProfileIcon.addEventListener("click", function () {
                                    const contextPath = "<%= request.getContextPath()%>";
                                    window.location.href = contextPath + "/secured/user/profile";
                                });
                            }
                        });
        </script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                fetchCartData(); // Không cần truyền token
            });

            function fetchCartData() {
                const contextPath = "/" + window.location.pathname.split('/')[1];
                fetch(contextPath + '/secured/user/has-cart', {
                    credentials: 'include', // <-- BẮT BUỘC khi dùng session nếu frontend/backend khác domain
                    method: 'GET'
                })
                        .then(response => {
                            if (!response.ok) {
                                // Có thể là do chưa đăng nhập hoặc session hết hạn
                                document.querySelector('.mid__information').style.display = 'none';
                                throw new Error("Unauthorized or error");
                            }
                            return response.json();
                        })
                        .then(data => {
                            const total = data.totalPrice || 0;
                            const count = data.totalQuantity || 0;

                            document.getElementById("cart-total-price").textContent = formatMoney(total);
                            document.getElementById("cart-count").textContent = count;

                            if (count === 0) {
                                document.getElementById("cart-count").style.display = 'none';
                            }
                        })
                        .catch(err => {
                            console.error(err);
                            document.querySelector('.mid__information').style.display = 'none';
                        });
            }

            function formatMoney(amount) {
                // Kiểm tra nếu amount không tồn tại (undefined, null) hoặc không phải số
                if (amount === undefined || amount === null || isNaN(amount)) {
                    return '0 ₫'; // hoặc có thể return '' tùy nhu cầu
                }

                // Chuyển đổi sang number phòng trường hợp amount là string
                const number = Number(amount);

                // Định dạng tiền tệ Việt Nam
                return number.toLocaleString('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                });
            }
            const cartModal = document.getElementById('cart-modal');
            const cartItemsContainer = document.getElementById('cart-items');
            const cartModalTotal = document.getElementById('cart-modal-total');

            document.querySelector('.mid__information--cart').addEventListener('click', showCartModal);
            document.getElementById('close-cart-modal').addEventListener('click', () => cartModal.classList.add('hidden'));

            function showCartModal() {
                const contextPath = "/" + window.location.pathname.split('/')[1];
                fetch(contextPath + '/secured/user/has-cart', {
                    credentials: 'include',
                    method: 'GET'
                })
                        .then(response => {
                            if (!response.ok)
                                throw new Error("Unauthorized or error");
                            return response.json();
                        })
                        .then(data => {
                            cartItemsContainer.innerHTML = ''; // Xóa cũ
                            let total = 0;
                            if (data.itemCartResponses && data.itemCartResponses.length > 0) {
                                data.itemCartResponses.forEach(item => {
                                    const div = document.createElement('div');
                                    const ttpr = formatMoney(item.price);
                                    console.log(ttpr);
                                    div.className = 'cart-item';
                                    div.innerHTML = `
                                <span>` + item.productName + ` x ` + item.quantity + `</span>
                                <strong>` + ttpr + `</strong>
                            `;
                                    cartItemsContainer.appendChild(div);
                                });
                                total = data.totalPrice;
                            } else {
                                cartItemsContainer.innerHTML = '<p>Giỏ hàng trống.</p>';
                            }

                            cartModalTotal.textContent = formatMoney(total);
                            cartModal.classList.remove('hidden');
                        })
                        .catch(err => {
                            console.error(err);
                            alert("Không thể lấy thông tin giỏ hàng");
                        });
            }

            document.getElementById('checkout-btn').addEventListener('click', () => {
                const contextPath = "/" + window.location.pathname.split('/')[1];
                window.location.href = contextPath + "/secured/user/shopping-cart";
            });

            document.addEventListener('DOMContentLoaded', function () {
                const searchInput = document.getElementById('form1');
                const hiddenInput = document.getElementById('name-search');

                // Cập nhật ngay khi người dùng gõ (input event)
                searchInput.addEventListener('input', function () {
                    hiddenInput.value = this.value;
                    console.log('Input hidden đã cập nhật:', hiddenInput.value); // (Tùy chọn) Debug
                });

                // Hoặc nếu muốn cập nhật khi ô search mất focus (change event)
                searchInput.addEventListener('change', function () {
                    hiddenInput.value = this.value;
                    console.log('Input hidden đã cập nhật (onchange):', hiddenInput.value);
                });
            });
        </script>

        <script>
            function toggleDropdown(element) {
                const dropdown = element.parentElement;
                dropdown.classList.toggle('open');
            }

            // Đóng dropdown khi click ra ngoài
            window.addEventListener('click', function (e) {
                document.querySelectorAll('.dropdown').forEach(dropdown => {
                    if (!dropdown.contains(e.target)) {
                        dropdown.classList.remove('open');
                    }
                });
            });
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        
        <script>
<% if (success != null) { %>
    window.addEventListener('load', function() {
        document.getElementById("toast-title").innerText = "Thành công";
        document.getElementById("toast-body").innerText = "<%= success %>";
        var toast = new bootstrap.Toast(document.getElementById('liveToast'));
        toast.show();
    });
<% } %>

<% if (error != null) { %>
    window.addEventListener('load', function() {
        document.getElementById("toast-title").innerText = "Lỗi";
        document.getElementById("toast-body").innerText = "<%= error %>";
        var toast = new bootstrap.Toast(document.getElementById('liveToast'));
        toast.show();
    });
<% } %>
</script>
