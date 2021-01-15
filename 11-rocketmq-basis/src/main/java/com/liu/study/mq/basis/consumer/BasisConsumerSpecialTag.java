package com.liu.study.mq.basis.consumer;

import com.liu.study.mq.common.CommonConstants;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 第一示例：消息消费者。
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/10 13:27
 */
public class BasisConsumerSpecialTag {

    public static void main(String[] args) throws Exception {
        // 01、创建消息生产者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_group");

        // 02、添加namespace
        consumer.setNamesrvAddr("localhost:9876");

        consumer.subscribe(CommonConstants.FIRST_TOPIC, "lia || *");

        // 04、添加监听器。
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 05、启动消费者。
        consumer.start();

        System.out.println("---------消费者启动成功---------");

    }

}