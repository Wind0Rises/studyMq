package com.liu.study.mq.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 顺序消息--消息消费者
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/11 11:47
 */
public class OrderConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_group");

        consumer.setNamesrvAddr("127.0.0.1:9876");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.subscribe("order_topic", "*");

        consumer.setMessageListener(new MessageListenerOrderly() {

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                try {
                    System.out.println("orderInfo: " + new String(msgs.get(0).getBody(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return ConsumeOrderlyStatus.SUCCESS;

            }
        });

        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

}