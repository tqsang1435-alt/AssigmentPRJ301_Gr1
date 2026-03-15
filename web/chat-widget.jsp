<%@page contentType="text/html" pageEncoding="UTF-8" %>

    <div class="chat-widget-btn" onclick="toggleChatWidget()" title="Chat với chúng tôi">
        <i class="ti-comments"></i>
    </div>

    <div class="chat-window" id="chatWidgetWindow">
        <div class="chat-header">
            <span><i class="ti-headphone-alt"></i> Hỗ trợ trực tuyến</span>
            <i class="ti-close" style="cursor: pointer;" onclick="toggleChatWidget()"></i>
        </div>
        <div class="chat-body" id="chatBody">
            <div
                style="background: #e4e6eb; padding: 8px 12px; border-radius: 10px; align-self: flex-start; max-width: 80%;">
                Xin chào! Shop có thể giúp gì cho bạn?
            </div>
        </div>
        <div class="chat-footer">
            <input type="text" id="chatInput" placeholder="Nhập tin nhắn..." onkeypress="handleEnter(event)">
            <button onclick="sendMessage()"><i class="ti-arrow-circle-right"></i></button>
        </div>
    </div>

    <script>
        function toggleChatWidget() {
            var chatWindow = document.getElementById("chatWidgetWindow");
            if (chatWindow.style.display === "none" || chatWindow.style.display === "") {
                chatWindow.style.display = "flex";
            } else {
                chatWindow.style.display = "none";
            }
        }

        function handleEnter(event) {
            if (event.key === "Enter") {
                sendMessage();
            }
        }

        function sendMessage() {
            var input = document.getElementById("chatInput");
            var message = input.value.trim();
            if (message === "") return;

            // 1. Hiển thị tin nhắn của người dùng lên khung chat
            appendMessage(message, "user");
            input.value = ""; // Xóa ô nhập

            // 2. Gửi tin nhắn đến Servlet qua API fetch
            fetch("chat-bot", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
                },
                body: "message=" + encodeURIComponent(message)
            })
                .then(response => response.text())
                .then(data => {
                    // 3. Hiển thị phản hồi từ Bot
                    appendMessage(data, "bot");
                })
                .catch(error => console.error('Error:', error));
        }

        function appendMessage(text, sender) {
            var chatBody = document.getElementById("chatBody");
            var div = document.createElement("div");
            div.style.padding = "8px 12px";
            div.style.borderRadius = "10px";
            div.style.maxWidth = "80%";
            div.style.marginBottom = "5px";

            if (sender === "user") {
                div.style.background = "#007bff";
                div.style.color = "white";
                div.style.alignSelf = "flex-end";
            } else {
                div.style.background = "#e4e6eb";
                div.style.color = "black";
                div.style.alignSelf = "flex-start";
            }

            div.innerText = text;
            chatBody.appendChild(div);

            // Tự động cuộn xuống dưới cùng
            chatBody.scrollTop = chatBody.scrollHeight;
        }
    </script>