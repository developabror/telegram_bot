package uz.app.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.app.service.StateEnum;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String id;
    private String chatId;
    private String name;
    private StateEnum state;
    private String phoneNumber;
}
