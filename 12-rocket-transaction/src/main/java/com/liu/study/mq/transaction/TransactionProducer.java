package com.liu.study.mq.transaction;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 事务模式--消息发送者
 *
 * 注意：事务是不支持延迟和批量发送的。
 *
 * 好的博客：
 *      https://www.cnblogs.com/huangying2124/p/11702761.html
 *
 * @author lwa
 * @version 1.0.0
 * @createTime 2020/8/12 13:06
 */
public class TransactionProducer {



    /**
     * 事务的二阶段提交；
     *      1：MQ发送方，发送half消息。
     *      2：MQ接收方：返回给发送方，half消费发送成功。
     *      3：提交事务或者回滚。
     *
     */
    public static void main(String[] args) throws Exception {

        /**
         * 事务监听器。
         *
         *
         *
         * 举例：用户下单成功以后，要送积分；
         *      1、下单成功以后，发送积分。
         *
         */
        TransactionListener transactionListener = new TransactionListener() {

            private AtomicInteger transactionIndex = new AtomicInteger(0);

            private Object object = new Object();

            private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>(8);

            private  CopyOnWriteArraySet set = new CopyOnWriteArraySet(new HashSet(8));


            /**
             * 当发送方发送half消息成功以后，这个方法会别调用。
             *
             * 【【执行本地事务】】
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                System.out.printf("MQ发送方，发送half消息【成功】，开始【执行本地事务】\n");

                Order order = JSONObject.parseObject(new String(msg.getBody()), Order.class);
                System.out.println("=================  进行下单操作  ====================");


                if (order.getStatus() % 6 == 1) {
                    System.out.printf("订单号：%s，本地事务处理【成功】，提交事务\n", order.toString());
                    return LocalTransactionState.COMMIT_MESSAGE;
                } if (order.getStatus() % 6 == 2) {
                    System.out.printf("订单号：%s，本地事务处理【回滚】，回滚事务\n", order.toString());
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                System.out.printf("订单号：%s，本地事务处理【未知】，【未知】\n", order.toString());
                localTrans.put(order.orderNo, Integer.valueOf(order.getStatus()));
                return LocalTransactionState.UNKNOW;
            }

            /**
             * 当half发送没有相应的时候，broker将发送检查消息去检查transaction的状态，这个方法将
             * 被调用获取本地的事务状态。
             *
             * 尝试15次后进入死信队列。
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {

                Order order = JSONObject.parseObject(new String(msg.getBody()), Order.class);
                System.out.println("=================  查询本地事务  ====================");

                Integer status = localTrans.get(order.getOrderNo());
                if (null != status) {
                    switch (status % 6) {
                        case 3:
                            if (set.contains(status)) {
                                synchronized (object) {
                                    if (set.contains(status)) {
                                        System.out.printf("checkLocalTransaction----订单号：%s，COMMIT_MESSAGE\n", order.getOrderNo());
                                        set.remove(status);
                                        return LocalTransactionState.COMMIT_MESSAGE;
                                    } else {
                                        return LocalTransactionState.UNKNOW;
                                    }
                                }

                            } else {
                                System.out.printf("checkLocalTransaction----订单号：%s，unknow\n", order.getOrderNo());
                                set.add(status);
                                return LocalTransactionState.UNKNOW;
                            }

                        case 4:
                            System.out.printf("checkLocalTransaction----订单号：%s，commit\n", order.getOrderNo());
                            return LocalTransactionState.COMMIT_MESSAGE;

                        case 5:
                            System.out.printf("checkLocalTransaction----订单号：%s，unknow\n", order.getOrderNo());
                            return LocalTransactionState.UNKNOW;

                        case 0:
                            System.out.printf("checkLocalTransaction----订单号：%s，rollback\n", order.getOrderNo());
                            return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        };

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        TransactionMQProducer producer = new TransactionMQProducer("transaction_group");

        producer.setNamesrvAddr("127.0.0.1:9876");

        producer.setExecutorService(executorService);

        producer.start();

        producer.setTransactionListener(transactionListener);

        int index = 1;

        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("输入：");
            String input = scanner.nextLine();

            Order order = new Order();
            order.setOrderNo("" + index);
            order.setStatus((byte) index);
            order.setUsername(input);
            Message msg = new Message("transaction_topic", (JSONObject.toJSONString(order)).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.sendMessageInTransaction(msg, null);
            index++;
        }
    }

    @Data
    static class Order {
        private String orderNo;

        private Byte status;

        private String username;
    }

}