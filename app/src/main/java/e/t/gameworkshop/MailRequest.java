package e.t.gameworkshop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MailRequest extends StringRequest {

    final static private String URL_DELETE = "https://start1a.cafe24.com/DeleteMail.php";
    private Map<String, String> parameters;

    public MailRequest(int tag, Response.Listener<String> listener)
    {
        super(Method.POST, URL_DELETE, listener, null);
        parameters = new HashMap<>();
        parameters.put("tag",  tag + "");
    }

    public Map<String, String> getParams()
    {
        return parameters;
    }
}