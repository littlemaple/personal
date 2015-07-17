package mcloud.medzone.com.processor.patch;

import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by blue on 2015/7/13.
 */
public abstract class AbstractMeasureProcessor implements IMessageProcessor {

    public static final String TAG = "MeasureProcessor";
    private HashMap<String, Object> config = new HashMap<String, Object>();

    public AbstractMeasureProcessor(Message msg) {
        Log.d(TAG, "-->" + getClass().getSimpleName());
    }

    public <S extends Serializable> S getConfig(String key, S defaultValue) {
        if (config.containsKey(key)) {
            return (S) config.get(key);
        }
        return defaultValue;
    }

    public <S extends Serializable> void setConfig(String key, S value) {
        config.put(key, value);
    }

}
