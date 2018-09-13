package com.learn.rocketmq.openmessaging;

import com.learn.rocketmq.constant.MqConstants;
import io.openmessaging.Message;
import io.openmessaging.MessagingAccessPoint;
import io.openmessaging.OMS;
import io.openmessaging.producer.Producer;
import io.openmessaging.producer.SendResult;
import java.io.UnsupportedEncodingException;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 该实例实践了，如果通过RocketMQ发送同步，异步，以及One-Way消息
 *
 * @author xianglujun
 * @datetime 2018/9/13 15:59
 */
public class OMSProducer {

  public static void main(String[] args) throws UnsupportedEncodingException {
    String url = "openmessaging:rocketmq://" + MqConstants.MQ_NAME_SRV_ADDRESS + "/namespace";
    MessagingAccessPoint messagingAccessPoint = OMS.getMessagingAccessPoint(url);

    // 创建生产者
    Producer mqProducer = messagingAccessPoint.createProducer();

    // 启动OMS
    messagingAccessPoint.startup();

    System.out.printf("Messaging access point start up OK %n");

    // 启动消息生产者
    mqProducer.startup();
    System.out.printf("Producer startup ok %n");

    // 同步发送消息
    {
      Message message =
          mqProducer.createBytesMessage("TopicTest",
              "OMS MESSSAGE".getBytes(RemotingHelper.DEFAULT_CHARSET));

      // 发送消息
      SendResult sendResult = mqProducer.send(message);
      System.out.printf("send sync message success, messageId: %d", sendResult.messageId());
    }
  }
}
