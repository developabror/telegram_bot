package uz.app.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.entity.User;
import uz.app.repository.UserRepository;

import java.util.Optional;

import static uz.app.db.Database.*;
import static uz.app.utills.Utill.adminChatId;

public class CallbackService {
    MainBotService mainBotService = MainBotService.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    public void service(Update update) {
        SendMessage sendMessage = new SendMessage();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String userChatId = update.getCallbackQuery().getData();
        Optional<User> optionalUser = userRepository.getUserByChatId(userChatId);
        if (optionalUser.isEmpty()){
            System.out.println("user not found");
            sendMessage.setText("please confirm your number");
            return;
        }
        User user = optionalUser.get();
        sendMessage.setText("enter your message");
        sendMessage.setChatId(chatId);
//        userState.put(chatId, StateEnum.RESPOND_TO_USER);
        user.setState(StateEnum.RESPOND_TO_USER);
        respond.put(chatId, userChatId);
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText("respond:   " + update.getCallbackQuery().getMessage().getText());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        mainBotService.executeMessage(sendMessage);
        editMessageText.setChatId(chatId);
        try {
            mainBotService.execute(editMessageText);
        } catch (TelegramApiException e) {
        }
    }


    private static CallbackService callbackService;

    public static CallbackService getInstance() {
        if (callbackService == null) {
            callbackService = new CallbackService();
        }
        return callbackService;
    }
}
