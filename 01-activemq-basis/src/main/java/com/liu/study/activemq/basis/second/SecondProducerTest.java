package com.liu.study.activemq.basis.second;

import javax.jms.Session;

/**
 * @desc Session的模式处理。
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 15:40
 */
public class SecondProducerTest {

    /**
     * 创建Session的两个参数：transaction、acknowledgeMode
     *      transaction：true、false
     *      acknowledgeMode：AUTO_ACKNOWLEDGE(1)、CLIENT_ACKNOWLEDGE(2)、DUPS_OK_ACKNOWLEDGE(3)、SESSION_TRANSACTED(0)
     *
     *      transaction          acknowledgeMode
     *         false            SESSION_TRANSACTED              -->     异常
     *         false            AUTO_ACKNOWLEDGE                -->     可以
     *         false            CLIENT_ACKNOWLEDGE              -->     可以
     *         false            DUPS_OK_ACKNOWLEDGE             -->     可以
     *     ###################################################################
     *         true             SESSION_TRANSACTED              -->     可以（开启事务有且只有这一种模式）
     *         true             AUTO_ACKNOWLEDGE                -->     不使用
     *         true             CLIENT_ACKNOWLEDGE              -->     不使用
     *         true             DUPS_OK_ACKNOWLEDGE             -->     不使用
     *
     *    如果事务为true是，强制使用SESSION_TRANSACTED模式。
     */
    public static void main(String[] args) throws Exception {
        /** 1、开启事务 */
        // haveTransaction();

        //noTransactionAndAuto();

        noTransactionAndClient();
    }

    /**
     * 开启事务
     */
    public static void haveTransaction() throws Exception {
        SecondProducer producer = new SecondProducer();
        producer.createTransactionProducer();
    }

    /**
     * 不开启事务  +  AUTO_ACKNOWLEDGE
     */
    public static void noTransactionAndAuto() throws Exception {
        SecondProducer producer = new SecondProducer();
        producer.createNonTransactionProducer(Session.AUTO_ACKNOWLEDGE);
    }


    /**
     * 不开启事务  +  CLIENT_ACKNOWLEDGE
     */
    public static void noTransactionAndClient() throws Exception {
        SecondProducer producer = new SecondProducer();
        producer.createNonTransactionProducer(Session.CLIENT_ACKNOWLEDGE);
    }


}
