package com.mycompany.quanlynongsan.controller;

import com.mycompany.quanlynongsan.model.User;
import com.mycompany.quanlynongsan.service.GeminiService;
import com.mycompany.quanlynongsan.service.TrainingDataService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "GeminiChatServlet", urlPatterns = {"/user/gemini-chat"})
public class GeminiChatServlet extends HttpServlet {

    private final GeminiService geminiService = new GeminiService();
    private final TrainingDataService trainingDataService = new TrainingDataService();

    private static final Pattern DETAIL_REQUEST_PATTERN = Pattern.compile(
            "(thêm|nói|mô tả|chi tiết|giải thích|nói rõ|thêm thông tin|cụ thể hơn|giới thiệu thêm|nói thêm|giải thích thêm|kể rõ|hỏi tiếp|nói tiếp|hãy nói đi|tiếp đi|tiếp tục)",
            Pattern.CASE_INSENSITIVE
    );

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String userMessage = request.getParameter("message");
    HttpSession session = request.getSession();
    StringBuilder conversationHistory = (StringBuilder) session.getAttribute("conversationHistory");
    if (conversationHistory == null) {
        conversationHistory = new StringBuilder();
    }

    boolean isDetailRequest = DETAIL_REQUEST_PATTERN.matcher(userMessage).find();

    String dataTraining = trainingDataService.getTrainingData();
    User user = (User) session.getAttribute("user");
    Integer roleId = (user != null) ? user.getRoleId() : 3;

    String contextPrompt = String.format(""
            + "Bạn là một trợ lý hỗ trợ khách hàng trong hệ thống phân phối nông sản.\n" +
"            Hãy tư vấn cách sử dụng nền tảng, giải thích quy trình đặt hàng, các trạng thái đơn hàng.\n" +
"            Nếu khách hàng nhắn 'nói thêm', 'giải thích thêm'... thì hãy hiểu khách muốn nghe tiếp phần trước, không được hỏi lại.\n" +
"            Tránh dùng thuật ngữ kỹ thuật, giải thích dễ hiểu. Đây là role id của khách: %d\n" +
"            Dữ liệu đơn hàng mẫu: %s", roleId, dataTraining);

    if (!isDetailRequest) {
        conversationHistory.setLength(0);
    }
    conversationHistory.append("Khách hàng: ").append(userMessage).append("\n");

    StringBuilder prompt = new StringBuilder(contextPrompt);
    prompt.append("\n").append(isDetailRequest ? "Khách hàng đang muốn tiếp tục phần trước:\n" : "Đoạn hội thoại trước đây:\n");
    prompt.append(conversationHistory);

    String reply = geminiService.chatWithGemini(prompt.toString());

    response.setContentType("application/json;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
        JSONObject jsonResponse = new JSONObject(reply);

        // ✅ Kiểm tra nếu có lỗi từ API Gemini
        if (jsonResponse.has("error")) {
            JSONObject errorObj = jsonResponse.getJSONObject("error");
            String errorMessage = errorObj.optString("message", "Đã xảy ra lỗi không xác định từ API Gemini.");
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", errorMessage);
            out.print(errorResponse.toString());
            return;
        }

        JSONArray candidates = jsonResponse.getJSONArray("candidates");
        String assistantReply = candidates
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        conversationHistory.append("Trợ lý: ").append(assistantReply).append("\n");
        session.setAttribute("conversationHistory", conversationHistory);

        out.print(jsonResponse.toString());
    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", "Đã xảy ra lỗi trong hệ thống: " + e.getMessage());
        response.getWriter().print(errorResponse.toString());
    }
}

}
