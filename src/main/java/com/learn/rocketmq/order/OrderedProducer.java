package com.learn.rocketmq.order;

import com.learn.rocketmq.constant.MqConstants;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author xianglujun
 * @datetime 2018/8/31 15:28
 */
public class OrderedProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        MQProducer producer = new DefaultMQProducer("OrderedProducerGroupName");
        ((DefaultMQProducer) producer).setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);

        producer.start();
        // 设置tab列表
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 100; i++) {
            int orderId = i % 10;
            // 创建消息体，指定topic, tap以及消息体
            Message msg = new Message("TopicTestOrdered", tags[i % tags.length],
                    "KEY" + i, ("Hello RocketMQ Ordered" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);

            System.out.printf("%s%n", sendResult);
        }

        // 关闭生产对象
        producer.shutdown();
    }
}
