package com.liu.study.mq.batch;

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
 * @createTime 2020/8/11 16:12
 */
public class BatchProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("batch");

        producer.setNamesrvAddr("127.0.0.1:9876");

        producer.start();

        while (true) {
            Scanner scanner = new Scanner(System.in);

            List<Message> messages = Lists.newArrayList();
            IntStream.range(0, 3).forEach(item -> {
                    System.out.printf("输入信息为：");

                    String inputMessage = scanner.nextLine();

                    Message message = new Message("test_batch", (inputMessage + "  发送时间：" + DateUtils.parseDateToString(new Date())).getBytes());

                    messages.add(message);
                }
            );

            System.out.println("======== 批量发送消息 =============");
            producer.send(messages);

        }
    }

}