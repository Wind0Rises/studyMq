package com.liu.study.activemq.basis.first;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @desc
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 14:58
 */
public class FirstConsumeMessageListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(FirstConsumeMessageListener.class);

    @Override
    public void onMessage(Message message) {
        logger.info("【消费者】【FirstConsumeMessageListener】-----> " + message.toString());
    }
}
