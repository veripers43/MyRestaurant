package comassi.example.aiden.myresmanage;


import java.io.Serializable;

public class RestaurantData implements Serializable {
    int image;
    String MyReview;

    public RestaurantData(int image, String myReview) {
        this.image = image;
        this.MyReview = myReview;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMyReview() {
        return MyReview;
    }

    public void setMyReview(String myReview) {
        MyReview = myReview;
    }
}
