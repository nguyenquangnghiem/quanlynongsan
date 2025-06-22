<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />
<%
    User user = (User) request.getAttribute("user");
%>

<div class="container my-5">
    <h2 class="fw-bold mb-4">Chỉnh sửa vai trò</h2>

    <form action="<%= request.getContextPath() %>/secured/admin/account/edit-role" method="post" class="w-50">
        <input type="hidden" name="userId" value="<%= user.getUserId() %>">

        <div class="mb-3">
            <label class="form-label">Vai trò</label>
            <select name="roleId" class="form-select" required>
                <option value="1" <%= user.getRoleId() == 1 ? "selected" : "" %>>Nông dân</option>
                <option value="2" <%= user.getRoleId() == 2 ? "selected" : "" %>>Nhà phân phối - Vận chuyển</option>
                <option value="3" <%= user.getRoleId() == 3 ? "selected" : "" %>>Khách hàng</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
        <a href="<%= request.getContextPath() %>/secured/admin/account" class="btn btn-secondary">Hủy</a>
    </form>
</div>

<jsp:include page="/footer.jsp" />
