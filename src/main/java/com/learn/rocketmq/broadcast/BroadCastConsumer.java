package com.learn.rocketmq.broadcast;

import com.learn.rocketmq.constant.MqConstants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author xianglujun
 * @datetime 2018/8/31 18:22
 */
public class BroadCastConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("BroadcastConsumer");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 使用广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setInstanceName("BroadcastConsumer");
        consumer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);
        consumer.setConsumerGroup("Broadcast_consumer_group");

        consumer.subscribe("TopicBroadcasting", "TagA");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt : msgs) {
                    String messageBody = new String(messageExt.getBody());
                    //输出消息内容
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                        new Date())+"消费响应：msgId : " + messageExt.getMsgId() + ",  msgBody : " + messageBody);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.printf("Broadcast Consumer Started. %n");
    }
}
