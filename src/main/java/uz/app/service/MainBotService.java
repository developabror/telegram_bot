package uz.app.service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.utills.Utill;

import java.util.ArrayList;
import java.util.List;

public class MainBotService extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatId);
        switch (text) {
            case "/start" -> {
                sendMessage.setText("Salom, botga xush kelibsiz!");
                ReplyKeyboardMarkup markup =new ReplyKeyboardMarkup();
                markup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(markup);

                List<KeyboardRow> keyboardRows = new ArrayList<>();
                markup.setKeyboard(keyboardRows);

                KeyboardRow row1 = new KeyboardRow();
                row1.add("info");
                row1.add("menu");
                KeyboardRow row2 = new KeyboardRow();
                row2.add("contact us");
                keyboardRows.add(row1);
                keyboardRows.add(row2);

            }
            case "hello" -> {
                sendMessage.setText("hi");
            }
            case "question" -> {
                sendMessage.setText("answer");
            }
            case "contact" -> {
                sendMessage.setText("send your contact");
            }
            default -> sendMessage.setText(text);
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {}
    }


    public String getBotUsername() {
        return Utill.botUsername;
    }

    public String getBotToken() {
        return Utill.botToken;
    }

    private static MainBotService mainBotService;

    public static MainBotService getInstance() {
        if (mainBotService == null) {
            mainBotService = new MainBotService();
        }
        return mainBotService;
    }
}
