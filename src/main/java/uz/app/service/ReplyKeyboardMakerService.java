package uz.app.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardMakerService {


    public ReplyKeyboardMarkup replyMakerWithList(List<List<String>> buttons) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        markup.setKeyboard(keyboardRows);
        for (List<String> button : buttons) {
            KeyboardRow row = new KeyboardRow();
            keyboardRows.add(row);
            for (String s : button) {
                row.add(s);
            }
        }
        return markup;
    }

    public ReplyKeyboardMarkup replyMaker(String[][] buttons) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        markup.setKeyboard(keyboardRows);
        for (String[] button : buttons) {
            KeyboardRow row = new KeyboardRow();
            keyboardRows.add(row);
            for (String s : button) {
                if (s.equals("contact")) {
                    row.add(KeyboardButton.builder().text(s).requestContact(true).build());
                } else
                    row.add(s);
            }
        }
        return markup;
    }

    public ReplyKeyboardMarkup replyMaker(List<String> buttons) {
        if (!buttons.get(buttons.size() - 1).equals("back")) {
            buttons.add("back");
        }
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        markup.setKeyboard(keyboardRows);
        KeyboardRow row = new KeyboardRow();
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).equals("contact")) {
                row.add(KeyboardButton.builder().text(buttons.get(i)).requestContact(true).build());
            } else
                row.add(buttons.get(i));
            if (i % 2 == 1) {
                keyboardRows.add(row);
                row = new KeyboardRow();
            }
        }
        if (buttons.size() % 2 == 1) keyboardRows.add(row);
        return markup;
    }

}
