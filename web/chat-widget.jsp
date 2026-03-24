<%@page contentType="text/html" pageEncoding="UTF-8" %>

    <style>
        .chat-dot {
            width: 6px;
            height: 6px;
            background-color: #90949c;
            border-radius: 50%;
            animation: chat-typing 1.4s infinite both;
        }
        .chat-dot:nth-child(1) { animation-delay: -0.32s; }
        .chat-dot:nth-child(2) { animation-delay: -0.16s; }
        @keyframes chat-typing {
            0%, 80%, 100% { transform: scale(0); }
            40% { transform: scale(1); }
        }
        /* Expanded (full-screen) state */
        #chatWidgetWindow.chat-expanded {
            width: 520px !important;
            height: 620px !important;
            bottom: 20px !important;
            right: 20px !important;
        }
    </style>

    <div class="chat-widget-btn" onclick="toggleChatWidget()" title="Chat với chúng tôi">
        <i class="ti-comments"></i>
    </div>

    <div class="chat-window" id="chatWidgetWindow">
        <div class="chat-header">
            <span><i class="ti-headphone-alt"></i> Hỗ trợ trực tuyến</span>
            <div style="display:flex; gap:10px; align-items:center;">
                <i class="ti-zoom-in" id="expandIcon" title="Phóng to" style="cursor: pointer;" onclick="toggleExpandChat()"></i>
                <i class="ti-close" title="Đóng" style="cursor: pointer;" onclick="toggleChatWidget()"></i>
            </div>
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

            // Hiện hiệu ứng loading
            var chatBody = document.getElementById("chatBody");
            var loadingDiv = document.createElement("div");
            loadingDiv.id = "botLoadingIndicator";
            loadingDiv.style.padding = "10px 14px";
            loadingDiv.style.borderRadius = "10px";
            loadingDiv.style.maxWidth = "80%";
            loadingDiv.style.marginBottom = "5px";
            loadingDiv.style.background = "#e4e6eb";
            loadingDiv.style.alignSelf = "flex-start";
            loadingDiv.style.display = "flex";
            loadingDiv.style.alignItems = "center";
            loadingDiv.style.gap = "4px";
            loadingDiv.style.width = "fit-content";
            loadingDiv.innerHTML = "<span class='chat-dot'></span><span class='chat-dot'></span><span class='chat-dot'></span>";
            chatBody.appendChild(loadingDiv);
            chatBody.scrollTop = chatBody.scrollHeight;

            var mode = "${sessionScope.ACC.role == 'Admin' ? 'admin' : 'user'}";
            // 2. Gửi tin nhắn đến Servlet qua API fetch
            fetch("chat-bot", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"
                },
                body: "message=" + encodeURIComponent(message) + "&mode=" + mode
            })
                .then(response => response.text())
                .then(data => {
                    // Xóa loading
                    var loadingEl = document.getElementById("botLoadingIndicator");
                    if (loadingEl) loadingEl.remove();

                    // 3. Hiển thị phản hồi từ Bot
                    appendMessage(data, "bot");
                })
                .catch(error => {
                    var loadingEl = document.getElementById("botLoadingIndicator");
                    if (loadingEl) loadingEl.remove();
                    console.error('Error:', error);
                });
        }

        function toggleExpandChat() {
            var chatWindow = document.getElementById("chatWidgetWindow");
            var icon = document.getElementById("expandIcon");
            chatWindow.classList.toggle("chat-expanded");
            if (chatWindow.classList.contains("chat-expanded")) {
                icon.classList.replace("ti-zoom-in", "ti-zoom-out");
                icon.title = "Thu nhỏ";
            } else {
                icon.classList.replace("ti-zoom-out", "ti-zoom-in");
                icon.title = "Phóng to";
            }
        }

        function appendMessage(text, sender) {
            var chatBody = document.getElementById("chatBody");
            var div = document.createElement("div");
            div.style.padding = "8px 12px";
            div.style.borderRadius = "10px";
            div.style.maxWidth = "80%";
            div.style.marginBottom = "5px";
            div.style.lineHeight = "1.7";
            div.style.letterSpacing = "0.01em";

            if (sender === "user") {
                div.style.background = "#007bff";
                div.style.color = "white";
                div.style.alignSelf = "flex-end";
            } else {
                div.style.background = "#e4e6eb";
                div.style.color = "black";
                div.style.alignSelf = "flex-start";
            }

            // Phân tích markdown cơ bản: **bold**, [text](link) và \n -> <br>
            var formattedText = text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
            formattedText = formattedText.replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2" target="_blank">$1</a>');
            formattedText = formattedText.replace(/\n/g, '<br>');
            
            div.innerHTML = formattedText;
            chatBody.appendChild(div);

            // Tự động cuộn xuống dưới cùng
            chatBody.scrollTop = chatBody.scrollHeight;
        }
    </script>