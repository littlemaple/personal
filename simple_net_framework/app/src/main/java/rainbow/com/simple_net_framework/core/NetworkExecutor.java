package rainbow.com.simple_net_framework.core;

import android.util.Log;

import org.apache.http.HttpStatus;

import java.util.concurrent.BlockingQueue;

import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.base.Response;
import rainbow.com.simple_net_framework.cache.LruMemberCache;
import rainbow.com.simple_net_framework.stack.HttpStack;

/**
 * Created by blue on 2015/7/6.
 */
public class NetworkExecutor extends Thread {

    public static final String TAG = "NetworkExecutor";
    private BlockingQueue<Request<?>> mQueue;
    private HttpStack stack;
    private ResponseDeliver deliver = new ResponseDeliver();
    private boolean isStop = false;
    private LruMemberCache responseCache = new LruMemberCache();

    public NetworkExecutor(BlockingQueue<Request<?>> mQueue, HttpStack stack) {
        this.mQueue = mQueue;
        this.stack = stack;
    }

    @Override
    public void run() {
        while (!isStop) {
            Log.d(TAG, "the looper is run?" + isStop);
            try {
                final Request<?> request = mQueue.take();
                if (request == null) {
                    Log.d(TAG, "the request is null");
                    continue;
                }
                if (request.isCancel()) {
                    Log.d(TAG, "the request is cancel");
                    continue;
                }
                Response response = null;
                if (isUseCache(request)) {
                    response = responseCache.get(request.getUrl());
                } else {
                    response = stack.performResult(request);
                    if (isSuccess(response) && request.isUsingCache()) {
                        responseCache.put(request.getUrl(), response);
                    }
                    deliver.deliverResponse(request, response);
                }
            } catch (InterruptedException e) {
                Log.d(TAG, "the thread is excepted");
                e.printStackTrace();
            }
        }

    }

    private boolean isSuccess(Response response) {
        if (response == null || response.getStatusCode() != HttpStatus.SC_OK)
            return false;
        return true;
    }

    private boolean isUseCache(Request<?> request) {
        if (request.isUsingCache() && responseCache.get(request.getUrl()) != null) {
            return true;
        }
        return false;
    }

    public void quit() {
        Log.d(TAG, "quite the thread");
        isStop = true;
        interrupt();
    }

}
