package rainbow.com.simple_net_framework.stack;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;
import java.util.Map;

import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.base.Response;
import rainbow.com.simple_net_framework.config.Framework;
import rainbow.com.simple_net_framework.config.HttpConfig;

/**
 * Created by blue on 2015/7/6.
 */
public class HttpNormalStack implements HttpStack {
    private HttpClient client = AndroidHttpClient.newInstance(HttpConfig.userAgent);

    @Override
    public Response performResult(Request<?> request) {
        HttpUriRequest httpRequest = createRequest(request);

        addHeader(httpRequest, request);
        try {
            HttpResponse response = client.execute(httpRequest);
            Response result = new Response(response.getStatusLine());
            result.setEntity(response.getEntity());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setConnection(HttpEntityEnclosingRequestBase request) {
        HttpConnectionParams.setConnectionTimeout(request.getParams(), HttpConfig.connTimeout);
        HttpConnectionParams.setSoTimeout(request.getParams(), HttpConfig.soTimeout);
    }

    private void addHeader(HttpUriRequest httpRequest, Request<?> request) {
        if (request == null || request.getHeader().size() == 0)
            return;
        for (Map.Entry<String, String> entry : request.getHeader().entrySet()) {
            httpRequest.addHeader(entry.getKey(), entry.getValue());
            Log.d(Framework.TAG, "Http,addHead>>>key:" + entry.getKey() + ",value:" + entry.getValue());
        }
    }


    private HttpUriRequest createRequest(Request<?> request) {
        if (request == null)
            return null;
        HttpUriRequest ret = null;
        switch (request.getHttpMethod()) {
            case POST:
                ret = new HttpPost(request.getUrl());
                ret.addHeader(Request.HEADER_CONTENT_TYPE, request.getBodyContentType());
                setEntityIfExist((HttpPost) ret, request);
                break;
            case GET:
                ret = new HttpGet(request.getUrl());
                break;
            case PUT:
                ret = new HttpPut(request.getUrl());
                setEntityIfExist((HttpPut) ret, request);
                break;
            case DELETE:
                ret = new HttpDelete(request.getUrl());
                break;

        }
        return ret;
    }

    private void setEntityIfExist(HttpEntityEnclosingRequestBase httpRequest, Request<?> request) {
        byte[] data = request.getParamsBody();
        if (data != null) {
            httpRequest.setEntity(new ByteArrayEntity(data));
        }
    }
}
