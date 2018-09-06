package com.learn.rocketmq.filter;

import com.learn.rocketmq.constant.MqConstants;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author xianglujun
 * @datetime 2018/9/4 12:36
 */
public class FilterProducer {

  public static void main(String[] args)
      throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
    // 创建生产内容
    DefaultMQProducer producer = new DefaultMQProducer("FilterProducer");
    // 设置namesrvAddr
    producer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);

    // 启动生产者
    producer.start();

    // 发送消息
    for (int i = 0; i < 100; i++) {
      Message msg = new Message("FilterTopic", "TagA", ("FilterMessage" + i).getBytes());
      msg.putUserProperty("index", String.valueOf(i));
      SendResult sendResult = producer.send(msg);
      System.out.println(sendResult);
    }

    producer.shutdown();
  }
}
