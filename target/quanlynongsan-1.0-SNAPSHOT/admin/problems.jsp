<%@page import="com.mycompany.quanlynongsan.model.Problem"%>
<%@page import="com.mycompany.quanlynongsan.repository.ProblemRepository"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<jsp:include page="/header.jsp" />

<%
    ProblemRepository problemRepository = new ProblemRepository();

    int pageNo = 1;
    int size = 10;
    String pageParam = request.getParameter("page");
    if (pageParam != null) {
        try {
            pageNo = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            pageNo = 1;
        }
    }

    int total = problemRepository.countProblemsByIsResolved(false);
    int totalPages = (int) Math.ceil(total * 1.0 / size);

    List<Problem> problems = problemRepository.getProblemsByIsResolvedWithPaging(false, pageNo, size);
%>

<div class="container my-5">
    <h4 class="fw-bold mb-4">Danh sách vấn đề sản phẩm chưa xử lý</h4>

    <table class="table table-bordered">
        <thead class="table-light">
            <tr>
                <th>#</th>
                <th>Tiêu đề</th>
                <th>Lý do</th>
                <th>Thời gian tạo</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <%
                int index = (pageNo - 1) * size + 1;
                for (Problem problem : problems) {
            %>
            <tr>
                <td><%= index++ %></td>
                <td><%= problem.getName() %></td>
                <td><%= problem.getReason() %></td>
                <td><%= problem.getCreatedDate() %></td>
                <td>
                    <a href="<%= request.getContextPath() %>/secured/admin/problem-detail?problemId=<%= problem.getProblemId() %>" class="btn btn-info btn-sm mb-1">Xem chi tiết</a>

                    <form action="<%= request.getContextPath() %>/secured/admin/solve-problem" method="post" style="display: inline;">
                        <input type="hidden" name="problemId" value="<%= problem.getProblemId() %>">
                        <button type="submit" class="btn btn-success btn-sm">Đánh dấu đã xử lý</button>
                    </form>
                </td>
            </tr>
            <%
                }
                if (problems.isEmpty()) {
            %>
            <tr><td colspan="5" class="text-center">Không có vấn đề nào cần xử lý.</td></tr>
            <% } %>
        </tbody>
    </table>

    <!-- Phân trang -->
    <nav>
        <ul class="pagination justify-content-center">
            <% for (int i = 1; i <= totalPages; i++) { %>
            <li class="page-item <%= i == pageNo ? "active" : "" %>">
                <a class="page-link" href="?page=<%= i %>"><%= i %></a>
            </li>
            <% } %>
        </ul>
    </nav>
</div>

<jsp:include page="/footer.jsp" />
