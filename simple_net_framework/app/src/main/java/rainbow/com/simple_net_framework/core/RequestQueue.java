package rainbow.com.simple_net_framework.core;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.stack.HttpNormalStack;
import rainbow.com.simple_net_framework.stack.HttpStack;

/**
 * Created by blue on 2015/7/6.
 */
public final class RequestQueue {

    /**
     * 请求队列
     */
    private BlockingQueue<Request<?>> mQueue = new PriorityBlockingQueue<Request<?>>();

    private AtomicInteger mSerialGenerate = new AtomicInteger(0);
    public static final int DEFAULT_CORE_NUM = Runtime.getRuntime().availableProcessors() + 1;
    private int dispatcherNum = 1;
    NetworkExecutor[] mDispatchers = null;
    private HttpStack stack;


    public RequestQueue(int coreNum, HttpStack stack) {
        this.stack = stack == null ? new HttpNormalStack() : stack;
        this.dispatcherNum = coreNum;
    }

    public void start() {
        stop();
        startNetworkStack();
    }

    public void stop() {
        Log.d("NetworkExecutor","stop");
        if (mDispatchers == null || mDispatchers.length == 0)
            return;
        for (int i = 0; i < mDispatchers.length; i++) {
            mDispatchers[i].quit();
        }
    }

    private void startNetworkStack() {
        mDispatchers = new NetworkExecutor[dispatcherNum];
        for (int i = 0; i < dispatcherNum; i++) {
            mDispatchers[i] = new NetworkExecutor(mQueue, stack);
            mDispatchers[i].start();
        }
    }

    public void addRequest(Request<?> request) {
        if (mQueue == null) {
            Log.e(RequestQueue.class.getSimpleName(), "please the request queue initial");
            return;
        }
        if (!mQueue.contains(request)) {
            request.setSerialNum(generateSerial());
            mQueue.add(request);
        } else {
            Log.d(Request.class.getSimpleName(), "the request has been exist in the queue,will not add the queue");
        }
    }

    private int generateSerial() {
        return mSerialGenerate.incrementAndGet();
    }
}
