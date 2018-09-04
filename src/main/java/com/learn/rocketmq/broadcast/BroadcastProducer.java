package com.learn.rocketmq.broadcast;

import com.learn.rocketmq.constant.MqConstants;
import java.io.UnsupportedEncodingException;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 广播发送message:
 * <p>
 *     广播适用于发送消息给订阅了同一个topic的消费者，如果你希望所有的订阅者都能够收到topic消息，
 *     广播是一个好的选择
 * </p>
 *
 * @author xianglujun
 * @datetime 2018/8/31 18:17
 */
public class BroadcastProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("BroadCastingGroupName");
        producer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);
        producer.setInstanceName("BroadCastInstance");
        producer.start();

        for (int i = 0; i < 1000; i++) {
            Message msg = new Message("TopicBroadcasting",
                    "TagA",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));

            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }

        producer.shutdown();
    }

}
