<%@page import="com.mycompany.quanlynongsan.response.UserWithRole"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="/header.jsp" />

<%
    List<UserWithRole> users = (List<UserWithRole>) request.getAttribute("users");
    Integer selectedRoleId = (Integer) request.getAttribute("selectedRoleId"); // dùng cho form
%>

<div class="container my-5">
    <h2 class="mb-4 fw-bold">Danh sách tài khoản</h2>

    <!-- 🔔 Form lọc theo Vai trò -->
    <form method="get" class="mb-4 d-flex align-items-center gap-2">
        <label for="roleId" class="form-label mb-0 fw-semibold">Lọc theo vai trò:</label>
        <select name="roleId" id="roleId" class="form-select" style="width: 200px;" onchange="this.form.submit()">
            <option value="">-- Tất cả --</option>
            <option value="1" <%= (selectedRoleId != null && selectedRoleId == 1) ? "selected" : "" %>>Nhập kho</option>
            <option value="2" <%= (selectedRoleId != null && selectedRoleId == 2) ? "selected" : "" %>>Người bán</option>
            <option value="3" <%= (selectedRoleId != null && selectedRoleId == 3) ? "selected" : "" %>>Khách hàng</option>
        </select>
    </form>

    <!-- 🔔 Bảng hiển thị -->
    <table class="table table-striped table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Họ và tên</th>
                <th>Email</th>
                <th>Điện thoại</th>
                <th>Địa chỉ</th>
                <th>Vai trò</th>
                <th>Ngày tạo</th>
                <th>Trạng thái</th>
                <th>Hành động</th> <!-- ✅ Thêm cột hành động -->
            </tr>
        </thead>
        <tbody>
            <%
                if (users != null) {
                    for (UserWithRole u : users) {
            %>
            <tr>
                <td><%= u.getUserId() %></td>
                <td><%= u.getFullName() != null ? u.getFullName() : "-" %></td>
                <td><%= u.getEmail() %></td>
                <td><%= u.getPhoneNumber() != null ? u.getPhoneNumber() : "-" %></td>
                <td><%= u.getAddress() != null ? u.getAddress() : "-" %></td>
                <td><%= u.getRoleName() != null ? u.getRoleName() : "-" %></td>
                <td><%= u.getCreatedDate() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(u.getCreatedDate()) : "-" %></td>
                <td>
                    <% if (u.getIsActive() != null && u.getIsActive()) { %>
                        <span class="badge bg-success">Hoạt động</span>
                    <% } else { %>
                        <span class="badge bg-secondary">Đã khóa</span>
                    <% } %>
                </td>
                <td>
                    <!-- ✅ Nút Sửa -->
                    <a href="<%= request.getContextPath() %>/secured/admin/account/edit-role?userId=<%= u.getUserId() %>" class="btn btn-sm btn-warning ms-1">Chỉnh vai trò</a>

                    <!-- ✅ Nút Khóa/Kích hoạt -->
                    <form action="<%= request.getContextPath() %>/secured/admin/account/status" method="post" class="d-inline">
                        <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                        <button type="submit" class="btn btn-sm <%= u.getIsActive() ? "btn-warning" : "btn-success" %>">
                            <%= u.getIsActive() ? "Khóa" : "Kích hoạt" %>
                        </button>
                    </form>
                </td>
            </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
</div>

<jsp:include page="/footer.jsp" />
