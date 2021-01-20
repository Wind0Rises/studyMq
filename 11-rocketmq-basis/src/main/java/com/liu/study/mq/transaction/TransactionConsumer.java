package com.liu.study.mq.transaction;

/**
 *
 * 使用事务的限制：
 *      1. 事务消息不支持延时消息和批量消息。
 *      2. 为了避免单个消息被检查太多次而导致半队列消息累积，我们默认将单个消息的检查次数限制为15次，但是用户可以通过Broker配置文件的 transactionCheckMax参数来修改此限制。
 *         如果已经检查某条消息超过N次的话（N = transactionCheckMax）则Broker将丢弃此消息，并在默认情况下同时打印错误日志。用户可以通过重写 AbstractTransactionalMessageCheckListener
 *         类来修改这个行为。
 *      3. 事务消息将在Broker配置文件中的参数transactionTimeout这样的特定时间长度之后被检查。当发送事务消息时，用户还可以通过设置用户属性CHECK_IMMUNITY_TIME_IN_SECONDS来改变
 *         这个限制，该参数优先于transactionTimeout参数。
 *      4. 事务性消息可能不止一次被检查或消费。
 *      5. 提交给用户的目标主题消息可能会失败，目前这依日志的记录而定。它的高可用性通过 RocketMQ 本身的高可用性机制来保证，如果希望确保事务消息不丢失、并且事务完整性得到保证，建议使
 *         用同步的双重写入机制。
 *      6. 事务消息的生产者ID不能与其他类型消息的生产者ID共享。与其他类型的消息不同，事务消息允许反向查询、MQ服务器能通过它们的生产者ID查询到消费者。
 *
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2021/1/19 15:07
 */
public class TransactionConsumer {
}
