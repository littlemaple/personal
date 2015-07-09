package rainbow.com.rainbow.network;

import android.os.MessageQueue;

import org.json.JSONObject;

import java.io.File;

import rainbow.com.rainbow.bean.AccountProxy;
import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.base.Response;
import rainbow.com.simple_net_framework.core.RequestQueue;
import rainbow.com.simple_net_framework.proxy.NetProxy;
import rainbow.com.simple_net_framework.request.JsonRequest;

/**
 * Created by blue on 2015/7/6.
 */
public class NetworkClient {
    private static final String URL_LOGIN = "http://dev.mcloudlife.com/api/login";
    private static final String URL_UPLOAD_ATTACHMENT = "http://dev.mcloudlife.com/api/attachment";


    public static void doLogin(String target, String password, Request.IRequestListener listener) {
        final JsonRequest request = new JsonRequest(Request.HttpMethod.POST, URL_LOGIN, listener);
        request.getParams().put("target", target);
        request.getParams().put("password", password);
        NetProxy.startRequest(request);
    }

    public static void doUploadFile(String accessToken, File file, int totalSize, Request.IRequestListener listener) {
        JsonRequest request = new JsonRequest((Request.HttpMethod.POST), URL_UPLOAD_ATTACHMENT, listener);
        request.getParams().put("access_token", AccountProxy.getInstance().getCurrent().getAccessToken());
        request.getParams().put("filename", "test.txt");
        request.getParams().put("size", "123123");
        NetProxy.startRequest(request);
    }


}
