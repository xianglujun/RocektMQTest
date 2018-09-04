package com.learn.rocketmq.shedule;

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
 * 计划消息不同于普通的消息，计划消息递送消息会在指定之间之后才会发送消息
 *
 * @author xianglujun
 * @datetime 2018/9/3 20:05
 */
public class ScheduleProducer {

  public static void main(String[] args)
      throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
    // 初始化消息生产对象
    DefaultMQProducer producer = new DefaultMQProducer("ScheduleProducer");
    producer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);
    producer.start();

    int totalMessageSends = 100;
    for (int i = 0; i < totalMessageSends; i++) {
      Message msg = new Message("ScheduledTopic", ("Scheduled Message"+ i).getBytes(RemotingHelper.DEFAULT_CHARSET));

      // 设置消息延迟10秒后发送
      msg.setDelayTimeLevel(3);
      SendResult sendResult = producer.send(msg);

      System.out.println(sendResult);
    }

    producer.shutdown();
  }
}
