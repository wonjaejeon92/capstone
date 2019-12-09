package e.t.gameworkshop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Request_Register extends StringRequest {

    final static private String URL = "https://start1a.cafe24.com/Register.php";
    private Map<String, String> parameters;

    public Request_Register(String userID, String userPassword, int userLevel, int userExp, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userLevel", userLevel + "");
        parameters.put("userExp", userExp + "");
    }

    public Map<String, String> getParams()
    {
        return parameters;
    }

}
