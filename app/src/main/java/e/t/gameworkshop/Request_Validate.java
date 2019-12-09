package e.t.gameworkshop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Request_Validate extends StringRequest {

    final static private String URL = "https://start1a.cafe24.com/Validate.php";
    private Map<String, String> parameters;

    public Request_Validate(String userID, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    public Map<String, String> getParams()
    {
        return parameters;
    }

}
