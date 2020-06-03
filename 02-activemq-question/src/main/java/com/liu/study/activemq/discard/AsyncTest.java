package com.liu.study.activemq.discard;

import javax.jms.Session;

/**
 * @desc 测试类。
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/6/3 11:08
 */
public class AsyncTest {

    public static void main(String[] args) throws Exception {
        AsyncSendLostMessageProducer producer = new AsyncSendLostMessageProducer();
        producer.createNonTransactionProducerAndAsync(Session.CLIENT_ACKNOWLEDGE);
    }

}
