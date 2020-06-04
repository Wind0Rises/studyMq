package com.liu.study.mq.transaction.producer;

import com.liu.study.activemq.common.CommonConstants;
import org.apache.activemq.*;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @desc  producer  <===>  broker之间的事务。
 *
 *
 * @author Liuweian
 * @createTime 2020/6/3 22:42
 * @version 1.0.0
 */
public class TransactionProducer {

    public void producer() throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(CommonConstants.COMPANY_SINGLETON_ADDRESS);
        ActiveMQConnection connection = (ActiveMQConnection) connectionFactory.createConnection();
        ActiveMQSession session = (ActiveMQSession) connection.createSession(Boolean.TRUE, Session.SESSION_TRANSACTED);

        Destination destination = session.createQueue(CommonConstants.SECOND_QUEUE_TRANSACTION_NAME);
        ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(destination);

        ActiveMQTextMessage message = new ActiveMQTextMessage();
        message.setText("This is ActiveMQTextMessage");

        producer.send(message, new AsyncCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onException(JMSException exception) {

            }
        });

        //producer.
    }


}
