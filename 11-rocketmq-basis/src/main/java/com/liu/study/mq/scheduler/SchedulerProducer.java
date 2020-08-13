package com.liu.study.mq.scheduler;

import com.liu.study.mq.common.utils.DateUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Date;
import java.util.Scanner;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/11 15:17
 */
public class SchedulerProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("scheduler");

        producer.setNamesrvAddr("127.0.0.1:9876");

        producer.start();

        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("输入信息为：");

            String inputMessage = scanner.nextLine();

            Message message = new Message("test_scheduler", (inputMessage + "  发送时间：" + DateUtils.parseDateToString(new Date())).getBytes());

            /**
             * 延迟发送消息。
             *
             * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
             * 1  2  3   4   5  6  7  8  9....
             */
            message.setDelayTimeLevel(3);

            producer.send(message);

        }
    }

}