package ru.samborskiy.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.samborskiy.config.BotConfig;
import ru.samborskiy.model.CurrencyModel;
import ru.samborskiy.repository.Repo;
import ru.samborskiy.service.CurrencyService;

import java.io.IOException;


@Component
public class Listener {

    private TelegramBot telegramBot;
    private final BotConfig botConfig;
    @Autowired
    private final CurrencyService currencyService;
    @Autowired
    private Repo repo;
    private final CurrencyModel model = new CurrencyModel();
    private final Logger logger = LoggerFactory.getLogger(Listener.class);


    public Listener(CurrencyService currencyService, BotConfig botConfig) {
        this.currencyService = currencyService;
        this.botConfig = botConfig;

    }
    @PostConstruct
    public void init() {
        telegramBot = new TelegramBot(botConfig.getBotToken());
        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    Message message = update.message();
                    Long chatId = message.chat().id();
                    String text = message.text();
                    String userName = getUserName(message);
                    String[] split = text.split(" ");
                    if ("/start".equals(text)) {
                        sendMessage(chatId, "Приветствую тебя " + userName + "!\nЯ конвертер валют:)\n" +
                                "Вводи сумму, валюту которую нужно конвертировать и в какую валюту нужно конвертировать!\n" +
                                "Валюта вводится в формате кода валюты.\nПример ввода: 3 USD RUB");
                    } else if (!checkErrorsInResponse(split, chatId)) {
                        logger.info("Error in text " + chatId + " " + userName);
                    } else {
                        try {
                        Response response = currencyService.getCurrencyRate(split[1], getUpperCaseValute(split[2]), getUpperCaseValute(split[0]));
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        save(chatId, userName, jsonObject);
                        }catch (JSONException e) {
                            sendMessage(chatId, "Похоже ты ввел валюту которую я не знаю :(");
                            logger.info("Enter non-existent currency " + chatId + " " + userName);
                        } catch (IOException ex) {
                            logger.info(ex.getMessage());
                        }
                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void save(Long chatId, String userName, JSONObject jsonObject) {
        JSONObject query = (JSONObject) jsonObject.get("query");
        JSONObject info = (JSONObject) jsonObject.get("info");
        model.setFrom(query.getString("from"));
        model.setTo(query.getString("to"));
        model.setAmount(query.getInt("amount"));
        model.setRate(info.getDouble("rate"));
        model.setResult(jsonObject.getDouble("result"));
        model.setDate(jsonObject.getString("date"));
        model.setNameUser(userName);
        repo.save(model);
        sendMessage(chatId, model.toString());
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.info("Error when sending a message");
        }
    }
    private String getUserName(Message msg) {
        User user = msg.from();
        String userName = user.firstName();
        return (userName != null) ? userName : String.format("%s %s", user.lastName(), user.firstName());
    }

    private String getUpperCaseValute(String valute) {
        return valute.toUpperCase().trim();
    }

    private boolean checkErrorsInResponse(String[] split, Long chatId) {
        int codeLenght = 3;
        if (split.length != codeLenght) {
            sendMessage(chatId, "Похоже что ты указал неверный формат");
            logger.info("text out of bounds");
            return false;
        }
        for(int i = 0; i < split[0].length(); i++) {
            if(!Character.isDigit(split[0].charAt(i))) {
                sendMessage(chatId, "Сперва укажи сумму которую необходимо конвертировать");
                logger.info("first text is not digit");
                return false;
            }
        }
        if (split[1].length() != codeLenght || split[2].length() != codeLenght) {
            sendMessage(chatId, "Похоже ты упустил какую-то букву");
            logger.info("text after first out of bounds");
            return false;
        }
        for(int i = 0; i < codeLenght; i++) {
            if(!Character.isLetter(split[1].charAt(i)) || !Character.isLetter(split[2].charAt(i))) {
                sendMessage(chatId, "После суммы необходимо указать валюту");
                logger.info("text after first is not Letter");
                return false;
            }
        }
      return true;
    }

}
