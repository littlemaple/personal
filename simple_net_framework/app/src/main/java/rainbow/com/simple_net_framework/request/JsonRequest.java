package rainbow.com.simple_net_framework.request;

import org.json.JSONException;
import org.json.JSONObject;

import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.base.Response;

/**
 * Created by blue on 2015/7/6.
 */
public class JsonRequest extends Request<JSONObject> {

    public JsonRequest(HttpMethod httpMethod, String mUrl, IRequestListener listener) {
        super(httpMethod, mUrl, listener);
    }

    @Override
    public JSONObject parseResponse(Response response) {
        if (response != null)
            try {
                String message = new String(response.rawData);
                JSONObject result = new JSONObject(message);
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
    }

}
