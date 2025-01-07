package dev.padrewin.chatreport.utils;

import org.bukkit.entity.Player;
import dev.padrewin.chatreport.setting.SettingKey;

import java.util.*;

public class ChatLogger {

    private static final Map<UUID, Deque<StringBuilder>> playerMessages = new HashMap<>();

    /**
     * Logs a message for a specific player.
     *
     * @param player  The player sending the message.
     * @param message The message to log.
     */
    public static void logMessage(Player player, String message) {
        UUID playerId = player.getUniqueId();
        playerMessages.putIfAbsent(playerId, new LinkedList<>());

        Deque<StringBuilder> messages = playerMessages.get(playerId);
        int maxMessages = SettingKey.MAX_LOG_MESSAGES.get(); // Preia valoarea din configurare

        if (messages.size() >= maxMessages) {
            messages.pollFirst(); // Remove the oldest message if the limit is reached
        }
        messages.addLast(new StringBuilder(message));
    }

    /**
     * Retrieves the most recent messages for a player.
     *
     * @param player       The player whose messages are being retrieved.
     * @param messageCount The number of messages to retrieve.
     * @return A list of the most recent messages, or an empty list if no messages exist.
     */
    public static List<String> getRecentMessages(Player player, int messageCount) {
        UUID playerId = player.getUniqueId();
        Deque<StringBuilder> messages = playerMessages.getOrDefault(playerId, new LinkedList<>());

        List<String> recentMessages = new ArrayList<>();
        int fromIndex = Math.max(0, messages.size() - messageCount);
        int index = 0;

        for (StringBuilder sb : messages) {
            if (index++ >= fromIndex) {
                recentMessages.add(sb.toString());
            }
        }

        return recentMessages;
    }

    /**
     * Clears all logged messages for a specific player.
     *
     * @param player The player whose messages should be cleared.
     */
    public static void clearMessages(Player player) {
        playerMessages.remove(player.getUniqueId());
    }
}