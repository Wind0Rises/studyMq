package com.liu.study.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * home：D:\application_softwore\apache-activemq-5.15.0\bin\win64
 *
 *
 * 注意在home，activeMq-admin与erl.exe[Erlang]端口5672冲突，启动activeMq-admin时，需要把erl.exe关闭，这个是RabbitMQ。启动RabbitMQ直接去
 * 服务中开启RabbitMQ。
 *
 * MQ端口：61616
 * 管理页面端口：8161
 *
 * @author
 *
 */
public class ActiveMqTest {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMqTest.class);

    public static void main(String[] args) {
        System.out.println( "Hello World!" );
    }
}
