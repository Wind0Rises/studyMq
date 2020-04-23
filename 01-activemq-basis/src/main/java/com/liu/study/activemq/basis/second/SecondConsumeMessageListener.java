package com.liu.study.activemq.basis.second;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @desc
 *
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 14:58
 */
public class SecondConsumeMessageListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(SecondConsumeMessageListener.class);

    @Override
    public void onMessage(Message message) {

        /**
         * 如果使用的无事务，并且使用的是Client_Acknowledge，如果没有下面代码，消息是不会出队的。
         */
        try {
            message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        logger.info("【消费者】【FirstConsumeMessageListener】-----> " + message.toString());
    }
}
