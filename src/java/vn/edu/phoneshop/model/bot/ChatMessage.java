package vn.edu.phoneshop.model.bot;

public class ChatMessage implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public String role;
    public String content;

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
