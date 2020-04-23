package com.liu.study.activemq.basis.first;

import com.liu.study.activemq.basis.CommonConstants;
import com.liu.study.activemq.basis.MessageUtils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @desc 简单的创建过程。
 *
 *      1、创建连接并开启连接
 *      2、创建Session，并通过session创建Destination。注意模式的选择。
 *      3、消息生产者
 *      4、构建消息并发送消息
 *
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 14:49
 */
public class FirstProducer {

    private static Logger logger = LoggerFactory.getLogger(FirstProducer.class);

    public void createProducer() throws Exception {

        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;

        try {
            /** 1、创建连接并开启连接 */
            connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
            connection = connectionFactory.createConnection();
            connection.start();
            logger.info("【FirstProducer】----> 生产者连接成功！");


            /** 2、创建Session，并通过session创建Destination。注意模式的选择。 */
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(CommonConstants.FIRST_QUEUE_NAME);
            logger.info("【FirstProducer】----> 创建Destination成功！");


            /** 3、消息生产者 */
            MessageProducer producer = session.createProducer(destination);
            /**
             * 设置消息在服务端队里中生存的时间，如果在指定时间内没有消息没有被消费，消息将被移除队列，并
             * 添加到死信队列中。
             */
            producer.setTimeToLive(10000);


            /** 4、构建消息并发送消息 */
            while (true) {
                TextMessage message = session.createTextMessage(MessageUtils.getMessage("FIRST"));
                producer.send(message);
                logger.info("生产者发送数据【成功】  --> {}", message.getText());
                TimeUnit.SECONDS.sleep(30);
            }
        } finally {
            if (session != null) {
                session.close();
            }

            if(connection != null) {
                connection.close();
            }

        }
    }

}
