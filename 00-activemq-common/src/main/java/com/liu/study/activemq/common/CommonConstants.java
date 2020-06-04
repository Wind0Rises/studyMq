package com.liu.study.activemq.common;

/**
 * @desc 常量类。
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 14:52
 */
public class CommonConstants {

    private CommonConstants() {

    }

    public static final String COMPANY_SINGLETON_ADDRESS = "tcp://127.0.0.1:61616";

    public static final String FIRST_QUEUE_NAME = "queue_first";

    public static final String SECOND_QUEUE_TRANSACTION_NAME = "queue_second_transaction";

    public static final String SECOND_QUEUE_NON_TRANSACTION_NAME = "queue_second_no_transaction";

    public static final String SECOND_QUEUE_NON_TRANSACTION_NAME_ASYNC = "queue_second_no_transaction_async";

    public static final String FIRST_TOPIC_NAME = "first_topic";

}
