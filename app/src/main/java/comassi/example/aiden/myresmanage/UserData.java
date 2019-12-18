package comassi.example.aiden.myresmanage;

import java.io.Serializable;

public class UserData implements Serializable {

    private String email;
    private String nick;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
