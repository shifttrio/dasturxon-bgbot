package org.example.tezkor.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DasturxonBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    public DasturxonBot(BotConfig botConfig) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getFrom().getFirstName();

            log.info("Received message: {} from chatId: {}", messageText, chatId);

            if (messageText.equals("/start")) {
                sendWelcomeMessage(chatId, firstName);
            }
        }
    }

    /**
     * Welcome xabar va Web App tugmasi
     */
    private void sendWelcomeMessage(long chatId, String firstName) {
        String welcomeText = String.format(
                "👋 Assalomu alaykum, %s!\n\n" +
                        "🍽 Dasturxon botiga xush kelibsiz!\n\n" +
                        "Bizning xizmatlarimiz:\n" +
                        "✅ Tezkor yetkazib berish\n" +
                        "✅ Eng yaxshi restoranlar\n" +
                        "✅ Sifatli taomlar\n\n" +
                        "📱 Buyurtma berish uchun quyidagi tugmani bosing 👇",
                firstName
        );

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(welcomeText);

        // Web App tugmasi
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton webAppButton = new InlineKeyboardButton();
        webAppButton.setText("🍽 Buyurtma berish");

        // Web App URL
        WebAppInfo webAppInfo = new WebAppInfo();
        webAppInfo.setUrl(botConfig.getWebappUrl());
        webAppButton.setWebApp(webAppInfo);

        row1.add(webAppButton);
        rows.add(row1);

        // Qo'shimcha tugmalar (ixtiyoriy)
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton aboutButton = new InlineKeyboardButton();
        aboutButton.setText("ℹ️ Biz haqimizda");
        aboutButton.setCallbackData("about");

        InlineKeyboardButton supportButton = new InlineKeyboardButton();
        supportButton.setText("📞 Yordam");
        supportButton.setCallbackData("support");

        row2.add(aboutButton);
        row2.add(supportButton);
        rows.add(row2);

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
            log.info("Welcome message sent to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Error sending welcome message: {}", e.getMessage());
        }
    }

    /**
     * Callback query uchun handler (ixtiyoriy tugmalar uchun)
     */
    @Override
    public void onUpdatesReceived(List<Update> updates) {
        for (Update update : updates) {
            if (update.hasCallbackQuery()) {
                handleCallbackQuery(update);
            } else {
                onUpdateReceived(update);
            }
        }
    }

    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        switch (callbackData) {
            case "about":
                message.setText(
                        "📖 Biz haqimizda:\n\n" +
                                "🍽 Dasturxon - O'zbekistonning eng tezkor yetkazib berish xizmati!\n\n" +
                                "✅ 100+ restoran\n" +
                                "✅ 20-30 daqiqada yetkazib berish\n" +
                                "✅ Ishonchli xizmat"
                );
                break;

            case "support":
                message.setText(
                        "📞 Yordam:\n\n" +
                                "Savollaringiz bo'lsa biz bilan bog'laning:\n" +
                                "☎️ Telefon: +998 90 123 45 67\n" +
                                "📧 Email: support@dasturxon.uz\n" +
                                "🕐 24/7 xizmat"
                );
                break;

            default:
                message.setText("Noma'lum buyruq");
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error handling callback: {}", e.getMessage());
        }
    }
}