package mcloud.medzone.com.processor.patch;

import android.os.Message;

/**
 * Created by blue on 2015/7/13.
 * 缓存相关配置信息，序列化，用于数据恢复
 * 返回处理后的消息
 *
 */
public interface  IMessageProcessor {
    public Message process();
}
