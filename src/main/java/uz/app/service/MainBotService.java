package uz.app.service;

import lombok.extern.java.Log;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.repository.MenuRepostory;
import uz.app.utills.Utill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static uz.app.utills.Utill.*;

@Log
public class MainBotService extends TelegramLongPollingBot {


    static Map<String, StateEnum> userState = new HashMap<>();
    MenuRepostory menuRepostory = new MenuRepostory();
    ReplyKeyboardMakerService makerService = new ReplyKeyboardMakerService();
    static Map<String, String> respond = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String userChatId = update.getCallbackQuery().getData();
            sendMessage.setText("enter your message");
            sendMessage.setChatId(chatId);
            userState.put(chatId, StateEnum.RESPOND_TO_USER);
            respond.put(chatId,userChatId);
            executeMEssage(sendMessage);
        } else {
            String text = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            userState.putIfAbsent(chatId, StateEnum.DEF);
            sendMessage.setChatId(chatId);


//        if (stateEnum.equals(StateEnum.ADD_MENU)){
//            List<String> menu = menuRepostory.getMenu();
//            menu.add(text);
//            menuRepostory.saveMenu(menu);
//            stateEnum = StateEnum.DEF;
//            sendMessage.setText("success");
//            try {
//                execute(sendMessage);
//            } catch (TelegramApiException e) {}
//            return;
//        }

            if (userState.get(chatId).equals(StateEnum.WRITE_TO_ADMIN)) {
                sendMessage.setText(update.getMessage().getText());
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                sendMessage.setReplyMarkup(markup);
                List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();
                List<InlineKeyboardButton> keyboard = new ArrayList<>();
                InlineKeyboardButton button = InlineKeyboardButton.builder().text("reply").callbackData(chatId).build();
                keyboard.add(button);
                keyboardList.add(keyboard);
                markup.setKeyboard(keyboardList);
//                sendMessage.setChatId(adminChatId);
                for (String s : adminChatId) {
                    sendMessage.setChatId(s);
                    executeMEssage(sendMessage);
                }
                userState.put(chatId, StateEnum.DEF);

                return;
            } else if (userState.get(chatId).equals(StateEnum.RESPOND_TO_USER)) {
                sendMessage.setText(text);
                sendMessage.setChatId(respond.get(chatId));
                userState.put(chatId,StateEnum.DEF);
            }
            if (update.getMessage().hasContact()) {
                String phoneNumber = update.getMessage().getContact().getPhoneNumber();
                log.log(Level.INFO, "Phone number is {0}, chat id is {1}", new Object[]{phoneNumber, chatId});
            }
            switch (text) {


                case "/start", "back" -> {
                    sendMessage.setText("Salom, botga xush kelibsiz!");
                    sendMessage.setReplyMarkup(makerService.replyMaker(Utill.mainMenu));
                }
                case "information" -> {
                    sendMessage.setText("this bot specified to do smth");
                }
                case "add  menu" -> {
                    sendMessage.setText("enter menu name");
                    userState.put(chatId, StateEnum.ADD_MENU);
                }
                case "menu" -> {
                    sendMessage.setText("here is menu");
                    sendMessage.setReplyMarkup(makerService.replyMaker(menuRepostory.getMenu()));
                }
                case "contact us" -> {
                    sendMessage.setText("ask your question from admin");
                    userState.put(chatId, StateEnum.WRITE_TO_ADMIN);
                }
                case "locations" -> {
                    sendMessage.setText("here is our locations");

                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    sendMessage.setReplyMarkup(markup);
                    List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();
                    List<InlineKeyboardButton> keyboard = new ArrayList<>();

                    InlineKeyboardButton button1 = InlineKeyboardButton.builder().text("location 1").callbackData("chilonzor filiali").build();
                    InlineKeyboardButton button2 = InlineKeyboardButton.builder().text("location 2").callbackData("yunusobod filiali").build();
                    InlineKeyboardButton button3 = InlineKeyboardButton.builder().text("location 3").callbackData("qoraqamish filiali").build();
                    InlineKeyboardButton button4 = InlineKeyboardButton.builder().text("location 4").callbackData("Chorsu filiali").build();
                    keyboard.add(button1);
                    keyboard.add(button2);
                    keyboardList.add(keyboard);
                    keyboard = new ArrayList<>();
                    keyboard.add(button3);
                    keyboardList.add(keyboard);
                    keyboard = new ArrayList<>();
                    keyboard.add(button4);
                    keyboardList.add(keyboard);
                    markup.setKeyboard(keyboardList);
                }
                case "123.142:4235.132" -> {
                    sendMessage.setText("here is out location 1");
                }


                default -> sendMessage.setText(text);
            }
            executeMEssage(sendMessage);
        }
    }


    public String getBotUsername() {
        return Utill.botUsername;
    }

    public String getBotToken() {
        return Utill.botToken;
    }

    public void executeMEssage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }

    private static MainBotService mainBotService;

    public static MainBotService getInstance() {
        if (mainBotService == null) {
            mainBotService = new MainBotService();
        }
        return mainBotService;
    }
}
