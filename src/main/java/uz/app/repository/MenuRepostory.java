package uz.app.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import uz.app.entity.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MenuRepostory {
    static String path = "src/main/resources/menu.json";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
static {
    System.out.println(MenuRepostory.class.getClassLoader().getResource("menu.json").toString());
}

    public List<String> getMenu() {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        ArrayList<String> menu =null;
        System.out.println("getting menu info");
        try {
            menu = gson.fromJson(new FileReader(path), type);
            if (menu ==null) menu = new ArrayList<>();
        } catch (FileNotFoundException e) {
        }
        System.out.println("returning menu info");
        return menu;
    }

    public void saveMenu(List<String> menu) {
        String json = gson.toJson(menu);
        System.out.println("starting to save");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(json);

            System.out.println("menu successfully saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
