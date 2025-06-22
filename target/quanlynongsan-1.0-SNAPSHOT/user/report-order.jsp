<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />
<div class="container my-5">
    <h2 class="mb-4">Báo cáo đơn hàng</h2>
    <form action="<%= request.getContextPath() %>/secured/user/report-order" method="post">
        <input type="hidden" name="orderId" value="<%= request.getParameter("orderId") %>">
        
        <div class="mb-3">
            <label for="name" class="form-label">Tiêu đề báo cáo</label>
            <input type="text" class="form-control" id="name" name="name" required>
        </div>

        <div class="mb-3">
            <label for="reason" class="form-label">Lý do</label>
            <textarea class="form-control" id="reason" name="reason" rows="4" required></textarea>
        </div>

        <button type="submit" class="btn btn-danger">Gửi báo cáo</button>
        <a href="<%= request.getContextPath() %>/secured/user/my-order" class="btn btn-secondary">Hủy</a>
    </form>
</div>
<jsp:include page="/footer.jsp" />
