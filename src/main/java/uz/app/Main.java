package uz.app;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.app.service.MainBotService;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("working");
        TelegramBotsApi register =new TelegramBotsApi(DefaultBotSession.class);
        register.registerBot(MainBotService.getInstance());
    }
}