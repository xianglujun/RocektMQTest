# 通过Filter方式消费消息

## 场景描述
In most cases, tag is a simple and useful design to select message you want. For example:

```java

DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CID_EXAMPLE");
consumer.subscribe("TOPIC", "TAGA || TAGB || TAGC");

```

消费者将会受到TAGA, TAGB, TAGC标签的数据，但是一条消息只能包含一个Tag,这种方式因此在比较复杂的场景不能很好的工作,
在这种复杂场景下，可以通过SQL表达式来进行过滤信息

## 规则
SQL特性，能够在你发送消息的时候，通过配置进行一些简单的计算。语法是通过RocketMQ进行定义。你能够实现一些
比较有意思的Topic.

|message|
|---|
|a = 10|
|b = 'abc'|
|c = true|

------> a > 5 and b = 'abc' GOTTEN

|message|
|---|
|a = 1|
|b = 'abc'|
|c = true|

------> a > 5 and b = 'abc' MISS

## 语法
RocketMQ定义了一些简单的语法来支持SQL特性，我们可以通过集成来简单的使用。
1. 数字比较 >, >=, <, <=, between, =
2. 字符比较: = 、<> 、IN
3. IS NULL OR IS NOT NULL
4. logic: AND, NOT, OR


## 类型常量:

1. Numeric, like 123, 3.1415;
2. Character, like ‘abc’, must be made with single quotes;
3. NULL, special constant;
4. Boolean, TRUE or FALSE;

## Usage constraints
Only push consumer could select messages by SQL92. The interface is:
```java
public void subscribe(final String topic, final MessageSelector messageSelector)
```

## Producer example
You can put properties in message through method putUserProperty when sending.
```java
DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
producer.start();

Message msg = new Message("TopicTest",
    tag,
    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
);
// Set some properties.
msg.putUserProperty("a", String.valueOf(i));

SendResult sendResult = producer.send(msg);
   
producer.shutdown();
```

## Consumer example
Use MessageSelector.bySql to select messages through SQL92 when consuming.
```java
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name_4");

// only subsribe messages have property a, also a >=0 and a <= 3
consumer.subscribe("TopicTest", MessageSelector.bySql("a between 0 and 3");

consumer.registerMessageListener(new MessageListenerConcurrently() {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
});
consumer.start();
```