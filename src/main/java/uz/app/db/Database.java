package uz.app.db;

import uz.app.repository.MenuRepostory;
import uz.app.service.ReplyKeyboardMakerService;
import uz.app.service.StateEnum;

import java.util.HashMap;
import java.util.Map;

public class Database {
    public static Map<String, StateEnum> userState = new HashMap<>();
    public static MenuRepostory menuRepostory = new MenuRepostory();
    public static Map<String, String> respond = new HashMap<>();
}
