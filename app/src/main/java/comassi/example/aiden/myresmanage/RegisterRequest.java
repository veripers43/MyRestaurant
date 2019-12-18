package comassi.example.aiden.myresmanage;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest   extends StringRequest {
    final static String URL = "http://alfo07.dothome.co.kr/addrestaurant.php";
    private Map<String,String> map;
    private String name;
    private String address;
    private String menu;
    private String phone;
    private String lati;
    private String longi;
    private String email;





    public RegisterRequest  (String name, String address,String menu, String phone,
                             String lati, String longi, String email, Response.Listener<String> listener) {

        super(Method.POST,URL,listener, null); //이 문장이 중요함
        map = new HashMap<>();
        this.name =name;
        this.address = address;
        this.menu = menu;
        this.phone = phone;
        this.lati = lati;
        this.longi = longi;
        this.email = email;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("name",this.name);
        map.put("address",this.address);
        map.put("menu",this.menu);
        map.put("phone",this.phone);
        map.put("lati",this.lati);
        map.put("longi",longi);
        map.put("email",email);
        return map;
    }
}