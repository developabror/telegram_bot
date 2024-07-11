package uz.app.service;

import lombok.extern.java.Log;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.repository.MenuRepostory;
import uz.app.utills.Utill;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static uz.app.utills.Utill.*;

@Log
public class MainBotService extends TelegramLongPollingBot {

    MenuRepostory menuRepostory = new MenuRepostory();
    static StateEnum stateEnum =StateEnum.DEF;
    ReplyKeyboardMakerService makerService = new ReplyKeyboardMakerService();
    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        String chatId = update.getMessage().getChatId().toString();
        sendMessage.setChatId(chatId);


        if (stateEnum.equals(StateEnum.ADD_MENU)){
            List<String> menu = menuRepostory.getMenu();
            menu.add(text);
            menuRepostory.saveMenu(menu);
            stateEnum = StateEnum.DEF;
            sendMessage.setText("success");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {}
            return;
        }

        if (update.getMessage().hasContact()) {
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
            log.log(Level.INFO, "Phone number is {0}, chat id is {1}", new Object[]{phoneNumber, chatId});
        }
        switch (text) {


            case "/start","back" -> {
                sendMessage.setText("Salom, botga xush kelibsiz!");
                sendMessage.setReplyMarkup(makerService.replyMaker(Utill.mainMenu));
            }
            case "information" -> {
                sendMessage.setText("this bot specified to do smth");
            }
            case "add  menu" -> {
                sendMessage.setText("enter menu name");
                stateEnum = StateEnum.ADD_MENU;
            }
            case "menu" -> {
                sendMessage.setText("here is menu");
                sendMessage.setReplyMarkup(makerService.replyMaker(menuRepostory.getMenu()));
            }
            case "contact us" -> {
                sendMessage.setText("send your contact");
            }

            default -> sendMessage.setText(text);
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }
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
