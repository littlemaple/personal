package rainbow.com.simple_net_framework.proxy;

import java.util.List;

import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.core.RequestQueue;
import rainbow.com.simple_net_framework.stack.HttpStack;

/**
 * Created by blue on 2015/7/6.
 */
public class NetProxy {
    public static RequestQueue newRequestQueue(int coreNums, HttpStack stack) {
        RequestQueue mQueue = new RequestQueue(coreNums, stack);
        mQueue.start();
        return mQueue;
    }

    public static RequestQueue newRequestQueue(int coreNum) {
        return newRequestQueue(coreNum, null);
    }

    public static RequestQueue newRequestQueue() {
        return newRequestQueue(1);
    }

    public static void startRequest(Request<?> request) {
        RequestQueue mQueue = newRequestQueue();
        mQueue.addRequest(request);
    }

    public static void startRequest(List<Request<?>> list) {
        if (list == null)
            return;
        RequestQueue mQueue = newRequestQueue();
        for (Request<?> request : list) {
            mQueue.addRequest(request);
        }

    }


}
