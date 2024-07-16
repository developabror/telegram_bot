package uz.app.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import uz.app.entity.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    static String userPath = "src/main/resources/user.json";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public List<User> getUser() {
        Type type = new TypeToken<List<User>>() {
        }.getType();
        ArrayList<User> users =null;
        System.out.println("getting users info");
        try {
            users = gson.fromJson(new FileReader(userPath), type);

        } catch (FileNotFoundException e) {}
        if (users ==null) return new ArrayList<>();
        System.out.println("returning users info");
        return users;
    }

    public void saveUser(User user) {
        List<User> users = getUser();
        if (users.stream().anyMatch(user1 -> user1.getChatId().equals(user.getChatId()))) {
            for (User user1 : users) {
                if(user1.getId().equals(user.getId())){
                    user1.setState(user.getState());
                    user1.setPhoneNumber(user.getPhoneNumber());
                }
            }
            saveUser(users);
            return;
        }
        users.add(user);
        saveUser(users);

    }

    public void saveUser(List<User> users) {
        String json = gson.toJson(users);
        System.out.println("starting to save");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userPath))) {
            writer.write(json);

            System.out.println("user successfully saved");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<User> getUserByChatId(String chatId) {
        for (User user : getUser()) {
            if (user.getChatId().equals(chatId)){
                return Optional.of(user);
            }
        }
        return Optional.empty();

    }



    private static UserRepository userRepository;
    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }
}
