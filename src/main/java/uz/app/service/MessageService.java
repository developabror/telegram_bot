package uz.app.service;


import lombok.extern.java.Log;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.utills.Utill;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static uz.app.utills.Utill.adminChatId;
import static uz.app.db.Database.*;

@Log
public class MessageService {
    ReplyKeyboardMakerService makerService = new ReplyKeyboardMakerService();
    MainBotService mainBotService = MainBotService.getInstance();

    public void service(Update update) {
        SendMessage sendMessage = new SendMessage();
        String text = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        userState.putIfAbsent(chatId, StateEnum.DEF);
        sendMessage.setChatId(chatId);

        if (update.getMessage().hasContact()) {
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
            log.log(Level.INFO, "Phone number is {0}, chat id is {1}", new Object[]{phoneNumber, chatId});
        }

        if (stateController(update,sendMessage,chatId)) {
            return;
        }
        switch (text) {


            case "/start", "back" -> {
                sendMessage.setText("Salom, botga xush kelibsiz!");
                sendMessage.setReplyMarkup(makerService.replyMaker(Utill.mainMenu));
            }
            case "information" -> {
                sendMessage.setText("this bot specified to do smth");
            }
            case "add menu" -> {
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

            default -> sendMessage.setText(text);
        }
        mainBotService.executeMessage(sendMessage);
    }



    public boolean stateController(Update update, SendMessage sendMessage, String chatId){
        boolean worked = false;
        if (userState.get(chatId).equals(StateEnum.WRITE_TO_ADMIN)) {
            worked = true;
            sendMessage.setText(update.getMessage().getText());
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            sendMessage.setReplyMarkup(markup);
            List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();
            List<InlineKeyboardButton> keyboard = new ArrayList<>();
            InlineKeyboardButton button = InlineKeyboardButton.builder().text("reply").callbackData(chatId).build();
            keyboard.add(button);
            keyboardList.add(keyboard);
            markup.setKeyboard(keyboardList);

            for (String s : adminChatId) {
                sendMessage.setChatId(s);
                mainBotService.executeMessage(sendMessage);
            }

            userState.put(chatId, StateEnum.DEF);
        } else if (userState.get(chatId).equals(StateEnum.RESPOND_TO_USER)) {
            worked = true;
            sendMessage.setText(update.getMessage().getText());
            sendMessage.setChatId(respond.get(chatId));
            userState.put(chatId, StateEnum.DEF);
        }
        if (worked) mainBotService.executeMessage(sendMessage);
        return worked;
    }




    private static MessageService messageService;
    public  static MessageService getInstance(){
        if (messageService ==null){
            messageService = new MessageService();
        }
        return messageService;
    }
}
