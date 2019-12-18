package comassi.example.aiden.myresmanage;

import java.io.Serializable;
import java.util.Comparator;

public class CardData implements Serializable {
    private String name;
    private String address;
    private String menu;
    private String phone;
    private String lati;
    private String longi;
    private String lastimage;
    private String email;
    private int distance;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getLastimage() {
        return lastimage;
    }

    public void setLastimage(String lastimage) {
        this.lastimage = lastimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }



}
