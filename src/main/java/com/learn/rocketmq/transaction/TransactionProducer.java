package com.learn.rocketmq.transaction;

import com.learn.rocketmq.constant.MqConstants;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 生产与事务有关的消息
 *
 * @author xianglujun
 * @datetime 2018/9/13 17:04
 */
public class TransactionProducer {

  public static void main(String[] args)
      throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
    TransactionMQProducer producer = new TransactionMQProducer("producer_transaction");
    producer.setNamesrvAddr(MqConstants.MQ_NAME_SRV_ADDRESS);

    // 指定线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(200),
        new ThreadFactory() {
          public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("producer_thread_");
            return thread;
          }
        }, new DiscardOldestPolicy());

    // 启动生产者
    producer.start();

    TransactionListenerImpl transactionListener = new TransactionListenerImpl();
    producer.setTransactionCheckListener(transactionListener);
    producer.setCallbackExecutor(executor);

    String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
    for (int i = 0, len = 10; i < len; i++) {
      Message message = new Message("TopicTransaction", tags[i % tags.length],
          ("message body_" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

      SendResult sendResult = producer.sendMessageInTransaction(message, transactionListener, null);
      System.out.printf("send result success, %s %n", sendResult);

      Thread.sleep(10);
    }

    producer.shutdown();
  }
}
