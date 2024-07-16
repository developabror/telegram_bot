package uz.app.service;


import lombok.extern.java.Log;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.entity.User;
import uz.app.repository.UserRepository;
import uz.app.utills.Utill;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import static uz.app.utills.Utill.adminChatId;
import static uz.app.db.Database.*;

@Log
public class MessageService {
    ReplyKeyboardMakerService makerService = new ReplyKeyboardMakerService();
    MainBotService mainBotService = MainBotService.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    public void service(Update update) {
        SendMessage sendMessage = new SendMessage();
        String text = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();
        System.out.println(userRepository);
        Optional<User> optionalUser = userRepository.getUserByChatId(chatId);
        System.out.println(optionalUser);
        if (optionalUser.isEmpty()) {
            User newUser = new User();
            newUser.setState(StateEnum.DEF);
            newUser.setId(UUID.randomUUID().toString());
            newUser.setPhoneNumber("");
            newUser.setName(update.getMessage().getChat().getFirstName());
            newUser.setChatId(chatId);
            userRepository.saveUser(newUser);
        }
        User user = optionalUser.get();
        sendMessage.setChatId(chatId);

        if (update.getMessage().hasContact()) {
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
            log.log(Level.INFO, "Phone number is {0}, chat id is {1}", new Object[]{phoneNumber, chatId});
            user.setPhoneNumber(phoneNumber);
            userRepository.saveUser(user);
        }
        if (stateController(update, sendMessage, chatId, user)) {
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
                user.setState(StateEnum.ADD_MENU);
                userRepository.saveUser(user);
            }
            case "menu" -> {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                sendPhoto.setReplyMarkup(makerService.replyMaker(menuRepostory.getMenu()));
                sendPhoto.setCaption("here is menu");

                sendPhoto.setPhoto(new InputFile(new File("/home/user/Documents/image.jpeg")));
                try {
                    mainBotService.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }
            case "contact us" -> {
                sendMessage.setText("ask your question from admin");
                user.setState(StateEnum.WRITE_TO_ADMIN);
                userRepository.saveUser(user);
            }
            case "send photo" -> {
                sendMessage.setText("upload photo");
                user.setState(StateEnum.SEND_PHOTO);
                userRepository.saveUser(user);
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


    public boolean stateController(Update update, SendMessage sendMessage, String chatId, User user) {
        boolean worked = false;
        StateEnum stateEnum = user.getState();
        if (stateEnum.equals(StateEnum.WRITE_TO_ADMIN)) {
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

            user.setState(StateEnum.DEF);
        } else if (stateEnum.equals(StateEnum.RESPOND_TO_USER)) {
            worked = true;
            sendMessage.setText(update.getMessage().getText());
            sendMessage.setChatId(respond.get(chatId));
            user.setState(StateEnum.DEF);
        } else if (stateEnum.equals(StateEnum.SEND_PHOTO)) {
            List<PhotoSize> photo = update.getMessage().getPhoto();
            String filePath = photo.get(photo.size() - 1).getFileId();
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setReplyMarkup(makerService.replyMaker(menuRepostory.getMenu()));
            sendPhoto.setCaption(user.getName() + " send photo: " + user.getPhoneNumber());
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            sendPhoto.setReplyMarkup(markup);
            List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();
            List<InlineKeyboardButton> keyboard = new ArrayList<>();
            InlineKeyboardButton button = InlineKeyboardButton.builder().text("reply").callbackData(chatId).build();
            keyboard.add(button);
            keyboardList.add(keyboard);
            markup.setKeyboard(keyboardList);

            sendPhoto.setPhoto(new InputFile(filePath));
            for (String s : adminChatId) {
                sendPhoto.setChatId(s);
                try {
                    mainBotService.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }
        user.setState(StateEnum.DEF);
        userRepository.saveUser(user);
        if (worked) mainBotService.executeMessage(sendMessage);
        return worked;
    }


    private static MessageService messageService;

    public static MessageService getInstance() {
        if (messageService == null) {
            messageService = new MessageService();
        }
        return messageService;
    }
}
