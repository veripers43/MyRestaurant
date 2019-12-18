package comassi.example.aiden.myresmanage;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    final static String URL = "http://alfo07.dothome.co.kr/seltest.php";
    private Map<String,String> map;
    private String email;



    public LoginRequest  (String email, Response.Listener<String> listener) {

        super(Method.POST,URL,listener, null); //이 문장이 중요함
        map = new HashMap<>();
        this.email = email;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("email",email);
        return map;
    }
}

