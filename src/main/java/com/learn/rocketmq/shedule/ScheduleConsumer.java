package com.learn.rocketmq.shedule;

import com.learn.rocketmq.constant.MqConstants;
import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 客户端用于消息费消息，该消息会在消息生产后，10秒之后发送
 *
 * @author xianglujun
 * @datetime 2018/9/3 20:12
 */
public class ScheduleConsumer {

  public static void main(String[] args) throws MQClientException {
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerScheduled");
    consumer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);
    consumer.subscribe("ScheduledTopic", "*");

    // 注册消息监听器
    consumer.registerMessageListener(new MessageListenerConcurrently() {
      public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
          ConsumeConcurrentlyContext context) {
        for (int i = 0, len = msgs.size(); i < len; i++) {
          MessageExt messageExt = msgs.get(i);
          System.out.printf("[%s] %d ms later, %s %n", messageExt.getMsgId(), System.currentTimeMillis() - messageExt.getStoreTimestamp(), messageExt);
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      }
    });

    consumer.start();
  }

}
