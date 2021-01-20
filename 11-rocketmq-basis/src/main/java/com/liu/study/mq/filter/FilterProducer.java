package com.liu.study.mq.filter;

import com.liu.study.mq.common.utils.DateUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * 通过{@link Message#putProperty(name, value)}方法进行操作。相对于使用name这个字段进行判断。
 *
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

            String inputMessage = scanner.nextLine();

            int value = new Random().nextInt(10);

            System.out.println("输入的信息为：" + inputMessage + value);

            Message message = new Message("filter-topic", "*",
                    (inputMessage + "  发送时间：" + DateUtils.parseDateToString(new Date()) + "   " + value).getBytes());

            message.putUserProperty("column", String.valueOf(value));

            producer.send(message);
        }
    }
}