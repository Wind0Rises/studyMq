package com.liu.study.activemq.basis.second;

import com.liu.study.activemq.basis.CommonConstants;
import com.liu.study.activemq.basis.MessageUtils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * @desc 简单的创建过程。
 *
 *      1、创建连接并开启连接
 *      2、创建Session，并通过session创建Destination。注意模式的选择。
 *      3、消息生产者
 *
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 14:49
 */
public class SecondProducer {

    private static Logger logger = LoggerFactory.getLogger(SecondProducer.class);

    /**
     * 开启事务，开启事务以后模式，配不配都会使用SESSION_TRANSACTED。
     * 数据要可以多条提交。
     */
    public void createTransactionProducer() throws Exception {

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
            session = connection.createSession(Boolean.TRUE, Session.SESSION_TRANSACTED);
            Destination destination = session.createQueue(CommonConstants.SECOND_QUEUE_TRANSACTION_NAME);
            logger.info("【FirstProducer】----> 创建Destination成功！");


            /** 3、消息生产者 */
            MessageProducer producer = session.createProducer(destination);
            /**
             * 设置消息在服务端队里中生存的时间，如果在指定时间内没有消息没有被消费，消息将被移除队列，并
             * 添加到死信队列中。
             * 如果有事务的话这个存活时间是事务提价以后开始算。
             */
            producer.setTimeToLive(6000000);


            int i = 0;
            /** 4、构建消息并发送消息 */
            while (true) {
                TextMessage message = session.createTextMessage(MessageUtils.getMessage("SECOND_NO_TRANSACTION"));
                producer.send(message);
                logger.info("生产者发送数据【成功】{}  --> {}", i, message.getText());
                TimeUnit.SECONDS.sleep(1);

                /**
                 * 如果开启事务，消息生产者只发送，不提交，服务端是没有这条数据的，只有提交了，服务端才能真正的接收到数据。
                 * 数据是没有办法【【入队的】】
                 */
                i++;
                if (i%2 == 0) {
                    logger.info("提交：{}", i);
                    session.commit();
                }

                logger.info("生产者事务提交【成功】{}  --> {}", i - 1, message.getText());
                TimeUnit.SECONDS.sleep(15);
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


    /**
     *
     */
    public void createNonTransactionProducer(int acknowledgeMode) throws Exception {

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
            session = connection.createSession(Boolean.TRUE, acknowledgeMode);
            Destination destination = session.createQueue(CommonConstants.SECOND_QUEUE_NON_TRANSACTION_NAME);
            logger.info("【FirstProducer】----> 创建Destination成功！");


            /** 3、消息生产者 */
            MessageProducer producer = session.createProducer(destination);
            /**
             * 设置消息在服务端队里中生存的时间，如果在指定时间内没有消息没有被消费，消息将被移除队列，并
             * 添加到死信队列中。
             * 如果有事务的话这个存活时间是事务提价以后开始算。
             */
            producer.setTimeToLive(6000000);


            /** 4、构建消息并发送消息 */
            while (true) {
                TextMessage message = session.createTextMessage(MessageUtils.getMessage("SECOND_NO_TRANSACTION"));
                producer.send(message);
                logger.info("生产者发送数据【成功】  --> {}", message.getText());

                if (acknowledgeMode == Session.CLIENT_ACKNOWLEDGE) {
                    TimeUnit.SECONDS.sleep(15);
                    session.commit();
                    logger.info("生产者发送数据【客户端确认】  --> {}", message.getText());
                    TimeUnit.SECONDS.sleep(50000);
                }
                TimeUnit.SECONDS.sleep(20);
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
