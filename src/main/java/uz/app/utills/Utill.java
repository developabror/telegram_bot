package uz.app.utills;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public interface Utill {
    String botToken = "7468325908:AAEHcl6fV-gzG_JrEAxL7No08LH4tJJDQzU";
    String botUsername = "g44testbot";
    KeyboardButton contactRequest = KeyboardButton.builder().text("send contact").requestContact(true).build();
    KeyboardButton locationRequest = KeyboardButton.builder().text("send location").requestContact(true).build();
    String mainMenu[][] = {
            {"menu"},
            {"contact request", "location request"}
    };
    String menu[][] = {
            {"burger", "lavash"},
            {"shaurma", "twister"},
            {"how dog", "drinks"},
            {"back"}
    };
    String lavash[][] = {
            {"lavash mini", "lavash"},
            {"lavash with cheese", "chicken lavash"},
            {"back"},
    };
    String burger[][] = {
            {"Classic Burger", "Cheese Burger"},
            {"Bacon Burger", "BBQ Burger"},
            {"Veggie Burger", "back"}
    };
    String shaurma[][] = {
            {"Chicken Shawarma", "Beef Shawarma"},
            {"Lamb Shawarma", "Turkey Shawarma"},
            {"Falafel Shawarma","back"}
    };
    String drinks[][] = {
            {"moxito", "ice coffee"},
            {"milk shake", "fresh juice"},
            {"flat white","back"}
    };

//    String BACK="back";
//    String PIZZA = "pizza";
//    String menu[][]= {
//            {"burger","twister","spinner"},
//            {"hot drinks","cold drinks"},
//            {"snacks"},
//            {PIZZA,"pide","roll"},
//            {BACK}
//    };

}
