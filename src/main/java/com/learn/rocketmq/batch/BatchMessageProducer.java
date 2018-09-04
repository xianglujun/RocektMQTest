package com.learn.rocketmq.batch;

import com.learn.rocketmq.constant.MqConstants;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 批量消息生产，单次批量消息发送建议不超过1M,如果超过1M，建议分割消息进行发送
 *
 * @author xianglujun
 * @datetime 2018/9/3 20:51
 */
public class BatchMessageProducer {

  public static void main(String[] args)
      throws UnsupportedEncodingException, MQClientException, RemotingException, InterruptedException, MQBrokerException {
    DefaultMQProducer producer = new DefaultMQProducer("BATCH_PRODUCER");

    producer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);
    producer.start();

    String topic = "ScheduledTopic";

    List<Message> msgs = new ArrayList<Message>(1000);
    for (int i = 0, len = 1000000; i < len; i++) {
      Message message = new Message(topic, "TagA", ("BatchMessage "+ i).getBytes(RemotingHelper.DEFAULT_CHARSET));
      msgs.add(message);
    }

    LimitSpliter limitSpliter = new LimitSpliter(msgs);
    int count = 0;
    while (limitSpliter.hasNext()) {
      List<Message> messages = limitSpliter.next();
      SendResult sendResult = producer.send(messages);
      System.out.println(sendResult);
      count++;
    }

    System.out.printf("一共循环%d次", count);
    producer.shutdown();
  }

}
