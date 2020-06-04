package com.liu.study.activemq.topic;

import com.liu.study.activemq.common.CommonConstants;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @desc
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/6/3 11:49
 */
public class TopicFirstConsumer {

    public void consumer() throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
        ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
        connection.start();
        ActiveMQSession session = (ActiveMQSession) connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
        Destination destination = session.createTopic(CommonConstants.FIRST_TOPIC_NAME);
        ActiveMQMessageConsumer consumer = (ActiveMQMessageConsumer) session.createConsumer(destination);

        consumer.setMessageListener((Message message) -> {

            try {
                System.out.println("==============>> Topic First Consumer: " + ((ActiveMQTextMessage) message).getText());
                message.acknowledge();
                System.out.println("==============>> 确认成功");
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new TopicFirstConsumer().consumer();
    }

}
