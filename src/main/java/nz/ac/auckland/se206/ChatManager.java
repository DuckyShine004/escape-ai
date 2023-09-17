package nz.ac.auckland.se206;

/** The chat manager class helps sync all chat messages across all scenes */
public class ChatManager {
    private static String chatContent = "";

    /**
     * Get the chat content
     * @return
     */
    public static String getChatContent() {
        return chatContent;
    }

    /**
     * Set the chat content
     * @param content
     */
    public static void setChatContent(String content) {
        chatContent = content;
    }
}

