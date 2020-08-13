package com.liu.study.mq.basis.producer;

import com.liu.study.mq.common.CommonConstants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;

import java.sql.Time;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 第二示例：异步发送消息。
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/10 19:58
 */
public class SecondAsyncProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test_group");

        producer.setNamesrvAddr("127.0.0.1:9876");

        producer.start();

        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 20;

        CountDownLatch2 countDownLatch = new CountDownLatch2(20);

        for (int i = 0; i < messageCount; i++) {

            TimeUnit.MILLISECONDS.sleep(6000);

            final int index = i;

            Message message = new Message(CommonConstants.FIRST_TOPIC, ("second message " + i).getBytes());

            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    /**
                     * 消息发送broker成功以后回调。sendResult发送结果。
                     */
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }

        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }
}