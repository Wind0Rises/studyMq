package com.liu.study.activemq.discard;

import com.liu.study.activemq.common.CommonConstants;
import com.liu.study.activemq.common.MessageUtils;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Destination;
import javax.jms.TextMessage;
import java.util.concurrent.TimeUnit;

/**
 * @desc 1、什么是异步发送消息丢失？
 *          由于消息不阻塞，生产者会认为所有send的消息均被成功发送至MQ。如果服务端突然宕机，
 *          此时生产者端内存中尚未被发送至MQ的消息都会丢失。
 *
 *       2、如何解决
 *          在发送消息时，设置回调函数。
 *
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/6/3 10:09
 */
public class AsyncSendLostMessageProducer {

    private static Logger logger = LoggerFactory.getLogger(AsyncSendLostMessageProducer.class);

    public void createNonTransactionProducerAndAsync(int acknowledgeMode) throws Exception {

        ActiveMQConnectionFactory connectionFactory = null;
        ActiveMQConnection connection = null;
        ActiveMQSession session = null;

        try {
            /** 1、创建连接并开启连接 */
            connectionFactory= new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
            connectionFactory.setUseAsyncSend(true);

            connection = (ActiveMQConnection) connectionFactory.createConnection();
            connection.start();
            logger.info("【FirstProducer】----> 生产者连接成功！");


            /** 2、创建Session，并通过session创建Destination。注意模式的选择。 */
            session = (ActiveMQSession) connection.createSession(Boolean.FALSE, acknowledgeMode);
            Destination destination = session.createQueue(CommonConstants.SECOND_QUEUE_NON_TRANSACTION_NAME_ASYNC);
            logger.info("【FirstProducer】----> 创建Destination成功！");


            /** 3、消息生产者 */
            ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(destination);
            /**
             * 设置消息在服务端队里中生存的时间，如果在指定时间内没有消息没有被消费，消息将被移除队列，并
             * 添加到死信队列中。
             * 如果有事务的话这个存活时间是事务提价以后开始算。
             */
            producer.setTimeToLive(600);


            /** 4、构建消息并发送消息 */
            while (true) {
                TextMessage message = session.createTextMessage(MessageUtils.getMessage("SECOND_NO_TRANSACTION_ASYNC"));

                /** ######################################################
                 *  #                   添加回调类。                      #
                 *  ######################################################
                 */
                producer.send(message, new CustomAsyncCallback());
                logger.info("生产者发送数据【成功】  --> {}", message.getText());

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
