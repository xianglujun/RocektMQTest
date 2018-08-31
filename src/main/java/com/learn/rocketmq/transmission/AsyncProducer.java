package com.learn.rocketmq.transmission;

import com.learn.rocketmq.constant.MqConstants;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * 异步沈婵真模式:
 *  <p>对于返回时间有铭感的要求的业务场景</p>
 * @author xianglujun
 * @datetime 2018/8/31 14:14
 */
public class AsyncProducer {

    public static void main(String[] args) throws UnsupportedEncodingException, RemotingException, MQClientException, InterruptedException {
        // 初始化生产者组名称
        DefaultMQProducer producer = new DefaultMQProducer("AsyncProducerGroup");

        // 加载实例
        producer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        // 设置异步模式下，发送失败的，重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);

        for (int i = 0; i < 100; i++) {
            final int index = i;

            // 创建消息实例，指定topic,tag 以及message body
            Message msg = new Message("TopicTest",
                    "TagA",
                    "OrderID188",
                    "Hello World".getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 生产者发送消息,异步发送消息，发送后立即返回，返回的结果调用sendResult对象的方法
            producer.send(msg, new SendCallback() {
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }

                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }

        // 当生产者不在被使用的时候，关闭生产者
        producer.shutdown();
    }
}
