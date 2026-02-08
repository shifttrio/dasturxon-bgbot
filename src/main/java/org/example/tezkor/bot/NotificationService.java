package org.example.tezkor.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tezkor.bot.DasturxonBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Telegram orqali foydalanuvchilarga xabar yuborish servisi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final DasturxonBot dasturxonBot;

    /**
     * Buyurtma tasdiqlanganda xabar yuborish
     */
    public void sendOrderConfirmation(Long chatId, Long orderId, String deliveryAddress) {
        if (chatId == null) {
            log.warn("ChatId is null, cannot send notification");
            return;
        }

        String message = String.format(
                "✅ Buyurtma qabul qilindi!\n\n" +
                        "📦 Buyurtma #%d\n" +
                        "📍 Manzil: %s\n\n" +
                        "Tez orada operator siz bilan bog'lanadi.\n" +
                        "Buyurtmangiz 25-35 daqiqada yetkazib beriladi! 🚚",
                orderId,
                deliveryAddress
        );

        sendMessage(chatId, message);
    }

    /**
     * Buyurtma holati o'zgarganda xabar yuborish
     */
    public void sendOrderStatusUpdate(Long chatId, Long orderId, String status) {
        if (chatId == null) return;

        String emoji = getStatusEmoji(status);
        String statusText = getStatusText(status);

        String message = String.format(
                "%s Buyurtma holati o'zgartirildi\n\n" +
                        "📦 Buyurtma #%d\n" +
                        "📊 Holat: %s",
                emoji,
                orderId,
                statusText
        );

        sendMessage(chatId, message);
    }

    /**
     * Umumiy xabar yuborish
     */
    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            dasturxonBot.execute(message);
            log.info("Notification sent to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Error sending notification to chatId {}: {}", chatId, e.getMessage());
        }
    }

    /**
     * Status bo'yicha emoji
     */
    private String getStatusEmoji(String status) {
        switch (status) {
            case "PENDING": return "🕐";
            case "CONFIRMED": return "✅";
            case "PREPARING": return "👨‍🍳";
            case "DELIVERING": return "🚚";
            case "DELIVERED": return "🎉";
            case "CANCELLED": return "❌";
            default: return "📋";
        }
    }

    /**
     * Status bo'yicha matn
     */
    private String getStatusText(String status) {
        switch (status) {
            case "PENDING": return "Kutilmoqda";
            case "CONFIRMED": return "Tasdiqlandi";
            case "PREPARING": return "Tayyorlanmoqda";
            case "DELIVERING": return "Yo'lda";
            case "DELIVERED": return "Yetkazib berildi";
            case "CANCELLED": return "Bekor qilindi";
            default: return status;
        }
    }
}