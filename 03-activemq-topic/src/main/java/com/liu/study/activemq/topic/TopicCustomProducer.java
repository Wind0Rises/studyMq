package com.liu.study.activemq.topic;

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
 * @desc  无法保证每条数据都被消费者消费。
 *        如果一个消息如果被多个消费者消息，就会出队的消息，就是消费者 * 消息数。
 *
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/6/3 11:36
 */
public class TopicCustomProducer {

    public void producer() throws Exception {

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
        ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
        ActiveMQSession session = (ActiveMQSession) connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);

        /**
         * ####################################
         * #               TOPIC模式          #
         * ####################################
         */
        Destination destination = session.createTopic(CommonConstants.FIRST_TOPIC_NAME);
        ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(destination);
        /**
         * ####################################
         * #                持久化             #
         * ####################################
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

            TimeUnit.SECONDS.sleep(1000);
        }
    }

    public static void main(String[] args) throws Exception {
        new TopicCustomProducer().producer();
    }
}
