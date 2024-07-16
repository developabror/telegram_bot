package uz.app.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static uz.app.db.Database.*;
import static uz.app.utills.Utill.adminChatId;

public class CallbackService {
    MainBotService mainBotService = MainBotService.getInstance();

    public void service(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String userChatId = update.getCallbackQuery().getData();
        sendMessage.setText("enter your message");
        sendMessage.setChatId(chatId);
        userState.put(chatId, StateEnum.RESPOND_TO_USER);
        respond.put(chatId, userChatId);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText("respond:   " + update.getCallbackQuery().getMessage().getText());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        mainBotService.executeMessage(sendMessage);
        editMessageText.setChatId(chatId);
        try {
            mainBotService.execute(editMessageText);
        } catch (TelegramApiException e) {}
    }


    private static CallbackService callbackService;

    public static CallbackService getInstance() {
        if (callbackService == null) {
            callbackService = new CallbackService();
        }
        return callbackService;
    }
}
