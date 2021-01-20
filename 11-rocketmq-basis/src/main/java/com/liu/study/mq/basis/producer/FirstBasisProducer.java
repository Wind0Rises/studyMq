package com.liu.study.mq.basis.producer;

import com.liu.study.mq.common.CommonConstants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.Scanner;

/**
 * 第一个示例：消息生产者。
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/10 13:26
 */
public class FirstBasisProducer {


    public static void main(String[] args) throws Exception {
        // 01、创建一个MQ生产者。
        DefaultMQProducer producer = new DefaultMQProducer("test_group");

        // 02、设置生产者的namespace地址。
        producer.setNamesrvAddr("127.0.0.1:9876");

        // 03、开启生产者。
        producer.start();

        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("请输入发送的消息：");

            String message = scanner.nextLine();

            // 04、创建消息。
            Message msg = new Message(CommonConstants.FIRST_TOPIC, "12", message.getBytes());

            /**
             * 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
             */
            // msg.setDelayTimeLevel(3);

            SendResult sendResult = producer.send(msg);

            /**
             * 发送单向消息，没有任何返回结果。
             */
            // producer.sendOneway(msg);



            System.out.printf("%s%n", sendResult);
        }
    }

}