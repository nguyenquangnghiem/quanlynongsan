<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.repository.ReturnRequestRepository"%>
<%@page import="com.mycompany.quanlynongsan.model.ReturnRequest"%>
<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<%
    User currentUser = (User) session.getAttribute("user");
    ReturnRequestRepository repository = new ReturnRequestRepository();
    List<ReturnRequest> requests = repository.getByUserId(currentUser.getUserId());
%>

<div class="container my-5">
    <h4 class="fw-bold mb-4">Danh sách yêu cầu trả hàng</h4>

    <% if (requests.isEmpty()) { %>
        <p>Chưa có yêu cầu nào.</p>
    <% } else { %>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Đơn hàng</th>
                    <th>Lý do</th>
                    <th>Trạng thái</th>
                    <th>Ngày yêu cầu</th>
                </tr>
            </thead>
            <tbody>
                <% int i = 1; for (ReturnRequest req : requests) { %>
                <tr>
                    <td><%= i++ %></td>
                    <td>#<%= req.getOrderId() %></td>
                    <td><%= req.getReason() %></td>
                    <td>
                        <% if ("PENDING".equals(req.getStatus())) { %>
                    <span class="badge bg-warning">Chờ duyệt</span>
                    <% } else if ("ACCEPTED".equals(req.getStatus())) { %>
                    <span class="badge bg-success">Đã chấp nhận</span>
                    <% } else if ("REJECTED".equals(req.getStatus())) { %>
                    <span class="badge bg-danger">Từ chối</span>
                    <% } else if ("RETURNED".equals(req.getStatus())) { %>
                    <span class="badge bg-primary">Đã trả hàng</span>
                    <% } else {%>
                    <span class="badge bg-secondary"><%= req.getStatus()%></span>
                    <% }%>
                    </td>
                    <td><%= req.getCreatedDate() %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</div>

<jsp:include page="/footer.jsp" />
