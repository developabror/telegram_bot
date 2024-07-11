package uz.app.service;

import lombok.extern.java.Log;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.utills.Utill;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Log
public class MainBotService extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatId);

        if (update.getMessage().hasContact()){
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
            log.log(Level.INFO, "Phone number is {0}, chat id is {1}", new Object[]{phoneNumber, chatId});
        }
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
                KeyboardButton sendContact = new KeyboardButton("send contact");
                sendContact.setRequestContact(true);
                row2.add(sendContact);
                KeyboardButton sendLocation = new KeyboardButton("send location");
                sendLocation.setRequestLocation(true);
                row2.add(sendLocation);
                keyboardRows.add(row1);
                keyboardRows.add(row2);

            }
            case "info" -> {
                sendMessage.setText("this bot specified to do smth");
            }
            case "menu" -> {
                sendMessage.setText("here is menu");
            }
            case "contact us" -> {
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
