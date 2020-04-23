package com.liu.study.activemq.basis.first;

import com.liu.study.activemq.basis.CommonConstants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

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
public class FirstConsumer {

    private static Logger logger = LoggerFactory.getLogger(FirstConsumer.class);

    public void createConsumer() throws Exception {
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
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(CommonConstants.FIRST_QUEUE_NAME);

            /** 3、消息消费者，并注册监听器。*/
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new FirstConsumeMessageListener());

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
