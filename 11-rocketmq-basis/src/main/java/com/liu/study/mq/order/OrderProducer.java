package com.liu.study.mq.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

/**
 * 顺序消息--消息生产者。
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/11 11:47
 */
public class OrderProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test_group");

        producer.setNamesrvAddr("127.0.0.1:9876");

        producer.start();

        String[] tags = new String[]{"创建订单", "支付", "发货", "收货", "五星好评"};

        for (int i = 5; i < 25; i++) {
            int orderId = i / 5;

            Message message = new Message("order_topic", tags[i % tags.length], "KEY" + i,
                    ("OrderNo：" + orderId + "---" + tags[i % tags.length]).getBytes(RemotingHelper.DEFAULT_CHARSET));

            System.out.println("------------------------" + new String(message.getBody(), "UTF-8"));
            SendResult sendResult = producer.send(message, (mqs, msg, arg) -> {
                // 这个时候arg == orderId
                Integer id = (Integer) arg;
                int index = id % mqs.size();
                return mqs.get(index);
            }, orderId);

            System.out.printf("%s%n", sendResult);
        }

        producer.shutdown();
    }

}