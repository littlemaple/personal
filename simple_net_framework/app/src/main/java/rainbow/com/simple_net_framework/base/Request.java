package rainbow.com.simple_net_framework.base;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import rainbow.com.simple_net_framework.config.Framework;

/**
 * Created by blue on 2015/7/6.
 */
public abstract class Request<T> implements Comparable<Request<T>> {
    public static final String TAG = Request.class.getSimpleName();

    public enum HttpMethod {

        POST("post"),
        DELETE("delete"),
        PUT("put"),
        GET("get");
        private String method;

        private HttpMethod(String method) {
            this.method = method;
        }

        public String getMethod() {
            return this.method;
        }
    }

    public static String getTag() {
        return TAG;
    }

    public HashMap<String, String> getParams() {
        return mParams;
    }

    public HashMap<String, String> getHeader() {
        return mHeader;
    }

    public void setHeader(HashMap<String, String> mHeader) {
        this.mHeader = mHeader;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public IRequestListener<T> getListener() {
        return listener;
    }

    public void setListener(IRequestListener<T> listener) {
        this.listener = listener;
    }

    public boolean isUsingCache() {
        return mUsingCache;
    }

    public void setUsingCache(boolean mUsingCache) {
        this.mUsingCache = mUsingCache;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public int getSerialNum() {
        return mSerialNum;
    }

    public void setSerialNum(int mSerialNum) {
        this.mSerialNum = mSerialNum;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    /**
     * the priority about the requests in the queue;
     */
    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE;
    }

    protected int mSerialNum = 0x0001;
    private Priority priority = Priority.NORMAL;
    private boolean isCancel = false;
    private boolean mUsingCache = false;
    private IRequestListener<T> listener;
    private String mUrl;
    private HttpMethod httpMethod = HttpMethod.POST;
    private HashMap<String, String> mHeader = new HashMap<String, String>();
    private HashMap<String, String> mParams = new HashMap<String, String>();
    /**
     * Default Content-type
     */
    public final static String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String DEFAULT_PARAMS_ENCODING = "utf-8";

    public Request(HttpMethod httpMethod, String mUrl, IRequestListener listener) {
        this.httpMethod = httpMethod;
        this.mUrl = mUrl;
        this.listener = listener;
    }

    public void addHeader(String key, String value) {
        mHeader.put(key, value);
    }


    public abstract T parseResponse(Response response);

    public void deliveryResponse(Response response) {
        T result = parseResponse(response);
        if (listener != null) {
            if (response == null) {
                listener.onComplete(1001, null, null);
            } else {
                int code = response.getStatusCode();
                String message = response.getStatusMessage();
                Log.d(TAG, "the result:StatusCode:" + code + ",message:" + message);
                listener.onComplete(code, result, response);
            }
        }

    }

    @Override
    public int compareTo(Request<T> another) {
        Priority myPriority = this.getPriority();
        Priority anotherPriority = another.getPriority();
        // 如果优先级相等,那么按照添加到队列的序列号顺序来执行
        return myPriority.equals(anotherPriority) ? this.getSerialNum()
                - another.getSerialNum()
                : myPriority.ordinal() - anotherPriority.ordinal();
    }


    public interface IRequestListener<T> {
        public void onComplete(int statusCode, T result, Response response);
    }

    public byte[] getParamsBody() {
        if (getParams() != null) {
            return paramsToBytes(getParams());
        }
        return null;
    }

    private byte[] paramsToBytes(HashMap<String, String> params) {
        if (params == null)
            return null;
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                builder.append(URLEncoder.encode(entry.getKey(), getParamsEncoding()));
                builder.append('=');
                builder.append(URLEncoder.encode(entry.getValue()==null?"":entry.getValue(), getParamsEncoding()));
                builder.append('&');
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            if (Framework.isShowParams)
                Log.d(Framework.TAG, "params:" + builder.toString());
            return builder.toString().getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
