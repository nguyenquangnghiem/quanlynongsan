<%@page import="java.util.List"%>
<%@page import="com.mycompany.quanlynongsan.repository.ReturnRequestRepository"%>
<%@page import="com.mycompany.quanlynongsan.model.ReturnRequest"%>
<%@page import="com.mycompany.quanlynongsan.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<%
    User currentUser = (User) session.getAttribute("user");
    ReturnRequestRepository repository = new ReturnRequestRepository();
    List<ReturnRequest> requests = repository.getRequestsBySellerId(currentUser.getUserId());
%>

<div class="container my-5">
    <h4 class="fw-bold mb-4">Yêu cầu trả hàng từ khách hàng</h4>

    <% if (requests.isEmpty()) { %>
    <p>Chưa có yêu cầu nào từ khách hàng liên quan đến sản phẩm của bạn.</p>
    <% } else { %>
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>#</th>
                <th>Mã đơn hàng</th>
                <th>Lý do</th>
                <th>Trạng thái</th>
                <th>Ngày yêu cầu</th>
            </tr>
        </thead>
        <tbody>
            <% int i = 1;
        for (ReturnRequest req : requests) {%>
            <tr>
                <td><%= i++%></td>
                <td>#<%= req.getOrderId()%></td>
                <td><%= req.getReason()%></td>
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
                <td><%= req.getCreatedDate()%></td>
                <td>
                    <% if ("PENDING".equals(req.getStatus())) {%>
                    <form method="post" action="<%= request.getContextPath()%>/secured/seller/handle-return-request" style="display:inline;">
                        <input type="hidden" name="returnRequestId" value="<%= req.getReturnRequestId()%>">
                        <input type="hidden" name="action" value="accept">
                        <button class="btn btn-sm btn-success" onclick="return confirm('Bạn có chắc muốn chấp nhận?')">Chấp nhận</button>
                    </form>
                    <form method="post" action="<%= request.getContextPath()%>/secured/seller/handle-return-request" style="display:inline;">
                        <input type="hidden" name="returnRequestId" value="<%= req.getReturnRequestId()%>">
                        <input type="hidden" name="action" value="reject">
                        <button class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn từ chối?')">Từ chối</button>
                    </form>
                    <% } else { %>
                    <span class="text-muted">Không có hành động</span>
                    <% } %>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <% }%>
</div>

<jsp:include page="/footer.jsp" />
