package com.learn.rocketmq.batch;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.rocketmq.common.message.Message;

/**
 * @author xianglujun
 * @datetime 2018/9/3 20:59
 */
public class LimitSpliter {

  private static final int DEFAULT_SIZE = 1000 * 1000;

  private int currIndex;

  private List<Message> messages;

  public LimitSpliter(List<Message> messages) {
    this.messages = messages;
  }

  public boolean hasNext() {
    return currIndex < messages.size();
  }

  public List<Message> next() {
    int nextIndex = currIndex;
    int totalSize = 0;
    for (; nextIndex < messages.size(); nextIndex++) {
      Message message = messages.get(nextIndex);
      int tempSize = message.getTopic().length() + message.getBody().length;

      Map<String, String> properties = message.getProperties();
      for (Entry<String, String> entry : properties.entrySet()) {
        tempSize += entry.getKey().length() + entry.getValue().length();
      }

      // 为日志存放信息
      tempSize += 20;
      System.out.println(tempSize);

      if (tempSize > DEFAULT_SIZE) {
        if (nextIndex - currIndex > 0 ) {
          nextIndex++;
        }

        break;
      }

      // 判断总的大小是否超过了显示
      if (tempSize + totalSize > DEFAULT_SIZE) {
        break;
      } else {
        totalSize += tempSize;
      }
    }

    // 返回消息的子集合
    List<Message> messages = this.messages.subList(currIndex, nextIndex);
    currIndex = nextIndex;
    return messages;
  }
}
