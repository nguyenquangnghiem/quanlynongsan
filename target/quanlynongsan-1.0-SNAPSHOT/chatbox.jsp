<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- Chat button -->
<button id="chatToggleBtn" class="btn btn-gradient rounded-circle shadow-lg"
        style="position: fixed; bottom: 20px; right: 20px; z-index: 1000; width: 60px; height: 60px;">
    💬
</button>

<!-- Chat window -->
<div id="chatBox" class="card shadow-lg" style="position: fixed; bottom: 90px; right: 20px; width: 350px; height: 600px; display: none; z-index: 1000; border-radius: 15px; overflow: hidden;">
    <div class="card-header bg-gradient text-white py-2 px-3 d-flex justify-content-between align-items-center" style="background: linear-gradient(45deg, #007bff, #00c6ff);">
        <span style="font-weight: 500; color: blue;">Trợ lý Nông Sản</span>
        <button id="chatCloseBtn" class="btn-close btn-close-blue btn-sm"></button>
    </div>
    <div class="card-body p-3" id="chatMessages" style="height: 480px; overflow-y: auto; background-color: #f8f9fa;"></div>
    <div class="card-footer p-2 bg-white border-top">
        <form id="chatForm" class="d-flex gap-2">
            <input type="text" name="message" id="userMessage" class="form-control form-control-sm" placeholder="Nhập câu hỏi..." required>
            <button type="submit" class="btn btn-primary btn-sm px-3">Gửi</button>
        </form>
    </div>
</div>

<style>
    .btn-gradient {
        background: linear-gradient(45deg, #007bff, #00c6ff);
        color: white;
        border: none;
        transition: all 0.3s;
    }
    .btn-gradient:hover {
        opacity: 0.9;
        transform: scale(1.05);
    }
    #chatMessages::-webkit-scrollbar {
        width: 6px;
    }
    #chatMessages::-webkit-scrollbar-thumb {
        background-color: rgba(0, 0, 0, 0.2);
        border-radius: 3px;
    }
    .chat-bubble {
        padding: 8px 12px;
        border-radius: 15px;
        max-width: 80%;
        display: inline-block;
        word-wrap: break-word;
        white-space: pre-wrap;
        font-size: 14px;
    }
    .chat-bubble.user {
        background-color: #007bff;
        color: white;
        border-bottom-right-radius: 2px;
    }
    .chat-bubble.bot {
        background-color: #e9ecef;
        color: #333;
        border-bottom-left-radius: 2px;
    }
</style>

<script>
document.getElementById("chatToggleBtn").addEventListener("click", function () {
    document.getElementById("chatBox").style.display = "block";
});
document.getElementById("chatCloseBtn").addEventListener("click", function () {
    document.getElementById("chatBox").style.display = "none";
});
document.getElementById("chatForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const input = document.getElementById("userMessage");
    const message = input.value.trim();
    if (message === "") return;

    const chatMessages = document.getElementById("chatMessages");

    chatMessages.innerHTML += `
        <div class="text-end mb-2">
            <span class="chat-bubble user">` + message + `</span>
        </div>
    `;
    chatMessages.scrollTop = chatMessages.scrollHeight;
    input.value = "";

    try {
        const res = await fetch("<%=request.getContextPath()%>/user/gemini-chat", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ message })
        });
        const data = await res.json();

        const reply = data.candidates?.[0]?.content?.parts?.[0]?.text || "Không có phản hồi.";
        const htmlReply = marked.parse(reply);

        chatMessages.innerHTML += `
            <div class="text-start mb-2">
                <span class="chat-bubble bot">` + htmlReply + `</span>
            </div>
        `;
        chatMessages.scrollTop = chatMessages.scrollHeight;
    } catch (err) {
        console.error(err);
        chatMessages.innerHTML += `
            <div class="text-start mb-2 text-danger">Lỗi kết nối.</div>
        `;
    }
});
</script>

<script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
