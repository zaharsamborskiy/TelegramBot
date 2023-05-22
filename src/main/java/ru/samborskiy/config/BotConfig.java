package ru.samborskiy.config;


import com.pengrad.telegrambot.TelegramBot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {
    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botName;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(this.botToken);
    }
}
