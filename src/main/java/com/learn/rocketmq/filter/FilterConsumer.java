package com.learn.rocketmq.filter;

import com.learn.rocketmq.constant.MqConstants;
import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author xianglujun
 * @datetime 2018/9/4 21:29
 */
public class FilterConsumer {

  public static void main(String[] args) throws MQClientException {
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("FilterConsumer");
    consumer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);

    consumer.subscribe("FilterTopic", MessageSelector.bySql("index >= 60"));
    consumer.registerMessageListener(new MessageListenerConcurrently() {
      public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
          ConsumeConcurrentlyContext context) {
        for (MessageExt ext : msgs) {
          System.out.println(ext);
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      }
    });

    consumer.start();
  }

}
