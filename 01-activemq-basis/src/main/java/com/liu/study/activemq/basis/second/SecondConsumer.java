package com.liu.study.activemq.basis.second;

import com.liu.study.activemq.basis.CommonConstants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * @desc
 *
 *      1、创建连接并开启连接
 *      2、创建Session，并通过session创建Destination。注意模式的选择。
 *      3、消息消费者，并注册监听器
 *      4、构建消息并发送消息
 *
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 15:04
 */
public class SecondConsumer {

    private static Logger logger = LoggerFactory.getLogger(SecondConsumer.class);

    /**
     * 如果消费者开启了事务，消息监听器是可以接收到信息，但是不会从队列中移除。
     * 必须要提交以后才能出队。
     */
    public void createTransactionConsumer() throws Exception {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;

        try {
            /** 1、创建连接并开启连接 */
            connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
            connection = connectionFactory.createConnection();
            connection.start();
            logger.info("【FirstConsumer】----> 消费者连接成功！");


            /** 2、创建Session，并通过session创建Destination。注意模式的选择。 */
            session = connection.createSession(Boolean.TRUE, Session.SESSION_TRANSACTED);
            Destination destination = session.createQueue(CommonConstants.SECOND_QUEUE_TRANSACTION_NAME);

            /** 3、消息消费者，并注册监听器。*/
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new SecondConsumeMessageListener());
            logger.info("》》》》》数据已经接收到了，事务没有提交，消息没有出队。《《《《《");

            TimeUnit.SECONDS.sleep(30);

            /**
             * 如果消费者开启了事务，消息监听器是可以接收到信息，但是不会从队列中移除。
             * 必须要提交以后才能出队。
             */
            session.commit();
            logger.info("》》》》》事务提交，消息出队。《《《《《");

            Thread.sleep(Integer.MAX_VALUE);
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
     * 无事务处理。
     */
    public void createNonTransactionConsumer(int acknowledgeMode) throws Exception {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;

        try {
            /** 1、创建连接并开启连接 */
            connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
            connection = connectionFactory.createConnection();
            connection.start();
            logger.info("【FirstConsumer】----> 消费者连接成功！");


            /** 2、创建Session，并通过session创建Destination。注意模式的选择。 */
            session = connection.createSession(Boolean.TRUE, acknowledgeMode);
            Destination destination = session.createQueue(CommonConstants.SECOND_QUEUE_NON_TRANSACTION_NAME);

            /** 3、消息消费者，并注册监听器。*/
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new SecondConsumeMessageListener());

            Thread.sleep(Integer.MAX_VALUE);
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
