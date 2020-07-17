package com.liu.study.mq.transaction.consumer;

import com.liu.study.activemq.common.CommonConstants;
import org.apache.activemq.*;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import javax.jms.Message;

/**
 * @desc
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/6/4 9:43
 */
public class TransactionConsumer {

    public void consumer(boolean isCommit) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
        ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
        /**
         * 消费端要手动打开连接。
         */
        connection.start();

        System.out.println("Connection 是否是异步：" + connection.isUseAsyncSend());

        ActiveMQSession session = (ActiveMQSession) connection.createSession(Boolean.TRUE, Session.SESSION_TRANSACTED);

        Destination destination = session.createQueue(CommonConstants.SECOND_QUEUE_TRANSACTION_NAME);
        ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer) session.createConsumer(destination);

        /**
         * consumer.receive()：是同步的。
         * consumer.setMessageListener(..)：是异步的。
         */

        consumer.setMessageListener((Message message) -> {
            try {
                String textMessage = ((ActiveMQTextMessage) message).getText();
                System.out.println("=======  消费者信息为：" + textMessage);
                session.commit();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        openTransactionAndAsync();
    }

    public static void openTransactionAndAsync() throws Exception {

        /**
         * 方法一、二可以看出如果开启了事务，消费者不去commit，broker就不会出队。
         */
        new TransactionConsumer().consumer(Boolean.FALSE);

        new TransactionConsumer().consumer(Boolean.TRUE);
    }

}
