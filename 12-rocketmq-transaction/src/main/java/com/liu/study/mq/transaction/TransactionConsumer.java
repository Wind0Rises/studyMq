package com.liu.study.mq.transaction;

import com.liu.study.mq.common.utils.DateUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;
import java.util.List;

/**
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/12 16:00
 */
public class TransactionConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("transaction_group");

        consumer.setNamesrvAddr("127.0.0.1:9876");

        consumer.subscribe("transaction_topic", "*");

        consumer.setMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

                msgs.stream().forEach(item -> {
                    System.out.println("时间：" + DateUtils.parseDateToString(new Date()) + "  接收到的消息为：" + new String(item.getBody()));
                });

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
    }

}