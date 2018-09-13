package com.learn.rocketmq.transaction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author xianglujun
 * @datetime 2018/9/13 17:05
 */
public class TransactionListenerImpl implements TransactionCheckListener, LocalTransactionExecuter {

  AtomicInteger transCount = new AtomicInteger();
  Map<String, Integer> localTrans = new ConcurrentHashMap<String, Integer>();

  public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
    System.err.printf("check local message, msg=[%s] %n", msg);

    String transactionId = msg.getBuyerId();
    int rest = localTrans.get(transactionId);

    if (rest == 0) {
      return LocalTransactionState.UNKNOW;
    } else if (rest == 1) {
      return LocalTransactionState.COMMIT_MESSAGE;
    } else if (rest == 2) {
      return LocalTransactionState.ROLLBACK_MESSAGE;
    }

    return LocalTransactionState.COMMIT_MESSAGE;
  }

  public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
    // 判断当前的消息序列，并判断消息的状态
    int count = transCount.incrementAndGet();
    int rest = count * 3;
    String transactionId = msg.getBuyerId();

    localTrans.put(transactionId, rest);
    return LocalTransactionState.UNKNOW;
  }
}
