package com.liu.study.activemq.discard;

import org.apache.activemq.AsyncCallback;

import javax.jms.JMSException;

/**
 * @desc 自定义异步回调类，在发送数据以后，当队列持久化数据以后，会调用AsyncCallback的方法。
 * @author Liuweian
 * @version 1.0.0
 * @createTime 2020/6/3 11:01
 */
public class CustomAsyncCallback implements AsyncCallback {

    /**
     * 发送成功处理
     */
    @Override
    public void onSuccess() {
        System.out.println("队列持久化数据==========【成功】================");
    }

    /**
     *  发送失败处理。
     */
    @Override
    public void onException(JMSException exception) {
        System.out.println("队列持久化数据==========【失败】================");
    }
}
