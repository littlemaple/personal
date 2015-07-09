package rainbow.com.rainbow.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by blue on 2015/7/7.
 */
public class Account {
    private int id;
    private String userName;
    private String password;
    private String accessToken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    public static Account parse(Account account, JSONObject jo) {
        if (jo == null || account == null)
            return null;
        try {
            if (jo.has("target") && !jo.isNull("target")) {
                account.setUserName(jo.getString("target"));
            }
            if (jo.has("password") && !jo.isNull("password")) {
                account.setPassword(jo.getString("password"));
            }
            if (jo.has("access_token") && !jo.isNull("access_token")) {
                account.setAccessToken(jo.getString("access_token"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("Account:");
        buffer.append("\n" + this.getUserName());
        buffer.append("\n" + this.getPassword());
        buffer.append("\n" + this.getAccessToken());
        return buffer.toString();
    }
}
