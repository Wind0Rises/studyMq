package com.liu.study.mq.filter;

import com.google.common.collect.Lists;
import com.liu.study.mq.common.utils.DateUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/11 16:50
 */
public class FilterProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("filter");

        producer.setNamesrvAddr("127.0.0.1:9876");

        producer.start();

        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.printf("输入信息为：");


            IntStream.range(0, 10);

            String inputMessage = scanner.nextLine();

            Message message = new Message("test_filter", (inputMessage + "  发送时间：" + DateUtils.parseDateToString(new Date())).getBytes());



            producer.send(message);

        }
    }
}