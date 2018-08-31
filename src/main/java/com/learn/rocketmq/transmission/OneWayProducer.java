package com.learn.rocketmq.transmission;

import com.learn.rocketmq.constant.MqConstants;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * one-way传输使用与需要稳健依赖性的场景，例如日志归集.
 *
 * @author xianglujun
 * @datetime 2018/8/31 14:33
 */
public class OneWayProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("OneWayProducerGroup");

        // 设置mqnamesrv服务器地址
        producer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);
        producer.start();

        for (int i = 0; i < 100; i++) {
            // 创建消息实例
            Message msg = new Message("TopicTest",
                    "TagB",
                    ("Hello World" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 调用消息发送方法，向brokers中的一个发送消息
            producer.sendOneway(msg);
        }

        // 当生产者不再使用的时候，应当关闭
        producer.shutdown();
    }
}
