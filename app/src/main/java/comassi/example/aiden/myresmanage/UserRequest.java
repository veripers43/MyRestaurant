package comassi.example.aiden.myresmanage;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


//카카오톡 로그인을 했을때 이메일과 닉네임을 서버에 전송
public class UserRequest   extends StringRequest {
    final static String URL = "http://alfo07.dothome.co.kr/adduser.php";
    private Map<String,String> map;
    private String email;
    private String nick;


    public UserRequest  (String email, String nick, Response.Listener<String> listener) {

        super(Method.POST,URL,listener, null); //이 문장이 중요함
        map = new HashMap<>();
        this.email =email;
        this.nick = nick;

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("email",this.email);
        map.put("nick",this.nick);

        return map;
    }
}