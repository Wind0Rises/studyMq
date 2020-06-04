package com.liu.study.mq.transaction.producer;

import com.liu.study.activemq.common.CommonConstants;
import com.liu.study.activemq.common.MessageUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.activemq.*;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import javax.jms.Message;
import java.util.concurrent.TimeUnit;

/**
 * @desc  producer  <===>  broker之间的事务。
 *
 *        producer是堵塞的吗？？
 *
 * @author Liuweian
 * @createTime 2020/6/3 22:42
 * @version 1.0.0
 */
public class TransactionProducer {

    /**
     * 事务测试和持久化测试。
     * @param isCommit
     * @param persistence
     * @throws Exception
     */
    public void producer(boolean isCommit, int persistence, boolean  isAsync) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
        ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
        connection.setUseAsyncSend(isAsync);
        System.out.println("Connection 是否是异步：" + connection.isUseAsyncSend());

        ActiveMQSession session = (ActiveMQSession) connection.createSession(Boolean.TRUE, Session.SESSION_TRANSACTED);

        Destination destination = session.createQueue(CommonConstants.SECOND_QUEUE_TRANSACTION_NAME);
        ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(destination);
        System.out.println("持久化 是否开启：" + (DeliveryMode.PERSISTENT == persistence));
        producer.setDeliveryMode(persistence);

        while (true) {
            ActiveMQTextMessage message = new ActiveMQTextMessage();
            String messageText = MessageUtils.getMessage(CommonConstants.SECOND_QUEUE_TRANSACTION_NAME);
            message.setText(messageText);

            producer.send(message, new AsyncCallback() {
                @Override
                public void onSuccess() {
                    System.out.println(" ===========  消费发送成功  ====" + messageText);
                }

                @Override
                public void onException(JMSException exception) {
                    System.out.println(" ===========  消费发送失败  ====" + messageText);
                }
            });

            if(isCommit) {
                try {
                    session.commit();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

            TimeUnit.SECONDS.sleep(10);
        }
    }


    /**
     * 事务和持久化测试。
     */
    public static void main(String[] args) throws Exception {
        // openTransactionAndPersistence();
        openTransactionAndAsync();
    }

    /**
     * 开启事务，持久化模式、非异步连接
     * 如果启动了事务，但是没有提交的话，【持久化模式】，broker是无法持久化消息的，堆里里是没有消息。
     *
     * 同步发送时，Producer.send()方法会被阻塞，直到broker发送一个确认消息给生产者，这个确认消息
     * 暗示生产者broker已经成功地将它发送的消息路由到目标目的并把消息保存到二级存储中。
     * 如果这个时候开启事务，需要把手动commit，这是消息才会在broker中才会入队。
     */
    public static void openTransactionAndPersistence() throws Exception {
        /**
         * producer(是否commit, 持久化模式);
         *
         * 方法一和方法二：事务和持久化没有关系。开启了事务，如果没有commit()，broker是无法真正接收
         *      到消息的，虽然broker实际已经接收到消息，但是不会保存到队列中。
         *
         * 方法一和方法三：开启事务以后，如果生产者不手动commit，消息将不会入队。
         *
         * 方法三和方法四：做了持久化操作以后，activeMQ宕机以后，未消费的消息会恢复。
         */


        /**
         * 【持久化模式、非异步连接】
         * 如果启动了事务，但是没有提交的话，broker中没有消息。
         */
        //new TransactionProducer().producer(Boolean.FALSE, DeliveryMode.PERSISTENT, Boolean.TRUE);

        /**
         * 【非持久化模式、非异步连接】
         * 如果启动了事务，但是没有提交的话，broker中没有消息。
         */
        //new TransactionProducer().producer(Boolean.FALSE, DeliveryMode.NON_PERSISTENT, Boolean.TRUE);

        /**
         * 【持久化模式、非异步连接】
         * 如果启动了事务，只有提交了事务，broker中没有消息。activeMQ宕机重启，队列还在，未确认的
         * 消息消息中还有数据。
         */
        new TransactionProducer().producer(Boolean.TRUE, DeliveryMode.PERSISTENT, Boolean.TRUE);

        /**
         * 【非持久化模式、非异步连接】
         * 如果启动了事务，只有提交了事务，broker中没有消息。activeMQ宕机重启，整个队列就消息了
         * 数据丢失。
         */
        // new TransactionProducer().producer(Boolean.TRUE, DeliveryMode.NON_PERSISTENT, Boolean.TRUE);
    }


    /**
     *
     */
    public static void openTransactionAndAsync() throws Exception {
        new TransactionProducer().producer(Boolean.TRUE, DeliveryMode.PERSISTENT, Boolean.TRUE);

    }

}
