package e.t.gameworkshop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RecordRequest extends StringRequest {

    final static private String URL_EXPUP = "https://start1a.cafe24.com/ExpUp.php";
    final static private String URL_SENDMAIL = "https://start1a.cafe24.com/SendMail.php";
    final static private String URL_WINLOSE = "https://start1a.cafe24.com/WINLOSE.php";
    private Map<String, String> parameters;

    public RecordRequest(String userID, int exp, Response.Listener<String> listener)
    {
        super(Method.POST, URL_EXPUP, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("EXP", exp + "");
    }

    public RecordRequest(int tag, String receiverID, String senderID, int senderLevel, int senderExp, int senderWIN, int senderLOSE, int stageNumber, int recordCost, int recordTime, int recordAction, Response.Listener<String> listener)
    {
        super(Method.POST, URL_SENDMAIL, listener, null);
        parameters = new HashMap<>();
        parameters.put("tag", tag + "");
        parameters.put("receiverID", receiverID);
        parameters.put("senderID", senderID);
        parameters.put("senderLevel", senderLevel + "");
        parameters.put("senderExp", senderExp + "");
        parameters.put("senderWIN", senderWIN + "");
        parameters.put("senderLOSE", senderLOSE + "");
        parameters.put("stageNUMBER", stageNumber + "");
        parameters.put("recordCost", recordCost + "");
        parameters.put("recordTime", recordTime + "");
        parameters.put("recordAction", recordAction + "");
    }


    public RecordRequest(int WIN, int LOSE, String userID, Response.Listener<String> listener)
    {
        super(Method.POST, URL_WINLOSE, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("WIN", WIN + "");
        parameters.put("LOSE", LOSE + "");
    }


    public Map<String, String> getParams()
    {
        return parameters;
    }
}