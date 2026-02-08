package org.example.tezkor.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class BotInitializer {

    private final DasturxonBot dasturxonBot;

    public BotInitializer(DasturxonBot dasturxonBot) {
        this.dasturxonBot = dasturxonBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(dasturxonBot);
            log.info("✅ Telegram bot successfully started!");
        } catch (TelegramApiException e) {
            log.error("❌ Error starting Telegram bot: {}", e.getMessage());
        }
    }
}