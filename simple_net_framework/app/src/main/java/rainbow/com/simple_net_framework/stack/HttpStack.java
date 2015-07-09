package rainbow.com.simple_net_framework.stack;

import rainbow.com.simple_net_framework.base.Request;
import rainbow.com.simple_net_framework.base.Response;

/**
 * Created by blue on 2015/7/6.
 */
public interface HttpStack {
    Response performResult(Request<?> request);
}
