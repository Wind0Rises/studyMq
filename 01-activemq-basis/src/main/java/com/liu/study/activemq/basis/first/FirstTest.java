package com.liu.study.activemq.basis.first;

/**
 * @desc 入门操作。
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/4/23 15:12
 */
public class FirstTest {

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                new FirstProducer().createProducer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(10000);

        new Thread(() -> {
            try {
                new FirstConsumer().createConsumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
