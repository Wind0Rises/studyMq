package com.liu.study.activemq.persistence;

import com.liu.study.activemq.common.CommonConstants;
import com.liu.study.activemq.common.MessageUtils;
import org.apache.activemq.*;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.concurrent.TimeUnit;

/**
 * @desc
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/6/3 15:10
 */
public class PersistenceConsumer {

    public void producer() throws Exception {

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
        ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
        ActiveMQSession session = (ActiveMQSession) connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);

        Destination destination = session.createTopic(CommonConstants.FIRST_TOPIC_NAME);
        ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(destination);
        /**
         * ####################################
         * #                持久化             #
         * ####################################
         * 默认创建的是持久化模式。
         */
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.setTimeToLive(100000);

        while (true) {
            ActiveMQTextMessage message = (ActiveMQTextMessage) session.createTextMessage(MessageUtils.getMessage("first_topic_message"));
            producer.send(message, new AsyncCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("=============  消息发送成功  =================");
                }

                @Override
                public void onException(JMSException exception) {
                    System.out.println("=============  消息发送失败  =================");
                }
            });

            TimeUnit.SECONDS.sleep(10);
        }
    }

    public static void main(String[] args) throws Exception {
        new PersistenceConsumer().producer();
    }

}
