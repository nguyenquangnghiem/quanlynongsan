<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<%
    String productId = request.getParameter("productId");
%>

<div class="container my-5">
    <h4 class="fw-bold mb-4">Báo cáo sản phẩm</h4>

    <form action="<%= request.getContextPath() %>/secured/user/report-product" method="post">
        <input type="hidden" name="productId" value="<%= productId %>">

        <div class="mb-3">
            <label for="name" class="form-label">Tiêu đề vấn đề:</label>
            <input type="text" class="form-control" id="name" name="name" required>
        </div>

        <div class="mb-3">
            <label for="reason" class="form-label">Lý do chi tiết:</label>
            <textarea class="form-control" id="reason" name="reason" rows="4" required></textarea>
        </div>

        <button type="submit" class="btn btn-danger">Gửi báo cáo</button>
        <a href="<%= request.getContextPath() %>/" class="btn btn-secondary">Hủy</a>
    </form>
</div>

<jsp:include page="/footer.jsp" />
