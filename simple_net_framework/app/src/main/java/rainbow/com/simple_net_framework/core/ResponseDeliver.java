package rainbow.com.simple_net_framework.core;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.base.Response;

/**
 * Created by blue on 2015/7/6.
 */
public class ResponseDeliver implements Executor {
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable command) {
        handler.post(command);
    }

    public void deliverResponse(final Request<?> request, final Response response) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                request.deliveryResponse(response);
            }
        };
        execute(runnable);
    }

}
