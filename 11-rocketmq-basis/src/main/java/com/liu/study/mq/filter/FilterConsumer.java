package com.liu.study.mq.filter;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 *
 * 通过{@link MessageSelector}设计过滤条件。
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/11 16:49
 */
public class FilterConsumer {


    public static void main(String[] args) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("filter-group");

        consumer.setNamesrvAddr("127.0.0.1:9876");

        /**
         * CODE: 1  DESC: The broker does not support consumer to filter message by SQL92
         * 解决方法：
         *      需要在broker.conf中添加enablePropertyFilter = true，即开始配置过滤。
         *      启动的时候要指定broker.conf.
         */
        consumer.subscribe("filter-topic", MessageSelector.bySql("column between 0 and 6"));

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                msgs.forEach(item -> {
                    System.out.println("接收到的消息为：" + new String(item.getBody()));
                });
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
    }

}