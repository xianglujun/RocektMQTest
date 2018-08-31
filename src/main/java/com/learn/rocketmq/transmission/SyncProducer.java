package com.learn.rocketmq.transmission;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * 以下实例实现了可靠同步传输的实现.
 * <p>
 *     同步传输可以用于 重要的通知消息，短信通知, 短信营销等场景
 * </p>
 *
 * @author xianglujun
 * @datetime 2018/8/30 14:04
 */
public class SyncProducer {

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
        // 初始化生产者组名称
        DefaultMQProducer producer = new DefaultMQProducer("MyOwnGroupName");
        String nameSrvAddr = "192.168.59.201:9876";
        // 启动实例
        try {
            producer.setNamesrvAddr(nameSrvAddr);
//            transmission.setCreateTopicKey("TopicTest");
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 100; i++) {
            // 创建消息实例,指定主题，标签，以及消息体
            Message msg = new Message("TopicTest",
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }

        // 当生产实体不再使用的时候, 就应该关闭
        producer.shutdown();
    }
}
