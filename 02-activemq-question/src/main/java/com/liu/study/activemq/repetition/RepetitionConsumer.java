package com.liu.study.activemq.repetition;

/**
 * @desc
 *
 * 如何避免重复消费：
 *    1、生产者生产消息时为消息生成一个唯一ID，在消费者消费时，判断该唯一ID是否被消费了（可以把
 *       已经消费过的消息的ID保存到数据中）。
 *    2、把消费操作设计成幂等接口，重复消费对业务并没有影响。
 *
 *
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/6/3 9:20
 */
public class RepetitionConsumer {

}
